package Controller;

import BLL.EstacionamentoBLL;
import BLL.ReservaBLL;
import BLL.UtilizadorBLL;
import DAL.QuartoDAL;
import Model.*;
import Model.EstacionamentoAPI.Parking;
import Model.EstacionamentoAPI.TicketInfo;
import com.example.hotel.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;

import static BLL.ReservaBLL.getTotalReserva;

public class AdicionarReservaController implements Initializable {

    @FXML
    private Label EmptyMessage;

    @FXML
    private Label DataFim;

    @FXML
    private Label DataInicio;

    @FXML
    private Label ValidarQuarto;
    @FXML
    private DatePicker DatePickerFim;

    @FXML
    private DatePicker DatePickerInicio;

    @FXML
    private Button addServicoBtn;

    @FXML
    private Button adicionarReservaBtn;

    @FXML
    private Button btnRedictCriarCliente;

    @FXML
    private ComboBox<Utilizador> cmbClientes;


    @FXML
    private ComboBox<Quarto> cmbIDQuarto;

    @FXML
    private TextField dataFim;

    @FXML
    private TextField dataInicio;

    @FXML
    private TableColumn<Servico, String> descricaoServicoTable;

    @FXML
    private TableColumn<Servico, Integer> idServicoTable;

    @FXML
    private TextField nifCliente;

    @FXML
    private TableColumn<Servico, Double> precoServicoTable;

    @FXML
    private TableView<Servico> servicosTable;

    @FXML
    private TextField txtPreco;

    @FXML
    private Button voltarBtn;

    @FXML
    private CheckBox check;

    @FXML
    private ComboBox<String> cmbEstacionamento;

    @FXML
    private RadioButton exterior;

    @FXML
    private RadioButton interior;

    @FXML
    private Label lugarLbl;

    @FXML
    private Label matriculaLbl1;

    @FXML
    private TextField matriculaTxt;

    public static Boolean verifica = false;

    /**
     * Método de controlo do botão "Adicionar Reserva". Adiciona uma nova reserva ao sistema.
     *
     * @param event evento que provocou a chamada ao método
     * @throws SQLException exceção lançada em caso de erro na interação com a base de dados
     * @throws IOException  exceção lançada em caso de erro de input/output
     */
    @FXML
    void clickAddReservaBrn(ActionEvent event) throws SQLException, IOException {
        Quarto selectedRoom = cmbIDQuarto.getValue();
        LocalDate startDate = DatePickerInicio.getValue();
        if (selectedRoom == null || startDate == null) {
            // show error message
            return;
        }

        if (cmbClientes.getItems().isEmpty() == false && cmbIDQuarto.getItems().isEmpty() == false) {
            // if (VerificarDisponibilidade() == true){
            if (check.isSelected()) {
                if (matriculaTxt.getText().isEmpty() || cmbEstacionamento.getValue() == null) {
                    MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Preencha todos os campos!", "Erro");
                } else {
                    AdicionarReservaEEstacionamento();
                }
            } else {
                AdicionarReservaSemEstacionamento();
            }
            //   }
        } else {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Preencha todos os campos!", "Erro");
        }
    }

