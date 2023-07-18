package Model;

public class EntradaStock {

    private Double taxa;

    private Integer caixas;

    private String idProduto;

    private String local;

    private Double precoSemTaxa;

    private Integer unidades;

    private Double valorTaxa;

    private String ordemNum;

    private String ordemData;

    private Fornecedor fornecedor;

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    //public void setFornecedor(Fornecedor fornecedor) {
    //    this.fornecedor = fornecedor;
    //}

    private Double precoTotal;

    public Double getTaxa() {
        return taxa;
    }

    public Integer getCaixas() {
        return caixas;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public String getLocal() {
        return local;
    }

    public Double getPrecoSemTaxa() {
        return precoSemTaxa;
    }

    public Integer getUnidades() {
        return unidades;
    }

    public Double getPrecoTotal() {
        return precoTotal;
    }

    public Double getValorTaxa() {
        return valorTaxa;
    }

    public String getOrdemNum() {
        return ordemNum;
    }

    public String getOrdemData() {
        return ordemData;
    }

    public EntradaStock(Double taxa, Integer caixas, String idProduto, String local, Double precoSemTaxa,
                        Integer unidades, Double valorTaxa, Double precoTotal) {

        this.taxa = taxa;
        this.caixas = caixas;
        this.idProduto = idProduto;
        this.local = local;
        this.precoSemTaxa = precoSemTaxa;
        this.unidades = unidades;
        this.valorTaxa = valorTaxa;
        this.precoTotal = precoTotal;
    }

    public EntradaStock(String ordemNum, String ordemData, Fornecedor fornecedor) {
        this.ordemNum = ordemNum;
        this.ordemData = ordemData;
        this.fornecedor = fornecedor;
    }

    public EntradaStock() {
    }
}
