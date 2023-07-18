package BLL;

import DAL.ProdutoDAL;
import Model.Produto;
import javafx.collections.ObservableList;

public class ProdutoBLL {

    /**
     * Adiciona uma lista de produtos na base de dados.
     *
     * @param produtos lista de produtos a ser adicionada na base de dados
     */
    public void addProduto(ObservableList<Produto> produtos) {
        ProdutoDAL pdal = new ProdutoDAL();
        pdal.addProduto(produtos);
    }

}
