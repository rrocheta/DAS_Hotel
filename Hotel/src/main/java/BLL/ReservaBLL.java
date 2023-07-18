package BLL;

import DAL.QuartoDAL;
import DAL.ReservaDAL;
import Model.EstacionamentoAPI.Ticket;
import Model.EstacionamentoAPI.TicketInfo;
import Model.Reserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ReservaBLL {

    /**
     * Adiciona uma reserva ao sistema.
     *
     * @param reserva a reserva a ser adicionada
     * @return a reserva adicionada, com informação de total calculada
     * @throws SQLException Se houver algum erro ao acessar o banco de dados.
     */
    public Reserva addReserva(Reserva reserva) throws SQLException {
        ReservaDAL reservaDAL = new ReservaDAL();
        reserva = reservaDAL.addReserva(reserva);

        getTotalReserva(reserva);

        return reserva;
    }

    /**
     * Obtém todas as reservas
     *
     * @return ObservableList de todas as reservas
     * @throws Exception Se houver algum erro.
     */
    public ObservableList<Reserva> getReservas() {
        try {
            List<Reserva> reservas = ReservaDAL.getReservas();

            for (Reserva reserva : reservas) {
                getTotalReserva(reserva);
            }

            ObservableList<Reserva> lista = FXCollections.observableArrayList(reservas);
            return lista;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtém todas as reservas com ticket(estacionamento)
     *
     * @param idUtilizador ID de utilizador
     * @return ObservableList de todas as reservas com ticket(estacionamento)
     * @throws SQLException Se houver algum erro ao acessar o banco de dados.
     */
    public List<Reserva> getReservasComTicket(int idUtilizador) throws SQLException {
        ReservaDAL rDAL = new ReservaDAL();
        return rDAL.getReservasComTicket(idUtilizador);
    }

    /**
     * Obtém todas as reservas de um utilizador
     *
     * @param clientName nome utilizador
     * @return ObservableList de todas as reservas do utilizador
     * @throws SQLException Se houver algum erro ao acessar o banco de dados.
     */
    public List<Reserva> searchReservationsByClientName(String clientName) {
        ReservaDAL reservationDAL = new ReservaDAL();
        List<Reserva> reservations = null;
        try {
            reservations = reservationDAL.searchReservationsByClientName(clientName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Apaga uma reserva
     *
     * @param selectedReservation ID da Reserva
     * @return ObservableList de todas as reservas do utilizador
     */
    public static void deleteReservation(Reserva selectedReservation) {
        try {
            ReservaDAL.deleteReservation(selectedReservation.getId());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Atualiza a informação da reserva no banco de dados.
     *
     * @param reserva objeto da classe Reserva com as informações a serem atualizadas
     * @throws SQLException caso ocorra algum erro na atualização dos dados na base de dados.
     */
    public static void updateReserva(Reserva reserva) throws SQLException {
        ReservaDAL reservaDAL = new ReservaDAL();
        reservaDAL.updateReserva(reserva);
    }

    /**
     * Obtém o total dos serviços associados a uma reserva específica.
     *
     * @param reservationId ID da reserva
     * @return O valor total dos serviços associados à reserva
     * @throws SQLException caso ocorra algum erro ao acessar o banco de dados.
     */
    private static double getTotalServicosReserva(Integer reservationId) throws SQLException {
        ReservaDAL reservaDAL = new ReservaDAL();
        return reservaDAL.getTotalServicosReserva(reservationId);
    }

    /**
     * Obtém o total dos produtos associados a uma reserva específica.
     *
     * @param reservationId ID da reserva
     * @return O valor total dos produtos associados à reserva
     * @throws SQLException caso ocorra algum erro ao acessar o banco de dados.
     */
    private static double getTotalProdutosReserva(Integer reservationId) throws SQLException {
        ReservaDAL reservaDAL = new ReservaDAL();
        return reservaDAL.getTotalProdutosReserva(reservationId);
    }

    /**
     * Obtém o valor total da reserva, incluindo o quarto, os produtos e os serviços associados.
     *
     * @param reserva objeto da classe Reserva que representa a reserva a ser calculada
     * @return O valor total da reserva
     * @throws SQLException caso ocorra algum erro ao acessar o banco de dados.
     */
    public static double getTotalReserva(Reserva reserva) throws SQLException {
        QuartoDAL quartoDAL = new QuartoDAL();
        double precoQuarto = quartoDAL.getPreco(reserva.getIdQuarto());

        double precoFinal = 0;

        // Calculo do valor diário
        LocalDate fim = LocalDate.parse(reserva.getDataFim());
        LocalDate inicio = LocalDate.parse(reserva.getDataInicio());
        long diasReserva = ChronoUnit.DAYS.between(inicio, fim);

        precoFinal += diasReserva * precoQuarto;

        // Calculo do valor dos produtos RESERVA
        precoFinal += getTotalProdutosReserva(reserva.getId());


        // Calculo do valor dos servicos
        precoFinal += getTotalServicosReserva(reserva.getId());

        reserva.setPreco(precoFinal);

        return precoFinal;
    }

    /**
     * Cancela uma reserva específica.
     *
     * @param reservationId ID da reserva a ser cancelada
     * @throws SQLException caso ocorra algum erro ao acessar o banco de dados.
     */
    public void cancelReservation(int reservationId) throws SQLException {
        ReservaDAL reservaDAL = new ReservaDAL();
        reservaDAL.cancelReservation(reservationId);
    }

    /**
     * Obtém a lista de datas de início de todas as reservas associadas a um quarto específico.
     *
     * @param idQuarto ID do quarto a ser consultado
     * @return Lista de datas de início de todas as reservas associadas ao quarto.
     */
    public List<LocalDate> getDataInicial(int idQuarto) {
        ReservaDAL reservaDAL = new ReservaDAL();
        List<LocalDate> dataInicial = reservaDAL.getDataInicial(idQuarto);
        return dataInicial;
    }

    /**
     * Obtém a lista de datas de fim de todas as reservas associadas a um quarto específico.
     *
     * @param idQuarto ID do quarto a ser consultado
     * @return Lista de datas de fim de todas as reservas associadas ao quarto.
     */
    public List<LocalDate> getDataFinal(int idQuarto) {
        ReservaDAL reservaDAL = new ReservaDAL();
        List<LocalDate> dataFinal = reservaDAL.getDataFinal(idQuarto);
        return dataFinal;
    }

    /**
     * Verifica se existe alguma reserva associada a um quarto específico.
     *
     * @param idQuarto ID do quarto a ser consultado
     * @return True, caso exista alguma reserva associada ao quarto. False, caso contrário.
     */
    public Boolean verificaSeReservaExiste(int idQuarto) {
        ReservaDAL reservaDAL = new ReservaDAL();
        if (reservaDAL.verificaSeExisteReserva(idQuarto)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Obtém a próxima data disponível para reserva de um quarto específico.
     *
     * @param idQuarto ID do quarto a ser consultado
     * @param ultData  Última data para o qual se deseja verificar a disponibilidade
     * @return Próxima data disponível para reserva do quarto, após a data informada.
     */
    public LocalDate getProxData(int idQuarto, LocalDate ultData) {
        ReservaDAL reservaDAL = new ReservaDAL();
        LocalDate proxData = reservaDAL.getProximaData(idQuarto, ultData);
        return proxData;
    }

    /**
     * Faz uma nova reserva de estacionamento para uma reserva existente e atualiza a reserva com informações do ticket.
     *
     * @param reserva    Reserva existente para a qual se deseja adicionar a reserva de estacionamento.
     * @param ticketInfo Informações necessárias para a criação da reserva de estacionamento.
     * @return Reserva atualizada com informações do ticket de estacionamento.
     * @throws RuntimeException Caso ocorra algum erro ao acessar a base de dados ou ao realizar a reserva de estacionamento.
     */
    public Reserva reservaEstacionamento(Reserva reserva, TicketInfo ticketInfo) {
        try {
            var reservaDAL = new ReservaDAL();
            var estacionamentoBLL = new EstacionamentoBLL();

            var pedido = estacionamentoBLL.POSTCreateParkingReservation(ticketInfo);
            reservaDAL.updateReservaComResponseTicketID(reserva, pedido);

            return reserva;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método para atualizar o ID do ticket na reserva para nulo quando um ticket é apagado.
     *
     * @param ticketID ID do ticket a ser apagado.
     */
    public void updateTicketIDNaReservaToNullQuandoApagaTicket(String ticketID) {
        ReservaDAL reservaDAL = new ReservaDAL();
        reservaDAL.updateTicketIDNaReservaToNullQuandoApagaTicket(ticketID);
    }

    /**
     * Método que retorna o QR code de uma reserva.
     *
     * @param idReserva ID da reserva para a qual se quer o QR code.
     * @return O QR code associado à reserva especificada.
     * @throws Exception Em caso de erro na obtenção do QR code.
     */
    public String retornaQRCodeDDeUmaReserva(String idReserva) {
        try {
            ReservaDAL reservaDAL = new ReservaDAL();
            EstacionamentoBLL eBLL = new EstacionamentoBLL();

            String ticketId = reservaDAL.retornaTicketIDDeUmaReserva(idReserva);

            Ticket ticket = eBLL.GETParkingTicket(ticketId);

            return ticket.ParkingQR;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Método que retorna o ID de ticket de uma reserva.
     *
     * @param idReserva ID da reserva para a qual se quer o ID de ticket.
     * @return O ID de ticket associado à reserva especificada.
     * @throws Exception Em caso de erro na obtenção do ID de ticket.
     */
    public String retornaTicketIDDeUmaReserva(String idReserva) {
        try {
            ReservaDAL reservaDAL = new ReservaDAL();
            return reservaDAL.retornaTicketIDDeUmaReserva(idReserva);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
