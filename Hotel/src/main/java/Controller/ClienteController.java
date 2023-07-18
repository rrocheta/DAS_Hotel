package Controller;

import com.example.hotel.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ClienteController {
    @FXML
    private Button Comentarios;

    @FXML
    private ImageView LogOut;

    @FXML
    private Button cartao;

    @FXML
    private Button qrCodeBtn;

    /**
     * Este método é acionado quando o botão "Comentários" é clicado pelo usuário.
     * Ele cria uma nova janela (uma nova etapa) com o título "Comentários" e carrega o arquivo FXML "ClienteComentarios.fxml".
     * A janela atual é escondida.
     *
     * @param event O evento que desencadeou a chamada desse método.
     * @throws IOException Se houver algum problema ao carregar o arquivo FXML.
     */

    @FXML
    void ComentariosClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ClienteComentarios.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) Comentarios.getScene().getWindow();
        stage.setTitle("Comentarios");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    /**
     * Este método controla a ação de clique no botão de logout.
     * Ele carrega a tela de login, esconde a tela atual e exibe a tela de login.
     *
     * @param event o evento de clique do mouse
     * @throws IOException se o arquivo fxml não puder ser carregado
     */
    @FXML
    void ClickLogout(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) LogOut.getScene().getWindow();
        stage.setTitle("Login");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    /**
     * Controla o evento de clique no botão de "Cartão". Carrega a tela de "Cartão do Cliente" e a exibe.
     *
     * @param event O evento de clique no botão.
     * @throws IOException se o arquivo FXML não puder ser carregado.
     */
    @FXML
    void cartaoClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CartaoCliente.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) cartao.getScene().getWindow();
        stage.setTitle("Passar Cartão");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    /**
     * Este método é chamado quando o botão de código QR é clicado. Ele cria uma nova janela com o título "Codigo QR" e carrega o FXML "QRCodeCliente.fxml" como sua cena. A janela anterior é ocultada.
     *
     * @param event O evento gerado pelo clique do botão de código QR.
     * @throws IOException Se houver um erro ao carregar o arquivo FXML.
     */
    @FXML
    void qrCodeBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("QRCodeCliente.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) qrCodeBtn.getScene().getWindow();
        stage.setTitle("Codigo QR");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

}
