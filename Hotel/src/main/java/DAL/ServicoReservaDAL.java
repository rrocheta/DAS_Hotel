package DAL;

import Model.MessageBoxes;
import Model.Reserva;
import Model.Servico;
import Model.ServicoReserva;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicoReservaDAL {

    /**
     * Adiciona um serviço a uma reserva existente.
     *
     * @param servicoReserva objeto contendo informações sobre a reserva e o serviço a ser adicionado
     * @throws SQLException se ocorrer um erro ao acessar o banco de dados
     */
    public void addServicoToReserva(ServicoReserva servicoReserva) throws SQLException {
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO ServicoReserva(idReserva, idServico) VALUES (?, ?)");
        ps.setInt(1, servicoReserva.getIdReserva());
        ps.setInt(2, servicoReserva.getIdServico());
        ps.executeUpdate();
    }

    /**
     * Verifica se o serviço já existe para a reserva.
     *
     * @param idReserva ID da reserva a verificar.
     * @param idServico ID do serviço a verificar.
     * @return Retorna verdadeiro se o serviço já existir para a reserva, senão retorna falso.
     * @throws SQLException Lançada se houver algum problema com a consulta ao banco de dados.
     */
    public boolean isServicoExistsForReserva(int idReserva, int idServico) throws SQLException {
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM ServicoReserva WHERE idReserva = ? AND idServico = ?");
        ps.setInt(1, idReserva);
        ps.setInt(2, idServico);
        ResultSet rs = ps.executeQuery();

        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        return count > 0;
    }

    /**
     * Método para remover um serviço de uma reserva.
     *
     * @param selectedReservation reserva selecionada
     * @param selectedServico     serviço selecionado
     * @throws SQLException caso ocorra algum erro ao acessar o banco de dados
     */
    public void removeServicoFromReservation(Reserva selectedReservation, Servico selectedServico) throws SQLException {

        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        PreparedStatement ps = connection.prepareStatement("DELETE FROM ServicoReserva WHERE idReserva = ? AND idServico = ?");
        ps.setInt(1, selectedReservation.getId());
        ps.setInt(2, selectedServico.getIdServico());
        ps.executeUpdate();
    }

}
