package Controller;

import BLL.ReservaBLL;
import BLL.UtilizadorPreferences;
import Model.Reserva;
import com.example.hotel.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class QRCodeClienteController implements Initializable {

    @FXML
    private Button Voltar;

    @FXML
    private ComboBox<Reserva> reservaCmb;

    @FXML
    private ImageView qrImage;

    @FXML
    void VoltarClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PainelCliente.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) Voltar.getScene().getWindow();
        stage.setTitle("Pagina Cliente");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCombos();
        reservaClienteCombobox();
    }

    private void initCombos() {
        ReservaBLL rBLL = new ReservaBLL();
        try {
            List<Reserva> reservasComTicket = rBLL.getReservasComTicket(UtilizadorPreferences.utilizadorId());
            reservaCmb.getItems().addAll(reservasComTicket);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void reservaClienteCombobox() {
        ReservaBLL rBLL = new ReservaBLL();

        reservaCmb.setOnAction(event -> {
            String ticketIDDaReservaSelecionada = rBLL.retornaQRCodeDDeUmaReserva(reservaCmb.getValue().getId().toString());

            qrImage.setImage(new Image(ticketIDDaReservaSelecionada));
        });
    }

}
