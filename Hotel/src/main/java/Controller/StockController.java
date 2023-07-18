package Controller;

import BLL.UtilizadorPreferences;
import DAL.StockDAL;
import com.example.hotel.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    @FXML
    private TableView<Model.Stock> tblStock;

    @FXML
    private TableColumn<Model.Stock, String> idProdutoTable;

    @FXML
    private TableColumn<Model.Stock, Integer> quantidadeTable;

    @FXML
    private Button btnVoltar;

    @FXML
    private Text descricaoTxt;

    @FXML
    private Button GEntradaStock;


    @FXML
    private Button precoProdBtn;

    @FXML
    void clickTable(MouseEvent event) throws SQLException {
            StockDAL sdal = new StockDAL();
            Model.Stock selectedID = tblStock.getSelectionModel().getSelectedItem();
            if (selectedID != null) {
                descricaoTxt.setText(sdal.getDescricaoStock(selectedID));
            }
    }

    @FXML
    void GEntradaStockClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CarregarXML.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) GEntradaStock.getScene().getWindow();
        stage.setTitle("Entrada Stock");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void clickBtnVoltar(ActionEvent event) throws IOException {
        if (UtilizadorPreferences.comparaTipoLogin()){
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PainelGestor.fxml"));
            Stage stage = new Stage();
            Stage newStage = (Stage) btnVoltar.getScene().getWindow();
            stage.setTitle("Pagina Gestor");
            newStage.hide();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PainelFuncionario.fxml"));
            Stage stage = new Stage();
            Stage newStage = (Stage) btnVoltar.getScene().getWindow();
            stage.setTitle("Pagina Funcionario");
            newStage.hide();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        }
    }

    @FXML
    void clickgestaoProdBtn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoProdutos.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) btnVoltar.getScene().getWindow();
        stage.setTitle("Gestao Produtos");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void clickPrecoProdBtn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PrecosProdutosCliente.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) precoProdBtn.getScene().getWindow();
        stage.setTitle("Preços Produtos");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    private void initTable() {
        idProdutoTable.setResizable(false);
        quantidadeTable.setResizable(false);

        idProdutoTable.setCellValueFactory(new PropertyValueFactory<Model.Stock, String>("idProduto"));
        quantidadeTable.setCellValueFactory(new PropertyValueFactory<Model.Stock, Integer>("quantidade"));
        tblStock.setItems(StockDAL.getStock());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
    }

}
