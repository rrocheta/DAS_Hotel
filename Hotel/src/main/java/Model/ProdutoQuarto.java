package Model;

public class ProdutoQuarto {
    private int id;

    private int idQuarto;
    private String idProduto;
    private int quantidade;

    public ProdutoQuarto(int id, int idQuarto, String idProduto, int quantidade) {
        this.id = id;
        this.idQuarto = idQuarto;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    public ProdutoQuarto(int idQuarto, String idProduto, int quantidade) {
        this.idQuarto = idQuarto;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdQuarto() {
        return idQuarto;
    }

    public void setIdQuarto(int idQuarto) {
        this.idQuarto = idQuarto;
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
}
