package BLL;

import DAL.EntradaStockDAL;
import DAL.ReservaDAL;
import Model.EntradaStock;
import Model.Stock;
import javafx.collections.ObservableList;

public class EntradaStockBLL {

    /**
     * Dá entrada de stock de produtos, fornecedores e quantidade de Stock
     * @param entradaStocks lista de objetos entrada stock com informaçoes do XML
     * @param fornecedores lista de objetos fornecedores com informaçoes do XML
     * @param stocks lista de objetos stocks com informaçoes do XML
     */
    public void addEntradaStock(ObservableList<EntradaStock> entradaStocks,
                                ObservableList<EntradaStock> fornecedores,
                                ObservableList<Stock> stocks) {

        EntradaStockDAL esdal = new EntradaStockDAL();
        esdal.addEntradaStock(entradaStocks, fornecedores, stocks);
    }

    /**
     * Verifica se já foi dada entrada de alguma encomenda com o ID recebido por parâmetro
     * @param idEncomenda ID da encomenda a ser verificada
     * @retrun false - Caso não existe encomenda com esse ID na base de dados
     * @return true - Caso exista encomenda com esse ID na base de dados
     */
    public Boolean verificaSeExisteEncomendaRepetida(String idEncomenda) {
        EntradaStockDAL entradaDAL = new EntradaStockDAL();
        if (entradaDAL.verificaSeExisteEncomendaRepetida(idEncomenda)) {
            return true;
        } else {
            return false;
        }
    }
}
