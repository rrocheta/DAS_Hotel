package Controller;

import BLL.RegistoBLL;
import BLL.UtilizadorPreferences;
import DAL.CartaoDAL;
import DAL.RegistoDAL;
import DAL.ServicoDAL;
import Model.Cartao;
import Model.Registo;
import Model.Servico;
import com.example.hotel.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CartaoController implements Initializable {

    @FXML
    private Text cidadeTxt;

    @FXML
    private Text dataTxt;

    @FXML
    private Text idFornecedorTxt;

    @FXML
    private Text moradaTxt;

    @FXML
    private Text noneFornecedorTxt;

    @FXML
    private Text ordemTxt;


    @FXML
    private ComboBox<Cartao> cmbIdCartao;

    @FXML
    private ComboBox<Servico> cmbLocal;

    @FXML
    private TableColumn<Registo, java.sql.Date> tbl_data;

    @FXML
    private TableColumn<Registo, Integer> tbl_id;

    @FXML
    private TableColumn<Registo, Integer> tbl_idCartao;

    @FXML
    private TableColumn<Registo, Integer> tbl_idCliente;

    @FXML
    private TableColumn<Registo, String> tbl_local;

    @FXML
    private TableView<Registo> tv_registosCartao;

    @FXML
    private Text urlText;

    @FXML
    private Button btnVoltar;

    /**
     * Método responsável por gerir o evento de clique no botão de voltar.
     * Carrega a janela "PainelGestor.fxml" e esconde a janela atual.
     *
     * @param event O evento de clique no botão "Voltar".
     * @throws IOException Caso ocorra algum erro ao carregar a janela "PainelGestor.fxml".
     */
    @FXML
    void clickVoltarBtn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PainelGestor.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) btnVoltar.getScene().getWindow();
        stage.setTitle("Pagina cliente");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    /**
     * Método de inicialização da classe.
     * Inicializa a tabela e os comboboxes.
     *
     * @param location  URL da localização
     * @param resources Recursos utilizados na inicialização
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCombos();
    }

    /**
     * Inicializa os componentes do tipo ComboBox.
     * Adiciona todos os cartões existentes à ComboBox cmbIdCartao.
     * Adiciona todos os serviços existentes à ComboBox cmbLocal.
     */
    private void initCombos() {
        cmbIdCartao.getItems().addAll(CartaoDAL.getAllCartoes());
        cmbLocal.getItems().addAll(ServicoDAL.getAllServicos());
    }

    /**
     * Método para inicializar a tabela de registos.
     * Busca todos os registos através da classe RegistoBLL e atribui os valores correspondentes às colunas da tabela.
     * Caso ocorra uma exceção SQL, é imprimido o erro.
     */
    private void initTable() {

        RegistoBLL registoBLL = new RegistoBLL();
        try {
            ObservableList<Registo> registos = registoBLL.getAllRegistos();

            tbl_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tbl_idCartao.setCellValueFactory(new PropertyValueFactory<>("idCartao"));
            tbl_idCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
            tbl_local.setCellValueFactory(new PropertyValueFactory<>("Local"));
            tbl_data.setCellValueFactory(new PropertyValueFactory<>("Data"));
            tv_registosCartao.setItems(registos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método responsável por filtrar os registos de estacionamento por id do cartão.
     * Este método é chamado ao clicar no botão de filtragem por id do cartão.
     * Ele seleciona o cartão selecionado no combo box cmbIdCartao e, se existir um cartão selecionado,
     * filtra os registos de estacionamento associados a esse cartão através da chamada ao método
     * RegistoDAL.getRegistosByCartaoId. O resultado é colocado na tabela tv_registosCartao.
     * Em caso de exceção, o erro é impresso com o método e.printStackTrace().
     *
     * @param event evento de clique no botão de filtragem por id do cartão
     */
    @FXML
    void filterIdCartao(ActionEvent event) {
        Cartao selectedCartao = cmbIdCartao.getSelectionModel().getSelectedItem();
        if (selectedCartao != null) {
            try {
                ObservableList<Registo> filteredRegistos = RegistoDAL.getRegistosByCartaoId(selectedCartao.getId());
                tv_registosCartao.setItems(filteredRegistos);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Filtra os registos da tabela de acordo com o local selecionado.
     *
     * @param event evento de clique do botão.
     */
    @FXML
    void filterLocal(ActionEvent event) {
        Servico selectedServico = cmbLocal.getSelectionModel().getSelectedItem();
        if (selectedServico != null) {
            try {
                ObservableList<Registo> filteredRegistos = RegistoDAL.getRegistosByLocal(selectedServico.getServico());
                tv_registosCartao.setItems(filteredRegistos);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}