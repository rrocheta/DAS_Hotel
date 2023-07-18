package Controller;

import BLL.CartaoBLL;
import BLL.QuartoBLL;
import BLL.UtilizadorPreferences;
import DAL.DBconn;
import DAL.QuartoDAL;
import Model.*;
import com.example.hotel.Main;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CriarQuartoController implements Initializable {

    @FXML
    private Button BtnAddProduto;

    @FXML
    private Label ValidarPiso;

    @FXML
    private Label EmptyMessage;

    @FXML
    private Label ValidarCartao;

    @FXML
    private Button ProdutoQuarto;

    @FXML
    private AnchorPane CriarQuarto;

    @FXML
    private Button btnAddQuarto;

    @FXML
    private Button btnAddTipoQuarto;

    @FXML
    private ImageView btnBack;

    @FXML
    private ComboBox<String> cmbPiso;

    @FXML
    private ComboBox<String> cmbTipoQuarto;

    @FXML
    private TableColumn<Quarto, Integer> tbl_id;

    @FXML
    private TableColumn<Quarto, String> tbl_piso;

    @FXML
    private TableColumn<Quarto, Double> tbl_preco;

    @FXML
    private TableColumn<Quarto, String> tbl_tipQuarto;

    @FXML
    private TableColumn<Quarto, String> tbl_idCartao;

    @FXML
    private TableView<Quarto> tv_Quarto;

    @FXML
    private TextField txt_numcartao;

    @FXML
    private TextField txt_piso;

    @FXML
    private TextField txt_preco;

    @FXML
    private Button btnRmvQuarto;

    @FXML
    private Button btnEditar;
    @FXML
    private Button voltarBtn;

    @FXML
    private Button addProdutosQuarto;

    private QuartoBLL quartoBLL = new QuartoBLL();

    /**
     * Este método é executado quando o botão "Adicionar Quarto" é clicado. Verifica se todos os campos necessários (piso, tipo de quarto, preço, número de cartão) foram preenchidos e, se sim, chama o método ADDQuarto(). Se não, exibe uma mensagem de erro informando que todos os campos precisam ser preenchidos.
     *
     * @param event O evento de clique do botão.
     * @throws SQLException Se houver um erro de conexão com o banco de dados.
     */

    @FXML
    void clickAddQuarto(ActionEvent event) throws SQLException {

        if (cmbPiso.getItems().isEmpty() == false && cmbTipoQuarto.getItems().isEmpty() == false && txt_preco.getText().isEmpty() == false && txt_numcartao.getText().isEmpty() == false) {
            ADDQuarto();
        } else {
            EmptyMessage.setText("Preencha todos os campos");
        }
    }

    /**
     * Método para adicionar um quarto.
     * Verifica se o piso e o tipo de quarto são válidos, cria um objeto {@link Quarto} com os valores selecionados pelo usuário
     * e chama o método {@link QuartoBLL#addQuarto(Quarto)} para adicionar o quarto no banco de dados.
     * Caso ocorra algum erro, o método {@link MessageBoxes#ShowMessage(Alert.AlertType, String, String)} é chamado para exibir
     * uma mensagem de erro ao usuário.
     *
     * @throws SQLException          se ocorrer algum erro ao acessar o banco de dados
     * @throws NumberFormatException se o ID do cartão inserido pelo usuário não for um número válido
     * @throws RuntimeException      se ocorrer algum erro durante a adição do quarto
     */
    public void ADDQuarto() throws SQLException {
        try {
            String floor = cmbPiso.getValue();
            String roomType = cmbTipoQuarto.getValue();

            if (floor != null && (floor.equals("1") || floor.equals("2"))) {
                if (roomType != null && (roomType.equals("Singular") || roomType.equals("Duplo") || roomType.equals("Familiar"))) {
                    Quarto quarto = new Quarto(
                            null,
                            roomType,
                            floor,
                            Double.parseDouble(txt_preco.getText().trim()),
                            false,
                            new Cartao(Integer.parseInt(txt_numcartao.getText().trim())));

                    quartoBLL.addQuarto(quarto);
                    initTable();
                } else {
                    MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Tipo quarto invalido", "Erro!");
                }
            } else {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Piso invalido", "Erro!");
            }
        } catch (NumberFormatException e) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Apenas números para o ID Cartão", "Erro!");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Não foi possível adicionar o quarto.", "Erro!");
            throw new RuntimeException(e);
        }
    }


    /**
     * Método que gere o evento do botão "Voltar". É responsável por trocar de cena, levando o utilizador para o painel de gestor ou funcionário, consoante o tipo de utilizador que se encontra logado.
     *
     * @param event O evento de clique do botão "Voltar".
     * @throws IOException Lança uma exceção caso haja algum erro ao carregar a nova cena.
     */
    @FXML
    void clickVoltarBtn(ActionEvent event) throws IOException {

        if (UtilizadorPreferences.comparaTipoLogin()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PainelGestor.fxml"));
            Stage stage = new Stage();
            Stage newStage = (Stage) voltarBtn.getScene().getWindow();
            stage.setTitle("Pagina Gestor");
            newStage.hide();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PainelFuncionario.fxml"));
            Stage stage = new Stage();
            Stage newStage = (Stage) voltarBtn.getScene().getWindow();
            stage.setTitle("Pagina Funcionario");
            newStage.hide();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        }

    }

    /**
     * Método que é executado quando o botão "addProdutosQuarto" é clicado.
     * Este método carrega a página "GestaoProdutoQuarto.fxml".
     *
     * @param event Evento gerado pelo clique do botão.
     * @throws IOException Caso ocorra um erro ao carregar o arquivo "GestaoProdutoQuarto.fxml".
     */
    @FXML
    void addProdutosQuartoClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoProdutoQuarto.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) addProdutosQuarto.getScene().getWindow();
        stage.setTitle("Gestao Produtos Quarto");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    /**
     * Método que inicializa os ComboBoxes cmbTipoQuarto e cmbPiso com os valores "Singular", "Duplo", "Familiar" e "1", "2", respectivamente.
     */
    private void initCombos() {
        cmbTipoQuarto.getItems().add("Singular");
        cmbTipoQuarto.getItems().add("Duplo");
        cmbTipoQuarto.getItems().add("Familiar");
        cmbPiso.getItems().add("1");
        cmbPiso.getItems().add("2");
    }

    /**
     * Método inicial que é chamado ao carregar a tabela.
     * Inicializa os combos cmbTipoQuarto e cmbPiso com as opções disponíveis.
     * Chama o método initTable para inicializar a tabela de quartos.
     *
     * @param location  URL do local onde o arquivo fxml está localizado.
     * @param resources Recursos usados na tela.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCombos();
        initTable();
    }

    /**
     * Este método é invocado ao clicar no botão "Editar".
     * Ele atualiza o quarto selecionado na tabela com as informações fornecidas nos campos da interface.
     * Primeiro, o quarto selecionado é obtido da tabela.
     * Em seguida, os valores de tipo de quarto, piso e preço são atualizados com as informações fornecidas na interface.
     * Por fim, a atualização é enviada para a camada de negócios (quartoBLL) para ser persistida no banco de dados.
     * Uma mensagem é exibida ao usuário para indicar que a operação foi realizada com sucesso.
     *
     * @param actionEvent O evento de ação disparado ao clicar no botão "Editar".
     */
    public void clickEditar(ActionEvent actionEvent) {

        Quarto selectedQuarto = tv_Quarto.getSelectionModel().getSelectedItem();


        if (selectedQuarto != null) {

            selectedQuarto.setTipoQuarto(cmbTipoQuarto.getValue());
            selectedQuarto.setPiso(cmbPiso.getValue());
            selectedQuarto.setPreco(Double.parseDouble(txt_preco.getText()));


            quartoBLL.updateQuarto(selectedQuarto);


            MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Quarto alterado", "Information");
        }
    }

    /**
     * Método responsável pela ação de remoção de um quarto.
     * Verifica se um quarto foi selecionado na tabela de quartos, e se foi, remove o quarto e seu respectivo cartão do banco de dados.
     *
     * @param actionEvent Evento de clique do botão "Remover Quarto".
     */
    public void clickRmvQuarto(ActionEvent actionEvent) {
        QuartoBLL quartoBLL = new QuartoBLL();
        CartaoBLL cartaoBLL = new CartaoBLL();
        Quarto selectedQuarto = tv_Quarto.getSelectionModel().getSelectedItem();
        if (selectedQuarto != null) {
            try {

                quartoBLL.removeQuarto(selectedQuarto.getId());


                cartaoBLL.deleteCartao(selectedQuarto.getCartao().getId());

                tv_Quarto.getItems().remove(selectedQuarto);
                MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Quarto Removido", "Information");
            } catch (SQLException ex) {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Reserva existente com estes dados", "Reserva existente");
                throw new RuntimeException(ex);
            }
        } else {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Selecione um quarto para eliminar!", "Erro:");
        }
    }

    /**
     * Inicializa a tabela "tv_Quarto" com as colunas "tbl_id", "tbl_piso", "tbl_preco", "tbl_tipQuarto", "tbl_idCartao".
     * O valor de cada coluna é obtido através da chamada do método "setCellValueFactory".
     * A coluna "tbl_id" obtém o valor do atributo "id" de cada objeto da lista.
     * A coluna "tbl_piso" obtém o valor do atributo "piso" de cada objeto da lista.
     * A coluna "tbl_preco" obtém o valor do atributo "preco" de cada objeto da lista.
     * A coluna "tbl_tipQuarto" obtém o valor do atributo "tipoQuarto" de cada objeto da lista.
     * A coluna "tbl_idCartao" obtém o valor do atributo "id" do objeto "Cartao" associado a cada objeto "Quarto".
     * Finalmente, a lista de "Quartos" é definida como o conteúdo da tabela "tv_Quarto", obtida através do método "getQuartos" da classe "QuartoBLL".
     */
    private void initTable() {
        tbl_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbl_piso.setCellValueFactory(new PropertyValueFactory<>("piso"));
        tbl_preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tbl_tipQuarto.setCellValueFactory(new PropertyValueFactory<>("tipoQuarto"));
        tbl_idCartao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCartao().getId().toString()));
        tv_Quarto.setItems(QuartoBLL.getQuartos());
    }

}