package BLL;

import DAL.RegistoDAL;
import Model.Registo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class RegistoBLL {

    /**
     * Obtém a lista de todos os registos existentes na base de dados.
     *
     * @return A lista de todos os registos na forma de uma {@link ObservableList}.
     * @throws SQLException se houver algum erro na execução da consulta na base de dados.
     */
    public ObservableList<Registo> getAllRegistos() throws SQLException {
        RegistoDAL dal = new RegistoDAL();
        List<Registo> registos = dal.getAllRegistos();
        ObservableList<Registo> observableList = FXCollections.observableArrayList();
        observableList.addAll(registos);
        return observableList;
    }

    /**
     * Adiciona um novo registo.
     *
     * @param idCartao  O ID do cartão associado ao registo.
     * @param idCliente O ID do cliente associado ao registo.
     * @param local     O local onde o registo foi feito.
     * @param data      A data e hora do registo.
     * @throws SQLException Se houver algum erro ao acessar o banco de dados.
     */
    public static void addNewRegisto(int idCartao, int idCliente, String local, Timestamp data) throws SQLException {
        RegistoDAL dal = new RegistoDAL();
        Registo registo = new Registo(idCartao, idCliente, local, data);
        dal.addRegisto(registo);
    }

    /**
     * Delete um novo registo.
     *
     * @param id  O ID do cartão associado ao registo.
     * @throws SQLException Se houver algum erro ao acessar o banco de dados.
     */
    public void deleteRegisto(int id) throws SQLException {
        RegistoDAL dal = new RegistoDAL();
        dal.deleteRegisto(id);
    }

    /**
     * Retorna o ID do cartão pelo ID do cliente.
     *
     * @param clientId  O ID do cliente.
     * @return O ID do cartão
     * @throws SQLException Se houver algum erro ao acessar o banco de dados.
     */
    public static int getCardIdByClientId(int clientId) throws SQLException {
        return RegistoDAL.getCardIdByClientId(clientId);
    }

}
