package Model;

public class Comentario {

    private Integer id;

    private Integer idCliente;

    private String comentario;

    private String tipoComentario;

    public String getTipoComentario() {
        return tipoComentario;
    }

    public void setTipoComentario(String tipoComentario) {
        this.tipoComentario = tipoComentario;
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Comentario(Integer id, Integer idCliente, String comentario, String tipoComentario) {
        this.id = id;
        this.idCliente = idCliente;
        this.comentario = comentario;
        this.tipoComentario = tipoComentario;
    }

    public Comentario() {
    }

}
