package ui;

import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.Payment;
import bank.PrivateBank;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
public class FxApplication extends Application {

    @FXML
    Button auswaehlenKnopf;

    public static void main(String[] args) {
        launch(args); // Startet die JavaFX-Anwendung
    }


    @Override
    public void start(Stage stage) throws Exception {

        try {

            Controller.stage = stage;
            Controller.bank = new PrivateBank("bank", 0.1, 0.1, "json/");
            Controller.bank.createAccount("test");
            Controller.bank.addTransaction("test",new Payment("Date",100,"Beschreibung"));
            Controller.bank.addTransaction("test",new IncomingTransfer("Date",50,"Beschreibung"));
            Controller.bank.addTransaction("test",new OutgoingTransfer("Date",50,"Beschreibung"));

            StartseiteController controller = new StartseiteController();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Startseite.fxml"));
            loader.setController(controller);

            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Startseite");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Fehlerprotokoll, falls die Datei nicht geladen werden kann
        }
    }
}
