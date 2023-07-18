package DAL;

import Model.Comentario;
import Model.Produto;
import Model.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ComentarioDAL {

    /**
     * A função addComentario serve para o cliente puder dar o feedback da sua estadia no hotel
     *
     * @param comentario recebe um comentario
     */
    public void addComentario(Comentario comentario) {
        PreparedStatement ps2;

        try {
            // create a database connection
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();

            // prepare the insert statement
            ps2 = connection.prepareStatement("INSERT INTO Comentario (idCliente,comentario, tipoComentario) VALUES (?,?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // set the values for the prepared statement
            ps2.setInt(1, comentario.getIdCliente());
            ps2.setString(2, comentario.getComentario());
            ps2.setString(3, comentario.getTipoComentario());

            // execute the insert statement
            ps2.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * A função getAllComentarios serve para mostrar todos os comentarios guardadoos
     *
     * @return devolve uma lista de todos os comentários feitos
     */
    public static ObservableList<Comentario> getAllComentarios() {
        ObservableList<Comentario> list = FXCollections.observableArrayList();

        try {
            String cmd = "SELECT * FROM Comentario";

            Statement st = DBconn.getConn().createStatement();

            ResultSet rs = st.executeQuery(cmd);

            while (rs.next()) {
                Comentario objComentario = new Comentario(rs.getInt("id"), rs.getInt("idCliente"),
                        rs.getString("comentario"), rs.getString("tipoComentario"));
                list.add(objComentario);
            }

            st.close();
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
        }
        return list;
    }

    /**
     * A função getAllSugestoes serve para guardar todas as sugestoes
     *
     * @return devolve uma lista com todas as sugestoes
     */
    public static ObservableList<Comentario> getAllSugestoes() {
        ObservableList<Comentario> list = FXCollections.observableArrayList();

        try {
            String cmd = "SELECT * FROM Comentario " +
                    "WHERE comentario.tipoComentario IN ('sugestao')";

            Statement st = DBconn.getConn().createStatement();

            ResultSet rs = st.executeQuery(cmd);

            while (rs.next()) {
                Comentario objComentario = new Comentario(rs.getInt("id"), rs.getInt("idCliente"),
                        rs.getString("comentario"), rs.getString("tipoComentario"));
                list.add(objComentario);
            }

            st.close();
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
        }
        return list;
    }


    /**
     * A função getAllQueixas serve para guardar todas as queixas
     *
     * @return devolve uma lista com todas as sugestoes
     */
    public static ObservableList<Comentario> getAllQueixas() {
        ObservableList<Comentario> list = FXCollections.observableArrayList();

        try {
            String cmd = "SELECT * FROM Comentario " +
                    "WHERE comentario.tipoComentario IN ('queixa')";

            Statement st = DBconn.getConn().createStatement();

            ResultSet rs = st.executeQuery(cmd);

            while (rs.next()) {
                Comentario objComentario = new Comentario(rs.getInt("id"), rs.getInt("idCliente"),
                        rs.getString("comentario"), rs.getString("tipoComentario"));
                list.add(objComentario);
            }

            st.close();
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
        }
        return list;
    }

    /**
     * A função getComentario serve para receber o comentario de um cliente
     */
    public String getComentario(Comentario selectedID) throws SQLException {
        PreparedStatement ps2;
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        ps2 = connection.prepareStatement("SELECT comentario FROM Comentario WHERE id = ?");
        ps2.setInt(1, selectedID.getId());
        for (int i = 0; i < getAllComentarios().size(); i++) {
            if (selectedID != null && selectedID.getId() == getAllComentarios().get(i).getId()) {
                String comentario = getAllComentarios().get(i).getComentario();
                return comentario;
            }
        }
        return null;
    }

    /**
     * A função getComentario serve para apagar um comentario
     */
    public void deleteComentario(int clienteId) {
        try (Connection con = DBconn.getConn();
             PreparedStatement stmt = con.prepareStatement("DELETE FROM dbo.Comentario WHERE idCliente = ?")) {

            stmt.setInt(1, clienteId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
