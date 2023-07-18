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

public class FuncionarioController {

    @FXML
    private Button FCheck;

    @FXML
    private Button btnPassarCartao;

    @FXML
    private Button FEntradaStock;

    @FXML
    private Button FQuartos;

    @FXML
    private Button FReserva;

    @FXML
    private Button FServicos;

    @FXML
    private Button FStock;

    @FXML
    private Button FUtilizadores;

    @FXML
    private ImageView btnLogOut;

    @FXML
    private Button estacionamento;

    /**

     Método para abrir a página de Check-In quando o botão "FCheck" é clicado.
     @param event evento do clique no botão "FCheck"
     @throws IOException exceção lançada caso ocorra um erro ao carregar a página FXML.
     */
    @FXML
    void FCheckClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Checkin.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) FCheck.getScene().getWindow();
        stage.setTitle("Gestao CheckIn");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void FEntradaStockClick(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CarregarXML.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) FEntradaStock.getScene().getWindow();
        stage.setTitle("Entrada de Stock");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    /**

     Método que responde ao clique do botão "FQuartos".
     Este método carrega uma nova janela "CriarQuarto.fxml".
     @param event O evento de clique do botão "FQuartos".
     @throws IOException Caso haja algum problema na leitura do arquivo "CriarQuarto.fxml".
     */
    @FXML
    void FQuartosClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CriarQuarto.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) FQuartos.getScene().getWindow();
        stage.setTitle("Criar Quarto");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }
    /**

     Método que é invocado quando o botão "FReserva" é clicado. Este método abre a janela "Gerir Reservas".
     @param event O evento que ocorreu ao clicar no botão "FReserva".
     @throws IOException Pode ser lançada se o arquivo "GestaoReservas.fxml" não puder ser carregado.
     */
    @FXML
    void FReservaClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoReservas.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) FReserva.getScene().getWindow();
        stage.setTitle("Gerir Reservas");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    /**

     Este método é chamado quando o botão "FServiços" é clicado. Abre a janela "Gestão Serviços".
     @param event O evento de clique no botão "FServiços".
     @throws IOException
     */
    @FXML
    void FServicosClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoServicos.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) FServicos.getScene().getWindow();
        stage.setTitle("Gestao Servicos");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void FStockClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoStock.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) FStock.getScene().getWindow();
        stage.setTitle("Gestao Stock");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void FUtilizadoresClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoUtilizadores.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) FUtilizadores.getScene().getWindow();
        stage.setTitle("Registo Funcionario");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void logoutBtn(MouseEvent event) throws IOException {
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
    void btnPassarCartaoAction(ActionEvent event) throws IOException {
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
