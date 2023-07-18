package DAL;

import Model.ProdutoReserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ProdutoReservaDAL {

    /**
     * A função addProductInReservation serve para verificar se é possível adicionar os produtos à reserva
     *
     * @param reservationId recebe o id da reserva
     * @param productId     recebe o id do produto
     * @param quantity      recebe a quantidade do produto
     * @param idQuarto      recebe o id do quarto
     * @throws SQLException mostra a informacao do erro
     */
    public static void addProductInReservation(int reservationId, String productId, int quantity, int idQuarto) throws SQLException {

        String query1 = "INSERT INTO ProdutoReserva (idReserva, idProduto, quantidade, idQuarto) VALUES (?,?,?,?)";
        try (Connection connection = DBconn.getConn();
             PreparedStatement ps1 = connection.prepareStatement(query1)) {
            ps1.setInt(1, reservationId);
            ps1.setString(2, productId);
            ps1.setInt(3, quantity);
            ps1.setInt(4, idQuarto);
            ps1.executeUpdate();
        }
    }

    /**
     * A função deleteProductFromReservation serve para eliminar os produtos da reserva
     *
     * @param productId recebe o id do produto
     * @throws SQLException mostra a informacao do erro
     */
    public static void deleteProductFromReservation(int productId) throws SQLException {
        PreparedStatement ps2;
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();

            ps2 = connection.prepareStatement("DELETE FROM ProdutoReserva WHERE id =?");
            ps2.setInt(1, productId);
            ps2.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * A função getProdutoReserva serve para guardar os produtos da reserva
     *
     * @return devolve uma lista dos produtos da reserva
     */
    public static ObservableList<ProdutoReserva> getProdutoReserva() {
        ObservableList<ProdutoReserva> lista = FXCollections.observableArrayList();
        try {
            String cmd = "SELECT * FROM ProdutoReserva";
            Statement st = DBconn.getConn().createStatement();
            ResultSet rs = st.executeQuery(cmd);
            while (rs.next()) {
                ProdutoReserva obj = new ProdutoReserva(rs.getInt("id"), rs.getInt("idReserva"), rs.getString("idProduto"),
                        rs.getInt("quantidade"), rs.getInt("idQuarto"));
                lista.add(obj);
            }
            st.close();
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
        }
        return lista;
    }

}
