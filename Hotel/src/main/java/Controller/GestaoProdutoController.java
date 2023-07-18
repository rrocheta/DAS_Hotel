package Controller;

import BLL.UtilizadorPreferences;
import DAL.DBconn;
import DAL.ProdutoDAL;
import Model.MessageBoxes;
import Model.Produto;
import Model.Stock;
import com.example.hotel.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GestaoProdutoController implements Initializable {

    @FXML
    private Button btnVoltar;

    @FXML
    private Button consumivelBtn;

    @FXML
    private TableColumn<Produto, Boolean> consumivelTable;

    @FXML
    private TableColumn<Produto, String> descricaoTable;

    @FXML
    private TableColumn<Produto, String> idTable;

    @FXML
    private Button nConsumivelBtn;

    @FXML
    private TableColumn<Produto, Double> pesoTable;

    @FXML
    private TableColumn<Produto, Double> precoTable;

    @FXML
    private TableView<Produto> produtosTable;

    @FXML
    private Text quantidade;

    @FXML
    void clickBtnVoltar(ActionEvent event) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoStock.fxml"));
            Stage stage = new Stage();
            Stage newStage = (Stage) btnVoltar.getScene().getWindow();
            stage.setTitle("Gestao Stock");
            newStage.hide();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        }

    @FXML
    void clickConsumivelBtn(ActionEvent event) throws SQLException {
        ProdutoDAL pdal = new ProdutoDAL();
            Produto selectedID = produtosTable.getSelectionModel().getSelectedItem();
            if (selectedID != null && selectedID.getConsumivel() == false) {
                pdal.updateConsumivelProduto(selectedID, true);
                MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Produto passou a consumível", "Informação");
                initTable();
            } else {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Produto já é consumível", "Erro");
            }
    }

    @FXML
    void clickNConsumivelBtn(ActionEvent event) throws SQLException {
        ProdutoDAL pdal = new ProdutoDAL();
            Produto selectedID = produtosTable.getSelectionModel().getSelectedItem();
            if (selectedID != null && selectedID.getConsumivel() == true) {
                pdal.updateConsumivelProduto(selectedID, false);
                MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Produto passou a não consumível", "Informação");
                initTable();
            } else {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Produto já é não consumível", "Erro");
            }
    }

    @FXML
    void clickTable(MouseEvent event) throws SQLException {
        ProdutoDAL pdal = new ProdutoDAL();
        Produto selectedID = produtosTable.getSelectionModel().getSelectedItem();
        if (selectedID != null) {
            quantidade.setText(String.valueOf(pdal.getQuantidadeProduto(selectedID)));
        }
    }

    private void initTable() {
        idTable.setCellValueFactory(new PropertyValueFactory<Produto, String>("idProduto"));
        descricaoTable.setCellValueFactory(new PropertyValueFactory<Produto, String>("descricao"));
        precoTable.setCellValueFactory(new PropertyValueFactory<Produto, Double>("precoUnidade"));
        pesoTable.setCellValueFactory(new PropertyValueFactory<Produto, Double>("peso"));
        consumivelTable.setCellValueFactory(new PropertyValueFactory<Produto, Boolean>("consumivel"));
        produtosTable.setItems(ProdutoDAL.getAllProdutos());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
    }
}