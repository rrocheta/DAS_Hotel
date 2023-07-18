package DAL;

import Model.Reserva;
import Model.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class StockDAL {

    /**
     * Obtém a descrição de um produto a partir de uma instância de Stock.
     *
     * @param selectedID a instância de Stock para a qual se deseja obter a descrição do produto
     * @return a descrição do produto
     * @throws SQLException se houver uma falha ao acessar o banco de dados
     */
    public String getDescricaoStock(Stock selectedID) throws SQLException {
        PreparedStatement ps2;
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        ps2 = connection.prepareStatement("SELECT descricao FROM Produto WHERE id = ?");
        ps2.setString(1, selectedID.getIdProduto());
        for (int i = 0; i < ProdutoDAL.getAllProdutos().size(); i++) {
            if (selectedID != null && selectedID.getIdProduto().equals(ProdutoDAL.getAllProdutos().get(i).getIdProduto())) {
                String descricao = ProdutoDAL.getAllProdutos().get(i).getDescricao();
                return descricao;
            }
        }
        return null;
    }

    /**
     * Obtém a lista de Stock de um produto.
     *
     * @return Lista de Stock de um produto.
     */
    public static ObservableList<Stock> getStock() {
        ObservableList<Stock> lista = FXCollections.observableArrayList();
        try {
            String cmd = "SELECT * FROM Stock";
            Statement st = DBconn.getConn().createStatement();
            ResultSet rs = st.executeQuery(cmd);
            while (rs.next()) {
                Stock obj = new Stock(rs.getString("idProduto"), rs.getInt("quantidade"));
                lista.add(obj);
            }
            st.close();
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
        }
        return lista;
    }

    /**
     * Verifica se o produto tem quantidade suficiente para ser vendido.
     *
     * @param idProduto     ID do produto
     * @param quantDesejada quantidade desejada do produto
     * @return true se houver quantidade suficiente do produto, false caso contrário, null se houver erro.
     * @throws Exception
     */
    public Boolean verificaSeProdutoTemQuantidadeSuficiente(String idProduto, int quantDesejada) {
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();
            String cmd = "SELECT s.quantidade as quantidade FROM Stock s " +
                    "INNER JOIN Produto p on p.id = s.idProduto " +
                    "WHERE p.id = ?";
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.setString(1, idProduto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int quantExistente = rs.getInt("quantidade");
                if (quantExistente > quantDesejada) {
                    return true;
                } else {
                    return null;
                }
            }
            ps.close();
        } catch (Exception ex) {
        }
        return null;
    }
}
