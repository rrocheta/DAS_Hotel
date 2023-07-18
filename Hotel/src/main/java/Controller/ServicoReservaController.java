package Controller;

import BLL.ReservaBLL;
import BLL.ServicoBLL;
import BLL.ServicoReservaBLL;
import DAL.ReservaDAL;
import Model.MessageBoxes;
import Model.Reserva;
import Model.Servico;
import Model.ServicoReserva;
import com.example.hotel.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ServicoReservaController implements Initializable {

    @FXML
    private Button btnAdicionar;

    @FXML
    private Button btnRemover;

    @FXML
    private Button Voltar;

    @FXML
    private Label precoLabel;


    @FXML
    private ComboBox<Reserva> cmbReserva;

    @FXML
    private ListView<Servico> listViewServicos;


    @FXML
    void btnAdicionar(ActionEvent event) throws IOException, SQLException {
        Reserva selectedReservation = cmbReserva.getValue();
        if (selectedReservation == null) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Por favor, selecione uma reserva antes de continuar.", "ERRO");
            return;
        }
        Servico selectedServico = listViewServicos.getSelectionModel().getSelectedItem();
        ServicoReserva servicoReserva = new ServicoReserva();
        servicoReserva.setIdReserva(selectedReservation.getId());
        servicoReserva.setIdServico(selectedServico.getIdServico());

        ServicoReservaBLL servicoReservaBLL = new ServicoReservaBLL();
        try {
            servicoReservaBLL.addServicosToReserva(List.of(servicoReserva), selectedReservation.getId());
            MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION,"Serviço inserido à reserva", "Inserido");
            double totalCost = ReservaBLL.getTotalReserva(selectedReservation);
            precoLabel.setText(String.valueOf(totalCost));
        } catch (SQLException e) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR,"Erro ao inserir Serviço", "Erro");
        }
        if (AdicionarReservaController.verifica == true) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Adicionar serviços");
            alert.setContentText("Deseja adicionar mais serviços à esta reserva?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){

            } else {
                addProdutoReserva();
            }
        }
    }


    private void addProdutoReserva() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoProdutoReserva.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) Voltar.getScene().getWindow();
        stage.setTitle("Produtos Reserva");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
        AdicionarReservaController.verifica = false;
    }



    @FXML
    void btnRemover(ActionEvent event) throws SQLException {
        Reserva selectedReservation = cmbReserva.getValue();
        Servico selectedServico = listViewServicos.getSelectionModel().getSelectedItem();
        ServicoReservaBLL servicoReservaBLL = new ServicoReservaBLL();
        servicoReservaBLL.removeServicoFromReservation(selectedReservation, selectedServico);
        MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION,"Removido","Servico removido da reserva");
    }



    @FXML
    void VoltarClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoReservas.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) Voltar.getScene().getWindow();
        stage.setTitle("Gestao CheckIn");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCombos();
        initListView();

        cmbReserva.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                double total = 0;
                try {
                    total = ReservaBLL.getTotalReserva(newValue);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                precoLabel.setText(String.valueOf(total));
            }
        });
    }

    private void initListView() {
        ObservableList<Servico> servicos = ServicoBLL.getAllServicos();
        listViewServicos.setItems(servicos);
    }

    private void initCombos() {
        cmbReserva.getItems().addAll(ReservaDAL.getReservasPendentes());

    }
}
