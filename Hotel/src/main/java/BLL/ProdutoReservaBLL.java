package BLL;

import DAL.ProdutoReservaDAL;
import Model.MessageBoxes;
import Model.ProdutoReserva;
import javafx.scene.control.Alert;

import java.sql.SQLException;

public class ProdutoReservaBLL {

    /**
     * Adiciona um produto em uma reserva de quarto específica.
     *
     * @param roomId    id da reserva
     * @param productId id do produto
     * @param quantity  quantidade do produto
     * @param idQuarto  id do quarto
     */
    public static void addProductInRoom(int roomId, String productId, int quantity, int idQuarto) {
        try {
            ProdutoReservaDAL.addProductInReservation(roomId, productId, quantity, idQuarto);
        } catch (SQLException e) {
            e.printStackTrace();
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Erro ao adicionar produto no quarto", "Erro");
        }
    }

    /**
     * Método para remover um produto da reserva.
     *
     * @param selectedProduct Produto selecionado para ser removido.
     */
    public static void deleteProductFromRoom(ProdutoReserva selectedProduct) {
        try {
            ProdutoReservaDAL.deleteProductFromReservation(selectedProduct.getId());
            MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Produto removido", "Information");
        } catch (SQLException ex) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Reserva existente com estes dados", "Reserva existente");
            throw new RuntimeException(ex);
        }
    }

}
