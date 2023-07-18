package BLL;

import Model.EntradaStock;
import Model.Fornecedor;
import Model.Produto;
import Model.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JSONReaderBLL {
    //---------------------------------------------------- FORNECEDOR ----------------------------------------------------

    /**
     * Método que lê o cabeçalho de uma ordem de compra a partir de um ficheiro JSON.
     *
     * @param path Caminho para o ficheiro JSON
     * @return Uma ObservableList de objetos EntradaStock
     * @throws IOException Se ocorrer algum erro ao ler o arquivo JSON.
     */
    public ObservableList<EntradaStock> lerHeader(String path) throws IOException {
        ObservableList<EntradaStock> fornecedores = FXCollections.observableArrayList();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(path));

        JsonNode orderNode = rootNode.path("Order");
        JsonNode headerNode = orderNode.path("Header");
        String ordemNum = headerNode.path("OrderNumber").asText();
        String ordemData = headerNode.path("OrderDate").asText();
        JsonNode supplierPartyNode = headerNode.path("SupplierParty");
        String idFornecedor = supplierPartyNode.path("PartyIdentifier").asText();
        JsonNode addressNode = supplierPartyNode.path("NameAddress");
        String nomeFornecedor = addressNode.path("Name").asText();
        String moradaFornecedor = addressNode.path("Address1").asText();
        String cidadeFornecedor = addressNode.path("City").asText();
        String codPostalFornecedor = addressNode.path("PostalCode").asText();
        JsonNode countryNode = addressNode.path("Country");
        String paisFornecedor = countryNode.path("Value").asText();

        Fornecedor fornecedor = new Fornecedor(idFornecedor, nomeFornecedor, moradaFornecedor, codPostalFornecedor, paisFornecedor, cidadeFornecedor);

        fornecedores.add(new EntradaStock(ordemNum, ordemData, fornecedor));
        return fornecedores;
    }

    //-------------------------------------------------- ENTRADA STOCK ---------------------------------------------------

    /**
     * Método que lê o corpo de um arquivo JSON de entrada de stock.
     *
     * @param path Caminho para o arquivo JSON de entrada de stock.
     * @return Uma ObservableList de objetos EntradaStock.
     * @throws IOException Se ocorrer algum erro ao ler o arquivo JSON.
     */
    public ObservableList<EntradaStock> lerBody(String path) throws IOException {
        ObservableList<EntradaStock> item = FXCollections.observableArrayList();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(path));

        JsonNode orderNode = rootNode.path("Order");
        JsonNode lineNode = orderNode.get("Line");
        if (lineNode.isArray()) {
            for (JsonNode lineArray : lineNode) {
                String idProduto = "";
                Integer unidades;
                Double precoSemTaxa;
                Double taxa;
                Double valorTaxa;
                String local;
                Double precoTotal;
                JsonNode productNode = lineArray.path("Product");
                JsonNode productIdentNode = productNode.path("ProductIdentifier");
                if (productIdentNode.isArray()) {
                    for (JsonNode identifier : productIdentNode) {
                        idProduto = identifier.path("Code").asText();
                    }
                }
                JsonNode quantityNode = lineArray.path("Quantity");
                JsonNode valueNode = quantityNode.path("Value");
                unidades = (valueNode.path("Value").asInt());
                JsonNode monAjustNode = lineArray.path("MonetaryAdjustment");
                JsonNode monAjustStartNode = monAjustNode.path("MonetaryAdjustmentStartAmount");
                JsonNode CurrencyValueNode = monAjustStartNode.path("CurrencyValue");
                precoSemTaxa = (CurrencyValueNode.path("Value").asDouble());
                JsonNode taxAdjustNode = monAjustNode.path("TaxAdjustment");
                taxa = (taxAdjustNode.path("TaxPercent").asDouble());
                JsonNode taxAmountNode = taxAdjustNode.path("TaxAmount");
                JsonNode currencyValueNodeTax = taxAmountNode.path("CurrencyValue");
                valorTaxa = (currencyValueNodeTax.path("Value").asDouble());
                local = (taxAdjustNode.path("TaxLocation").asText());
                JsonNode lineBaseNode = lineArray.path("LineBaseAmount");
                JsonNode currencyValueNode = lineBaseNode.path("CurrencyValue");
                precoTotal = (currencyValueNode.path("Value").asDouble());

                item.add(new EntradaStock(taxa, 1, idProduto, local, precoSemTaxa, unidades, valorTaxa, precoTotal));
            }
        }
        return item;
    }

    //----------------------------------------------------- PRODUTO ------------------------------------------------------

    /**
     * Lê um arquivo JSON e retorna uma lista de objetos Produto.
     *
     * @param path O caminho para o arquivo JSON a ser lido.
     * @return Uma ObservableList de objetos Produto.
     * @throws IOException Se ocorrer algum erro ao ler o arquivo JSON.
     */

    public ObservableList<Produto> lerProduto(String path) throws IOException {
        ObservableList<Produto> produtos = FXCollections.observableArrayList();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(path));

        JsonNode orderNode = rootNode.path("Order");
        JsonNode lineNode = orderNode.get("Line");
        if (lineNode.isArray()) {
            for (JsonNode lineArray : lineNode) {
                String idProduto = "";
                String descricao = "";
                Double peso = 0.0;
                Double precoUnidade = 0.0;
                Integer unidades;
                JsonNode productNode = lineArray.path("Product");
                JsonNode productIdentNode = productNode.path("ProductIdentifier");
                if (productIdentNode.isArray()) {
                    for (JsonNode identifier : productIdentNode) {
                        idProduto = identifier.path("Code").asText();
                    }
                }
                descricao = productNode.path("ProductDescription").asText();
                JsonNode infQuantNode = lineArray.path("InformationalQuantity");
                if (productIdentNode.isArray()) {
                    for (JsonNode quantity : infQuantNode) {
                        JsonNode pesoNode = quantity.path("Value");
                        peso = pesoNode.path("Value").asDouble();
                    }
                }
                JsonNode pricePerUnitNode = lineArray.path("PricePerUnit");
                JsonNode currencyValueNode = pricePerUnitNode.path("CurrencyValue");
                precoUnidade = currencyValueNode.path("Value").asDouble();
                JsonNode quantityNode = lineArray.path("Quantity");
                JsonNode valueNode = quantityNode.path("Value");
                unidades = (valueNode.path("Value").asInt());

                Double pesoPorUnidade = (peso / unidades);

                produtos.add(new Produto(idProduto, descricao, precoUnidade, pesoPorUnidade, false));
            }
        }
        return produtos;
    }

    //------------------------------------------------------- STOCK ------------------------------------------------------

    /**
     * Método para ler o arquivo JSON de Stock.
     *
     * @param path Caminho do arquivo JSON de Stock.
     * @return ObservableList de objetos Stock.
     * @throws IOException Se ocorrer algum erro ao ler o arquivo JSON.
     */
    public ObservableList<Stock> lerStock(String path) throws IOException {
        ObservableList<Stock> stocks = FXCollections.observableArrayList();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(path));

        JsonNode orderNode = rootNode.path("Order");
        JsonNode lineNode = orderNode.get("Line");
        if (lineNode.isArray()) {
            for (JsonNode lineArray : lineNode) {
                String idProduto = "";
                Integer unidades;
                JsonNode productNode = lineArray.path("Product");
                JsonNode productIdentNode = productNode.path("ProductIdentifier");
                if (productIdentNode.isArray()) {
                    for (JsonNode identifier : productIdentNode) {
                        idProduto = identifier.path("Code").asText();
                    }
                }
                JsonNode quantityNode = lineArray.path("Quantity");
                JsonNode valueNode = quantityNode.path("Value");
                unidades = (valueNode.path("Value").asInt());

                stocks.add(new Stock(idProduto, unidades));
            }
        }
        return stocks;
    }
}

