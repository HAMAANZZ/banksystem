package ui;

import bank.PrivateBank;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;



public class Controller {

    public static Stage stage; // Die aktuelle Stage speichern
    public static PrivateBank bank;


    /**
     * Methode zum Wechseln zwischen Scenes
     *
     * @param fxmlFile Der Pfad zur FXML-Datei
     */
    public FXMLLoader loadScene(String fxmlFile, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setController(controller);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
            return loader;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
