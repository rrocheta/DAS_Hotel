package Controller;

import BLL.*;
import Model.Checkout;
import Model.EstacionamentoAPI.Ticket;
import Model.MessageBoxes;
import Model.Pagamento;
import Model.Reserva;
import com.example.hotel.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CheckInController implements Initializable {

    @FXML
    private AnchorPane PainelGestorStock;

    @FXML
    private Button Voltar;

    @FXML
    private Button btnCheckOut;

    @FXML
    private Button btnCheckin;

    @FXML
    private ImageView imgGestorGestaoProduto;

    @FXML
    private ImageView imgGestorStock;

    @FXML
    private ImageView imgGestorStock1;

    @FXML
    private ImageView imgGestorStock11;

    @FXML
    private Label lblData1;

    @FXML
    private Label lblData11;

    @FXML
    private Label lblHotel;

    @FXML
    private Label lblSamos;

    @FXML
    private ListView<Reserva> listViewReservaComcheckin;

    @FXML
    private ListView<Reserva> listViewReservaSemCheckin;

    @FXML
    private ComboBox<Pagamento> metodoPagamento;

    /**
     * O método initCombos inicializa o combo box de pagamentos.
     * Ele adiciona uma lista de objetos do tipo Pagamento ao combo box.
     * A lista é obtida a partir da chamada ao método getPagamentos na classe CheckoutBLL.
     *
     * @throws SQLException é lançada quando ocorre um erro ao obter dados do banco de dados.
     */
    private void initCombos() throws SQLException {
        CheckoutBLL checkoutBLL = new CheckoutBLL();
        ObservableList<Pagamento> pagamentos = checkoutBLL.getPagamentos();
        metodoPagamento.setItems(pagamentos);

    }

    /**
     * Método que controla o evento de clique no botão "Voltar".
     * Verifica o tipo de utilizador logado através da classe UtilizadorPreferences, e abre a página correspondente.
     * Se o utilizador for do tipo gestor, abre a página "PainelGestor.fxml".
     * Se o utilizador for do tipo funcionário, abre a página "PainelFuncionario.fxml".
     *
     * @param event evento de clique no botão "Voltar".
     * @throws IOException pode ocorrer uma exceção de entrada/saída.
     */
    @FXML
    void VoltarClick(ActionEvent event) throws IOException {
        if (UtilizadorPreferences.comparaTipoLogin()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PainelGestor.fxml"));
            Stage stage = new Stage();
            Stage newStage = (Stage) Voltar.getScene().getWindow();
            stage.setTitle("Pagina Gestor");
            newStage.hide();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PainelFuncionario.fxml"));
            Stage stage = new Stage();
            Stage newStage = (Stage) Voltar.getScene().getWindow();
            stage.setTitle("Pagina Funcionario");
            newStage.hide();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        }
    }

    /**
     * Método responsável pelo checkout da reserva selecionada.
     * Verifica se uma reserva foi selecionada na lista de reservas com check-in realizado. Se não for selecionada, é mostrado uma mensagem de erro.
     * Verifica se um método de pagamento foi selecionado. Se não for selecionado, é mostrado uma mensagem de erro.
     * Atualiza o estado da reserva selecionada para checkout.
     * Devolve os não consumíveis da reserva de volta ao estoque.
     * Calcula o custo total da reserva selecionada.
     * Adiciona um novo checkout a base de dados com o id da reserva, o custo total e o método de pagamento selecionado.
     * Mostra um recibo com o id da reserva, o custo total e o método de pagamento selecionado.
     *
     * @param event O evento de clique no botão de checkout.
     */
    @FXML
    void checkoutBtnAction(ActionEvent event) {
        Reserva selectedReservation = listViewReservaComcheckin.getSelectionModel().getSelectedItem();
        if (selectedReservation == null) {

            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Erro", "Por favor, selecione uma reserva.");
            return;
        }

        Pagamento selectedPaymentMethod = metodoPagamento.getSelectionModel().getSelectedItem();
        if (selectedPaymentMethod == null) {

            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Erro", "Por favor, selecione um método de pagamento.");
            return;
        }

        CheckoutBLL checkoutBll = new CheckoutBLL();
        try {
           // ReservaBLL rBLL = new ReservaBLL();
           // EstacionamentoBLL eBLL = new EstacionamentoBLL();

            checkoutBll.updateReservationStateCheckout(selectedReservation.getId());

            checkoutBll.voltaNaoConsumiveisAoStock(selectedReservation.getId());
           //String ticketIDDaReserva = rBLL.retornaTicketIDDeUmaReserva(selectedReservation.getId().toString());
           //Ticket ticket = eBLL.GETParkingTicket(selectedReservation.getId().toString());
           //var ticketBody = ticket.TicketInfo.get(0);
           //ticketBody.Active = false;
            // eBLL.PUTCreateParkingReservation(ticketBody, ticketIDDaReserva);
            ReservaBLL reservaBLL = new ReservaBLL();
            double totalCost = reservaBLL.getTotalReserva(selectedReservation);

            Checkout checkout = new Checkout(selectedReservation.getId(), totalCost, selectedPaymentMethod.getMetodoPagamento());

            CheckoutBLL bll = new CheckoutBLL();
            bll.addCheckout(checkout);

            String receiptText = "Reserva: " + selectedReservation.getId() + "\n";
            receiptText += "Preço Final: " + totalCost + "\n";
            receiptText += "Método de Pagamento: " + selectedPaymentMethod.getMetodoPagamento() + "\n";

            MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Recibo", receiptText);
            initListViews();
        } catch (SQLException e) {

            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Erro", "Ocorreu um problema ao realizar o checkout. Por favor, tente novamente mais tarde.");
        }
    }

    /**
     * O método handleCheckInButtonAction é invocado quando o botão de check-in é pressionado.
     * Ele irá selecionar uma reserva da lista de reservas sem check-in, e irá realizar o check-in para essa reserva.
     * Se uma reserva for selecionada, o método {@link #performCheckIn(Reserva)} é invocado para realizar o check-in.
     * Se não houver reserva selecionada, uma mensagem de erro é exibida ao usuário.
     *
     * @param event O evento que disparou a ação.
     */
    @FXML
    void handleCheckInButtonAction(ActionEvent event) {

        Reserva selectedReservation = listViewReservaSemCheckin.getSelectionModel().getSelectedItem();


        if (selectedReservation != null) {

            performCheckIn(selectedReservation);


            initListViews();
        } else {

            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Por favor, selecione uma reserva da lista.", "Erro");
        }
    }

    /**
     * Realiza o check-in de uma reserva.
     *
     * @param reservation a reserva que será realizada o check-in
     */
    private void performCheckIn(Reserva reservation) {
        CheckInBLL checkInBll = new CheckInBLL();
        LocalDate today = LocalDate.parse(LocalDate.now().toString());
        LocalDate reservationStart = LocalDate.parse(reservation.getDataInicio().toString());
        if (today.isEqual(reservationStart)) {
            try {
                checkInBll.checkIn(reservation.getId());
                double updatedTotal = ReservaBLL.getTotalReserva(reservation);
                reservation.setPreco(updatedTotal);
                ReservaBLL.updateReserva(reservation);
                MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Check-in realizado com sucesso.", "Sucesso");
            } catch (SQLException e) {
                MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Ocorreu um problema ao realizar o check-in. Por favor, tente novamente mais tarde.", "ERRO");
            }
        } else {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Check-in não pode ser realizado pois hoje não está dentro do período da reserva.", "ERRO");
        }
    }


    /**
     * Método para inicializar as list views de reservas.
     */
    private void initListViews() {
        CheckoutBLL bll = new CheckoutBLL();
        try {
            listViewReservaSemCheckin.setItems(FXCollections.observableArrayList(bll.getPendingReservations()));
        } catch (SQLException e) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Erro", "Ocorreu um problema ao recuperar as reservas pendentes. Por favor, tente novamente mais tarde.");
            e.printStackTrace();
        }

        try {
            listViewReservaComcheckin.setItems(FXCollections.observableArrayList(bll.getCheckedInReservations()));
        } catch (SQLException e) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Erro", "Ocorreu um problema ao recuperar as reservas pendentes. Por favor, tente novamente mais tarde.");
            e.printStackTrace();
        }
    }

    /**
     * Método inicializa as combo boxes e as list views.
     *
     * @param location  URL do local da inicialização
     * @param resources Recursos da inicialização
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initCombos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        initListViews();
    }
}