package BLL;

import DAL.ServicoReservaDAL;
import Model.MessageBoxes;
import Model.Reserva;
import Model.Servico;
import Model.ServicoReserva;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.List;

public class ServicoReservaBLL {
    private ServicoReservaDAL servicoReservaDAL = new ServicoReservaDAL();

    /**
     * Método que adiciona serviços a uma reserva.
     *
     * @param servicos      Lista de serviços a serem adicionados à reserva.
     * @param reservationId ID da reserva.
     * @throws SQLException Em caso de erro na adição dos serviços à reserva.
     */
    public void addServicosToReserva(List<ServicoReserva> servicos, int reservationId) throws SQLException {
        for (ServicoReserva servico : servicos) {
            if (!isServicoAssociatedWithReserva(reservationId, servico.getIdServico())) {
                servico.setIdReserva(reservationId);
                servicoReservaDAL.addServicoToReserva(servico);
            } else {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Serviço já existe nesta reserva!", "ERRO");
            }
        }
    }

    /**
     * Método que remove um serviço de uma reserva.
     *
     * @param selectedReservation A reserva a partir da qual o serviço será removido.
     * @param selectedServico     O serviço a ser removido da reserva.
     * @throws SQLException Em caso de erro na remoção do serviço da reserva.
     */
    public void removeServicoFromReservation(Reserva selectedReservation, Servico selectedServico) throws SQLException {
        ServicoReservaDAL servicoReservaDAL = new ServicoReservaDAL();
        servicoReservaDAL.removeServicoFromReservation(selectedReservation, selectedServico);
    }

    /**
     * Método que verifica se um serviço está associado a uma reserva.
     *
     * @param idReserva ID da reserva.
     * @param idServico ID do serviço.
     * @return Retorna true se o serviço estiver associado à reserva, caso contrário retorna false.
     * @throws SQLException Em caso de erro na verificação da associação entre serviço e reserva.
     */
    public boolean isServicoAssociatedWithReserva(int idReserva, int idServico) throws SQLException {
        ServicoReservaDAL dal = new ServicoReservaDAL();
        return dal.isServicoExistsForReserva(idReserva, idServico);
    }
}
