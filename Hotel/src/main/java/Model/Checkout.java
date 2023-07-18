package Model;

import java.time.LocalDateTime;

public class Checkout {


    private Integer idReserva;
    private Double preco;
    private String metodoPagamento;

    private LocalDateTime checkoutTime;
    public Checkout() {

    }

    public Checkout(Integer idReserva, Double preco, String metodoPagamento, LocalDateTime checkoutTime) {
        this.idReserva = idReserva;
        this.preco = preco;
        this.metodoPagamento = metodoPagamento;
        this.checkoutTime = checkoutTime;
    }

    public Checkout(Integer idReserva, Double preco, String metodoPagamento) {
        this.idReserva = idReserva;
        this.preco = preco;
        this.metodoPagamento = metodoPagamento;
    }

    public LocalDateTime getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(LocalDateTime checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}
