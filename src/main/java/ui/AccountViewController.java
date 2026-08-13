package ui;

import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.Payment;
import bank.Transaction;
import bank.exceptions.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 *
 */
public class AccountViewController extends Controller {

    @FXML
    private Button BackKnopf, PaymentErstellen, TransferErstellen;

    @FXML
    private ListView<String> TrasaktionenListeView;

    @FXML
    private Label AccountDetails;

    @FXML
    private MenuItem menuSortAscending, menuSortDescending, menuShowPositive, menuShowNegative, menuLoeschen;

    protected String accountName;

    /**
     * @param stage
     */
    public AccountViewController(Stage stage) {

    }

    // Initialisierungsmethode
    @FXML
    public void initialize() throws AccountDoesNotExistException {
        menuAktualisieren();

        menuSortAscending.setOnAction(event -> menuSortAscending());
        menuSortDescending.setOnAction(event -> menuSortDescending());
        menuShowPositive.setOnAction(event -> menuShowPositive());
        menuShowNegative.setOnAction(event -> menuShowNegative());
        menuLoeschen.setOnAction(event -> menuLoeschen());
        PaymentErstellen.setOnAction(event -> PaymentErstellen());
        TransferErstellen.setOnAction(event -> TransferErstellen());
        BackKnopf.setOnAction(event -> backKnopf());
        AccountDetails.setText("Name: " + accountName + "\n" + "Balance: " + bank.getAccountBalance(accountName));

    }

    /**
     * Zurück
     */
    private void backKnopf() {
        // System.out.println("Back Knopf");

        this.loadScene("/Startseite.fxml", new StartseiteController());

    }

