package BLL;

import DAL.CheckInDAL;
import DAL.CheckoutDAL;
import DAL.QuartoDAL;
import DAL.ReservaDAL;
import Model.CheckIn;
import Model.Checkout;
import Model.Pagamento;
import Model.Reserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class CheckoutBLL {

    /**
     * A função serve para conectar o controlador e a DAL e retorna todos os pagamentos
     * @throws SQLException mostra a informacao do erro de SQL
     * @return devolve a lista de todos os pagamentos
     */
    public ObservableList<Pagamento> getPagamentos() throws SQLException {
        CheckoutDAL dal = new CheckoutDAL();
        return dal.getPagamentos();
    }

    /**
     * A função serve para conectar o controlador e a DAL e retorna todas as reservas pendentes
     * @throws SQLException mostra a informacao do erro de SQL
     * @return devolve a lista de todas as reservas pendentes
     */
    public List<Reserva> getPendingReservations() throws SQLException {
        CheckInDAL dal = new CheckInDAL();
        return dal.getPendingReservations();
    }

    /**
     * A função serve para conectar o controlador e a DAL e retorna todas as reservas em estado "checkin"
     * @throws SQLException mostra a informacao do erro de SQL
     * @return devolve a lista de todas as reservas em estado "checkin"
     */
    public List<Reserva> getCheckedInReservations() throws SQLException {
        CheckoutDAL dal = new CheckoutDAL();
        return dal.getCheckedInReservations();
    }

    /**
     * A função serve para conectar o controlador e a DAL e atualiza o estado da reserva para "checkout"
     * @param reservationId ID da reserva a passar para o estado "checkout" e marca o respetivo quarto a "false"
     * @throws SQLException mostra a informacao do erro de SQL
     */
    public void updateReservationStateCheckout(int reservationId) throws SQLException {
        ReservaDAL dal = new ReservaDAL();
        dal.updateReservationState(reservationId, "checkout");

        QuartoDAL quartoDal = new QuartoDAL();
        quartoDal.updateAtivo(reservationId, false);
    }

    /**
     * A função serve para conectar o controlador e a DAL e grava na base de dados na tabela "checkout"
     * @param checkout objeto Checkout com as informações para serem guardadas
     * @throws SQLException mostra a informacao do erro de SQL
     */
    public void addCheckout(Checkout checkout) throws SQLException {
        CheckoutDAL checkoutDAL = new CheckoutDAL();
        checkoutDAL.addCheckout(checkout);
    }

    /**
     * A função serve para conectar o controlador e a DAL e devolve os
     * produtos "não consumiveis" da respetiva reserva ao stock
     * @param idReserva ID da reserva
     * @throws SQLException mostra a informacao do erro de SQL
     */
    public void voltaNaoConsumiveisAoStock(int idReserva) throws SQLException {
        CheckoutDAL checkoutDAL = new CheckoutDAL();
        checkoutDAL.voltaNaoConsumiveisAoStock(idReserva);
    }

}
