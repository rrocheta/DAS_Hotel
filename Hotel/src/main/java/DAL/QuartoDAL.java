package DAL;

import Model.Cartao;
import Model.MessageBoxes;
import Model.Quarto;
import Model.TipoUtilizador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class QuartoDAL {

    /**
     * A função addQuarto serve para verificar se é possível adicionar o quarto
     *
     * @param quarto recebe o quarto
     */
    public void addQuarto(Quarto quarto) {
        PreparedStatement ps2;

        try {

            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();

            if (checkRoomAvailability(quarto.getTipoQuarto(), quarto.getPiso())) {

                ps2 = connection.prepareStatement("INSERT INTO Quarto (tipoQuarto,piso,preco,idCartao) VALUES (?,?,?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);


                ps2.setString(1, quarto.getTipoQuarto());
                ps2.setString(2, quarto.getPiso());
                ps2.setDouble(3, quarto.getPreco());
                ps2.setInt(4, quarto.getCartao().getId());

                ps2.executeUpdate();
                MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Quarto criado com sucesso", "Criado!");
            } else {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Não há quartos disponíveis para o tipo e piso selecionado", "Erro!");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * A função checkRoomAvailability serve para verificar se o quarto está disponivél
     *
     * @param roomType recebe o tipo de quarto
     * @param floor    recebe o piso
     * @return devolve se está ou não disponível
     */
    public boolean checkRoomAvailability(String roomType, String floor) {
        try {

            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();

            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM Quarto WHERE tipoQuarto = ? AND piso = ?");
            ps.setString(1, roomType);
            ps.setString(2, floor);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if (floor.equals("1")) {
                    if (roomType.equals("Singular") && count >= 7) {
                        return false;
                    } else if (roomType.equals("Duplo") && count >= 10) {
                        return false;
                    } else if (roomType.equals("Familiar") && count >= 3) {
                        return false;
                    }
                } else if (floor.equals("2")) {
                    if (roomType.equals("Singular") && count >= 7) {
                        return false;
                    } else if (roomType.equals("Duplo") && count >= 10) {
                        return false;
                    } else if (roomType.equals("Familiar") && count >= 3) {
                        return false;
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }


    /**
     * A função updateQuarto serve para atualizar o quarto
     *
     * @param quarto recebe o quarto
     */
    public void updateQuarto(Quarto quarto) {
        PreparedStatement ps2;

        try {

            DBconn dbConn = new DBconn();
            Connection connection = dbConn.getConn();


            ps2 = connection.prepareStatement("UPDATE Quarto SET tipoQuarto = ?, piso = ?, preco = ? WHERE id = ?");


            ps2.setString(1, quarto.getTipoQuarto());
            ps2.setString(2, quarto.getPiso());
            ps2.setDouble(3, quarto.getPreco());
            ps2.setInt(4, quarto.getId());


            ps2.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * A função deleteQuarto serve para eliminar um quarto
     *
     * @param idCartao recebe o id do cartão
     * @throws SQLException mostra a informacao do erro
     */
    public void deleteQuarto(int idCartao) throws SQLException {
        DBconn dbConn = new DBconn();
        Connection connection = dbConn.getConn();

        PreparedStatement ps = connection.prepareStatement("DELETE FROM Quarto WHERE id = ?");
        ps.setInt(1, idCartao);
        ps.executeUpdate();
    }

    /**
     * A função getAllQuartos serve para guardar os quartos
     *
     * @return devolve uma lista de todos os quartos
     */
    public static ObservableList<Quarto> getAllQuartos() {
        ObservableList<Quarto> list = FXCollections.observableArrayList();

        try {
            String cmd = "SELECT q.*, c.id as cId FROM Quarto q " +
                    "LEFT OUTER JOIN Cartao c ON c.Id = q.idCartao";

            Statement st = DBconn.getConn().createStatement();

            ResultSet rs = st.executeQuery(cmd);

            while (rs.next()) {
                Quarto objQuarto = new Quarto(rs.getInt("id"), rs.getString("tipoQuarto"),
                        rs.getString("piso"), rs.getDouble("preco"), rs.getBoolean("ativo"),
                        new Cartao(rs.getInt("cId")));
                list.add(objQuarto);
            }

            st.close();
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
        }
        return list;
    }

    /**
     * A função updateAtivo serve para por o quarto ocupado
     *
     * @param reservationId recebe o id da reserva
     * @param ativo         recebe um boolean
     * @throws SQLException mostra a informacao do erro
     */
    public void updateAtivo(int reservationId, boolean ativo) throws SQLException {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            DBconn dbConn = new DBconn();
            connection = dbConn.getConn();
            ps = connection.prepareStatement("UPDATE Quarto SET ativo = ? WHERE id = (SELECT idQuarto FROM Reserva WHERE id = ?)");
            ps.setBoolean(1, ativo);
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
     * A função getPreco serve para guardar o preço do quarto
     *
     * @param quartoId recebe o id do quarto
     * @return devolve o preço do quarto
     * @throws SQLException mostra a informacao do erro
     */
    public double getPreco(int quartoId) throws SQLException {
        String sql = "SELECT preco " +
                "FROM Quarto q " +
                "WHERE q.id = ?";

        try (Connection conn = DBconn.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quartoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("preco");
                }
            }
        }

        return 0;
    }
}
