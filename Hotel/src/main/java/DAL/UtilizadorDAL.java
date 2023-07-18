package DAL;

import BLL.Encriptacao;
import Model.MessageBoxes;
import Model.TipoUtilizador;
import Model.Utilizador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static BLL.Encriptacao.encrypt;

public class UtilizadorDAL {

    /**
     * Obtém uma lista de utilizadores a partir do tipo de utilizador especificado.
     *
     * @param i O ID do tipo de utilizador a partir do qual a lista de utilizadores será obtida.
     * @return Uma lista de utilizadores que são do tipo especificado pelo ID fornecido.
     */
    public static List<Utilizador> getUsersByType(int i) {
        List<Utilizador> users = new ArrayList<>();
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT u.id as uId, u.nome as uNome, nif, morada, dataNascimento, email, contacto, utilizador, palavrapasse, tu.id as tuId, tu.nome as tuNome " +
                            "FROM Utilizador u " +
                            "INNER JOIN TipoUtilizador tu ON tu.Id = u.idTipoUtilizador " +
                            "WHERE tu.id = ?"
            );
            ps.setInt(1, i);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                users.add(new Utilizador(
                        result.getInt("uId"),
                        result.getString("uNome"),
                        result.getString("nif"),
                        result.getString("morada"),
                        result.getDate("dataNascimento"),
                        result.getString("email"),
                        result.getString("contacto"),
                        result.getString("utilizador"),
                        new TipoUtilizador(
                                result.getInt("tuId"),
                                result.getString("tuNome")
                        )
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Recupera o nome de um cliente com base no seu identificador.
     *
     * @param idCliente Identificador do cliente.
     * @return Nome do cliente.
     */
    public static String getClientName(Integer idCliente) {
        String clientName = "";
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT nome FROM Utilizador WHERE id = ?"
            );
            ps.setInt(1, idCliente);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                clientName = result.getString("nome");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientName;
    }

    /**
     * Realiza o login de um utilizador.
     *
     * @param utilizador Nome de utilizador
     * @param password   Palavra-passe de acesso
     * @return Utilizador O utilizador que realizou o login ou null se o login falhar
     * @throws SQLException Exceção gerada pelo acesso à base de dados
     */
    public Utilizador Login(String utilizador, String password) throws SQLException {
        PreparedStatement ps;
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();
        ps = connection.prepareStatement(
                "SELECT u.id as uId, u.nome as uNome, nif, morada, dataNascimento, email, contacto, utilizador, palavrapasse, tu.id as tuId, tu.nome as tuNome " +
                        "FROM Utilizador u " +
                        "INNER JOIN TipoUtilizador tu ON tu.Id = u.idTipoUtilizador " +
                        "WHERE u.utilizador = ? " +
                        "AND u.palavrapasse = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, utilizador);

        ps.setString(2, password);
        ResultSet result = ps.executeQuery();

        if (result.next()) {
            if (result.first()) {
                return new Utilizador(
                        result.getInt("uId"),
                        result.getString("uNome"),
                        result.getString("nif"),
                        result.getString("morada"),
                        result.getDate("dataNascimento"),
                        result.getString("email"),
                        result.getString("contacto"),
                        result.getString("utilizador"),
                        new TipoUtilizador(
                                result.getInt("tuId"),
                                result.getString("tuNome")
                        )
                );
            }
        }
        return null;
    }

    /**
     * Cria um utilizador na base de dados.
     *
     * @param nome           Nome do utilizador.
     * @param nif            NIF do utilizador.
     * @param morada         Morada do utilizador.
     * @param dataNascimento Data de nascimento do utilizador.
     * @param email          Email do utilizador.
     * @param contacto       Contacto do utilizador.
     * @param utilizador     Nome de utilizador do utilizador.
     * @param password       Palavra-passe do utilizador.
     * @param tipoUser       Tipo de utilizador.
     * @return PreparedStatement object.
     * @throws SQLException             se erros na base de dados ocurerem.
     * @throws IllegalArgumentException se algum parametro for null ou tipoUser invalido is invalid.
     */
    public static PreparedStatement CriarUtilizador(String nome, String nif, String morada, Date dataNascimento, String email, String contacto, String utilizador, String password, String tipoUser) throws SQLException {

        if (nome == null || nif == null || morada == null || dataNascimento == null || email == null || contacto == null || utilizador == null || password == null || tipoUser == null) {
            throw new IllegalArgumentException("All fields are required");
        }


        TipoUtilizador userType = TipoUtilizadorDAL.getByNome(tipoUser);
        String encryptpass = Encriptacao.encrypt(password);
        if (userType == null) {
            throw new IllegalArgumentException("Invalid user type: " + tipoUser);
        }

        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO Utilizador (nome, nif, morada, dataNascimento, email, contacto, utilizador, palavrapasse, idTipoUtilizador) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

        ps.setString(1, nome);
        ps.setString(2, nif);
        ps.setString(3, morada);
        ps.setDate(4, (java.sql.Date) dataNascimento);
        ps.setString(5, email);
        ps.setString(6, contacto);
        ps.setString(7, utilizador);
        ps.setString(8, encryptpass);
        ps.setInt(9, userType.getId());
        ps.executeUpdate();

        return ps;

    }

    /**
     * Seleciona todos os utilizadores existentes na base de dados.
     *
     * @return ObservableList de objetos Utilizador que representa todos os utilizadores existentes.
     */
    public static ObservableList<Utilizador> getTodosUtilizadores() {
        ObservableList<Utilizador> utilizadores = FXCollections.observableArrayList();

        try {
            String cmd = "SELECT u.*, tu.id as tuId, tu.nome as tuNome FROM Utilizador u " +
                    "INNER JOIN TipoUtilizador tu ON tu.Id = u.idTipoUtilizador " +
                    "WHERE tu.nome IN ('Gestor', 'Funcionario', 'Cliente') ";

            Statement st = DBconn.getConn().createStatement();

            ResultSet rs = st.executeQuery(cmd);

            while (rs.next()) {
                Utilizador obj = new Utilizador(rs.getInt("id"), rs.getString("nome"), rs.getString("nif"),
                        rs.getString("morada"), rs.getDate("dataNascimento"), rs.getString("email"),
                        rs.getString("contacto"), rs.getString("utilizador"),
                        new TipoUtilizador(
                                rs.getInt("tuId"),
                                rs.getString("tuNome")));
                utilizadores.add(obj);
            }

            st.close();
        } catch (Exception ex) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Não encontrado", "Erro");
        }
        return utilizadores;
    }

    /**
     * Obtém uma lista observável de utilizadores do tipo Gestor.
     *
     * @return ObservableList<Utilizador> - Lista observável de utilizadores do tipo Gestor
     */
    public static ObservableList<Utilizador> getGestores() {
        ObservableList<Utilizador> clientes = FXCollections.observableArrayList();

        try {
            String cmd = "SELECT u.*, tu.id as tuId, tu.nome as tuNome FROM Utilizador u " +
                    "INNER JOIN TipoUtilizador tu ON tu.Id = u.idTipoUtilizador " +
                    "WHERE tu.nome IN ('Gestor') ";

            Statement st = DBconn.getConn().createStatement();

            ResultSet rs = st.executeQuery(cmd);

            while (rs.next()) {
                Utilizador obj = new Utilizador(rs.getInt("id"), rs.getString("nome"), rs.getString("nif"),
                        rs.getString("morada"), rs.getDate("dataNascimento"), rs.getString("email"),
                        rs.getString("contacto"), rs.getString("utilizador"),
                        new TipoUtilizador(
                                rs.getInt("tuId"),
                                rs.getString("tuNome")));
                clientes.add(obj);
            }

            st.close();
        } catch (Exception ex) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Não encontrado", "Erro");
        }
        return clientes;
    }

