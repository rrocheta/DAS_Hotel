package Controller;

import BLL.UtilizadorPreferences;
import BLL.UtilizadorBLL;
import Model.MessageBoxes;
import Model.Utilizador;
import com.example.hotel.Main;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class LoginController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private TextField userTxt;

    @FXML
    void clickLoginBtn(ActionEvent event) {

        if (userTxt.getText().isEmpty() == false && passwordTxt.getText().isEmpty() == false) {
            Login();
        } else {
            MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Preencha todos os campos","Informação");
        }
    }

    void Login() {

        try {
            UtilizadorBLL bll = new UtilizadorBLL();
            Utilizador utilizador = bll.Login(userTxt.getText(), passwordTxt.getText());

            if (utilizador != null) {
                // FEZ LOGIN
                if (utilizador.getTipoUser().getTipo().equals("Gestor")) {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PainelGestor.fxml"));
                    Stage stage = new Stage();
                    Stage newStage = (Stage) loginBtn.getScene().getWindow();
                    stage.setTitle("Pagina Gestor");
                    newStage.hide();
                    stage.setScene(new Scene(fxmlLoader.load()));
                    stage.show();
                } else if (utilizador.getTipoUser().getTipo().equals("Funcionario")) {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PainelFuncionario.fxml"));
                    Stage stage = new Stage();
                    Stage newStage = (Stage) loginBtn.getScene().getWindow();
                    stage.setTitle("Pagina Funcionário");
                    newStage.hide();
                    stage.setScene(new Scene(fxmlLoader.load()));
                    stage.show();
               } else if (utilizador.getTipoUser().getTipo().equals("Cliente")) {
                   FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PainelCliente.fxml"));
                   Stage stage = new Stage();
                   Stage newStage = (Stage) loginBtn.getScene().getWindow();
                   stage.setTitle("Pagina Cliente");
                   newStage.hide();
                   stage.setScene(new Scene(fxmlLoader.load()));
                   stage.show();
                }
            } else {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Login incorreto!","Erro");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}