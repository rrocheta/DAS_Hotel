package Controller;


import BLL.ProdutoQuartoBLL;
import BLL.QuartoBLL;
import BLL.StockBLL;
import DAL.ProdutoDAL;
import DAL.ProdutoQuartoDAL;
import Model.MessageBoxes;
import Model.Produto;
import Model.ProdutoQuarto;
import Model.Quarto;
import com.example.hotel.Main;
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
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GestaoProdutoQuartoController implements Initializable {

    @FXML
    private AnchorPane PainelProdutoReserva;

    @FXML
    private Button btnAddQuarto;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnRmvQuarto;

    @FXML
    private ComboBox<Produto> cmbProduto;

    @FXML
    private ComboBox<Quarto> cmbQuarto;

    @FXML
    private ImageView imgGestorGestaoProduto;

    @FXML
    private Label lblDataFim;

    @FXML
    private Label lblGestaoProdutoAdicionar;

    @FXML
    private Label lblGestaoReservas;

    @FXML
    private Label lblHotel;

    @FXML
    private Label lblIdClientes;

    @FXML
    private Label lblIdQuarto;

    @FXML
    private Label lblSamos;

    @FXML
    private TableColumn<?, ?> tbl_id;

    @FXML
    private TableColumn<ProdutoQuarto, String> tbl_idProduto;

    @FXML
    private TableColumn<ProdutoQuarto, Integer> tbl_idReserva;

    @FXML
    private TableColumn<ProdutoQuarto, Integer> tbl_quantidade;

    @FXML
    private TableView<ProdutoQuarto> tv_ProdutoQuarto;

    @FXML
    private TextField txt_quantidade;

    @FXML
    private Button voltarBtn;

    @FXML
    void clickAddProdutoQuarto(ActionEvent event) {
        Quarto selectedQuarto = cmbQuarto.getValue();
        StockBLL sBLL = new StockBLL();
        Produto selectedProduct = cmbProduto.getValue();
        int quantity = Integer.parseInt(txt_quantidade.getText());
        if (selectedQuarto != null && selectedProduct != null){
            if (sBLL.verificaSeProdutoTemQuantidadeSuficiente(selectedProduct.getIdProduto(), quantity)) {
                try {
                    ProdutoQuartoBLL.addProductToQuarto(selectedQuarto.getId(), selectedProduct.getIdProduto(), quantity);
                    MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Produto adicionado ao quarto.", "Sucesso!");
                    initTable();
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }else{
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "A quantidade desejada é superior à existente em stock!", "Erro:");
            }
        } else {
            MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Preencha todos os campos!", "Aviso:");
        }
    }

    @FXML
    void clickEditar(ActionEvent event) {

    }

    @FXML
    void clickRmvProdutoQuarto(ActionEvent event) {
        ProdutoQuarto selectedProduct = tv_ProdutoQuarto.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                ProdutoQuartoBLL.deleteProductFromQuarto(selectedProduct.getId());
                MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Produto removido.", "Sucesso!");
                initTable();
            } catch (SQLException e) {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, e.getMessage(), "Erro:");
            }
        } else {
            MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Selecione um produto para remover!", "Aviso:");
        }
    }

    @FXML
    void clickVoltarBtn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CriarQuarto.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) voltarBtn.getScene().getWindow();
        stage.setTitle("Criar Quarto");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCombos();
        try {
            initTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initCombos() {
        cmbQuarto.getItems().addAll(QuartoBLL.getQuartos());
        cmbProduto.getItems().addAll(ProdutoDAL.getAllProdutos().stream()
                .filter(p -> !p.getConsumivel())
                .collect(Collectors.toList()));
    }

    private void initTable() throws SQLException {


        tbl_idProduto.setResizable(false);
        tbl_idReserva.setResizable(false);
        tbl_quantidade.setResizable(false);


        tbl_idReserva.setCellValueFactory(new PropertyValueFactory<ProdutoQuarto, Integer>("idQuarto"));
        tbl_idProduto.setCellValueFactory(new PropertyValueFactory<ProdutoQuarto, String>("idProduto"));
        tbl_quantidade.setCellValueFactory(new PropertyValueFactory<ProdutoQuarto, Integer>("quantidade"));


        tv_ProdutoQuarto.setItems(ProdutoQuartoDAL.getProdutoQuarto());
    }
}
