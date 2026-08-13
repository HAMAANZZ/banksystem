package ui;

import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

import javax.swing.*;
import java.io.IOException;

/**
 * StartseiteController
 */
public class StartseiteController extends Controller {

    @FXML
    private ListView<String> listViewAcconts;

    @FXML
    private Button AccountErstellen;

    @FXML
    private MenuItem menuAuswaehlen;

    @FXML
    private MenuItem menuLoeschen;

    /**
     * Konstrucktor
     */
    public StartseiteController() {

    }

    /**
     *
     */
    // Initialisierungsmethode
    @FXML
    public void initialize() {
        listViewAcconts.getItems().addAll(bank.getAllAccounts());
        // Beispiel: Aktionen hinzufügen
        AccountErstellen.setOnAction(event -> erstelleNeuenAccount());
        menuAuswaehlen.setOnAction(event -> accountAuswaehlen());
        menuLoeschen.setOnAction(event -> accountLoeschen());
    }

    /**
     * erstelleNeuenAccount
     */

    private void erstelleNeuenAccount() {

        String accountName = JOptionPane.showInputDialog(
                null,
                "Bitte geben Sie den neuen Accountnamen ein:",
                "Neuen Account hinzufügen",
                JOptionPane.QUESTION_MESSAGE);

        // Benutzer hat auf Abbrechen geklickt
        if (accountName == null) {
            System.out.println("Benutzer hat den Vorgang abgebrochen.");
            return;
        }

        try {

            // Leerzeichen vorne und hinten entfernen
            String cleanName = accountName.trim();

            // Account erstellen
            bank.createAccount(cleanName);

            // Account direkt in der ListView anzeigen
            listViewAcconts.getItems().add(accountName.trim()); // " Muster " ===> "Muster"

            JOptionPane.showMessageDialog(
                    null,
                    "Account '" + cleanName + "' wurde erfolgreich erstellt.",
                    "Erfolg",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Ungültiger Accountname",
                    JOptionPane.WARNING_MESSAGE);

        } catch (AccountAlreadyExistsException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Account existiert bereits",
                    JOptionPane.WARNING_MESSAGE);

        } catch (AccountDoesNotExistException | IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * accountAuswaehlen
     */
    private void accountAuswaehlen() {

        // Ausgewählten Account holen
        String selectedAccount = listViewAcconts
                .getSelectionModel()
                .getSelectedItem();

        // Prüfen, ob überhaupt ein Account ausgewählt wurde
        if (selectedAccount == null) {

            JOptionPane.showMessageDialog(
                    null,
                    "Bitte zuerst einen Account auswählen.",
                    "Kein Account ausgewählt",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        System.out.println("Account ausgewählt: " + selectedAccount);

        // Controller für Account Ansicht erstellen
        AccountViewController controller = new AccountViewController(stage);

        // Accountname übergeben
        controller.accountName = selectedAccount;

        // Account Ansicht öffnen
        this.loadScene("/AccountView.fxml", controller);
    }

    /**
     * accountLoeschen
     */
    private void accountLoeschen() {
        String selectedItem = listViewAcconts.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            // Bestätigungsdialog mit JOptionPane
            int result = JOptionPane.showConfirmDialog(
                    null,
                    "Möchten Sie den Account '" + selectedItem + "' wirklich löschen?",
                    "Account löschen",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                try {
                    // Entfernen des Accounts aus der PrivateBank
                    bank.deleteAccount(selectedItem);
                    // Entfernen des Accounts aus der ListView
                    listViewAcconts.getItems().remove(selectedItem);
                    JOptionPane.showMessageDialog(
                            null,
                            "Der Account '" + selectedItem + "' wurde erfolgreich gelöscht.",
                            "Erfolg",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (AccountDoesNotExistException e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Fehler: " + e.getMessage(),
                            "Account existiert nicht",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Fehler beim Löschen des Accounts Datei: " + e.getMessage(),
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Löschvorgang wurde abgebrochen.",
                        "Abgebrochen",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Warnung, wenn kein Account ausgewählt wurde
            JOptionPane.showMessageDialog(
                    null,
                    "Bitte wählen Sie einen Account aus, den Sie löschen möchten.",
                    "Kein Account ausgewählt",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

}