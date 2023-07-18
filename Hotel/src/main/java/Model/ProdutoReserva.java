package Model;

public class ProdutoReserva {
    private int id;
    private int idReserva;
    private String idProduto;

    private int quantidade;

    private int idQuarto;

    public ProdutoReserva(){

    }

    public ProdutoReserva(int id, int idReserva, String idProduto, int quantidade, int idQuarto) {
        this.id = id;
        this.idReserva = idReserva;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.idQuarto = idQuarto;
    }


    public ProdutoReserva(int idReserva, String idProduto, int quantidade, int idQuarto) {
        this.idReserva = idReserva;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.idQuarto = idQuarto;
    }

    public int getIdQuarto() {
        return idQuarto;
    }

    public void setIdQuarto(int idQuarto) {
        this.idQuarto = idQuarto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "Reserva: " + idReserva + ", Produto: " + idProduto + ", Quantidade: " + quantidade;
    }
}
