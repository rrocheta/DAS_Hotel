package Model;

import java.sql.Date;
import java.sql.Timestamp;

public class Registo {
private Integer id;
private Integer idCartao;
private Integer idCliente;
private String Local;
private Timestamp Data;




    public Registo() {
    }

    public Registo(Integer idCartao, Integer idCliente, String local, Timestamp data) {
        this.idCartao = idCartao;
        this.idCliente = idCliente;
        Local = local;
        Data = data;
    }

    public Registo(Integer id, Integer idCartao, Integer idCliente, String local, Timestamp data) {
        this.id = id;
        this.idCartao = idCartao;
        this.idCliente = idCliente;
        Local = local;
        Data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCartao() {
        return idCartao;
    }

    public void setIdCartao(Integer idCartao) {
        this.idCartao = idCartao;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getLocal() {
        return Local;
    }

    public void setLocal(String local) {
        Local = local;
    }

    public Timestamp getData() {
        return Data;
    }

    public void setData(Timestamp data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "Registo{" +
                "id=" + id +
                ", idCartao=" + idCartao +
                ", idCliente=" + idCliente +
                ", local='" + Local + '\'' +
                ", date=" + Data +
                '}';
    }
}