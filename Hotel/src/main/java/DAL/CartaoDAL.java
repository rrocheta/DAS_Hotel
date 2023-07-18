package DAL;

import Model.Cartao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CartaoDAL {
    /**
     * A função addCartao serve para adicionar o cartao na base de dados
     *
     * @param cartao recebe os campos de um cartao
     * @throws SQLException mostra a informacao do erro
     */
    public void addCartao(Cartao cartao) throws SQLException {
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        PreparedStatement ps = connection.prepareStatement("INSERT INTO Cartao VALUES (?)");
        ps.setInt(1, cartao.getId());
        ps.executeUpdate();
    }

    /**
     * A função getAllCartoes serve para obter todos os cartoes da base de dados
     *
     * @return devolve uma lista de cartoes
     */
    public static ObservableList<Cartao> getAllCartoes() {
        ObservableList<Cartao> list = FXCollections.observableArrayList();

        try {
            String cmd = "SELECT * FROM Cartao";

            Statement st = DBconn.getConn().createStatement();

            ResultSet rs = st.executeQuery(cmd);

            while (rs.next()) {
                Cartao objCartao = new Cartao(rs.getInt("id"));
                list.add(objCartao);
            }

            st.close();
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
        }
        return list;
    }

    /**
     * A função deleteCartao serve para eleminar um cartao da base de dados
     *
     * @param idCartao recebe o Id de um cartao
     * @throws SQLException mostra a informacao do erro
     */
    public void deleteCartao(int idCartao) throws SQLException {
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        PreparedStatement ps = connection.prepareStatement("DELETE FROM Cartao WHERE id = ?");
        ps.setInt(1, idCartao);
        ps.executeUpdate();
    }
}
