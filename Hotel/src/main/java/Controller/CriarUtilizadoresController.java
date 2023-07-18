package Controller;

import BLL.UtilizadorBLL;
import BLL.UtilizadorPreferences;
import DAL.DBconn;
import DAL.TipoUtilizadorDAL;
import Model.MessageBoxes;
import Model.TipoUtilizador;
import com.example.hotel.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javafx.stage.Stage;


public class CriarUtilizadoresController implements Initializable {

    @FXML
    private Label VerificarNome;

    @FXML
    private Label VerificarContacto;

    @FXML
    private Label VerificarNIF;

    private Button btn_refresh;

    @FXML
    private Button btn_update_Voltar;

    @FXML
    private Button btn_add_funcionario;

    @FXML
    private Button btn_remove_funcionario;

    @FXML
    private Button btn_update_funcionario;

    @FXML
    private ComboBox<String> cmb_tipoUtilizador;


    @FXML
    private TextField txt_contacto;

    @FXML
    private DatePicker datePickerNasc;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_morada;

    @FXML
    private TextField txt_nif;

    @FXML
    private TextField txt_nome;

    @FXML
    private PasswordField txt_password;

    @FXML
    private TextField txt_utilizador;

    @FXML
    private Button VoltarBtn;

    static boolean flag;
    static boolean flagnome;

    /**
     * Método responsável por lidar com o evento de clique no botão "Voltar".
     * Este método carrega o arquivo FXML "GestaoUtilizadores.fxml" e o utiliza para criar uma nova cena.
     * A cena atual é ocultada e a nova cena é exibida, com o título "Gestão Utilizadores".
     *
     * @param event o evento de clique no botão "Voltar"
     * @throws IOException se houver algum problema ao carregar o arquivo FXML "GestaoUtilizadores.fxml"
     */
    @FXML
    void clickVoltarBtn(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoUtilizadores.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) VoltarBtn.getScene().getWindow();
        stage.setTitle("Gestao Utilizadores");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    /**
     * Inicializa os combos da interface. Dependendo do tipo de login (gestor ou funcionário), serão carregados diferentes combos.
     */
    private void initCombos() {
        if (UtilizadorPreferences.comparaTipoLogin()) {
            combosGestor();
        } else {
            combosFuncionario();
        }
    }

    /**
     * Método que adiciona os tipos de utilizador ao combo box se o utilizador atual for um gestor.
     */
    private void combosGestor() {
        if (UtilizadorPreferences.comparaTipoLogin()) {
            List<TipoUtilizador> tipos = TipoUtilizadorDAL.getTipoUtilizador();

            for (TipoUtilizador tipo : tipos) {
                cmb_tipoUtilizador.getItems().add(tipo.getTipo());
            }
        }
    }

    private void combosFuncionario() {
        cmb_tipoUtilizador.getItems().add("Cliente");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCombos();
    }

    /**
     * Método que lida com o evento de adição de um funcionário ao sistema.
     * É verificado se todos os campos estão preenchidos e se o email, NIF e nome do funcionário são válidos.
     * Em caso afirmativo, o método "RegistarUtilizador" é chamado para registar o novo funcionário.
     * Caso contrário, uma mensagem de erro é exibida.
     *
     * @param actionEvent evento de clique no botão "Adicionar Funcionário".
     */
    public void onActionAddFuncionario(ActionEvent actionEvent) {
        if (cmb_tipoUtilizador.getValue() != null) {
            String email = txt_email.getText();
            if (txt_nome.getText().isEmpty() == false && txt_nif.getText().isEmpty() == false && txt_morada.getText().isEmpty() == false &&
                    txt_contacto.getText().isEmpty() == false && txt_email.getText().isEmpty() == false && txt_utilizador.getText().isEmpty() == false
                    && txt_password.getText().isEmpty() == false && cmb_tipoUtilizador.getItems().isEmpty() == false) {
                if (VerifyNIFColaborador() == true && VerifyNIFColaboradorMin() == true && VerifyContacto() == true && VerifyNome() == true && isValidEmail(email) == true) {
                    RegistarUtilizador();
                } else {
                    MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Dados incorretos!", "Erro");
                }
            } else {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Preencha todos os campos!", "Erro");
            }
        } else {
            MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Escolha um tipo de utilizador.", "Aviso:");
        }
    }

