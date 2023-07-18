package BLL;

import DAL.ComentarioDAL;
import DAL.UtilizadorDAL;
import Model.Utilizador;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.SQLException;

import static BLL.Encriptacao.encrypt;

public class UtilizadorBLL {

    /**
     * Realiza o login de um utilizador no sistema.
     *
     * @param utilizador Nome de utilizador fornecido pelo utilizador.
     * @param password   Senha fornecida pelo utilizador.
     * @return O objeto Utilizador que representa o utilizador logado ou null se o login falhar.
     */
    public Utilizador Login(String utilizador, String password) {
        try {
            UtilizadorDAL dal = new UtilizadorDAL();
            String passwordCifrada = encrypt(password);

            Utilizador loggedIn = dal.Login(utilizador, passwordCifrada);

            UtilizadorPreferences.guardaridCliente(loggedIn.getId());
            UtilizadorPreferences.guardartipoLogin(loggedIn.getTipoUser().getTipo());

            return loggedIn;
        } catch (Exception ex) {

        }
        return null;
    }

    /**
     * Método que cria um utilizador com os dados especificados.
     *
     * @param nome           nome do utilizador
     * @param nif            NIF do utilizador
     * @param morada         morada do utilizador
     * @param dataNascimento data de nascimento do utilizador
     * @param email          email do utilizador
     * @param contacto       contacto do utilizador
     * @param utilizador     nome de utilizador para autenticação
     * @param password       palavra-passe para autenticação
     * @param tipoUser       tipo de utilizador (Administrador, Rececionista, ...)
     * @throws SQLException caso ocorra algum erro na comunicação com o banco de dados.
     */
    public void createUtilizador(String nome, String nif, String morada, Date dataNascimento, String email, String contacto, String utilizador, String password, String tipoUser) {
        try {
            UtilizadorDAL.CriarUtilizador(nome, nif, morada, dataNascimento, email, contacto, utilizador, password, tipoUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove um utilizador da base de dados.
     *
     * @param id O identificador do utilizador a ser removido.
     * @throws SQLException Se houver algum erro de conexão com a base de dados.
     */
    public void removeUtilizador(int id) throws SQLException {
        UtilizadorDAL udal = new UtilizadorDAL();
        Utilizador utilizador = udal.deleteUtilizador(id);
        if (utilizador != null) {
            udal.deleteUtilizador(id);
        }
    }

    /**
     * Retorna uma lista observável de todos os utilizadores.
     *
     * @return ObservableList Utilizador lista de todos os utilizadores
     */
    public static ObservableList<Utilizador> getAllUtilizadores() {
        return UtilizadorDAL.getTodosUtilizadores();
    }

    /**
     * Método para obter todos os utilizadores do tipo gestor.
     *
     * @return Uma ObservableList de utilizadores do tipo gestor.
     */
    public static ObservableList<Utilizador> getAllGestores() {
        return UtilizadorDAL.getGestores();
    }

    /**
     * Método para obter todos os utilizadores do tipo funcionario.
     *
     * @return Uma ObservableList de utilizadores do tipo funcionario.
     */
    public static ObservableList<Utilizador> getAllFuncionarios() {
        return UtilizadorDAL.getFuncionario();
    }

    /**
     * Método para obter todos os utilizadores do tipo cliente.
     *
     * @return Uma ObservableList de utilizadores do tipo cliente.
     */
    public static ObservableList<Utilizador> getAllClientes() {
        return UtilizadorDAL.getClientes();
    }

}