    /**
     * PaymentErstellen
     */
    // JFrame benutzt
    /**
     * Erstellt ein neues Payment.
     *
     * Positive Beträge = Einnahmen
     * Negative Beträge = Ausgaben
     */
    public void PaymentErstellen() {

        // --------------------------------------------------------
        // 1. Eingabefelder erstellen
        // --------------------------------------------------------

        JTextField dateField = new JTextField(10);
        JTextField amountField = new JTextField(10);
        JTextField descriptionField = new JTextField(10);

        // --------------------------------------------------------
        // 2. Eingabefenster erstellen
        // --------------------------------------------------------

        JPanel panel = new JPanel();

        panel.setLayout(
                new GridLayout(
                        3,
                        2,
                        5,
                        5));

        panel.add(new JLabel("Date (yyyy-mm-dd):"));
        panel.add(dateField);

        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        // --------------------------------------------------------
        // 3. Dialog anzeigen
        // --------------------------------------------------------

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Payment erstellen",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        // Benutzer hat abgebrochen
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        // --------------------------------------------------------
        // 4. Eingaben auslesen
        // --------------------------------------------------------

        String date = dateField.getText().trim();

        String description = descriptionField.getText().trim();

        // --------------------------------------------------------
        // 5. Prüfen, ob Datum leer ist
        // --------------------------------------------------------

        if (date.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Das Datum darf nicht leer sein.",
                    "Ungültiges Datum",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // --------------------------------------------------------
        // 6. Prüfen, ob Beschreibung leer ist
        // --------------------------------------------------------

        if (description.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Die Beschreibung darf nicht leer sein.",
                    "Ungültige Beschreibung",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // --------------------------------------------------------
        // 7. Betrag einlesen
        // --------------------------------------------------------

        double amount;

        try {

            amount = Double.parseDouble(
                    amountField
                            .getText()
                            .trim());

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Bitte einen gültigen Betrag eingeben.",
                    "Ungültiger Betrag",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // --------------------------------------------------------
        // 8. NaN und Infinity verhindern
        // --------------------------------------------------------

        if (!Double.isFinite(amount)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Der Betrag muss eine gültige Zahl sein.",
                    "Ungültiger Betrag",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // --------------------------------------------------------
        // 9. 0 Euro verhindern
        // --------------------------------------------------------

        if (amount == 0) {

            JOptionPane.showMessageDialog(
                    null,
                    "Der Betrag darf nicht 0 Euro sein.",
                    "Ungültiger Betrag",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // --------------------------------------------------------
        // 10. Payment erstellen
        // --------------------------------------------------------

        Payment payment;

        try {

            payment = new Payment(
                    date,
                    amount,
                    description);

        } catch (NegativeAmountException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Fehler beim Payment",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        // --------------------------------------------------------
        // 11. Payment zum Account hinzufügen
        // --------------------------------------------------------

        try {

            bank.addTransaction(
                    accountName,
                    payment);

            // ----------------------------------------------------
            // 12. Erfolg anzeigen
            // ----------------------------------------------------

            JOptionPane.showMessageDialog(
                    null,
                    "Payment wurde erfolgreich erstellt.",
                    "Erfolgreich",
                    JOptionPane.INFORMATION_MESSAGE);

            // Anzeige aktualisieren
            menuAktualisieren();

        } catch (TransactionAlreadyExistException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Transaktion existiert bereits",
                    JOptionPane.WARNING_MESSAGE);

        } catch (AccountDoesNotExistException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Account existiert nicht",
                    JOptionPane.ERROR_MESSAGE);

        } catch (TransactionAttributeException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Ungültige Transaktion",
                    JOptionPane.WARNING_MESSAGE);

        } catch (InvalidIncomingInterestException
                | InvalidOutgoingInterestException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Ungültiger Zinssatz",
                    JOptionPane.ERROR_MESSAGE);

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Fehler beim Speichern",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * TransferErstellen
     */
    /**
     * Erstellt eine echte Überweisung
     * vom aktuell geöffneten Account
     * zu einem anderen Account.
     */
    /**
     * Erstellt eine Überweisung vom aktuell geöffneten Account
     * zu einem anderen Account.
     */
    public void TransferErstellen() {

        // --------------------------------------------------------
        // 1. Eingabefelder erstellen
        // --------------------------------------------------------

        JTextField dateField = new JTextField(10);
        JTextField amountField = new JTextField(10);
        JTextField descriptionField = new JTextField(10);

        // Sender ist automatisch der aktuell geöffnete Account
        JTextField senderField = new JTextField(accountName);
        senderField.setEditable(false);

        JTextField recipientField = new JTextField(10);

        // --------------------------------------------------------
        // 2. Eingabefenster erstellen
        // --------------------------------------------------------

        JPanel panel = new JPanel();

        panel.setLayout(
                new GridLayout(
                        5,
                        2,
                        5,
                        5));

        panel.add(new JLabel("Date (yyyy-mm-dd):"));
        panel.add(dateField);

        panel.add(new JLabel("Betrag:"));
        panel.add(amountField);

        panel.add(new JLabel("Beschreibung:"));
        panel.add(descriptionField);

        panel.add(new JLabel("Sender:"));
        panel.add(senderField);

        panel.add(new JLabel("Empfänger:"));
        panel.add(recipientField);

        // --------------------------------------------------------
        // 3. Dialog anzeigen
        // --------------------------------------------------------

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Überweisung erstellen",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        // Benutzer hat abgebrochen
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        // --------------------------------------------------------
        // 4. Eingaben auslesen
        // --------------------------------------------------------

        String date = dateField
                .getText()
                .trim();

        String description = descriptionField
                .getText()
                .trim();

        String sender = accountName;

        String recipient = recipientField
                .getText()
                .trim();

        // --------------------------------------------------------
        // 5. Datum überprüfen
        // --------------------------------------------------------

        if (date.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Das Datum darf nicht leer sein.",
                    "Ungültiges Datum",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // --------------------------------------------------------
        // 6. Empfänger überprüfen
        // --------------------------------------------------------

        if (recipient.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Bitte einen Empfänger eingeben.",
                    "Ungültiger Empfänger",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // --------------------------------------------------------
        // 7. Sender und Empfänger vergleichen
        // --------------------------------------------------------

        if (sender.equals(recipient)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Sender und Empfänger dürfen nicht gleich sein.",
                    "Ungültige Überweisung",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // --------------------------------------------------------
        // 8. Betrag einlesen
        // --------------------------------------------------------

        double amount;

        try {

            amount = Double.parseDouble(
                    amountField
                            .getText()
                            .trim());

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Bitte einen gültigen Betrag eingeben.",
                    "Ungültiger Betrag",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // --------------------------------------------------------
        // 9. NaN und Infinity verhindern
        // --------------------------------------------------------

        if (!Double.isFinite(amount)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Der Betrag muss eine gültige Zahl sein.",
                    "Ungültiger Betrag",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // --------------------------------------------------------
        // 10. Betrag muss größer als 0 sein
        // --------------------------------------------------------

        if (amount <= 0) {

            JOptionPane.showMessageDialog(
                    null,
                    "Der Betrag muss größer als 0 Euro sein.",
                    "Ungültiger Betrag",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // --------------------------------------------------------
        // 11. Überweisung durchführen
        // --------------------------------------------------------

        try {

            bank.transfer(
                    sender,
                    recipient,
                    date,
                    amount,
                    description);

            // ----------------------------------------------------
            // 12. Erfolg anzeigen
            // ----------------------------------------------------

            JOptionPane.showMessageDialog(
                    null,
                    "Überweisung erfolgreich!\n\n"
                            + amount
                            + " Euro von "
                            + sender
                            + " an "
                            + recipient,
                    "Erfolgreich",
                    JOptionPane.INFORMATION_MESSAGE);

            // ----------------------------------------------------
            // 13. Anzeige aktualisieren
            // ----------------------------------------------------

            menuAktualisieren();

        } catch (AccountDoesNotExistException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Account existiert nicht",
                    JOptionPane.WARNING_MESSAGE);

        } catch (TransactionAlreadyExistException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Transaktion existiert bereits",
                    JOptionPane.WARNING_MESSAGE);

        } catch (TransactionAttributeException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Ungültige Überweisung",
                    JOptionPane.WARNING_MESSAGE);

        } catch (NegativeAmountException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Ungültiger Betrag",
                    JOptionPane.WARNING_MESSAGE);

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Fehler beim Speichern",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * löschen
     */
    public void menuLoeschen() {

        String selectedItem = TrasaktionenListeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {

            // Bestätigungsdialog mit JOptionPane
            int result = JOptionPane.showConfirmDialog(
                    null,
                    "Möchten Sie den Trasaktionen '" + selectedItem + "' wirklich löschen?",
                    "Account löschen",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                try {

                    for (Transaction transaction : bank.getTransactions(accountName)) {
                        if (selectedItem.equals(transaction.toString())) {
                            bank.removeTransaction(accountName, transaction);
                        }
                    }

                    JOptionPane.showMessageDialog(
                            null,
                            "Der Account '" + selectedItem + "' wurde erfolgreich gelöscht.",
                            "Erfolg",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Fehler beim Löschen des Trasaktionen: " + e.getMessage(),
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Löschvorgang wurde abgebrochen.",
                        "Löschvorgang",
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
        menuAktualisieren();
    }

    /**
     * menuAktualisieren
     */

    public void menuAktualisieren() {
        TrasaktionenListeView.getItems().clear();
        for (Transaction transaction : bank.getTransactions(accountName)) {
            TrasaktionenListeView.getItems().addAll(transaction.toString());
        }

        try {
            AccountDetails.setText("Name: " + accountName + "\n" + "Balance: " + bank.getAccountBalance(accountName));
        } catch (AccountDoesNotExistException e) {
            JOptionPane.showMessageDialog(null, "AccountDoesNotExistException: " + e.getMessage(), "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * menuSortAscending
     */
    public void menuSortAscending() {
        TrasaktionenListeView.getItems().clear();

        for (Transaction transaction : bank.getTransactionsSorted(accountName, true)) {
            TrasaktionenListeView.getItems().addAll(transaction.toString());
        }
    }

    /**
     * menuSortDescending
     */
    public void menuSortDescending() {
        TrasaktionenListeView.getItems().clear();

        for (Transaction transaction : bank.getTransactionsSorted(accountName, false)) {
            TrasaktionenListeView.getItems().addAll(transaction.toString());
        }
    }

    /**
     * menuShowPositive
     */
    public void menuShowPositive() {
        TrasaktionenListeView.getItems().clear();
        for (Transaction transaction : bank.getTransactionsByType(accountName, true)) {
            TrasaktionenListeView.getItems().addAll(transaction.toString());
        }
    }

    /**
     * menuShowNegative
     */
    public void menuShowNegative() {
        TrasaktionenListeView.getItems().clear();
        for (Transaction transaction : bank.getTransactionsByType(accountName, false)) {
            TrasaktionenListeView.getItems().addAll(transaction.toString());
        }
    }

}