    /**
     * Método que regista um novo utilizador na base de dados.
     * Ele pega os valores dos campos de texto do formulário, incluindo nome, NIF, morada, data de nascimento,
     * email, contato, nome de utilizador, senha e tipo de utilizador.
     * Em seguida, cria um objeto UtilizadorBLL e chama o método createUtilizador passando todos os valores
     * coletados. Se o registro for bem-sucedido, uma mensagem informativa é exibida ao usuário.
     */
    void RegistarUtilizador() {
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        String nome = txt_nome.getText();
        String nif = txt_nif.getText();
        String morada = txt_morada.getText();
        java.sql.Date sqlDate = java.sql.Date.valueOf(datePickerNasc.getValue());
        String email = txt_email.getText();
        String contacto = txt_contacto.getText();
        String utilizador = txt_utilizador.getText();
        String password = txt_password.getText();
        String tipoUser = cmb_tipoUtilizador.getValue();

        utilizadorBLL.createUtilizador(nome, nif, morada, sqlDate, email, contacto, utilizador, password, tipoUser);
        MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Utilizador criado!", "Informação:");
    }

    /**
     * Verifica se o NIF de um utilizador já existe na base de dados.
     *
     * @return boolean flag, que indica se o NIF já existe (false) ou não (true).
     */
    public boolean VerifyNIFColaborador() {
        int nif = Integer.parseInt(txt_nif.getText());
        String verificar = "SELECT count(1) FROM Utilizador WHERE nif =" + nif + "";
        try (Connection conn = DBconn.getConn();
             PreparedStatement stmt = conn.prepareStatement(verificar)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    VerificarNIF.setText("O nif já existe!");
                    flag = false;
                } else {
                    VerificarNIF.setText("");
                    flag = true;
                }
            }
        } catch (SQLException e) {
            e.getCause();
        }
        return flag;
    }

    /**
     * Método que verifica se o NIF do colaborador tem no mínimo 9 caracteres.
     *
     * @return flagnif retorna verdadeiro se o nif tem 9 ou mais caracteres, caso contrário retorna falso.
     */
    public boolean VerifyNIFColaboradorMin() {
        boolean flagnif;
        if (txt_nif.getText().length() < 9) {
            VerificarNIF.setText("Minimo 9 caracteres!");
            flagnif = false;
        } else {
            flagnif = true;
        }
        return flagnif;
    }

    /**
     * Verifica se o contacto introduzido tem pelo menos 9 caracteres.
     *
     * @return boolean flagcontacto - Devolve verdadeiro se o contacto tiver 9 ou mais caracteres,
     * falso caso contrário.
     */
    public boolean VerifyContacto() {
        boolean flagcontacto;
        if (txt_contacto.getText().length() < 9) {
            VerificarContacto.setText("Minimo 9 caracteres!");
            flagcontacto = false;
        } else {
            flagcontacto = true;
        }
        return flagcontacto;
    }

    /**
     * Verifica se o nome do utilizador introduzido contém algum caractere numérico.
     *
     * @return retorna "true" se o nome não contém números, ou "false" se o nome contém números.
     */
    public boolean VerifyNome() {
        char[] chars = txt_nome.getText().toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c)) {
                VerificarNome.setText("Nome nao pode conter numeros!");
                flagnome = false;
                break;
            } else {
                flagnome = true;
            }
        }
        return flagnome;
    }

    /**
     * Verifica se um endereço de email é válido.
     *
     * @param email Endereço de email a ser verificado.
     * @return true se o endereço de email for válido, false caso contrário.
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}