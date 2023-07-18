package BLL;

import Model.EntradaStock;
import Model.Fornecedor;
import Model.Produto;
import Model.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;

public class XMLReaderBLL {

    /**
     * Lê o cabeçalho do arquivo XML fornecido e retorna uma lista de entradas de stock.
     *
     * @param path O caminho para o arquivo XML a ser lido.
     * @return Uma ObservableList de entradas de estoque lidas do arquivo.
     */
    public ObservableList<EntradaStock> lerXMLHeader(String path) {
        ObservableList<EntradaStock> fornecedores = FXCollections.observableArrayList();

        try {
            File fXmlFile = new File(path);

            //Define a API que instancia o documento XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //Normaliza o XML
            doc.getDocumentElement().normalize();

            //----------------------------------- Header -----------------------------------

            //Grava numa NodeList
            NodeList nListHeader = doc.getElementsByTagName("Header");

            //Percorre a lista e faz o Get pelas respetivas Tags
            for (int tempHeader = 0; tempHeader < nListHeader.getLength(); tempHeader++) {
                Node hNode = nListHeader.item(tempHeader);
                if (hNode.getNodeType() == Node.ELEMENT_NODE) {

                    // Line
                    Element HeadElement = (Element) hNode;

                    // Order Number
                    Element orderNumberElement = FindInChildren(HeadElement, "OrderNumber");
                    String ordemNum = orderNumberElement.getTextContent();

                    // OrderDate
                    Element orderDateElement = FindInChildren(HeadElement, "OrderDate");
                    Element dateElement = FindInChildren(orderDateElement, "Date");
                    Element yearElement = FindInChildren(dateElement, "Year");
                    Element monthElement = FindInChildren(dateElement, "Month");
                    Element dayElement = FindInChildren(dateElement, "Day");
                    Integer ano = Integer.valueOf(yearElement.getTextContent());
                    Integer mes = Integer.valueOf(monthElement.getTextContent());
                    Integer dia = Integer.valueOf(dayElement.getTextContent());
                    String ordemData = ano + "/" + mes + "/" + dia;

                    // SupplierParty
                    Element supplierPartyElement = FindInChildren(HeadElement, "SupplierParty");
                    Element partyIdentifierEl = FindInChildren(supplierPartyElement, "PartyIdentifier");
                    Element nameAddressElement = FindInChildren(supplierPartyElement, "NameAddress");
                    Element nameElement = FindInChildren(nameAddressElement, "Name");
                    Element address1Element = FindInChildren(nameAddressElement, "Address1");
                    Element address2Element = FindInChildren(nameAddressElement, "Address2");
                    Element cityElement = FindInChildren(nameAddressElement, "City");
                    Element postalCodeElement = FindInChildren(nameAddressElement, "PostalCode");
                    Element countryElement = FindInChildren(nameAddressElement, "Country");
                    String address1 = address1Element.getTextContent();
                    String address2 = address2Element.getTextContent();
                    String idFornecedor = partyIdentifierEl.getTextContent();
                    String nomeFornecedor = nameElement.getTextContent();
                    String cidadeFornecedor = cityElement.getTextContent();
                    String moradaFornecedor = address1 + ", " + address2;
                    String codPostalFornecedor = postalCodeElement.getTextContent();
                    String paisFornecedor = countryElement.getTextContent();

                    Fornecedor fornecedor = new Fornecedor(idFornecedor, nomeFornecedor, moradaFornecedor, codPostalFornecedor, paisFornecedor, cidadeFornecedor);
                    fornecedores.add(new EntradaStock(ordemNum, ordemData, fornecedor));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fornecedores;
    }
    //----------------------------------- BODY -----------------------------------

    /**
     * Lê o arquivo XML especificado em 'path' e retorna uma lista observável de objetos EntradaStock.
     *
     * @param path Caminho do arquivo XML a ser lido
     * @return ObservableList de objetos EntradaStock
     */
    public ObservableList<EntradaStock> lerXMLBody(String path) {
        ObservableList<EntradaStock> item = FXCollections.observableArrayList();

        try {
            File fXmlFile = new File(path);

            //Define a API que instancia o documento XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //Normaliza o XML
            doc.getDocumentElement().normalize();

            //Grava numa NodeList
            NodeList nList = doc.getElementsByTagName("Line");

            //Percorre a lista e faz o Get pelas respetivas Tags
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    // Line
                    Element lineElement = (Element) nNode;

                    // Product
                    Element productElement = FindInChildren(lineElement, "Product");
                    Element productIdentifierEl = FindInChildren(productElement, "ProductIdentifier");
                    String idProduto = productIdentifierEl.getTextContent();

                    // Quantity
                    Element elQuantity = FindInChildren(lineElement, "Quantity");
                    Element elQuantityValue = FindInChildren(elQuantity, "Value");
                    Integer caixas = Integer.valueOf(elQuantityValue.getTextContent());

                    Element iqUnitsElement = FindInChildren(lineElement, "InformationalQuantity", 10);
                    Integer unidades = 0;
                    if (iqUnitsElement != null) {
                        Element iQUnitsValueElement = FindInChildren(iqUnitsElement, "Value");
                        unidades = Integer.valueOf(iQUnitsValueElement.getTextContent());
                    }

                    // MonetaryAdjustment
                    Element monAdjElement = FindInChildren(lineElement, "MonetaryAdjustment");
                    Element monAdjStartElement = FindInChildren(monAdjElement, "MonetaryAdjustmentStartAmount");
                    Element monAdjStartValueElement = FindInChildren(monAdjStartElement, "CurrencyValue");
                    Element monAdjTaxElement = FindInChildren(monAdjElement, "TaxAdjustment");
                    Element monAdjTaxPercentElement = FindInChildren(monAdjTaxElement, "TaxPercent");
                    Element monAdjTaxLocationElement = FindInChildren(monAdjTaxElement, "TaxLocation");
                    Element monAdjTaxAmountElement = FindInChildren(monAdjTaxElement, "TaxAmount");
                    Element monAdjTaxAmountValueElement = FindInChildren(monAdjTaxAmountElement, "CurrencyValue");
                    double precoSemTaxa = Double.parseDouble(monAdjStartValueElement.getTextContent());
                    double taxa = Double.parseDouble(monAdjTaxPercentElement.getTextContent());
                    String local = monAdjTaxLocationElement.getTextContent();
                    double valorTaxa = Double.parseDouble(monAdjTaxAmountValueElement.getTextContent());

                    // LineBaseAmount
                    Element lineBaseAmountElementElement = FindInChildren(lineElement, "LineBaseAmount");
                    Element CurrencyValueEl = FindInChildren(lineBaseAmountElementElement, "CurrencyValue");
                    double precoTotal = Double.parseDouble(CurrencyValueEl.getTextContent());

                    // Iguala nº de unidades ao nº de caixas caso unidades seja 0
                    if (unidades == 0) {
                        unidades = caixas;
                    }

                    item.add(new EntradaStock(taxa, caixas, idProduto, local, precoSemTaxa, unidades, valorTaxa, precoTotal));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    //----------------------------------- Criar Produto -----------------------------------

    /**
     * Método que lê um arquivo XML e retorna umaObservableList Produto.
     *
     * @param path Caminho para o arquivo XML.
     * @return ObservableList doss Produto.
     */
    public ObservableList<Produto> lerProduto(String path) {
        ObservableList<Produto> produtos = FXCollections.observableArrayList();

        try {
            File fXmlFile = new File(path);

            //Define a API que instancia o documento XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //Normaliza o XML
            doc.getDocumentElement().normalize();

            //Grava numa NodeList
            NodeList nList = doc.getElementsByTagName("Line");

            //Percorre a lista e faz o Get pelas respetivas Tags
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    // Line
                    Element lineElement = (Element) nNode;

                    // Product
                    Element productElement = FindInChildren(lineElement, "Product");
                    Element productIdentifierEl = FindInChildren(productElement, "ProductIdentifier");
                    Element productDescriptionEl = FindInChildren(productElement, "ProductDescription");
                    String idProduto = productIdentifierEl.getTextContent();
                    String descricao = productDescriptionEl.getTextContent();

                    // InformationalQuantity
                    Element iqWeightElement = FindInChildren(lineElement, "InformationalQuantity");
                    Element iQWeightValueElement = FindInChildren(iqWeightElement, "Value");
                    double peso = Double.parseDouble(iQWeightValueElement.getTextContent());

                    // PricePerUnit
                    Element ppuElement = FindInChildren(lineElement, "PricePerUnit");
                    Element ppuCurrencyValueElement = FindInChildren(ppuElement, "CurrencyValue");
                    double precoUnidade = Double.parseDouble(ppuCurrencyValueElement.getTextContent());

                    // Quantity
                    Element elQuantity = FindInChildren(lineElement, "Quantity");
                    Element elQuantityValue = FindInChildren(elQuantity, "Value");
                    Integer caixas = Integer.valueOf(elQuantityValue.getTextContent());

                    Element iqUnitsElement = FindInChildren(lineElement, "InformationalQuantity", 10);
                    Integer unidades = 0;
                    if (iqUnitsElement != null) {
                        Element iQUnitsValueElement = FindInChildren(iqUnitsElement, "Value");
                        unidades = Integer.valueOf(iQUnitsValueElement.getTextContent());
                    }
                    if (unidades == 0) {
                        unidades = caixas;
                    }

                    Double pesoPorUnidade = (peso / unidades);

                    produtos.add(new Produto(idProduto, descricao, precoUnidade, pesoPorUnidade, false));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produtos;
    }

    //----------------------------------- Cria StockController -----------------------------------

    /**
     * Método para ler informações de estoque de um ficheiro XML
     *
     * @param path Caminho do ficheiro XML
     * @return ObservableList Stock com informações de stock lidas do ficheiro
     */
    public ObservableList<Stock> lerStock(String path) {
        ObservableList<Stock> stocks = FXCollections.observableArrayList();

        try {
            File fXmlFile = new File(path);

            //Define a API que instancia o documento XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //Normaliza o XML
            doc.getDocumentElement().normalize();

            //Grava numa NodeList
            NodeList nList = doc.getElementsByTagName("Line");

            //Percorre a lista e faz o Get pelas respetivas Tags
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    // Line
                    Element lineElement = (Element) nNode;

                    // Product
                    Element productElement = FindInChildren(lineElement, "Product");
                    Element productIdentifierEl = FindInChildren(productElement, "ProductIdentifier");
                    String idProduto = productIdentifierEl.getTextContent();

                    // Quantity
                    Element elQuantity = FindInChildren(lineElement, "Quantity");
                    Element elQuantityValue = FindInChildren(elQuantity, "Value");
                    Integer caixas = Integer.valueOf(elQuantityValue.getTextContent());

                    Element iqUnitsElement = FindInChildren(lineElement, "InformationalQuantity", 10);
                    Integer unidades = 0;
                    if (iqUnitsElement != null) {
                        Element iQUnitsValueElement = FindInChildren(iqUnitsElement, "Value");
                        unidades = Integer.valueOf(iQUnitsValueElement.getTextContent());
                    }
                    if (unidades == 0) {
                        unidades = caixas;
                    }
                    stocks.add(new Stock(idProduto, unidades));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stocks;
    }

    //----------------------------------- FindInChildren -----------------------------------

    /**
     * Método privado que procura um child element específico num parent element.
     *
     * @param parent       O parent element que será percorrido.
     * @param elementToGet O nome do child element que será procurado.
     * @return O child element encontrado, ou nulo caso não seja encontrado.
     */
    private Element FindInChildren(Element parent, String elementToGet) {
        NodeList list = parent.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node nNode = list.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.getNodeName() == elementToGet) {
                return (Element) nNode;
            }
        }
        return null;
    }

    /**
     * Este método busca um child element específico em um parent element.
     *
     * @param parent       Parent element.
     * @param elementToGet Nome do child element que deseja obter.
     * @param elementCount Número do child element desejado.
     * @return O child element encontrado ou nulo caso não exista.
     */
    private Element FindInChildren(Element parent, String elementToGet, int elementCount) {
        NodeList list = parent.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node nNode = list.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE
                    && nNode.getNodeName() == elementToGet
                    && i + 1 == elementCount) {
                return (Element) nNode;
            }
        }
        return null;
    }
}
