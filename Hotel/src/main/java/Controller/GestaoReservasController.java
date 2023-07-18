package Controller;

import BLL.CheckoutBLL;
import BLL.ReservaBLL;
import BLL.UtilizadorPreferences;
import DAL.DBconn;
import DAL.ReservaDAL;
import Model.MessageBoxes;
import Model.Reserva;
import com.example.hotel.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GestaoReservasController implements Initializable {

    @FXML
    private Button adicionarReservaBtn;


    @FXML
    private TextField SearchClienteTxtField;

    @FXML
    private Button btnVoltar;

    @FXML
    private Button adicionarServico;

    @FXML
    private Button eliminarReservaBtn;

    @FXML
    private TableView<Reserva> tblReservas;

    @FXML
    private TableColumn<Reserva, Integer> tblCoIDQuarto;

    @FXML
    private TableColumn<Reserva, Double> tblCoPreco;

    @FXML
    private TableColumn<Reserva, Integer> tblColDReserva;

    @FXML
    private TableColumn<Reserva, String> tblColDataFim;

    @FXML
    private TableColumn<Reserva, String> tblColDataIni;

    @FXML
    private TableColumn<Reserva, Integer> tblCoIDCliente;

    @FXML
    private TableColumn<Reserva, String> tblColServEx;


    @FXML
    private Button ProdutoQuarto;

    @FXML
    private ComboBox<String> cbEstadoReserva;

    @FXML
    private Button cancelarReservaBtn;

    @FXML
    void clickAdicionarReservaBtn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AdicionarReservas.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) adicionarReservaBtn.getScene().getWindow();
        stage.setTitle("Adicionar Reserva");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void clickBtnVoltar(ActionEvent event) throws IOException {
        if (UtilizadorPreferences.comparaTipoLogin()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PainelGestor.fxml"));
            Stage stage = new Stage();
            Stage newStage = (Stage) btnVoltar.getScene().getWindow();
            stage.setTitle("Pagina Gestor");
            newStage.hide();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PainelFuncionario.fxml"));
            Stage stage = new Stage();
            Stage newStage = (Stage) btnVoltar.getScene().getWindow();
            stage.setTitle("Pagina Funcionario");
            newStage.hide();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        }
    }

    @FXML
    void clickEliminarReservaBtn(ActionEvent event) {
        Reserva selectedReservation = tblReservas.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            if (MessageBoxes.ConfirmationBox("Confirma a eliminação da reserva?")) {
                ReservaBLL.deleteReservation(selectedReservation);
                ReservaDAL reservaDAL = new ReservaDAL();
                tblReservas.setItems(reservaDAL.getReservas());
            }
        } else {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Selecione uma reserva para eliminar!", "Erro:");
        }
    }


    private void disableEliminarParaFuncionario() {
        if (!UtilizadorPreferences.comparaTipoLogin()) {
            eliminarReservaBtn.setDisable(true);
        }
    }

    @FXML
    void ServicoReservaClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ServicoReserva.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) btnVoltar.getScene().getWindow();
        stage.setTitle("Servicos Reserva");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    public void RedirectProdutoQuarto(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoProdutoReserva.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) ProdutoQuarto.getScene().getWindow();
        stage.setTitle("Adicionar Tipo de quarto");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void clickCancelar(ActionEvent event) throws SQLException {
        ReservaBLL reservabll = new ReservaBLL();
        CheckoutBLL checkoutBLL = new CheckoutBLL();
        Reserva selectedReservation = tblReservas.getSelectionModel().getSelectedItem();
        if (selectedReservation == null) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Selecione uma reserva para cancelar!", "Erro:");
            return;
        }
        if (MessageBoxes.ConfirmationBox("Confirma o cancelamento da reserva?")) {
            reservabll.cancelReservation(selectedReservation.getId());
            checkoutBLL.voltaNaoConsumiveisAoStock(selectedReservation.getId());
            initTable();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        disableEliminarParaFuncionario();
        initComboBox();
    }

    private void initTable() {
        ReservaDAL reservaDAL = new ReservaDAL();

        tblColDReserva.setCellValueFactory(new PropertyValueFactory<Reserva, Integer>("id"));
        tblCoIDCliente.setCellValueFactory(new PropertyValueFactory<Reserva, Integer>("idCliente"));
        tblCoIDQuarto.setCellValueFactory(new PropertyValueFactory<Reserva, Integer>("idQuarto"));
        tblColDataIni.setCellValueFactory(new PropertyValueFactory<Reserva, String>("dataInicio"));
        tblColDataFim.setCellValueFactory(new PropertyValueFactory<Reserva, String>("dataFim"));
        tblCoPreco.setCellValueFactory(new PropertyValueFactory<Reserva, Double>("preco"));
        if (cbEstadoReserva.getItems().size() == 0) {
            cbEstadoReserva.getItems().addAll("Todos", "pendente", "checkin", "checkout", "cancelada");
        }
        cbEstadoReserva.setValue("Todos");

        tblReservas.setItems(reservaDAL.getReservas());

    }


    private void initComboBox() {
        ReservaDAL reservaDAL = new ReservaDAL();
        tblReservas.setItems(reservaDAL.getReservas());
        if (cbEstadoReserva.getItems().isEmpty()) {
            cbEstadoReserva.getItems().addAll("Todos", "pendente", "checkin", "checkout", "cancelada");
        }
        cbEstadoReserva.setValue("Todos");
        cbEstadoReserva.setOnAction(event -> {
            String estadoReserva = cbEstadoReserva.getSelectionModel().getSelectedItem();
            if (estadoReserva.equals("Todos")) {
                tblReservas.setItems(reservaDAL.getReservas());
            } else {
                tblReservas.setItems(reservaDAL.getReservasByEstadoReserva(estadoReserva));
            }
        });
    }

    //Event listener criado para procurar cliente enquanto se escreve no textField
    @FXML
    void SearchClienteTxtFieldAction(ActionEvent event) {

        SearchClienteTxtField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ReservaBLL bll = new ReservaBLL();
                String name = newValue;
                List<Reserva> reservations = bll.searchReservationsByClientName(name);
                tblReservas.setItems(FXCollections.observableList(reservations));
            } catch (Exception e) {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Introduza o nome corretamente", "Erro");
                throw new RuntimeException(e);
            }
        });

    }
}