    /**
     * Método para lidar com o evento de clique no botão de Voltar na janela de adição de reservas.
     * Carrega a janela de gestão de reservas e esconde a janela atual.
     *
     * @param event O evento de clique no botão.
     * @throws IOException Se houver um problema ao carregar a janela de gestão de reservas.
     */
    @FXML
    void clickVoltarBtn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoReservas.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) voltarBtn.getScene().getWindow();
        stage.setTitle("Gerir Reservas");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    /**
     * Adiciona uma reserva sem estacionamento.
     *
     * @throws SQLException Exceção lançada em caso de erro ao acessar o banco de dados
     * @throws IOException  Exceção lançada em caso de erro ao acessar recursos de I/O
     */
    void AdicionarReservaSemEstacionamento() throws SQLException, IOException {
        ReservaBLL reservaBLL = new ReservaBLL();
        Reserva reserva = new Reserva(null, cmbClientes.getValue().getId(), cmbIDQuarto.getValue().getId(),
                DatePickerInicio.getValue().toString(), DatePickerFim.getValue().toString(), 0.0);

        reserva = reservaBLL.addReserva(reserva);

        double precoFinal = getTotalReserva(reserva);
        reserva.setPreco(precoFinal);

        txtPreco.setText(reserva.getPreco().toString());
        MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Reserva criada com sucesso.\nPreco total: " + precoFinal, "Reserva");
        addServicoReserva();
    }

    /**
     * Método que adiciona uma reserva e estacionamento ao mesmo tempo.
     *
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    void AdicionarReservaEEstacionamento() throws SQLException, IOException {
        ReservaBLL reservaBLL = new ReservaBLL();
        Reserva reserva = new Reserva(null, cmbClientes.getValue().getId(), cmbIDQuarto.getValue().getId(),
                DatePickerInicio.getValue().toString(), DatePickerFim.getValue().toString(), 0.0);

        reserva = reservaBLL.addReserva(reserva);

        double precoFinal = getTotalReserva(reserva);
        reserva.setPreco(precoFinal);

        TicketInfo ticketInfo = new TicketInfo(
                Integer.toString(cmbClientes.getValue().getId()),
                matriculaTxt.getText().trim(),
                DatePickerInicio.getValue().toString(),
                DatePickerFim.getValue().toString(),
                cmbEstacionamento.getValue().trim(),
                true);
        reservaBLL.reservaEstacionamento(reserva, ticketInfo);

        txtPreco.setText(reserva.getPreco().toString());
        MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Reserva criada com sucesso.\nPreco total: " + precoFinal, "Reserva");
        addServicoReserva();
    }

    /**
     * Adiciona os serviços a uma reserva existente.
     *
     * @throws IOException
     */
    private void addServicoReserva() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ServicoReserva.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) adicionarReservaBtn.getScene().getWindow();
        stage.setTitle("Servicos Reserva");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
        verifica = true;
    }

    /**
     * Inicializa as combobox da interface.
     * Adiciona todos os quartos retornados pela função getAllQuartos() do QuartoDAL à combobox cmbIDQuarto.
     * Adiciona todos os clientes retornados pela função getAllClientes() do UtilizadorBLL à combobox cmbClientes.
     */
    private void initCombos() {
        cmbIDQuarto.getItems().addAll(QuartoDAL.getAllQuartos());
        cmbClientes.getItems().addAll(UtilizadorBLL.getAllClientes().stream().collect(Collectors.toList()));
    }

    /**
     * Método que redireciona para a janela de gestão de utilizadores.
     *
     * @param actionEvent evento de ação ocorrido no botão
     * @throws IOException se ocorrer algum erro de entrada/saída durante o carregamento da janela
     */
    public void btnRedictCriarCliente(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoUtilizadores.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) btnRedictCriarCliente.getScene().getWindow();
        stage.setTitle("Gestao Utilizadores");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }


    @FXML
    void quartoAction(ActionEvent event) {
        resetDatePickers();
    }

    /**
     * Método para desativar os dias ocupados no picker de data inicial.
     *
     * @param idQuarto Id do quarto que será verificado
     */
    private void desativarDiasOcupadosDatePickerInicio(int idQuarto) {
        ReservaBLL rBLL = new ReservaBLL();
        List<LocalDate> listaDatasIniciais = rBLL.getDataInicial(idQuarto);
        List<LocalDate> listaDatasFinais = rBLL.getDataFinal(idQuarto);
        DatePickerInicio.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                ArrayList<LocalDate> diasDesativados = new ArrayList<>();
                for (int i = 0; i < listaDatasIniciais.size(); i++) {
                    LocalDate inicio = listaDatasIniciais.get(i);
                    LocalDate fim = listaDatasFinais.get(i);
                    while (!inicio.isAfter(fim)) {
                        diasDesativados.add(inicio);
                        inicio = inicio.plusDays(1);
                    }
                    if (diasDesativados.contains(date)) {
                        setDisable(true);
                        setStyle("-fx-background-color: #FFB6C1;");
                    }
                }
            }
        });
    }

    /**
     * Método para desativar as datas ocupadas em um date picker.
     *
     * @param dataInicio a data inicial selecionada
     */
    private void desativarDiasOcupadosDatePickerFim(LocalDate dataInicio) {
        int idQuarto = cmbIDQuarto.getValue().getId();
        ReservaBLL rBLL = new ReservaBLL();
        LocalDate dataFim = rBLL.getProxData(idQuarto, dataInicio);
        DatePickerFim.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                if (dataFim != null) {
                    if (date.isBefore(dataInicio.plusDays(1)) || date.isAfter(dataFim.minusDays(1))) {
                        setDisable(true);
                        setStyle("-fx-background-color: #FFB6C1;");
                    }
                }
            }
        });
    }

    /**
     * Desativa as datas ocupadas no date picker de fim de reserva quando não há nenhuma data de fim disponível.
     *
     * @param dataInicio data de inicio da reserva
     */
    private void desativarDiasOcupadosDatePickerFimCasoNull(LocalDate dataInicio) {
        DatePickerFim.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                if (date.isBefore(dataInicio.plusDays(1))) {
                    setDisable(empty || date.isBefore(dataInicio.plusDays(1)));
                    setStyle("-fx-background-color: #FFB6C1;");
                }
            }
        });
    }

    /**
     * Método que adiciona um listener ao ComboBox da reserva para verificar se já existe uma reserva para o quarto selecionado.
     * Caso exista, o método {@link #desativarDiasOcupadosDatePickerInicio(int)} é invocado para desativar os dias ocupados na escolha da data de início.
     * Caso contrário, o método {@link #listenDatePickerInicio(int)} é invocado para permitir a escolha da data de início.
     */
    private void listenComboboxReserva() {
        ReservaBLL rBLL = new ReservaBLL();
        cmbIDQuarto.setOnAction(event -> {
            int idQuarto = cmbIDQuarto.getValue().getId();
            if (rBLL.verificaSeReservaExiste(idQuarto)) {
                desativarDiasOcupadosDatePickerInicio(idQuarto);
                listenDatePickerInicio(idQuarto);
            } else {
                listenDatePickerInicio(idQuarto);
            }
        });
    }

    /**
     * Adiciona um listener ao DatePickerInicio para desabilitar os dias ocupados de acordo com a escolha do quarto.
     * Se houver uma reserva anterior para o quarto escolhido, os dias ocupados serão desabilitados no DatePickerFim.
     * Caso contrário, todas as datas antes da data escolhida no DatePickerInicio serão desabilitadas no DatePickerFim.
     *
     * @param idQuarto ID do quarto escolhido.
     */
    private void listenDatePickerInicio(int idQuarto) {
        ReservaBLL rBLL = new ReservaBLL();
        DatePickerInicio.valueProperty().addListener((observable, oldValue, newValue) -> {
            LocalDate dataInicio = DatePickerInicio.getValue();
            if (rBLL.getProxData(idQuarto, dataInicio) != null) {
                desativarDiasOcupadosDatePickerFim(dataInicio);
            } else {
                desativarDiasOcupadosDatePickerFimCasoNull(dataInicio);
            }
        });
    }

    /**
     * Método para resetar os valores dos componentes de seleção de data (DatePicker) de Início e Fim.
     * Define a data para nula e torna todas as datas disponíveis para seleção.
     */
    private void resetDatePickers() {
        DatePickerInicio.setValue(null);
        DatePickerInicio.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(false);
            }
        });
        DatePickerFim.setValue(null);
        DatePickerFim.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(false);
            }
        });
    }

    /**
     * Método para tratar o evento de clique no DatePickerFim.
     * Verifica se a data inicial já foi selecionada. Se não foi, exibe uma mensagem de erro ao usuário.
     *
     * @param event Evento de clique no DatePickerFim
     */
    @FXML
    void clickDateFim(MouseEvent event) {
        if (DatePickerInicio.getValue() == null) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Selecione primeiro a data inicial!", "Erro:");
        }
    }

    @FXML
    void cmbQuartoClick(MouseEvent event) {
        resetDatePickers();
    }

    /**
     * Verifica se as datas selecionadas nos DatePickers não são nulas.
     *
     * @return retorna "false" se as datas são nulas, e "true" caso contrário.
     */
    private Boolean verificaSeDatasNaoSaoNull() {
        if (DatePickerInicio.getValue() == null || DatePickerFim.getValue() == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Método que adiciona um listener ao checkbox para habilitar ou desabilitar a seleção de estacionamento.
     * Verifica se as datas inicial e final foram selecionadas antes de permitir a seleção de estacionamento.
     * Se todos os estacionamentos estiverem ocupados, exibe uma mensagem de erro.
     */
    private void listnerCheckBox() {
        check.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //conta nº de lugares ocupados
                EstacionamentoBLL eBLL = new EstacionamentoBLL();
                var lugares = eBLL.GetTicketsCriados();
                var estacionamentos = eBLL.GetLugares();
                int ticketsCriados = 0;
                for (int i = 0; i < lugares.Tickets.size(); i++) {
                    ticketsCriados++;
                }
                // verifica se estão todos ocupados e dá erro se estiverem
                if (ticketsCriados == estacionamentos.Parking.size()) {
                    MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Não há estacionamentos disponíveis!", "Erro:");
                    check.setSelected(false);
                    return;
                }

                if (verificaSeDatasNaoSaoNull()) {
                    if (check.isSelected()) {
                        interior.setDisable(false);
                        exterior.setDisable(false);
                        cmbEstacionamento.setDisable(false);
                        lugarLbl.setDisable(false);
                        interior.setSelected(true);
                        matriculaTxt.setDisable(false);
                        matriculaLbl1.setDisable(false);
                        cmbEstacionamento.getItems().clear();
                        cmbEstacionamento.getItems().addAll(getEstacionamentoDisponivel(true));
                    } else {
                        interior.setDisable(true);
                        exterior.setDisable(true);
                        cmbEstacionamento.setDisable(true);
                        lugarLbl.setDisable(true);
                        matriculaTxt.setDisable(true);
                        matriculaLbl1.setDisable(true);
                    }
                } else {
                    MessageBoxes.ShowMessage(Alert.AlertType.WARNING, "Preencher data inicial e final primeiro!", "Aviso:");
                    check.setSelected(false);
                }
            }
        });
    }

    /**
     * Método que inicializa os componentes da interface gráfica.
     *
     * @param location  URL
     * @param resources ResourceBundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCombos();
        listenComboboxReserva();
        listnerCheckBox();
        popularCmbEstacionamentoConsoanteOpcaoRadioButton();
    }


    /**
     * Método que retorna uma lista de estacionamentos disponíveis.
     *
     * @return lista de estacionamentos disponíveis
     */
    private List<String> getEstacionamentoDisponivel(Boolean indoor) {
        try {
            //LocalDate dataInicial = DatePickerInicio.getValue();
            //LocalDate dataFinal = DatePickerFim.getValue();
            List<String> list = new ArrayList<>();
            EstacionamentoBLL eBLL = new EstacionamentoBLL();
            //var ticket = eBLL.GetTicketsCriados();
            var lugares = eBLL.GetLugares();

                for (int j = 0; j < lugares.Parking.size(); j++) {
                    Parking estacionamento = lugares.Parking.get(j);
                  // LocalDateTime offSetStartDate = LocalDateTime.parse(currentParking.StartDate);
                  // LocalDate startDate = offSetStartDate.toLocalDate();
                  // LocalDateTime offSetEncDate = LocalDateTime.parse(currentParking.StartDate);
                  // LocalDate endDate = offSetEncDate.toLocalDate();
                    if (estacionamento.Occupied == false && estacionamento.Indoor == indoor) {
                        list.add(estacionamento.ParkingSpot);
                    }
                }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Este método popular o ComboBox de estacionamento com base na opção selecionada no RadioButton interior ou exterior.
     * Caso o RadioButton interior seja selecionado, o ComboBox será populado com lugares de estacionamento interno disponíveis.
     * Caso o RadioButton exterior seja selecionado, o ComboBox será populado com lugares de estacionamento externo disponíveis.
     */
    private void popularCmbEstacionamentoConsoanteOpcaoRadioButton() {
        interior.setOnAction(event -> {
            if (interior.isSelected()) {
                cmbEstacionamento.getItems().clear();
                cmbEstacionamento.getItems().addAll(getEstacionamentoDisponivel(true));
            }
        });
        exterior.setOnAction(event -> {
            if (exterior.isSelected()) {
                cmbEstacionamento.getItems().clear();
                cmbEstacionamento.getItems().addAll(getEstacionamentoDisponivel(false));
            }
        });
    }

}


