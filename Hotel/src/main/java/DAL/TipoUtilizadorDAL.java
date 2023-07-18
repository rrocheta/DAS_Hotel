package DAL;

import Model.TipoUtilizador;
import javafx.collections.FXCollections;

import java.sql.*;
import java.util.List;

public class TipoUtilizadorDAL {

    /**
     * Obtém um tipo de utilizador a partir do seu nome.
     *
     * @param nome O nome do tipo de utilizador a procurar.
     * @return O tipo de utilizador correspondente ao nome especificado. Retorna null se o tipo de utilizador não for encontrado.
     * @throws SQLException             Se ocorrer uma exceção de SQL.
     * @throws IllegalArgumentException Se o nome fornecido for nulo.
     */
    public static TipoUtilizador getByNome(String nome) throws SQLException {
        if (nome == null) {
            throw new IllegalArgumentException("Nome cannot be null");
        }

        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            ps = connection.prepareStatement("SELECT * FROM TipoUtilizador WHERE nome=?");
            ps.setString(1, nome);
            result = ps.executeQuery();
            if (result.next()) {
                return new TipoUtilizador(
                        result.getInt("Id"),
                        result.getString("Nome"));
            }
        } catch (SQLException e) {

        } finally {
            if (result != null) {
                result.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return null;
    }
    /**

     Recupera a lista de tipos de utilizadores existentes na base de dados.
     @return Lista de objetos do tipo {@link TipoUtilizador}.
     */
    public static List<TipoUtilizador> getTipoUtilizador() {
        List<TipoUtilizador> lista = FXCollections.observableArrayList();
        try {
            String cmd = "SELECT * FROM TipoUtilizador";

            Statement st = DBconn.getConn().createStatement();

            ResultSet rs = st.executeQuery(cmd);

            while (rs.next()) {
                TipoUtilizador obj = new TipoUtilizador(rs.getInt("id"), rs.getString("nome"));
                lista.add(obj);
            }

            st.close();
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
        }
        return lista;
    }
}
