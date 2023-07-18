package Model;

import DAL.UtilizadorDAL;

public class Reserva {

    private Integer id;
    private Integer idCliente;
    private Integer idQuarto;
    private String dataInicio;
    private String dataFim;
    private Double preco;
    private String ticketID;

    private String clientName;

    public Reserva() {
    }

    public Reserva(Integer id, Integer idCliente, Integer idQuarto, String dataInicio, String dataFim, Double preco) {
        this.id = id;
        this.idCliente = idCliente;
        this.idQuarto = idQuarto;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.preco = preco;
    }

    public Reserva(Integer id, Integer idCliente, Integer idQuarto, String dataInicio, String dataFim, Double preco, String ticketID) {
        this.id = id;
        this.idCliente = idCliente;
        this.idQuarto = idQuarto;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.preco = preco;
        this.ticketID = ticketID;
    }

    public Reserva(Integer id, Integer idCliente, Integer idQuarto, String dataInicio, String dataFim, Double preco, String ticketID, String clientName) {
        this.id = id;
        this.idCliente = idCliente;
        this.idQuarto = idQuarto;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.preco = preco;
        this.ticketID = ticketID;
        this.clientName = clientName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdQuarto() {
        return idQuarto;
    }

    public void setIdQuarto(Integer idQuarto) {
        this.idQuarto = idQuarto;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;

    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    @Override
    public String toString() {
        var name = clientName == null || clientName.isEmpty() || clientName.isBlank()
                ? UtilizadorDAL.getClientName(idCliente)
                : clientName;

        return name + " - Quarto: " + idQuarto + " - " + dataInicio;
    }
}

