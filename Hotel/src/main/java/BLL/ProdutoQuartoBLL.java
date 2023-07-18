package BLL;

import DAL.ProdutoQuartoDAL;

import java.sql.SQLException;

public class ProdutoQuartoBLL {
    /**
     * Adiciona um produto a um quarto específico.
     *
     * @param quartoId  id do quarto.
     * @param productId id do produto.
     * @param quantity  quantidade do produto.
     * @throws SQLException em caso de erro de acesso ao banco de dados.
     */
    public static void addProductToQuarto(int quartoId, String productId, int quantity) throws SQLException {
        ProdutoQuartoDAL.addProductInQuarto(quartoId, productId, quantity);
    }

    /**
     * Remove um produto de um quarto específico.
     *
     * @param productId id do produto.
     * @throws SQLException em caso de erro de acesso ao banco de dados.
     */
    public static void deleteProductFromQuarto(int productId) throws SQLException {
        ProdutoQuartoDAL.deleteProductFromQuarto(productId);
    }
}
