package DAL;

import BLL.EstacionamentoBLL;
import Model.EstacionamentoAPI.ResponseTicket;
import Model.EstacionamentoAPI.TicketInfo;
import Model.MessageBoxes;
import Model.Reserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAL {

    /**
     * A função `addReserva` serve para adicionar uma reserva à base de dados.
     *
     * @param reserva Um objeto do tipo `Reserva` que representa a reserva a ser adicionada.
     * @return Um objeto do tipo `Reserva` que representa a reserva adicionada, com um ID gerado automaticamente.
     * @throws SQLException Em caso de erro ao acessar a base de dados, uma exceção será lançada com informações sobre o erro.
     */
    public Reserva addReserva(Reserva reserva) throws SQLException {
        PreparedStatement ps = null;
        int reservationId = -1;
        Connection connection = null;
        try {
            DBconn dbConn = new DBconn();
            connection = dbConn.getConn();
            ps = connection.prepareStatement("INSERT INTO Reserva(idCliente, idQuarto," +
                    "dataInicio, dataFim, preco)" +
                    "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, reserva.getIdCliente());
            ps.setInt(2, reserva.getIdQuarto());
            ps.setString(3, reserva.getDataInicio());
            ps.setString(4, reserva.getDataFim());
            ps.setDouble(5, reserva.getPreco());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                reservationId = generatedKeys.getInt(1);
            }
            if (reservationId > 0) {
                addReservationState(reservationId, "pendente");
                transferProdutosToProdutoReserva(reservationId, reserva.getIdQuarto());
            }
            reserva.setId(reservationId);
            return reserva;
        } catch (SQLException e) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Preencher todos os campos!", "Erro:");
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * A função transferProdutosToProdutoReserva serve para transferir os produtos de uma reserva para a tabela ProdutoReserva.
     *
     * @param reservationId recebe o id da reserva
     * @param roomId        recebe o id do quarto
     */
    private void transferProdutosToProdutoReserva(int reservationId, int roomId) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement psSelect = connection.prepareStatement("SELECT idProduto, quantidade FROM ProdutoQuarto WHERE idQuarto = ?");
             PreparedStatement psInsert = connection.prepareStatement("INSERT INTO ProdutoReserva (idReserva, idProduto, quantidade, idQuarto) VALUES (?,?,?,?)")) {

            psSelect.setInt(1, roomId);
            ResultSet rs = psSelect.executeQuery();

            while (rs.next()) {
                String idProduto = rs.getString("idProduto");
                int quantidade = rs.getInt("quantidade");

                psInsert.setInt(1, reservationId);
                psInsert.setString(2, idProduto);
                psInsert.setInt(3, quantidade);
                psInsert.setInt(4, roomId);
                psInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A função getReservas devolve uma lista de objetos Reserva recuperados do banco de dados.
     *
     * @return Devolve uma lista observável de objetos Reserva
     */
    public static ObservableList<Reserva> getReservas() {
        ObservableList<Reserva> reservas = FXCollections.observableArrayList();
        try {
            String cmd = "SELECT * FROM Reserva";
            PreparedStatement ps = DBconn.getConn().prepareStatement(cmd);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reserva reserva = new Reserva(rs.getInt("id"), rs.getInt("idCliente"), rs.getInt("idQuarto"),
                        rs.getString("dataInicio"), rs.getString("dataFim"),
                        rs.getDouble("preco"));
                reservas.add(reserva);
            }
            ps.close();
        } catch (Exception ex) {
        }
        return reservas;
    }

    /**
     * Obtém a lista de reservas pendentes (com estado "pendente" ou "checkin").
     *
     * @return a lista de reservas pendentes.
     */
    public static List<Reserva> getReservasPendentes() {
        List<Reserva> reservas = new ArrayList<>();
        try {
            String cmd = "SELECT r.*, u.nome FROM Reserva r " +
                    "INNER JOIN EstadoReserva e ON r.id = e.reserva "+
                    "INNER JOIN Utilizador u ON r.idCliente = u.id " +
                    "WHERE e.estado = 'pendente' OR e.estado = 'checkin'";
            Statement st = DBconn.getConn().createStatement();
            ResultSet rs = st.executeQuery(cmd);
            while (rs.next()) {
                Reserva reserva = new Reserva(rs.getInt("id"), rs.getInt("idCliente"), rs.getInt("idQuarto"),
                        rs.getString("dataInicio"), rs.getString("dataFim"),
                        rs.getDouble("preco"), "", rs.getString("nome"));
                reservas.add(reserva);
            }
            st.close();
        } catch (Exception ex) {
        }
        return reservas;
    }

    /**
     * Verifica se o quarto está disponível no determinado data de início.
     *
     * @param roomId    ID do quarto
     * @param startDate Data de início
     * @return Retorna verdadeiro se o quarto estiver disponível, falso caso contrário
     * @throws SQLException Se ocorrer um erro durante a execução da consulta SQL
     */
    public static boolean isRoomAvailable(int roomId, LocalDate startDate) throws SQLException {
        String query = "SELECT dataInicio, dataFim FROM Reserva WHERE idQuarto = ? And dataInicio = ?";
        try (PreparedStatement stmt = DBconn.getConn().prepareStatement(query)) {
            stmt.setInt(1, roomId);
            stmt.setString(2, startDate.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                return !rs.next();
            }
        }
    }

    /**
     * Atualiza o preço de uma reserva.
     *
     * @param reservationId ID da reserva a ser atualizada.
     * @param newPrice      Novo preço da reserva.
     * @throws SQLException caso haja algum problema com a conexão com o banco de dados.
     */
    public void updateReservationPrice(int reservationId, double newPrice) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            DBconn dbConn = new DBconn();
            connection = dbConn.getConn();
            ps = connection.prepareStatement("UPDATE Reserva SET preco = ? WHERE id = ?");
            ps.setDouble(1, newPrice);
            ps.setInt(2, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * A função searchReservationByClientName serve para procurar a reserva pelo nome do cliente
     *
     * @param clientName recebe o nome do cliente
     * @return devolve a reserva
     * @throws SQLException mostra a informacao do erro
     */
    public List<Reserva> searchReservationsByClientName(String clientName) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        ResultSet rs = null;
        List<Reserva> reservations = new ArrayList<>();
        try {
            DBconn dbConn = new DBconn();
            connection = dbConn.getConn();
            ps = connection.prepareStatement("SELECT r.id, r.idCliente, r.idQuarto, r.dataInicio, r.dataFim, r.preco FROM Reserva r JOIN Utilizador u ON r.idCliente = u.id WHERE u.nome LIKE ?");
            ps.setString(1, "%" + clientName + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                Reserva reservation = new Reserva();
                reservation.setId(rs.getInt("id"));
                reservation.setIdCliente(rs.getInt("idCliente"));
                reservation.setIdQuarto(rs.getInt("idQuarto"));
                reservation.setDataInicio(String.valueOf(rs.getDate("dataInicio")));
                reservation.setDataFim(String.valueOf(rs.getDate("dataFim")));
                reservation.setPreco(rs.getDouble("preco"));
                reservations.add(reservation);
            }
            return reservations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * A função deleteReservation serve para eliminar uma reserva
     *
     * @param reservationId recebe o id da reserva
     * @throws SQLException mostra a informacao do erro
     */
    public static void deleteReservation(int reservationId) throws SQLException {
        String cmd = "SELECT estado FROM EstadoReserva WHERE reserva = ?";
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();
        PreparedStatement ps = connection.prepareStatement(cmd);
        ps.setInt(1, reservationId);
        ResultSet rs = ps.executeQuery();
        String estado = null;
        if (rs.next()) {
            estado = rs.getString("estado");
        }
        ps.close();
        if (estado == null || estado.equals("checkout") || estado.equals("checkin") || estado.equals("cancelada")) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Não é possível apagar esta reserva.", "Erro");
        } else {
            EstacionamentoBLL bll = new EstacionamentoBLL();
            String ticketId = getTicketIdForReservation(reservationId);
            if (ticketId != null) {
                bll.DeleteTicket(ticketId);
            }
            deleteEstadoReservaForReservation(reservationId);
            deleteProdutoReserva(reservationId);
            deleteServicoReservaForReservation(reservationId);
            deleteCheckoutForReservation(reservationId);
            deleteReservationById(reservationId);
            MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Reserva pendente apagada!", "Apagada:");
        }
    }


    /**
     * Atualiza a tabela de reservas após a exclusão de um ticket.
     *
     * @param reservationId ID da reserva a ser atualizada
     * @throws SQLException caso ocorra algum erro durante a atualização da tabela
     */
    private static void updateReservaTableAfterTicketDeletion(int reservationId) throws SQLException {
        String cmd = "UPDATE Reserva SET ticketID = null WHERE id = ?";
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();
        PreparedStatement ps = connection.prepareStatement(cmd);
        ps.setInt(1, reservationId);
        ps.executeUpdate();
        ps.close();
    }


    /**
     * Obtém o ID do bilhete associado à reserva especificada.
     *
     * @param reservationId O ID da reserva.
     * @return O ID do bilhete, ou nulo caso a reserva não esteja associada a um ticket.
     * @throws SQLException Caso ocorra algum erro na execução da consulta à base de dados.
     */
    private static String getTicketIdForReservation(int reservationId) throws SQLException {
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
     * Método responsável por apagar uma reserva específica da tabela 'ProdutoReserva'.
     *
     * @param reservationId O identificador da reserva.
     * @throws SQLException Caso ocorra algum erro durante a conexão com o banco de dados.
     */
    private static void deleteProdutoReserva(int reservationId) throws SQLException {

        String cmd = ("DELETE FROM ProdutoReserva WHERE idReserva = ?");
        executeDelete(cmd, reservationId);
    }

    /**
     * Método responsável por apagar as informações de estado de uma reserva específica na tabela 'EstadoReserva'.
     *
     * @param reservationId O identificador da reserva.
     * @throws SQLException Caso ocorra algum erro durante a conexão com o banco de dados.
     */
    private static void deleteEstadoReservaForReservation(int reservationId) throws SQLException {
        String cmd = "DELETE FROM EstadoReserva WHERE reserva = ?";

        executeDelete(cmd, reservationId);
    }

    /**
     * Método responsável por apagar as informações de serviço de uma reserva específica na tabela 'ServicoReserva'.
     *
     * @param reservationId O identificador da reserva.
     * @throws SQLException Caso ocorra algum erro durante a conexão com o banco de dados.
     */
    private static void deleteServicoReservaForReservation(int reservationId) throws SQLException {
        String cmd = "DELETE FROM ServicoReserva WHERE idReserva = ?";
        executeDelete(cmd, reservationId);
    }

    /**
     * Método responsável por apagar as informações de checkout de uma reserva específica na tabela 'Checkout'.
     *
     * @param reservationId O identificador da reserva.
     * @throws SQLException Caso ocorra algum erro durante a conexão com o banco de dados.
     */
    private static void deleteCheckoutForReservation(int reservationId) throws SQLException {
        String cmd = "DELETE FROM Checkout WHERE reservaId = ?";
        executeDelete(cmd, reservationId);
    }


    /**
     * Método responsável por apagar uma reserva específica da tabela 'Reserva'.
     *
     * @param reservationId O identificador da reserva.
     * @throws SQLException Caso ocorra algum erro durante a conexão com o banco de dados.
     */
    private static void deleteReservationById(int reservationId) throws SQLException {
        String cmd = "DELETE FROM Reserva WHERE id = ?";
        executeDelete(cmd, reservationId);
    }

    /**
     * Método privado auxiliar para executar operações de apagar.
     *
     * @param cmd           O comando SQL a ser executado.
     * @param reservationId O identificador da reserva.
     * @throws SQLException Caso ocorra algum erro durante a conexão com o banco de dados.
     */
    private static void executeDelete(String cmd, int reservationId) throws SQLException {
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();
        PreparedStatement ps = connection.prepareStatement(cmd);
        ps.setInt(1, reservationId);
        ps.executeUpdate();
        ps.close();
    }

    /**
     * Método para obter as reservas de um utilizador que possuem um ticket associado.
     *
     * @param idUtilizador O ID do utilizador cujas reservas se deseja obter
     * @return Uma lista de objetos Reserva, onde cada reserva é uma reserva do utilizador que possui um ticket associado
     * @throws SQLException caso ocorra um erro ao aceder à base de dados
     */
    public static List<Reserva> getReservasComTicket(int idUtilizador) throws SQLException {
        String cmd = "SELECT r.*, u.nome FROM Reserva r " +
                "INNER JOIN EstadoReserva er ON r.id = er.reserva " +
                "INNER JOIN Utilizador u ON r.idCliente = u.id " +
                "WHERE r.idCliente = ? AND r.ticketID IS NOT NULL AND (er.estado = 'pendente' OR er.estado = 'checkin')";
        DBconn dbconn = new DBconn();
        Connection connection = dbconn.getConn();
        PreparedStatement ps = connection.prepareStatement(cmd);
        ps.setInt(1, idUtilizador);
        ResultSet rs = ps.executeQuery();
        List<Reserva> reservas = new ArrayList<>();
        while (rs.next()) {
            Reserva reserva = new Reserva(rs.getInt("id"), rs.getInt("idCliente"),
                    rs.getInt("idQuarto"), rs.getString("dataInicio"), rs.getString("dataFim"),
                    rs.getDouble("preco"), rs.getObject("ticketID").toString(), rs.getString("nome"));
            reservas.add(reserva);
        }
        return reservas;
    }

    /**
     * Atualiza as informações de uma reserva existente na base de dados.
     *
     * @param reserva A reserva com as novas informações.
     * @throws SQLException Caso ocorra algum erro na execução da query SQL.
     */
    public void updateReserva(Reserva reserva) throws SQLException {
        String sql = "UPDATE Reserva SET idCliente = ?, idQuarto = ?, dataInicio = ?, dataFim = ?, preco = ? WHERE id = ?";

        try (Connection conn = DBconn.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reserva.getIdCliente());
            stmt.setInt(2, reserva.getIdQuarto());
            stmt.setString(3, reserva.getDataInicio());
            stmt.setString(4, reserva.getDataFim());
            stmt.setDouble(5, reserva.getPreco());
            stmt.setInt(6, reserva.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Atualiza o ticketID da reserva no banco de dados.
     *
     * @param reserva        a reserva a ser atualizada
     * @param responseTicket o ticket da resposta para atualizar o ticketID da reserva
     * @throws SQLException se ocorrer algum erro com o banco de dados
     */
    public void updateReservaComResponseTicketID(Reserva reserva, ResponseTicket responseTicket) throws SQLException {
        String sql = "UPDATE Reserva SET ticketID = ? WHERE id = ?";

        try (Connection conn = DBconn.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, responseTicket.TicketId);
            stmt.setInt(2, reserva.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Calcula o preço total dos serviços de uma reserva específica.
     *
     * @param reservationId o ID da reserva para calcular o preço total dos serviços
     * @return o preço total dos serviços para a reserva especificada, ou 0 se não houver serviços para a reserva
     * @throws SQLException se houver um erro ao acessar o banco de dados
     */
    public double getTotalServicosReserva(int reservationId) throws SQLException {
        String sql = "SELECT SUM(s.preco) as preco " +
                "FROM Reserva r " +
                "JOIN ServicoReserva sr ON r.id = sr.idReserva " +
                "JOIN Servico s ON s.id = sr.idServico " +
                "WHERE r.id = ?";

        try (Connection conn = DBconn.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("preco");
                }
            }
        }

        return 0;
    }

    /**
     * Obtém o total de produtos associados a uma reserva.
     *
     * @param reservationId O ID da reserva.
     * @return O total dos produtos associados à reserva.
     * @throws SQLException Se houver algum erro ao acessar o banco de dados.
     */
    public double getTotalProdutosReserva(int reservationId) throws SQLException {
        double total = 0;

        String sql = "SELECT SUM(pr.quantidade * p.precoParaCliente) AS total " +
                "FROM Reserva r INNER JOIN ProdutoReserva PR on r.id = PR.idReserva " +
                "INNER JOIN Produto P on P.id = PR.idProduto WHERE r.id = ?";

        Connection conn = DBconn.getConn();

        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, reservationId);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            total = rs.getDouble("total");
        }

        rs.close();
        stmt.close();
        conn.close();

        return total;
    }

    /**
     * Adiciona um estado à uma reserva.
     *
     * @param reservationId ID da reserva
     * @param estado        Estado da reserva
     * @throws SQLException Se houver algum erro com a conexão com o banco de dados
     */
    public void addReservationState(int reservationId, String estado) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            DBconn dbConn = new DBconn();
            connection = dbConn.getConn();
            ps = connection.prepareStatement("INSERT INTO EstadoReserva(reserva, estado) VALUES (?, ?)");
            ps.setInt(1, reservationId);
            ps.setString(2, estado);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Atualiza o estado da reserva identificada pelo ID fornecido.
     *
     * @param reservationId O ID da reserva a ser atualizada.
     * @param estado        O novo estado da reserva.
     * @throws SQLException Se houver algum problema ao acessar o banco de dados.
     */
    public void updateReservationState(int reservationId, String estado) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            DBconn dbConn = new DBconn();
            connection = dbConn.getConn();
            ps = connection.prepareStatement("UPDATE EstadoReserva SET estado = ? WHERE reserva = ?");
            ps.setString(1, estado);
            ps.setInt(2, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Obtém as reservas associadas a um determinado estado de reserva.
     *
     * @param estadoReserva o estado de reserva para o qual se pretendem obter as reservas
     * @return uma lista observável de reservas associadas ao estado de reserva fornecido
     */
    public static ObservableList<Reserva> getReservasByEstadoReserva(String estadoReserva) {
        ObservableList<Reserva> reservas = FXCollections.observableArrayList();
        try {
            String cmd = "SELECT r.* FROM Reserva r INNER JOIN EstadoReserva e ON r.id = e.reserva WHERE e.estado = ?";
            PreparedStatement ps = DBconn.getConn().prepareStatement(cmd);
            ps.setString(1, estadoReserva);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reserva reserva = new Reserva(rs.getInt("id"), rs.getInt("idCliente"), rs.getInt("idQuarto"),
                        rs.getString("dataInicio"), rs.getString("dataFim"),
                        rs.getDouble("preco"));
                reservas.add(reserva);
            }
            ps.close();
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
        }
        return reservas;
    }

    /**
     * Cancela uma reserva especificada.
     *
     * @param reservationId o ID da reserva a ser cancelada
     * @throws SQLException caso ocorra um erro de SQL
     */
    public void cancelReservation(int reservationId) throws SQLException {
        String cmd = "SELECT estado FROM EstadoReserva WHERE reserva = ?";
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();
        PreparedStatement ps = connection.prepareStatement(cmd);
        ps.setInt(1, reservationId);
        ResultSet rs = ps.executeQuery();
        String estado = null;
        if (rs.next()) {
            estado = rs.getString("estado");
        }
        ps.close();
        if (estado == null || estado.equals("checkout")) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Não é possível cancelar esta reserva.", "Erro");
        } else {
            if (estado == null || estado.equals("cancelada")) {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "A reserva já se encontra cancelada.", "Erro");
            } else {
                if (estado.equals("checkin")) {
                    returnProductToStock(reservationId);
                }
                String ticketId = getTicketIdForReservation(reservationId);
                if (ticketId != null) {
                    EstacionamentoBLL bll = new EstacionamentoBLL();
                    bll.DeleteTicket(ticketId);
                    updateReservaTableAfterTicketDeletion(reservationId);
                }
                cmd = "UPDATE EstadoReserva SET estado = 'cancelada' WHERE reserva = ?";
                ps = connection.prepareStatement(cmd);
                ps.setInt(1, reservationId);
                ps.executeUpdate();
                ps.close();
                MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Reserva cancelada!", "Sucesso:");
            }
        }
    }

    /**
     * Método que devolve um produto para o stock.
     *
     * @param reservationId ID da reserva associada ao produto a ser devolvido.
     * @throws SQLException Se houver erro ao acessar o banco de dados.
     */
    private void returnProductToStock(int reservationId) throws SQLException {
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        connection.setAutoCommit(false);

        try {
            String query = "SELECT idProduto, quantidade FROM ProdutoReserva WHERE idReserva = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, reservationId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String productId = rs.getString("idProduto");
                int quantity = rs.getInt("quantidade");

                updateStock(productId, quantity, connection);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Atualiza o stock de um produto específico.
     *
     * @param productId  ID do produto a ser atualizado
     * @param quantity   Quantidade a ser adicionada ao estoque do produto
     * @param connection Conexão com a base de dados
     * @throws SQLException Se ocorrer algum erro na atualização do estoque do produto
     */
    private void updateStock(String productId, int quantity, Connection connection) throws SQLException {
        String query2 = "UPDATE Stock set quantidade = quantidade + ? WHERE idProduto = ?";
        PreparedStatement ps2 = connection.prepareStatement(query2);
        ps2.setInt(1, quantity);
        ps2.setString(2, productId);
        ps2.executeUpdate();
    }

    /**
     * Recupera a data inicial de todas as reservas ativas ou com check-in pendente para um quarto específico.
     *
     * @param idQuarto ID do quarto para o qual deseja-se recuperar as datas iniciais de reserva.
     * @return Uma lista de objetos LocalDate representando as datas iniciais de todas as reservas ativas ou com check-in pendente para o quarto especificado. Retorna null em caso de erro.
     */
    public List<LocalDate> getDataInicial(int idQuarto) {
        List<LocalDate> datasIniciais = new ArrayList<>();
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();
            String cmd = "SELECT r.dataInicio FROM Reserva r " +
                    "INNER JOIN Quarto q on q.id = r.idQuarto " +
                    "INNER JOIN EstadoReserva er ON r.id = er.reserva " +
                    "WHERE q.id = ? AND (er.estado = 'checkin' OR er.estado = 'pendente')";
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.setInt(1, idQuarto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                java.sql.Date data = rs.getDate("dataInicio");
                datasIniciais.add(data.toLocalDate());
            }
            ps.close();
            return datasIniciais;
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * Obtém as datas finais de reserva de um quarto específico.
     *
     * @param idQuarto ID do quarto
     * @return lista de datas finais ou nulo em caso de erro
     * @throws SQLException em caso de problemas com o banco de dados
     */
    public List<LocalDate> getDataFinal(int idQuarto) {
        List<LocalDate> datasFinais = new ArrayList<>();
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();
            String cmd = "SELECT r.dataFim FROM Reserva r " +
                    "INNER JOIN Quarto q on q.id = r.idQuarto " +
                    "INNER JOIN EstadoReserva er ON r.id = er.reserva " +
                    "WHERE q.id = ? AND (er.estado = 'checkin' OR er.estado = 'pendente')";
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.setInt(1, idQuarto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                java.sql.Date data = rs.getDate("dataFim");
                datasFinais.add(data.toLocalDate());
            }
            ps.close();
            return datasFinais;
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * Retorna a próxima data de uma reserva em que o quarto especificado esteja ocupado.
     *
     * @param idQuarto O ID do quarto a ser verificado
     * @param ultData  A data mais recente da verificação
     * @return A próxima data em que o quarto estiver ocupado, ou null se não houver reservas futuras
     * @throws SQLException em caso de erro de acesso ao banco de dados
     */
    public LocalDate getProximaData(int idQuarto, LocalDate ultData) {
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();
            String cmd = "SELECT TOP 1 r.dataInicio FROM Reserva r " +
                    "INNER JOIN EstadoReserva er ON r.id = er.reserva " +
                    "WHERE r.dataInicio > ? " +
                    "AND r.idQuarto = ? AND (er.estado = 'checkin' OR er.estado = 'pendente') " +
                    "ORDER BY r.dataInicio ASC";
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.setDate(1, Date.valueOf((ultData)));
            ps.setInt(2, idQuarto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                java.sql.Date proxData = rs.getDate("dataInicio");
                return proxData.toLocalDate();
            }
            ps.close();
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * Verifica se existe uma reserva para o quarto especificado.
     *
     * @param idQuarto O ID do quarto a ser verificado.
     * @return Retorna verdadeiro se existir uma reserva para o quarto especificado, falso se não existir e nulo em caso de erro.
     */
    public Boolean verificaSeExisteReserva(int idQuarto) {
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();
            String cmd = "SELECT r.id FROM Reserva r " +
                    "INNER JOIN Quarto q on q.id = r.idQuarto " +
                    "WHERE q.id = ?";
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.setInt(1, idQuarto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps.close();
                return true;
            } else {
                ps.close();
                return false;
            }
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * Atualiza a coluna ticketID na tabela Reserva para nulo quando um ticket é apagado.
     *
     * @param ticketID ID do ticket que foi apagado
     */
    public void updateTicketIDNaReservaToNullQuandoApagaTicket(String ticketID) {
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();
            String query2 = "UPDATE Reserva set ticketID = null WHERE ticketID = ?";
            PreparedStatement ps2 = connection.prepareStatement(query2);
            ps2.setString(1, ticketID);
            ps2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recebe por parametro o ID de uma reserva e retorna o ID do ticket.
     *
     * @param idReserva ID do ticket correspondente a reserva
     */
    public String retornaTicketIDDeUmaReserva(String idReserva) {
        try {
            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();
            String query2 = "SELECT Reserva.ticketID FROM Reserva WHERE Reserva.id = ?";
            PreparedStatement ps2 = connection.prepareStatement(query2);
            ps2.setString(1, idReserva);
            ResultSet rs = ps2.executeQuery();
            while (rs.next()) {
                String ticketID = rs.getString("ticketID");
                return ticketID;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}