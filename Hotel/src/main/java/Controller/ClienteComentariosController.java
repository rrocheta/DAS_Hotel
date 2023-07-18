package Controller;

import BLL.ComentarioBLL;
import BLL.QuartoBLL;
import BLL.UtilizadorPreferences;
import Model.Cartao;
import Model.Comentario;
import Model.MessageBoxes;
import Model.Quarto;
import com.example.hotel.Main;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClienteComentariosController implements Initializable {


    @FXML
    private Text numLetras;

    @FXML
    private Button Submeter;

    @FXML
    private Button Voltar;

    @FXML
    private TextArea campoComentario;

    @FXML
    private ComboBox<String> tipoComentario;

    private ComentarioBLL comentarioBLL = new ComentarioBLL();

    /**
     * O método "SubmeterClick" é executado quando o botão "Submeter" é pressionado. Verifica se o tipo de comentário foi selecionado e o conteúdo do campo de mensagem não está vazio. Se tudo estiver correto, adiciona um novo comentário ao banco de dados.
     *
     * @param event evento associado a ação de clique no botão "Submeter"
     */
    @FXML
    void SubmeterClick(ActionEvent event) {
        if (tipoComentario.getValue() != null) {
            try {
                if (campoComentario.getText() == null || campoComentario.getText().trim().isEmpty()) {
                    MessageBoxes.ShowMessage(Alert.AlertType.WARNING, "O campo de mensagem não pode ser vazio.", "Erro no formulário.");
                    return;
                }

                Comentario comentario = new Comentario(
                        null,
                        UtilizadorPreferences.utilizadorId(),
                        campoComentario.getText(),
                        verificaTipoComentario());

                comentarioBLL.addComentario(comentario);
                MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Comentário inserido com sucesso", "Informação!");
                campoComentario.setText("");
            } catch (SQLException e) {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Não foi possível inserir o comentátrio.", "Erro!");
                throw new RuntimeException(e);
            }
        } else {
            MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Escolha um tipo de comentário.", "Aviso:");
        }
    }

    /**
     * Método para controlar o número de caracteres no campo de comentário.
     * Verifica se o número de caracteres ultrapassa 300 e, se sim, impede a inserção de mais caracteres.
     * A contagem de caracteres é exibida na interface gráfica através da ligação de propriedade com o elemento de texto "numLetras".
     */
    private void contaLetras() {
        int tamanhoTexto = 300;
        campoComentario.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.length() > tamanhoTexto) campoComentario.setText(oldValue);
                }
        );
        numLetras.textProperty().bind(Bindings.length(campoComentario.textProperty())
                .asString("%d"));
    }

    /**
     * Método de ação de clique do botão Voltar.
     *
     * @param event O evento gerado ao clicar no botão.
     * @throws IOException caso haja erro ao carregar o arquivo FXML.
     */
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

    /**
     * Verifica o tipo de comentário selecionado pelo utilizador.
     *
     * @return Uma string indicando o tipo de comentário, "sugestão" ou "queixa".
     */
    private String verificaTipoComentario() {
        String comentario;
        if (tipoComentario.getValue().equals("Sugestão")) {
            comentario = "sugestao";
            return comentario;
        } else {
            comentario = "queixa";
            return comentario;
        }
    }

    /**
     * Inicializa as opções disponíveis para o tipo de comentário.
     */
    private void initCombos() {
        tipoComentario.getItems().add("Sugestão");
        tipoComentario.getItems().add("Queixa");
    }

    /**
     * Método para inicializar a interface de utilizador.
     * Este método é executado automaticamente quando a tela é carregada.
     * Ele inicializa o contador de letras e preenche as combo boxes.
     *
     * @param location  URL que especifica o local da classe.
     * @param resources objeto ResourceBundle que contem as informações de recursos.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contaLetras();
        initCombos();
    }
}
