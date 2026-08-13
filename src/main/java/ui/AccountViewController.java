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
    public void PaymentErstellen() {
        Payment p = null;
        // Erstelle Eingabefelder
        JTextField dateField = new JTextField(10);
        JTextField amountField = new JTextField(10);
        JTextField descriptionField = new JTextField(10);

        // Panel für Eingaben erstellen
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Date (yyyy-mm-dd):"));
        panel.add(dateField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        // Zeige das Dialogfenster
        int result = JOptionPane.showConfirmDialog(null, panel,
                "Input Dialog", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Überprüfe, ob OK geklickt wurde
        if (result == JOptionPane.OK_OPTION) {
            String date = dateField.getText();
            double amount = 0;
            String description = descriptionField.getText() + "";

            // es muss einen int sein.
            try {
                amount = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid amount entered. Please enter a valid number.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                p = new Payment(date, amount, description);
            } catch (NegativeAmountException e) {
                JOptionPane.showMessageDialog(null, "NegativeAmountException: " + e.getMessage(),
                        "NegativeAmountException", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                bank.addTransaction(accountName, p);
            } catch (TransactionAlreadyExistException e) {
                JOptionPane.showMessageDialog(null, "TransactionAlreadyExistException: " + e.getMessage(),
                        "TransactionAlreadyExistException", JOptionPane.ERROR_MESSAGE);
            } catch (AccountDoesNotExistException e) {
                JOptionPane.showMessageDialog(null, "AccountDoesNotExistException: " + e.getMessage(),
                        "AccountDoesNotExistException", JOptionPane.ERROR_MESSAGE);

            } catch (TransactionAttributeException e) {
                JOptionPane.showMessageDialog(null, "TransactionAttributeException: " + e.getMessage(),
                        "TransactionAttributeException", JOptionPane.ERROR_MESSAGE);

            } catch (InvalidIncomingInterestException e) {
                JOptionPane.showMessageDialog(null, "InvalidIncomingInterestException: " + e.getMessage(),
                        "InvalidIncomingInterestException", JOptionPane.ERROR_MESSAGE);

            } catch (InvalidOutgoingInterestException e) {
                JOptionPane.showMessageDialog(null, "InvalidOutgoingInterestException: " + e.getMessage(),
                        "InvalidOutgoingInterestException", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "IOException: " + e.getMessage(), "IOException",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Input canceled.",
                    "Dialog Canceled", JOptionPane.INFORMATION_MESSAGE);
        }
        menuAktualisieren();
    }

    /**
     * TransferErstellen
     */
    public void TransferErstellen() {
        // Erstelle Eingabefelder
        JTextField dateField = new JTextField(10);
        JTextField amountField = new JTextField(10);
        JTextField descriptionField = new JTextField(10);
        JTextField senderField = new JTextField(10);
        JTextField recipientField = new JTextField(10);

        // Panel für Eingaben erstellen
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Date (yyyy-mm-dd):"));
        panel.add(dateField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Sender:"));
        panel.add(senderField);
        panel.add(new JLabel("Recipient:"));
        panel.add(recipientField);

        // Zeige das Dialogfenster
        int result = JOptionPane.showConfirmDialog(null, panel,
                "Input Dialog", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Überprüfe, ob OK geklickt wurde
        if (result == JOptionPane.OK_OPTION) {
            String date = dateField.getText();
            String description = descriptionField.getText() + "";
            String sender = senderField.getText();
            String recipient = recipientField.getText();

            double amount = 0;
            try {
                amount = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "NumberFormatException: " + e.getMessage(),
                        "TransactionAlreadyExistException", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (sender.equals(accountName)) {
                // Benutz = sender
                try {
                    OutgoingTransfer out = new OutgoingTransfer(date, amount, description, sender, recipient);
                    this.bank.addTransaction(accountName, out);
                } catch (NegativeAmountException e) {
                    JOptionPane.showMessageDialog(null, "TransactionAlreadyExistException: " + e.getMessage(),
                            "TransactionAlreadyExistException", JOptionPane.ERROR_MESSAGE);
                } catch (TransactionAlreadyExistException e) {
                    JOptionPane.showMessageDialog(null, "TransactionAlreadyExistException: " + e.getMessage(),
                            "TransactionAlreadyExistException", JOptionPane.ERROR_MESSAGE);
                } catch (AccountDoesNotExistException e) {
                    JOptionPane.showMessageDialog(null, "AccountDoesNotExistException: " + e.getMessage(),
                            "AccountDoesNotExistException", JOptionPane.ERROR_MESSAGE);

                } catch (TransactionAttributeException e) {
                    JOptionPane.showMessageDialog(null, "TransactionAttributeException: " + e.getMessage(),
                            "TransactionAttributeException", JOptionPane.ERROR_MESSAGE);

                } catch (InvalidIncomingInterestException e) {
                    JOptionPane.showMessageDialog(null, "InvalidIncomingInterestException: " + e.getMessage(),
                            "InvalidIncomingInterestException", JOptionPane.ERROR_MESSAGE);

                } catch (InvalidOutgoingInterestException e) {
                    JOptionPane.showMessageDialog(null, "InvalidOutgoingInterestException: " + e.getMessage(),
                            "InvalidOutgoingInterestException", JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "IOException: " + e.getMessage(), "IOException",
                            JOptionPane.ERROR_MESSAGE);
                }

            } else if (recipient.equals(accountName)) {
                // Benutz = empfänger
                try {
                    IncomingTransfer in = new IncomingTransfer(date, amount, description, sender, recipient);
                    this.bank.addTransaction(accountName, in);
                } catch (NegativeAmountException e) {
                    JOptionPane.showMessageDialog(null, "TransactionAlreadyExistException: " + e.getMessage(),
                            "TransactionAlreadyExistException", JOptionPane.ERROR_MESSAGE);
                } catch (TransactionAlreadyExistException e) {
                    JOptionPane.showMessageDialog(null, "TransactionAlreadyExistException: " + e.getMessage(),
                            "TransactionAlreadyExistException", JOptionPane.ERROR_MESSAGE);
                } catch (AccountDoesNotExistException e) {
                    JOptionPane.showMessageDialog(null, "AccountDoesNotExistException: " + e.getMessage(),
                            "AccountDoesNotExistException", JOptionPane.ERROR_MESSAGE);

                } catch (TransactionAttributeException e) {
                    JOptionPane.showMessageDialog(null, "TransactionAttributeException: " + e.getMessage(),
                            "TransactionAttributeException", JOptionPane.ERROR_MESSAGE);

                } catch (InvalidIncomingInterestException e) {
                    JOptionPane.showMessageDialog(null, "InvalidIncomingInterestException: " + e.getMessage(),
                            "InvalidIncomingInterestException", JOptionPane.ERROR_MESSAGE);

                } catch (InvalidOutgoingInterestException e) {
                    JOptionPane.showMessageDialog(null, "InvalidOutgoingInterestException: " + e.getMessage(),
                            "InvalidOutgoingInterestException", JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "IOException: " + e.getMessage(), "IOException",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Fehler beim sender und recipient",
                        "sender und recipient", JOptionPane.ERROR_MESSAGE);
                return;
            }

        } else {
            JOptionPane.showMessageDialog(null,
                    "Input canceled.",
                    "Dialog Canceled", JOptionPane.INFORMATION_MESSAGE);
        }
        menuAktualisieren();
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
