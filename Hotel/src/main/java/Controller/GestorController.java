package Controller;

import BLL.UtilizadorPreferences;
import com.example.hotel.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class GestorController {

    @FXML
    private Button btnPassarCartao;
    @FXML
    private Button regCartao;
    @FXML
    private Button GCheck;

    @FXML
    private Button GComentarios;

    @FXML
    private Button GEntradaStock;

    @FXML
    private Button GGestaoStock;

    @FXML
    private Button GQuarto;

    @FXML
    private Button GReservas;

    @FXML
    private Button GServico;

    @FXML
    private Button GUtilizadores;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private Button estacionamento;


    @FXML
    void GCheckClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Checkin.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) GCheck.getScene().getWindow();
        stage.setTitle("Gestao CheckIn");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void GComentariosClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestorComentarios.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) GComentarios.getScene().getWindow();
        stage.setTitle("Comentarios");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void GGestaoStockClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoStock.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) GGestaoStock.getScene().getWindow();
        stage.setTitle("Gestao Stock");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void GQuartoClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CriarQuarto.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) GQuarto.getScene().getWindow();
        stage.setTitle("Criar Quarto");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void GReservasClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoReservas.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) GReservas.getScene().getWindow();
        stage.setTitle("Gestao Reservas");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void GServicoClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoServicos.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) GServico.getScene().getWindow();
        stage.setTitle("Gestao Servicos");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void GUtilizadoresClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoUtilizadores.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) GUtilizadores.getScene().getWindow();
        stage.setTitle("Registo Funcionario");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void clickLogout(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) btnLogOut.getScene().getWindow();
        stage.setTitle("Login");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
        UtilizadorPreferences.apagarTipoLogin();
    }

    @FXML
    void registoCartaoAction(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("RegistosCartao.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) regCartao.getScene().getWindow();
        stage.setTitle("Registos Cartão");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void passarCartaoAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CartaoCliente.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) btnPassarCartao.getScene().getWindow();
        stage.setTitle("Passar Cartão");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void estacionamentoClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EstacionamentoGestor.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) estacionamento.getScene().getWindow();
        stage.setTitle("Estacionamento");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

}
