package Model;

import DAL.DBconn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class Utilizador {

    private int id;
    private String nome;
    private String nif;
    private String morada;
    private Date dataNascimento;
    private String email;
    private String contacto;
    private String utilizador;
    private String password;
    private TipoUtilizador tipoUser;

    public Utilizador(
            String nome,
            String nif,
            String morada,
            Date dataNascimento,
            String email,
            String contacto,
            String utilizador,
            String password,
            String tipoUser) {
        this.nome = nome;
        this.nif = nif;
        this.morada = morada;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.contacto = contacto;
        this.utilizador = utilizador;
        this.password = password;
        this.tipoUser = new TipoUtilizador(0, tipoUser);
    }

    public Utilizador(
            int id,
            String nome,
            String nif,
            String morada,
            Date dataNascimento,
            String email,
            String contacto,
            String utilizador,
            TipoUtilizador tipoUser) {
        this.id = id;
        this.nome = nome;
        this.nif = nif;
        this.morada = morada;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.contacto = contacto;
        this.utilizador = utilizador;
        this.tipoUser = tipoUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(String utilizador) {
        this.utilizador = utilizador;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TipoUtilizador getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(TipoUtilizador tipoUser) {
        this.tipoUser = tipoUser;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + ", NIF: " + nif;
    }

}
