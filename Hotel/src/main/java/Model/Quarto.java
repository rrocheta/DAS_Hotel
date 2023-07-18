package Model;

import DAL.DBconn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.Statement;

public class Quarto {
    private Integer id;
    private String tipoQuarto;
    private String piso;
    private Double preco;
    private Boolean ativo;
    private Cartao cartao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoQuarto() {
        return tipoQuarto;
    }

    public void setTipoQuarto(String tipoQuarto) {
        this.tipoQuarto = tipoQuarto;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public Quarto(Integer id, String tipoQuarto, String piso, Double preco, Boolean ativo, Cartao cartao) {
        this.id = id;
        this.tipoQuarto = tipoQuarto;
        this.piso = piso;
        this.preco = preco;
        this.ativo = ativo;
        this.cartao = cartao;
    }

    public Quarto() {
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Tipo de quarto: " + tipoQuarto + ", Piso: " + piso + ", Preço: " + preco;
    }

}
