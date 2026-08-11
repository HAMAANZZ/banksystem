package ui;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.*;

public class FxApplication extends Application {

    @FXML
    Button auswaehlenKnopf;

    public static void main(String[] args) {
//        // Zeigt ein Eingabefenster mit der Möglichkeit, Text einzugeben
//        String eingabe = JOptionPane.showInputDialog(null, "Bitte geben Sie etwas ein:", "Eingabefenster", JOptionPane.QUESTION_MESSAGE);
//
//        // Zeigt ein Fenster mit der eingegebenen Information
//        if (eingabe != null) { // Nutzer hat nicht auf "Abbrechen" geklickt
//            JOptionPane.showMessageDialog(null, "Sie haben eingegeben: " + eingabe, "Information", JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            JOptionPane.showMessageDialog(null, "Keine Eingabe vorgenommen!", "Abbrechen", JOptionPane.WARNING_MESSAGE);
//        }

        launch(args); // Startet die JavaFX-Anwendung
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Lade die FXML-Datei
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Startseite.fxml"));
            Scene s1 = fxmlLoader.load(); // Das Root-Element aus der FXML-Datei

            primaryStage.setScene(s1);

            // Titel des Fensters setzen
            primaryStage.setTitle("OOS P5");

            // Hauptfenster anzeigen
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Fehlerprotokoll, falls die Datei nicht geladen werden kann
        }
    }
}
