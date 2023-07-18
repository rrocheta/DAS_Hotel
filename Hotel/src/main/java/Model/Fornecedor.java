package Model;

public class Fornecedor {
    private String idFornecedor;
    private String nome;
    private String morada;
    private String codigoPostal;
    private String pais;
    private String cidade;

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Fornecedor(){
    }

    public Fornecedor(String idFornecedor, String nome, String morada, String codigoPostal, String pais, String cidade) {
        this.idFornecedor = idFornecedor;
        this.nome = nome;
        this.morada = morada;
        this.codigoPostal = codigoPostal;
        this.pais = pais;
        this.cidade = cidade;
    }

    public void setIdFornecedor(String idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getIdFornecedor() {
        return idFornecedor;
    }

    public String getNome() {
        return nome;
    }

    public String getMorada() {
        return morada;
    }
}
