package BLL;

import DAL.ServicoDAL;
import Model.Servico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class ServicoBLL {

    /**
     * Método estático que retorna uma lista de serviços.
     *
     * @return A lista de todos os serviços disponíveis.
     */
    public static ObservableList<Servico> getServicos() {
        return ServicoDAL.getAllServicos();
    }

    /**
     * Método que adiciona um serviço.
     *
     * @param servico O serviço a ser adicionado.
     */
    public void addServico(Servico servico) {
        ServicoDAL sdal = new ServicoDAL();
        sdal.addServico(servico);
    }

    /**
     * Método que remove um serviço.
     *
     * @param id ID do serviço a ser removido.
     * @throws SQLException Em caso de erro na remoção do serviço.
     */
    public void removeServico(int id) throws SQLException {
        ServicoDAL dal = new ServicoDAL();
        Servico servico = dal.deleteServico(id);
        if (servico != null) {
            dal.deleteServico(id);
        }
    }

    /**
     * Método estático que retorna uma lista de todos os serviços.
     *
     * @return A lista de todos os serviços disponíveis.
     */
    public static ObservableList<Servico> getAllServicos() {
        ObservableList<Servico> servicos = FXCollections.observableArrayList();
        try {
            servicos = ServicoDAL.getAllServicos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servicos;
    }

}
