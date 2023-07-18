package BLL;

import DAL.FornecedorDAL;
import Model.EntradaStock;
import javafx.collections.ObservableList;

public class FornecedorBLL {
    /**
     * Adiciona fornecedores a uma lista de fornecedores.
     *
     * @param fornecedores Lista de fornecedores a ser atualizada
     */
    public void addFornecedor(ObservableList<EntradaStock> fornecedores) {

        FornecedorDAL fdal = new FornecedorDAL();
        fdal.addFornecedor(fornecedores);
    }
}
