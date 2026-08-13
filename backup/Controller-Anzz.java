package ui;

import javafx.scene.control.Alert;

public abstract class Controller {
    // Eine Methode, um einfache Alerts zu zeigen
    protected void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
