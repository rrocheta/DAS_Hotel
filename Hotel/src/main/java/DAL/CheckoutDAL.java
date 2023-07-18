package DAL;

import BLL.EstacionamentoBLL;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CheckoutDAL {

    /**
     * A função addCheckout serve para adicionar o checkout na base de dados
     *
     * @param checkout recebe os parametros do checkout
     * @throws SQLException mostra a informacaoo do erro de SQL
     */
    public void addCheckout(Checkout checkout) throws SQLException {
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        PreparedStatement ps = connection.prepareStatement("INSERT INTO Checkout (reservaId, preco, metodoPagamento, checkout_date_time) VALUES (?, ?, ?, ?)");
        ps.setInt(1, checkout.getIdReserva());
        ps.setBigDecimal(2, BigDecimal.valueOf(checkout.getPreco()));
        ps.setString(3, checkout.getMetodoPagamento());
        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

        ps.executeUpdate();

        EstacionamentoBLL estacionamentoBLL = new EstacionamentoBLL();
        String ticketId = getTicketIdForReservation(checkout.getIdReserva());
        if (ticketId != null) {
            estacionamentoBLL.DeleteTicket(ticketId);
            updateReservaTableAfterTicketDeletion(checkout.getIdReserva());
        }
    }

    /**
     * A função updateReservaTableAfterTicketDeletion serva para atualizar a tabela
     * ao quando se remove um ticket da reserva
     * @throws SQLException mostra a informacao do erro de SQL
     */
    private void updateReservaTableAfterTicketDeletion(int reservationId) throws SQLException {
        String cmd = "UPDATE Reserva SET ticketID = null WHERE id = ?";
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();
        PreparedStatement ps = connection.prepareStatement(cmd);
        ps.setInt(1, reservationId);
        ps.executeUpdate();
        ps.close();
    }

    /**
     * A função getTicketIdForReservation serva para ir buscar o id do Ticket de uma reserva
     * @return retorna o id do Ticket
     * @throws SQLException mostra a informacao do erro de SQL
     */
    private String getTicketIdForReservation(int reservationId) throws SQLException {
        String cmd = "SELECT ticketID FROM Reserva WHERE id = ?";
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();
        PreparedStatement ps = connection.prepareStatement(cmd);
        ps.setInt(1, reservationId);
        ResultSet rs = ps.executeQuery();
        String ticketId = null;
        if (rs.next()) {
            ticketId = rs.getString("ticketID");
        }
        ps.close();

        return ticketId;
    }

    /**
     * A função getPagamentos serve para obter tipos de pagamentos
     * @return devolve a lista de todos os pagamentos
     * @throws SQLException mostra a informacao do erro de SQL
     */
    public ObservableList<Pagamento> getPagamentos() throws SQLException {
        ObservableList<Pagamento> pagamentos = FXCollections.observableArrayList();

        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Pagamento");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String metodoPagamento = resultSet.getString("metodoPagamento");

            Pagamento pagamento = new Pagamento(id, metodoPagamento);
            pagamentos.add(pagamento);
        }

        return pagamentos;
    }

    /**
     * A função getCheckedInReservations serve para obter todas as reservas em que o checkin foi feito
     *
     * @return devolve as reservas com o checkin feito
     * @throws SQLException mostra a informacao do erro de SQL
     */
    public List<Reserva> getCheckedInReservations() throws SQLException {
        List<Reserva> pendingReservations = new ArrayList<>();

        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        PreparedStatement ps = connection.prepareStatement("SELECT R.* FROM Reserva R INNER JOIN EstadoReserva ER ON R.id = ER.reserva WHERE ER.estado = 'checkin'");
        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int idCliente = resultSet.getInt("idCliente");
            int idQuarto = resultSet.getInt("idQuarto");
            String dataInicio = String.valueOf(resultSet.getDate("dataInicio"));
            String dataFim = String.valueOf(resultSet.getDate("dataFim"));
            Double preco = resultSet.getDouble("preco");
            Reserva reservation = new Reserva(id, idCliente, idQuarto, dataInicio, dataFim, preco);
            pendingReservations.add(reservation);
        }

        return pendingReservations;
    }

    /**
     * A função voltaNaoConsumiveisAoStock serve para os produtos que não são consumiveis voltarem ao stock
     *
     * @param idReserva recebe o id da reserva
     * @throws SQLException mostra a informacao do erro de SQL
     */
    public void voltaNaoConsumiveisAoStock(int idReserva) throws SQLException {
        EntradaStockDAL entradaStockDAL = new EntradaStockDAL();
        ObservableList<Stock> lista = FXCollections.observableArrayList();
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();
            String cmd = "SELECT p.id AS idProduto, pr.quantidade as quantidade " +
                    "FROM ProdutoReserva pr " +
                    "INNER JOIN Reserva r ON pr.idReserva = r.id " +
                    "INNER JOIN Produto p ON pr.idProduto = p.id " +
                    "WHERE r.id = ? AND p.consumivel = 0;";
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Stock obj = new Stock(rs.getString("idProduto"), rs.getInt("quantidade"));
                lista.add(obj);
            }
            entradaStockDAL.updateStock(lista, connection);
            ps.close();
        } catch (Exception ex) {
        }
    }

}

