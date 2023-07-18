package Model;

import java.security.Timestamp;

public class Cartao {

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Cartao(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id:" + id;
    }
}
