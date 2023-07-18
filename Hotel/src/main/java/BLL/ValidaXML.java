package BLL;

import Model.MessageBoxes;
import javafx.scene.control.Alert;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

public class ValidaXML {
    private static final String userDirectory = System.getProperty("user.dir");
    private static final String filePath = userDirectory + "/src/main/resources/lib/HotelAgencyPT.xsd";

    /**
     * Método que valida um ficheiro XML com base num esquema XSD especificado.
     *
     * @param xmlFile Ficheiro XML a ser validado.
     * @return Retorna 'true' se o ficheiro XML for válido, 'false' caso contrário. Em caso de erro, é exibida uma mensagem de erro.
     */
    public static Boolean validarXML(File xmlFile) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            File schemaLocation = new File(filePath);
            Schema schema = factory.newSchema(schemaLocation);

            Validator validator = schema.newValidator();

            StreamSource source = new StreamSource(xmlFile);
            validator.validate(source);
            return true;
        } catch (Exception e) {
            MessageBoxes.ShowMessage(Alert.AlertType.ERROR, "Ficheiro XML inválido!", "Erro:");
            return false;
        }
    }
}


