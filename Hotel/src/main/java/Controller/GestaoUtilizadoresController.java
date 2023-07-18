package Controller;

import BLL.*;
import DAL.ComentarioDAL;
import DAL.TipoUtilizadorDAL;
import Model.MessageBoxes;
import Model.Servico;
import Model.TipoUtilizador;
import Model.Utilizador;
import com.example.hotel.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import DAL.DBconn;
import javafx.stage.Stage;

public class GestaoUtilizadoresController implements Initializable {

    @FXML
    private Label VerificarContacto;

    @FXML
    private Label VerificarNIF;

    @FXML
    private Label VerificarNome;

    @FXML
    private Label EmptyMessage;

    @FXML
    private TextField txt_contacto;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_nif;

    @FXML
    private TextField txt_nome;

    @FXML
    private TextField txt_password;

    @FXML
    private TextField txt_utilizador;


    @FXML
    private TableColumn<Utilizador, String> tblContato;

    @FXML
    private TableColumn<Utilizador, String> tblEmail;

    @FXML
    private TableColumn<Utilizador, Integer> tblId;

    @FXML
    private TableColumn<Utilizador, String> tblMorada;

    @FXML
    private TableColumn<Utilizador, String> tblNif;

    @FXML
    private TableColumn<Utilizador, String> tblNome;

    @FXML
    private TableColumn<Utilizador, String> tblTipo;

    @FXML
    private TableColumn<Utilizador, String> tblUtilizador;

    @FXML
    private TableView<Utilizador> tblUtilizadores;

    @FXML
    private TableColumn<Utilizador, Date> tbtDataNascimento;

    @FXML
    private Button btnVoltar;


    @FXML
    private Button btnGestorEliminar;

    @FXML
    private ComboBox<String> cmbUtilizador;

    private void initCombos() {
        List<TipoUtilizador> tipos = TipoUtilizadorDAL.getTipoUtilizador();

        for (TipoUtilizador tipo : tipos) {
            cmbUtilizador.getItems().add(tipo.getTipo());
        }
        cmbUtilizador.getItems().add("Todos");
    }

    @FXML
    void clickCmbUtilizador(ActionEvent event) {
        if (cmbUtilizador.getValue().equals("Gestor")) {
            initTableGestores();
        } else if (cmbUtilizador.getValue().equals("Funcionario")) {
            UtilizadorBLL.getAllFuncionarios();
            initTableFuncionarios();
        } else if (cmbUtilizador.getValue().equals("Cliente")) {
            UtilizadorBLL.getAllClientes();
            initTableClientes();
        } else if (cmbUtilizador.getValue().equals("Todos")) {
            UtilizadorBLL.getAllClientes();
            initTable();
        }
    }

    @FXML
    void clickAddCliente(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CriarUtilizador.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) btnVoltar.getScene().getWindow();
        stage.setTitle("Registo Utilizadores");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    void clickBtnGestorEliminar(ActionEvent event) {
        UtilizadorBLL ubll = new UtilizadorBLL();
        ComentarioBLL cBll = new ComentarioBLL();
        RegistoBLL rBll = new RegistoBLL();
        Utilizador selectedUtilizador = tblUtilizadores.getSelectionModel().getSelectedItem();
        int selectedComentario = tblUtilizadores.getSelectionModel().getSelectedItem().getId();
        if (selectedUtilizador != null) {
            if (MessageBoxes.ConfirmationBox("Confirma a eliminação do utilizador?")) {
                try {
                    rBll.deleteRegisto(selectedComentario);
                    cBll.deleteComentario(selectedComentario);
                    ubll.removeUtilizador(selectedUtilizador.getId());
                    tblUtilizadores.getItems().remove(selectedUtilizador);
                    MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Utilizador Removido!", "Informação:");
                    initTable();
                } catch (SQLException ex) {
                    if (ex.getMessage().contains("The DELETE statement conflicted with the REFERENCE constraint")) {
                        MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Não é possível remover o utilizador pois existe uma reserva associada a ele!", "Erro:");
                    } else {
                        throw new RuntimeException(ex);
                    }
                }
            }
        } else {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Selecione um utilizador para eliminar!", "Erro:");
        }
    }

    private void initTable() {

        tblId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tblEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbtDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        tblMorada.setCellValueFactory(new PropertyValueFactory<>("morada"));
        tblContato.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        tblNif.setCellValueFactory(new PropertyValueFactory<>("nif"));
        tblUtilizador.setCellValueFactory(new PropertyValueFactory<>("utilizador"));
        tblTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoUser().getTipo()));
        tblUtilizadores.setItems(UtilizadorBLL.getAllUtilizadores());
    }

    @FXML
    void clickBtnVoltar(ActionEvent event) throws IOException {

        if (UtilizadorPreferences.comparaTipoLogin()) {
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

    private void disableEliminarParaFuncionario() {
        if (!UtilizadorPreferences.comparaTipoLogin()) {
            btnGestorEliminar.setDisable(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCombos();
        initTable();
        disableEliminarParaFuncionario();
    }

    private void initTableGestores() {

        tblId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tblEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbtDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        tblMorada.setCellValueFactory(new PropertyValueFactory<>("morada"));
        tblContato.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        tblNif.setCellValueFactory(new PropertyValueFactory<>("nif"));
        tblUtilizador.setCellValueFactory(new PropertyValueFactory<>("utilizador"));
        tblTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoUser().getTipo()));
        tblUtilizadores.setItems(UtilizadorBLL.getAllGestores());
    }

    private void initTableFuncionarios() {

        tblId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tblEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbtDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        tblMorada.setCellValueFactory(new PropertyValueFactory<>("morada"));
        tblContato.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        tblNif.setCellValueFactory(new PropertyValueFactory<>("nif"));
        tblUtilizador.setCellValueFactory(new PropertyValueFactory<>("utilizador"));
        tblTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoUser().getTipo()));
        tblUtilizadores.setItems(UtilizadorBLL.getAllFuncionarios());
    }

    private void initTableClientes() {

        tblId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tblEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbtDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        tblMorada.setCellValueFactory(new PropertyValueFactory<>("morada"));
        tblContato.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        tblNif.setCellValueFactory(new PropertyValueFactory<>("nif"));
        tblUtilizador.setCellValueFactory(new PropertyValueFactory<>("utilizador"));
        tblTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoUser().getTipo()));
        tblUtilizadores.setItems(UtilizadorBLL.getAllClientes());
    }

}


