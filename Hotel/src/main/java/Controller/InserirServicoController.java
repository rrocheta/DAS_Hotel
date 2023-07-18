package Controller;

import BLL.QuartoBLL;
import BLL.ServicoBLL;
import BLL.UtilizadorPreferences;
import DAL.DBconn;
import Model.Cartao;
import Model.MessageBoxes;
import Model.Quarto;
import Model.Servico;
import com.example.hotel.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InserirServicoController implements Initializable {

    @FXML
    private Button addBtn;

    @FXML
    private TextField descricao;

    @FXML
    private TableColumn<Servico, String> descricaoTable;

    @FXML
    private TableColumn<Servico, Integer> idTable;

    @FXML
    private TextField preco;

    @FXML
    private TableColumn<Servico, Double> precoTable;

    @FXML
    private Button removerBtn;

    @FXML
    private TableView<Servico> servicos;

    @FXML
    private Button voltarBtn;

    private ServicoBLL servicoBLL = new ServicoBLL();

    @FXML
    void clickAddBtn(ActionEvent event){
        if (descricao.getText().isEmpty() || preco.getText().isEmpty()
                || descricao.getText().isBlank() || preco.getText().isBlank()) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Introduza todos os campos", "Erro");
        } else {
            AddServico();
        }
    }

    public void AddServico() {
        try {
            double precoServico = Double.parseDouble(preco.getText().trim());
            Servico servico = new Servico(
                    null,
                    descricao.getText().trim(),
                    precoServico);

            servicoBLL.addServico(servico);
            initTable();
        } catch (NumberFormatException e) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR,"Erro ao inserir Serviço, O preco inserido não é valido", "Erro");
        }
    }


    @FXML
    void clickRemoverBtn(ActionEvent event) {
        ServicoBLL bll = new ServicoBLL();
        Servico selectedServico = servicos.getSelectionModel().getSelectedItem();
        if (selectedServico != null) {
            if (MessageBoxes.ConfirmationBox("Confirma a eliminação do serviço?")) {
                try {
                    bll.removeServico(selectedServico.getIdServico());
                    servicos.getItems().remove(selectedServico);
                    MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Servico Removido", "Information");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Selecione um serviço para eliminar!", "Erro:");
        }
    }

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

    private void initTable() {

        idTable.setResizable(false);
        descricaoTable.setResizable(false);
        precoTable.setResizable(false);

        idTable.setCellValueFactory(new PropertyValueFactory<Servico, Integer>("idServico"));
        descricaoTable.setCellValueFactory(new PropertyValueFactory<Servico, String>("servico"));
        precoTable.setCellValueFactory(new PropertyValueFactory<Servico, Double>("preco"));
        servicos.setItems(ServicoBLL.getServicos());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
    }
}
