package Controller;


import DAL.ProdutoDAL;
import Model.MessageBoxes;
import Model.Produto;
import com.example.hotel.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PrecosProdutosClienteController implements Initializable {

    @FXML
    private Button atualizaPreco;

    @FXML
    private Button btnVoltar;

    @FXML
    private TableColumn<Produto, String> descricaoTable;

    @FXML
    private TableColumn<Produto, String> idTable;

    @FXML
    private TableColumn<Produto, Double> precoClienteTable;

    @FXML
    private TableColumn<Produto, Double> precoTable;

    @FXML
    private TableView<Produto> produtosTable;

    @FXML
    private TextField quantidadeTxt;

    @FXML
    void atualizaPrecoClick(ActionEvent event) {
        try {
            Double quantidade = Double.valueOf(quantidadeTxt.getText());
            ProdutoDAL pdal = new ProdutoDAL();
            Produto selectedID = produtosTable.getSelectionModel().getSelectedItem();
            if (selectedID != null && quantidade >= 0) {
                pdal.updatePrecoProdutoParaCliente(selectedID, quantidade);
                MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Preço atualizado!", "Informação:");
                initTable();
                quantidadeTxt.setText("");
            } else {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Introduza uma quantidade válida!", "Erro:");
            }
        } catch (NumberFormatException e) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Introduza uma quantidade válida!", "Erro:");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
    void clickTable(MouseEvent event) {

    }

    private void initTable() {
        idTable.setCellValueFactory(new PropertyValueFactory<Produto, String>("idProduto"));
        descricaoTable.setCellValueFactory(new PropertyValueFactory<Produto, String>("descricao"));
        precoTable.setCellValueFactory(new PropertyValueFactory<Produto, Double>("precoUnidade"));
        precoClienteTable.setCellValueFactory(new PropertyValueFactory<Produto, Double>("precoParaCliente"));
        produtosTable.setItems(ProdutoDAL.getAllProdutosPrecosClientes());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
    }
}