    /**
     * Obtém a lista de utilizadores do tipo Funcionário.
     *
     * @return ObservableList de Utilizadores do tipo Funcionário.
     */
    public static ObservableList<Utilizador> getFuncionario() {
        ObservableList<Utilizador> clientes = FXCollections.observableArrayList();

        try {
            String cmd = "SELECT u.*, tu.id as tuId, tu.nome as tuNome FROM Utilizador u " +
                    "INNER JOIN TipoUtilizador tu ON tu.Id = u.idTipoUtilizador " +
                    "WHERE tu.nome IN ('Funcionario') ";

            Statement st = DBconn.getConn().createStatement();

            ResultSet rs = st.executeQuery(cmd);

            while (rs.next()) {
                Utilizador obj = new Utilizador(rs.getInt("id"), rs.getString("nome"), rs.getString("nif"),
                        rs.getString("morada"), rs.getDate("dataNascimento"), rs.getString("email"),
                        rs.getString("contacto"), rs.getString("utilizador"),
                        new TipoUtilizador(
                                rs.getInt("tuId"),
                                rs.getString("tuNome")));
                clientes.add(obj);
            }

            st.close();
        } catch (Exception ex) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Não encontrado", "Erro");
        }
        return clientes;
    }

    /**
     * Obtém a lista de clientes a partir da base de dados.
     *
     * @return A lista de clientes.
     */
    public static ObservableList<Utilizador> getClientes() {
        ObservableList<Utilizador> clientes = FXCollections.observableArrayList();

        try {
            String cmd = "SELECT u.*, tu.id as tuId, tu.nome as tuNome FROM Utilizador u " +
                    "INNER JOIN TipoUtilizador tu ON tu.Id = u.idTipoUtilizador " +
                    "WHERE tu.nome IN ('Cliente') ";

            Statement st = DBconn.getConn().createStatement();

            ResultSet rs = st.executeQuery(cmd);

            while (rs.next()) {
                Utilizador obj = new Utilizador(rs.getInt("id"), rs.getString("nome"), rs.getString("nif"),
                        rs.getString("morada"), rs.getDate("dataNascimento"), rs.getString("email"),
                        rs.getString("contacto"), rs.getString("utilizador"),
                        new TipoUtilizador(
                                rs.getInt("tuId"),
                                rs.getString("tuNome")));
                clientes.add(obj);
            }

            st.close();
        } catch (Exception ex) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Não encontrado", "Erro");
        }
        return clientes;
    }

    /**
     * Método que remove um utilizador da base de dados.
     *
     * @param id O id do utilizador a ser removido
     * @return Não retorna nada, apenas remove o utilizador da base de dados
     * @throws SQLException Se houver algum erro na execução da operação de remoção
     */
    public Utilizador deleteUtilizador(int id) throws SQLException {
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        PreparedStatement ps = connection.prepareStatement("DELETE FROM Utilizador WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        return null;
    }
}
