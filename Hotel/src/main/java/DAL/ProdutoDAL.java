package DAL;

import Model.MessageBoxes;
import Model.Produto;

import Model.Servico;
import Model.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class ProdutoDAL {

    /**
     * A função addProduto serve para verificar se é possivél adicionar o produto à base de dados
     *
     * @param produtos recebe o produto
     */
    public void addProduto(ObservableList<Produto> produtos) {
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();

            for (int iteradorProduto = 0; iteradorProduto < produtos.size(); iteradorProduto++) {
                String id = produtos.get(iteradorProduto).getIdProduto();
                if (!verificaProdutoExistente(id, connection)) {
                    PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO Produto(id, descricao, peso, precoPorUnidade, consumivel, precoParaCliente)" +
                            "VALUES (?,?,?,?,?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                    insertProduto(produtos.get(iteradorProduto), insertStatement);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Erro inesperado!", "Erro");
        }
    }

    /**
     * A função verificaProdutoExistente serve para verificar se o produto existe
     *
     * @param id         recebe o id do produto
     * @param connection recebe a conexão à base de dados
     * @return devolve se existe ou não o produto
     */
    private boolean verificaProdutoExistente(String id, Connection connection) {
        Statement ps2;
        try {
            ps2 = connection.createStatement();
            ResultSet rs = ps2.executeQuery("SELECT id FROM Produto WHERE id = '" + id + "'");
            return rs.isBeforeFirst();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * A função insertProduto serve para inserir o produto na base de dados
     *
     * @param produto   recebe o produto
     * @param statement recebe o statement
     * @throws SQLException mostra a informacao do erro
     */
    private void insertProduto(Produto produto, PreparedStatement statement) throws SQLException {
        statement.setString(1, produto.getIdProduto());
        statement.setString(2, produto.getDescricao());
        statement.setDouble(3, produto.getPeso());
        statement.setDouble(4, produto.getPrecoUnidade());
        statement.setBoolean(5, false);
        statement.setDouble(6, 1.0);
        statement.executeUpdate();
        statement.close();
    }

    /**
     * A função getAllProdutos serve para guardar todos os produtos numa lista
     *
     * @return devolve a lista com os produtos
     */
    public static ObservableList<Produto> getAllProdutos() {
        ObservableList<Produto> lista = FXCollections.observableArrayList();
        try {
            String cmd = "SELECT * FROM Produto";
            Statement st = DBconn.getConn().createStatement();
            ResultSet rs = st.executeQuery(cmd);
            while (rs.next()) {
                Produto obj = new Produto(rs.getString("id"), rs.getString("descricao"),
                        rs.getDouble("precoPorUnidade"), rs.getDouble("peso"),
                        rs.getBoolean("consumivel"));
                lista.add(obj);
            }
            st.close();
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
        }
        return lista;
    }

    /**
     * A função getAllProdutosPrecosClientes serve para guardar o produto só com o id, a descrição, preço por unidade e preço para cliente
     *
     * @return devolve uma lista com os produtos
     */
    public static ObservableList<Produto> getAllProdutosPrecosClientes() {
        ObservableList<Produto> lista = FXCollections.observableArrayList();
        try {
            String cmd = "SELECT * FROM Produto";
            Statement st = DBconn.getConn().createStatement();
            ResultSet rs = st.executeQuery(cmd);
            while (rs.next()) {
                Produto obj = new Produto(rs.getString("id"), rs.getString("descricao"),
                        rs.getDouble("precoPorUnidade"), rs.getDouble("precoParaCliente"));
                lista.add(obj);
            }
            st.close();
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
        }
        return lista;
    }


    /**
     * A função updateConsumivelProduto serve para atualizar todos os produtos consumivéis
     *
     * @param selectedID recebe o id do produto
     * @param estado     recebe o estado do produto
     * @throws SQLException mostra a informacao do erro
     */
    public void updateConsumivelProduto(Produto selectedID, boolean estado) throws SQLException {
        PreparedStatement ps2;
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        ps2 = connection.prepareStatement("UPDATE Produto SET consumivel = ? WHERE id = ?");
        ps2.setBoolean(1, estado);
        ps2.setString(2, selectedID.getIdProduto());
        ps2.executeUpdate();
    }


    /**
     * A função getQuantidadeProduto serve para guardar a quantidade de um certo produto
     *
     * @param selectedID recebe o id do produto
     * @return devolve a quantidade do produto
     * @throws SQLException mostra a informacao do erro
     */
    public Integer getQuantidadeProduto(Produto selectedID) throws SQLException {
        PreparedStatement ps2;
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        ps2 = connection.prepareStatement("SELECT quantidade FROM Stock WHERE idProduto = ?");
        ps2.setString(1, selectedID.getIdProduto());
        for (int i = 0; i < StockDAL.getStock().size(); i++) {
            if (selectedID != null && selectedID.getIdProduto().equals(StockDAL.getStock().get(i).getIdProduto())) {
                Integer quantidade = StockDAL.getStock().get(i).getQuantidade();
                return quantidade;
            }
        }
        return null;
    }

    /**
     * A função updatePrecoProdutoParaCliente serve para atualizar o preço do produto para o cliente
     *
     * @param selectedID recebe o id do produto
     * @param quantidade recebe a quantidade do produto
     * @throws SQLException mostra a informacao do erro
     */
    public void updatePrecoProdutoParaCliente(Produto selectedID, Double quantidade) throws SQLException {
        PreparedStatement ps2;
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        ps2 = connection.prepareStatement("UPDATE Produto SET precoParaCliente = ? WHERE id = ?");
        ps2.setDouble(1, quantidade);
        ps2.setString(2, selectedID.getIdProduto());
        ps2.executeUpdate();
    }

}