package DAL;

import BLL.UtilizadorPreferences;
import Model.MessageBoxes;
import Model.Servico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ServicoDAL {


    /**
     * Recupera todos os serviços e quartos registrados no banco de dados.
     *
     * @return Uma lista observável de objetos {@link Servico}.
     */
    public static ObservableList<Servico> getAllServicosAndQuartos() {
        ObservableList<Servico> lista = FXCollections.observableArrayList();
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();


            String cmd = "SELECT * FROM Servico";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(cmd);
            while (rs.next()) {
                Servico obj = new Servico(rs.getInt("id"), rs.getString("servico"), rs.getDouble("preco"));
                lista.add(obj);
            }


            cmd = "SELECT * FROM Quarto";
            rs = st.executeQuery(cmd);
            while (rs.next()) {
                Servico obj = new Servico(rs.getInt("id"), "Quarto" + rs.getInt("idCartao"), rs.getDouble("preco"));
                lista.add(obj);
            }
            st.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }


    /**
     * Adiciona um serviço ao banco de dados.
     *
     * @param servico O serviço a ser adicionado.
     */
    public void addServico(Servico servico) {
        try {
            if (isServicoExists(servico.getServico())) {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Serviço já existe!", "ERRO");
            } else {
                DBconn dbConn = new DBconn();
                Connection connection = dbConn.getConn();
                PreparedStatement ps2 = connection.prepareStatement("INSERT INTO Servico(servico, preco) VALUES (?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps2.setString(1, servico.getServico());
                ps2.setDouble(2, servico.getPreco());
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica se já existe o serviço no banco de dados.
     *
     * @param servico O nome do serviço a ser verificado.
     * @return True se o serviço existir no banco de dados, False caso contrário.
     * @throws SQLException Caso ocorra algum erro com o acesso ao banco de dados.
     */
    public boolean isServicoExists(String servico) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Servico WHERE servico = ?";

        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, servico);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    /**
     * Remove um serviço da base de dados a partir do seu ID.
     *
     * @param id ID do serviço a ser removido.
     * @return Nada é retornado.
     * @throws SQLException Caso ocorra algum erro de SQL durante a execução da operação.
     */
    public Servico deleteServico(int id) throws SQLException {
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        PreparedStatement ps = connection.prepareStatement("DELETE FROM Servico WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        return null;
    }

    /**
     * Método que obtém todos os serviços a partir da tabela Serviço.
     *
     * @return lista - Lista de objetos do tipo Serviço com todos os serviços.
     */
    public static ObservableList<Servico> getAllServicos() {
        ObservableList<Servico> lista = FXCollections.observableArrayList();
        try {
            String cmd = "SELECT * FROM Servico WHERE servico != 'Quarto'";
            Statement st = DBconn.getConn().createStatement();
            ResultSet rs = st.executeQuery(cmd);
            while (rs.next()) {
                Servico obj = new Servico(rs.getInt("id"), rs.getString("servico"), rs.getDouble("preco"));
                lista.add(obj);
            }
            st.close();
        } catch (Exception ex) {
        }
        return lista;
    }

    /**
     * Obtém todos os serviços prestados a um cliente específico.
     *
     * @return Lista observável de serviços prestados a um cliente.
     * @throws SQLException
     */
    public static ObservableList<Servico> getServicosByClientId() {
        Integer id = UtilizadorPreferences.utilizadorId();
        ObservableList<Servico> lista = FXCollections.observableArrayList();
        Set<Servico> servicos = new HashSet<>();
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();
            String cmd = "SELECT s.id as sId, s.servico as servico, q.idCartao as idCartao " +
                    "FROM Servico s JOIN ServicoReserva SR ON s.id = SR.idServico " + "JOIN Reserva r ON r.id = SR.idReserva " +
                    "JOIN EstadoReserva er ON er.reserva = r.id " + "JOIN Quarto q ON q.id = r.idQuarto " + "WHERE r.idCliente = ? " +
                    "AND er.estado = 'checkin'";
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Servico obj = new Servico(rs.getInt("sId"), rs.getString("servico"));
                servicos.add(obj);
                int idCartao = rs.getInt("idCartao");
                Servico quarto = new Servico("Quarto", idCartao);
                servicos.add(quarto);
            }
            lista.addAll(servicos);
            ps.close();
        } catch (Exception ex) {
        }
        return lista;
    }


}
