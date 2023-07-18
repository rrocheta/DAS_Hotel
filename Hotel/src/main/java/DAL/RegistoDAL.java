package DAL;

import BLL.UtilizadorPreferences;
import Model.Registo;
import Model.Servico;
import com.example.hotel.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistoDAL {

    /**
     * A função getAllRegistos serve para guardar todos os registoos
     *
     * @return devolve uma lista com os registos
     * @throws SQLException mostra a informacao do erro
     */
    public List<Registo> getAllRegistos() throws SQLException {
        List<Registo> registos = new ArrayList<>();

        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Registo");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int idCartao = resultSet.getInt("idCartao");
            int idCliente = resultSet.getInt("idCliente");
            String Local = resultSet.getString("Local");
            Timestamp Data = resultSet.getTimestamp("Data");

            Registo registo = new Registo(id, idCartao, idCliente, Local, Data);
            registos.add(registo);
        }

        return registos;
    }

    /**
     * A função addRegisto serve para adicionar os registos à base de dados
     *
     * @param registo recebe o registo
     * @throws SQLException mostra a informacao do erro
     */
    public void addRegisto(Registo registo) throws SQLException {
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        PreparedStatement ps = connection.prepareStatement("INSERT INTO Registo (idCartao, idCliente, Local, Data) VALUES (?, ?, ?, ?)");
        ps.setInt(1, registo.getIdCartao());
        ps.setInt(2, registo.getIdCliente());
        ps.setString(3, registo.getLocal());
        ps.setTimestamp(4, registo.getData());

        ps.executeUpdate();
    }

    /**
     * A função getCardIdByClientId serve para guardar os cartões de um cliente
     *
     * @param clientId recebe o id de um cliente
     * @return devolve o id do cartão
     * @throws SQLException mostra a informacao do erro
     */
    public static int getCardIdByClientId(int clientId) throws SQLException {
        String sql = "SELECT q.idCartao FROM Reserva r "
                + "JOIN EstadoReserva er ON er.reserva = r.id "
                + "JOIN Quarto q ON q.id = r.idQuarto "
                + "WHERE r.idCliente = ? AND er.estado = 'checkin'";

        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, clientId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("idCartao");
        }
        return -1;
    }

    /**
     * A função getRegistosByCartaoID serve para guardar os registos de um cartao
     *
     * @param idCartao recebe o id do cartao
     * @return devolve uma lista de registos
     * @throws SQLException mostra a informacao do erro
     */
    public static ObservableList<Registo> getRegistosByCartaoId(int idCartao) throws SQLException {
        String cmd = "SELECT * FROM Registo WHERE idCartao = ?";
        DBconn dbconn = new DBconn();
        Connection connection = dbconn.getConn();
        PreparedStatement ps = connection.prepareStatement(cmd);
        ps.setInt(1, idCartao);
        ResultSet rs = ps.executeQuery();
        ObservableList<Registo> registos = FXCollections.observableArrayList();
        while (rs.next()) {
            Registo registo = new Registo(rs.getInt("id"), rs.getInt("idCartao"),
                    rs.getInt("idCliente"), rs.getString("local"), rs.getTimestamp("data"));
            registos.add(registo);
        }
        return registos;
    }

    /**
     * A função deleteRegisto serve para eliminar um registo
     *
     * @param idCliente recebe o id de um cliente
     * @throws SQLException mostra a informacao do erro
     */
    public void deleteRegisto(int idCliente) throws SQLException {
        String cmd = "DELETE FROM Registo WHERE idCliente = ?";
        DBconn dbconn = new DBconn();
        Connection connection = dbconn.getConn();
        PreparedStatement ps = connection.prepareStatement(cmd);
        ps.setInt(1, idCliente);
        ps.executeUpdate();
    }

    /**
     * A função getRegistosByLocal serve para guardar os registos pelo local
     *
     * @param local recebe o local do registo
     * @return devolve a lista dos registos
     * @throws SQLException mostra a informacao do erro
     */
    public static ObservableList<Registo> getRegistosByLocal(String local) throws SQLException {
        String cmd = "SELECT * FROM Registo WHERE local = ?";
        DBconn dbconn = new DBconn();
        Connection connection = dbconn.getConn();
        PreparedStatement ps = connection.prepareStatement(cmd);
        ps.setString(1, local);
        ResultSet rs = ps.executeQuery();
        ObservableList<Registo> registos = FXCollections.observableArrayList();
        while (rs.next()) {
            Registo registo = new Registo(rs.getInt("id"), rs.getInt("idCartao"),
                    rs.getInt("idCliente"), rs.getString("local"), rs.getTimestamp("data"));
            registos.add(registo);
        }
        return registos;
    }
}
