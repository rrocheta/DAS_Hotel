package Model;

import DAL.DBconn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.Statement;

public class Stock {

    private String idProduto;

    private Integer quantidade;

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Stock() {
    }

    public Stock(String idProduto, Integer quantidade) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

}
