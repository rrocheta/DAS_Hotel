package Controller;

import BLL.*;
import Model.*;
import com.example.hotel.*;
import javafx.collections.ObservableList;
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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.minidev.json.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class XMLReaderController implements Initializable {

    @FXML
    private TableView<Produto> produtoTable;

    @FXML
    private TableColumn<Produto, String> descricaoTable;

    @FXML
    private TableColumn<Produto, String> idProdTable;

    @FXML
    private TableColumn<Produto, Double> pesoTable;

    @FXML
    private TableColumn<Produto, Double> precoTable;

    @FXML
    private TableView<EntradaStock> table;

    @FXML
    private TableColumn<EntradaStock, Integer> caixasTable;

    @FXML
    private TableColumn<EntradaStock, String> identificacaoTable;

    @FXML
    private TableColumn<EntradaStock, String> localTable;

    @FXML
    private TableColumn<EntradaStock, Double> precoSemTaxaTable;

    @FXML
    private TableColumn<EntradaStock, Double> taxaTable;

    @FXML
    private TableColumn<EntradaStock, Integer> unidadesTable;

    @FXML
    private TableColumn<EntradaStock, Double> valorTaxaTable;

    @FXML
    private TableColumn<EntradaStock, Double> precoTotalTable;

    @FXML
    private Text moradaTxt;

    @FXML
    private Text noneFornecedorTxt;

    @FXML
    private Text ordemTxt;

    @FXML
    private Text paisTxt;

    @FXML
    private Text idFornecedorTxt;

    @FXML
    private Text codigoPostalTxt;

    @FXML
    private Text dataTxt;


    @FXML
    private Text urlText;

    XMLReaderBLL xmlreader;

    JSONReaderBLL jsonreader = new JSONReaderBLL();

    @FXML
    private Text cidadeTxt;

    @FXML
    private Button addItensBtn;

    @FXML
    private Button voltarBtn;

    @FXML
    void clickVoltarBtn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestaoStock.fxml"));
        Stage stage = new Stage();
        Stage newStage = (Stage) voltarBtn.getScene().getWindow();
        stage.setTitle("Gestao Stock");
        newStage.hide();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    List<String> lstFile;

    private ProdutoBLL produtoBLL = new ProdutoBLL();
    private FornecedorBLL fornecedorBLL = new FornecedorBLL();
    private EntradaStockBLL entradaStockBLL = new EntradaStockBLL();

    private ObservableList<EntradaStock> entradaStocks;
    private ObservableList<EntradaStock> fornecedores;
    private ObservableList<Produto> produtos;
    private ObservableList<Stock> stocks;

    @FXML
    void clickAddItens(ActionEvent event) {
        EntradaStockBLL entradaBLL = new EntradaStockBLL();
        if (!entradaBLL.verificaSeExisteEncomendaRepetida(ordemTxt.getText())) {
            produtoBLL.addProduto(produtos);
            fornecedorBLL.addFornecedor(fornecedores);
            entradaStockBLL.addEntradaStock(entradaStocks, fornecedores, stocks);
            MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Produtos inseridos com sucesso!", "Sucesso!");
        } else {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Já existe uma encomenda com esse número!", "Erro:");
        }
    }

    //----------------------------------- Upload Ficheiro XML -----------------------------------

    @FXML
    void clickXmlBtn(ActionEvent event) throws IOException, ParseException {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML File", lstFile));
        File f = fc.showOpenDialog(null);
        String fileName = f.getName();
        if (f != null) {
            urlText.setText("Ficheiro selecionado: " + f.getAbsolutePath());
            String path = f.getAbsolutePath();

            if (fileName.substring(fileName.lastIndexOf(".") + 1).equals("xml") ||
                    fileName.substring(fileName.lastIndexOf(".") + 1).equals("XML")) {
                //Ler XML
                if (ValidaXML.validarXML(f)) {
                    lerXML(path);
                } else {
                    urlText.setText("");
                    return;
                }
            } else {
                //Ler JSON
                lerJSON(path);
                // MessageBoxes.ShowMessage(Alert.AlertType.INFORMATION, "Formato JSON detetado", "Formato");
                // return;
            }
            if (entradaStocks.isEmpty() && fornecedores.isEmpty()) {
                addItensBtn.setDisable(true);
            } else {
                addItensBtn.setDisable(false);
                popularTabelaEntradaStock();
                popularTabelaProduto();
            }
        }
    }

    public void lerJSON(String path) throws IOException {
        entradaStocks = jsonreader.lerBody(path);
        fornecedores = jsonreader.lerHeader(path);
        produtos = jsonreader.lerProduto(path);
        stocks = jsonreader.lerStock(path);
    }

    public void lerXML(String path) {
        entradaStocks = xmlreader.lerXMLBody(path);
        fornecedores = xmlreader.lerXMLHeader(path);
        produtos = xmlreader.lerProduto(path);
        stocks = xmlreader.lerStock(path);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        xmlreader = new XMLReaderBLL();
        lstFile = new ArrayList<>();
        lstFile.add("*.xml");
        lstFile.add("*.XML");
        lstFile.add("*.json");
        lstFile.add("*.JSON");
    }

    //----------------------------------- Popular Tabela EntradaStock -----------------------------------

    private void popularTabelaEntradaStock() {

        //------- Popular Rows da Tabela -------

        if (entradaStocks != null && entradaStocks.size() > 0) {
            identificacaoTable.setCellValueFactory(new PropertyValueFactory<EntradaStock, String>("idProduto"));
            taxaTable.setCellValueFactory(new PropertyValueFactory<EntradaStock, Double>("taxa"));
            caixasTable.setCellValueFactory(new PropertyValueFactory<EntradaStock, Integer>("caixas"));
            localTable.setCellValueFactory(new PropertyValueFactory<EntradaStock, String>("local"));
            precoSemTaxaTable.setCellValueFactory(new PropertyValueFactory<EntradaStock, Double>("precoSemTaxa"));
            unidadesTable.setCellValueFactory(new PropertyValueFactory<EntradaStock, Integer>("unidades"));
            valorTaxaTable.setCellValueFactory(new PropertyValueFactory<EntradaStock, Double>("valorTaxa"));
            precoTotalTable.setCellValueFactory(new PropertyValueFactory<EntradaStock, Double>("precoTotal"));

            table.setItems(entradaStocks);
        }

        //------- Popular Header -------

        for (int i = 0; i < fornecedores.size(); i++) {
            ordemTxt.setText(fornecedores.get(i).getOrdemNum());
            moradaTxt.setText(fornecedores.get(i).getFornecedor().getMorada());
            noneFornecedorTxt.setText(fornecedores.get(i).getFornecedor().getNome());
            paisTxt.setText(fornecedores.get(i).getFornecedor().getPais());
            idFornecedorTxt.setText(fornecedores.get(i).getFornecedor().getIdFornecedor());
            codigoPostalTxt.setText(fornecedores.get(i).getFornecedor().getCodigoPostal());
            dataTxt.setText(fornecedores.get(i).getOrdemData());
            cidadeTxt.setText(fornecedores.get(i).getFornecedor().getCidade());
        }
    }

    //----------------------------------- Popular Tabela Produto -----------------------------------

    private void popularTabelaProduto() {

        //------- Popular Rows da Tabela -------

        if (produtos != null && produtos.size() > 0) {
            idProdTable.setCellValueFactory(new PropertyValueFactory<Produto, String>("idProduto"));
            descricaoTable.setCellValueFactory(new PropertyValueFactory<Produto, String>("descricao"));
            pesoTable.setCellValueFactory(new PropertyValueFactory<Produto, Double>("peso"));
            precoTable.setCellValueFactory(new PropertyValueFactory<Produto, Double>("precoUnidade"));

            produtoTable.setItems(produtos);
        }
    }
}
