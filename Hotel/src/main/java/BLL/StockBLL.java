package BLL;

import DAL.StockDAL;

public class StockBLL {

    /**
     * Verifica se existe quantidade suficiente de um determinado produto.
     *
     * @param idProduto     ID do produto a ser verificado
     * @param quantDesejada Quantidade desejada do produto
     * @return true se existir quantidade suficiente, false caso contrário
     */
    public boolean verificaSeProdutoTemQuantidadeSuficiente(String idProduto, int quantDesejada) {
        StockDAL sDAL = new StockDAL();
        if (sDAL.verificaSeProdutoTemQuantidadeSuficiente(idProduto, quantDesejada) != null) {
            return true;
        } else {
            return false;
        }
    }
}
