package Model;

import DAL.DBconn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.Statement;

public class Servico {

    private Integer idServico;

    private String servico;

    private Double preco;
    private int idCartao;

    public int getIdCartao() {
        return idCartao;
    }

    public void setIdCartao(int idCartao) {
        this.idCartao = idCartao;
    }

    public Servico(String servico) {
        this.servico = servico;
    }

    public Integer getIdServico() {
        return idServico;
    }

    public void setIdServico(Integer idServico) {
        this.idServico = idServico;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Servico(Integer idServico, String servico, Double preco) {
        this.idServico = idServico;
        this.servico = servico;
        this.preco = preco;
    }

    public Servico(String servico, int idCartao) {
        this.servico = servico;
        this.idCartao = idCartao;
    }


    public Servico(Integer idServico, String servico) {
        this.idServico = idServico;
        this.servico = servico;
    }

    public Servico() {
    }

    @Override
    public String toString() {
        return "Serviço: " + servico + ", Preço: " + preco;
    }
}