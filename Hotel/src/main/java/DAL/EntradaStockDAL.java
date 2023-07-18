package DAL;

import Model.EntradaStock;
import Model.Stock;
import javafx.collections.ObservableList;

import java.sql.*;

public class EntradaStockDAL {

    /**
     * A função addEntradaStock serve para inserir todas os produtos importados
     *
     * @param entradaStocks recebe os produtos importados
     * @param fornecedores  recebe os fornecedores que entregaram os proodutos
     * @param stocks        recebe o stock atual
     */
    public void addEntradaStock(ObservableList<EntradaStock> entradaStocks,
                                ObservableList<EntradaStock> fornecedores,
                                ObservableList<Stock> stocks) {
        try {
            Connection connection = DBconn.getConn();

            for (int i = 0; i < entradaStocks.size(); i++) {
                PreparedStatement insertEntradaStockStatetement = connection.prepareStatement("INSERT INTO EntradaStock(idProduto, caixas, unidades," +
                        "precoSemTaxa, taxa, valorTaxa, localTaxa, precoTotal, ordemNumero, ordemData, idFornecedor)" +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                String idProduto = entradaStocks.get(i).getIdProduto();
                insertEntradaStock(entradaStocks.get(i), fornecedores.get(0), insertEntradaStockStatetement);

                if (!verificaStockExistente(entradaStocks.get(i).getIdProduto(), connection)) {
                    insertStock(stocks, connection);

                    return;
                }

                updateStock(connection, stocks, entradaStocks.get(i), idProduto);
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * A função insertEntradaStock serve para adicionar os produtos e os fornecedores à base de dados
     *
     * @param entradaStock recebe os produtos importados
     * @param fornecedores recebe os fornecedores que entregaram os proodutos
     * @param statement    recebe o statement
     * @throws SQLException mostra a informacao do erro
     */
    private void insertEntradaStock(EntradaStock entradaStock, EntradaStock fornecedores, PreparedStatement statement) throws SQLException {
        statement.setString(1, entradaStock.getIdProduto());
        statement.setInt(2, entradaStock.getCaixas());
        statement.setInt(3, entradaStock.getUnidades());
        statement.setDouble(4, entradaStock.getPrecoSemTaxa());
        statement.setDouble(5, entradaStock.getTaxa());
        statement.setDouble(6, entradaStock.getValorTaxa());
        statement.setString(7, entradaStock.getLocal());
        statement.setDouble(8, entradaStock.getPrecoTotal());
        statement.setString(9, fornecedores.getOrdemNum());
        statement.setString(10, fornecedores.getOrdemData());
        statement.setString(11, fornecedores.getFornecedor().getIdFornecedor());
        statement.executeUpdate();
        statement.close();
    }


    /**
     * A função verificaStockExistente serve para verificar que produtos existem na base de dados
     *
     * @param id         recebe o id do produto
     * @param connection recebe a conexão da base de dados
     * @return devolve se existe ou não o produto na base de dados
     */
    private boolean verificaStockExistente(String id, Connection connection) {
        Statement ps2;
        try {
            ps2 = connection.createStatement();
            ResultSet rs = ps2.executeQuery("SELECT idProduto FROM Stock WHERE idProduto = '" + id + "'");
            return rs.isBeforeFirst();
        } catch (SQLException e) {
            return false;
        }
    }


    /**
     * A função updateStock serve para atualizar a quantidade dos produtos que estão no stock
     *
     * @param connection   recebe a conexão da base de dados
     * @param stocks       recebe o stock existente
     * @param entradaStock recebe o stock importado
     * @param idProduto    recebe o id do produto
     * @throws SQLException mostra a informacao do erro
     */
    private void updateStock(Connection connection, ObservableList<Stock> stocks, EntradaStock entradaStock, String idProduto) throws SQLException {
        Stock stock = null;

        PreparedStatement updateStatement = connection.prepareStatement("UPDATE Stock SET quantidade = ? WHERE idProduto = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        for (int iteradorStock = 0; iteradorStock < stocks.size(); iteradorStock++) {
            if (stocks.get(iteradorStock).getIdProduto().equals(idProduto)) {
                stock = stocks.get(iteradorStock);
                break;
            }
        }

        if (stock != null) {
            Integer quantidade = selectStock(idProduto, connection);
            stock.setQuantidade(quantidade + entradaStock.getUnidades());
            updateStatement.setInt(1, stock.getQuantidade());
            updateStatement.setString(2, stock.getIdProduto());
            updateStatement.executeUpdate();
            updateStatement.close();
        }
    }


    /**
     * A função selectStock serve para descobrir a quantidade do produto em stock
     *
     * @param idProduto  recebe o id do produto
     * @param connection recebe a conexão da base de dados
     * @return recebe a quantidade do produto
     * @throws SQLException mostra a informacao do erro
     */
    private Integer selectStock(String idProduto, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT quantidade FROM Stock WHERE idProduto = '" + idProduto + "'");
        Integer quantidade = 0;
        while (rs.next()) {
            quantidade = rs.getInt("quantidade");
        }
        statement.close();
        return quantidade;
    }


    /**
     * A função insertStock serve para adicionar o novo stock à base de dados
     *
     * @param stocks     recebe o stock
     * @param connection recebe a conexão á base de dados
     */
    void insertStock(ObservableList<Stock> stocks, Connection connection) {
        try {
            PreparedStatement ps2 = connection.prepareStatement("INSERT INTO Stock(idProduto, quantidade)" +
                    "VALUES (?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            for (int i = 0; i < stocks.size(); i++) {
                ps2.setString(1, stocks.get(i).getIdProduto());
                ps2.setDouble(2, stocks.get(i).getQuantidade());
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
        }
    }

    /**
     * A função updateStock serve para atualizar a quantidade do produto
     *
     * @param stocks     recebe o stock atual
     * @param connection recebe a conexão à base de dados
     * @throws SQLException mostra a informacao do erro
     */
    public void updateStock(ObservableList<Stock> stocks, Connection connection) throws SQLException {
        for (Stock stock : stocks) {
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE Stock SET quantidade = ? WHERE idProduto = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            Integer quantidade = selectStock(stock.getIdProduto(), connection);
            updateStatement.setInt(1, stock.getQuantidade() + quantidade);
            updateStatement.setString(2, stock.getIdProduto());
            updateStatement.executeUpdate();
            updateStatement.close();
        }
    }

    /**
     * A função verificaSeExisteEncomendaRepetida serve para validar se a encomenda já foi feita
     *
     * @param idEncomenda recebe o id da encomenda
     * @return devolve se existe ou não a encomenda
     */
    public Boolean verificaSeExisteEncomendaRepetida(String idEncomenda) {
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();
            String cmd = "SELECT ordemNumero FROM EntradaStock WHERE ordemNumero =?";
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.setString(1, idEncomenda);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps.close();
                return true;
            } else {
                ps.close();
                return false;
            }
        } catch (Exception ex) {
        }
        return null;
    }

}
