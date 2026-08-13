package ui;

import bank.PrivateBank;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FxApplication extends Application {

    @FXML
    Button auswaehlenKnopf;

    public static void main(String[] args) {
        launch(args); // Startet die JavaFX-Anwendung
    }

    public void start(Stage stage) throws Exception {

        // Hauptfenster speichern
        Controller.stage = stage;

        // Bank erstellen
        // Dabei werden jetzt automatisch
        // vorhandene JSON Accounts geladen
        Controller.bank = new PrivateBank(
                "bank",
                0.1,
                0.1,
                "json/");

        // Controller für Startseite erstellen
        StartseiteController controller = new StartseiteController();

        // Startseite laden
        FXMLLoader loader = new FXMLLoader(
                FxApplication.class.getResource(
                        "/Startseite.fxml"));

        loader.setController(controller);

        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.setTitle("Startseite");
        stage.show();
    }
}
