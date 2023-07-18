package Model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class MessageBoxes {
    public static void ShowMessage(Alert.AlertType type, String msg, String header) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static Boolean ConfirmationBox(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        //alert.setTitle("Aplicação");
        alert.setHeaderText("Confirmação:");
        alert.setContentText(msg);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }
}
