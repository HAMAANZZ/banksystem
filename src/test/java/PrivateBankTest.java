import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrivateBankTest {

    private PrivateBank bank;
    private final String directory = "json_test/";

    /**
     * Setup-Methode, die vor jedem Test ausgeführt wird.
     * Initialisiert das Bankobjekt und stellt sicher, dass das Testverzeichnis existiert.
     *
     * @throws Exception Wenn beim Setup ein Fehler auftritt.
     */
    @BeforeEach
    void init() throws Exception {
        // Erstelle das Bankobjekt vor jedem Test
        bank = new PrivateBank("TestBank", 0.05, 0.01, directory);
        //TODO: trasaktionen

        // Stelle sicher, dass das Testverzeichnis existiert, und erstelle es, falls nicht
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * Bereinigungsmethode, die nach jedem Test ausgeführt wird.
     * Löscht alle Dateien im Testverzeichnis.
     */
    @AfterEach
    void cleanup() {
        // Bereinige das Verzeichnis nach jedem Test
        File dir = new File(directory);
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                file.delete();
            }
        }
    }

    /**
     * Testfall zum Erstellen eines Kontos.
     * Stellt sicher, dass beim Erstellen eines neuen Kontos keine Ausnahme geworfen wird.
     */
    @Test
    public void testCreateAccount_Success() {
        // Teste, dass beim Erstellen eines neuen Kontos keine Ausnahme geworfen wird
        assertDoesNotThrow(() -> {
            bank.createAccount("testAccount");
        });
    }

    /**
     * Testfall zum Erstellen eines bereits existierenden Kontos.
     * Stellt sicher, dass eine AccountAlreadyExistsException geworfen wird.
     */
    @Test
    void testCreateAccountThrowsExceptionForExistingAccount() {
        assertThrows(AccountAlreadyExistsException.class, () -> {
            bank.createAccount("testAccount"); // Erstes Erstellen
            bank.createAccount("testAccount"); // Versuch, dasselbe Konto erneut zu erstellen
        });
    }

    /**
     * Testfall zum Hinzufügen von Duplikat-Transaktionen.
     * Stellt sicher, dass das Hinzufügen derselben Transaktion zweimal eine TransactionAlreadyExistException wirft.
     *
     * @throws Exception Wenn beim Setup ein Fehler auftritt.
     */
    @Test
    void testAddDuplicateTransactionThrowsException() throws Exception {
        // Konto erstellen und eine Transaktion hinzufügen
        bank.createAccount("testAccount");
        Transaction transaction = new Payment("Test Payment", 100.0, "2024-11-30");
        bank.addTransaction("testAccount", transaction);

        // Versuch, dieselbe Transaktion erneut hinzuzufügen
        assertThrows(TransactionAlreadyExistException.class, () -> {
            bank.addTransaction("testAccount", transaction);
        });
    }

    /**
     * Parameterisierter Testfall zum Festlegen des Incoming Interest (Eingehender Zins).
     * Verifiziert, dass verschiedene Zinssätze korrekt angewendet werden.
     *
     * @param interestRate Der Zinssatz, der getestet wird.
     * @throws InvalidIncomingInterestException Wenn der eingehende Zinssatz ungültig ist.
     */
    @ParameterizedTest
    @ValueSource(doubles = {0.01, 0.02, 0.05})
    void testSetIncomingInterest(double interestRate) throws InvalidIncomingInterestException {
        bank.setIncomingInterest(interestRate);
        assertEquals(interestRate, bank.getIncomingInterest(), "Der eingehende Zinssatz sollte korrekt gesetzt werden.");
    }

    /**
     * Testfall für den Copy Constructor der PrivateBank-Klasse.
     * Verifiziert, dass eine neue Bank, die von einer bestehenden Bank erstellt wird,
     * eine andere Instanz ist, aber die gleichen Werte enthält.
     *
     * @throws InvalidIncomingInterestException Wenn der eingehende Zinssatz ungültig ist.
     * @throws InvalidOutgoingInterestException Wenn der ausgehende Zinssatz ungültig ist.
     */
    @Test
    public void testCopyConstructor() throws InvalidIncomingInterestException, InvalidOutgoingInterestException {
        PrivateBank bank2 = new PrivateBank(bank);
        assertEquals(bank, bank2);           // Die beiden Banken sollten gleich sein
        assertNotSame(bank, bank2);          // Sie sollten nicht die gleiche Instanz sein
    }

    /**
     * Testfall für die toString-Methode der PrivateBank-Klasse.
     * Verifiziert, dass die toString-Methode die korrekte String-Darstellung der Bank zurückgibt.
     */
    @Test
    public void testToString() {
        String expected = "PrivateBank{ name= TestBank, incomingInterest= 0.05, outgoingInterest= 0.01, accountsToTransactions= {}}";
        System.out.println(bank);
        assertEquals(expected, bank.toString()); // Sicherstellen, dass die toString-Methode den richtigen String zurückgibt
    }

    /**
     * Testfall für die equals-Methode der PrivateBank-Klasse.
     * Verifiziert, dass zwei Banken mit denselben Werten als gleich betrachtet werden.
     *
     * @throws InvalidIncomingInterestException Wenn der eingehende Zinssatz ungültig ist.
     * @throws InvalidOutgoingInterestException Wenn der ausgehende Zinssatz ungültig ist.
     */
    @Test
    public void testEquals() throws InvalidIncomingInterestException, InvalidOutgoingInterestException, IOException {
        PrivateBank bank2 = new PrivateBank("TestBank", 0.05, 0.01, directory);
        assertTrue(bank.equals(bank2)); // Sie sollten gleich sein
        assertFalse(bank.equals(null)); // Sie sollten nicht null gleich sein
        assertFalse(bank.equals(new Object())); // Sie sollten nicht gleich einem Objekt einer anderen Klasse sein
    }

    /**
     * Testfall zum Erstellen eines Kontos und sicherstellen, dass es existiert und keine Transaktionen enthält.
     *
     * @throws Exception Wenn beim Setup ein Fehler auftritt.
     */
    @Test
    void testCreateAccount() throws Exception {
        // Konto erstellen
        bank.createAccount("testAccount");

        // Überprüfen, dass das Konto existiert und keine Transaktionen enthält
        assertTrue(bank.getTransactions("testAccount").isEmpty(), "Das Konto sollte existieren und keine Transaktionen enthalten.");
    }

    /**
     * Testfall zum Hinzufügen einer Transaktion zu einem Konto.
     * Stellt sicher, dass die Transaktion erfolgreich hinzugefügt wird und im Konto existiert.
     *
     * @throws Exception Wenn beim Setup ein Fehler auftritt.
     */
    @Test
    void testAddTransaction() throws Exception {
        // Konto erstellen
        bank.createAccount("testAccount");

        // Neue Transaktion erstellen
        Transaction transaction = new Payment("Test Payment", 100.0, "2024-11-30");

        // Transaktion zum Konto hinzufügen
        bank.addTransaction("testAccount", transaction);

        // Überprüfen, dass die Transaktion hinzugefügt wurde
        assertTrue(bank.containsTransaction("testAccount", transaction), "Die Transaktion sollte hinzugefügt worden sein.");
    }

    /**
     * Testfall zum Überprüfen des Kontostands nach Transaktionen.
     * Stellt sicher, dass der Kontostand korrekt berechnet wird.
     *
     * @throws Exception Wenn beim Setup ein Fehler auftritt.
     */
    @Test
    void testGetAccountBalance() throws Exception {
        // Konto und Transaktionen erstellen
        bank.createAccount("testAccount");
        bank.addTransaction("testAccount", new Payment("Deposit", 200.0, "2024-11-30"));
        bank.addTransaction("testAccount", new Payment("Withdraw", -50.0, "2024-11-30"));

        // Kontostand überprüfen
        double balance = bank.getAccountBalance("testAccount");
        assertEquals(139.5, balance, "Der Kontostand sollte korrekt berechnet werden.");
    }

    /**
     * Testfall zum Schreiben von Kontodaten in eine Datei.
     * Stellt sicher, dass die Methode ohne Ausnahmen abgeschlossen wird.
     *
     * @throws Exception Wenn beim Setup ein Fehler auftritt.
     */
    @Test
    public void testWriteAccount() throws IOException, AccountDoesNotExistException, AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, InvalidIncomingInterestException, InvalidOutgoingInterestException, NegativeAmountException {
        bank.createAccount("account1");
        Transaction payment = new Payment("2024-11-30", 100.0, "Test Payment", 0.05, 0.02);
        bank.addTransaction("account1", payment);

        // Annahme: Eine Datei wird erwartet, wir können überprüfen, ob die Methode ohne Fehler abgeschlossen wird
        bank.writeAccount("account1"); // Wenn keine Ausnahme geworfen wird, ist der Test bestanden
    }

    /**
     * Testfall zum Entfernen einer Transaktion aus einem Konto.
     * Verifiziert, dass die Transaktion erfolgreich entfernt wird.
     *
     * @throws Exception Wenn beim Setup ein Fehler auftritt.
     */
    @Test
    public void testRemoveTransaction() throws Exception {
        bank.createAccount("account1");
        Transaction payment = new Payment("2024-11-30", 100.0, "Test Payment", 0.05, 0.02);
        bank.addTransaction("account1", payment);

        // Transaktion entfernen
        bank.removeTransaction("account1", payment); // Transaktion entfernen
        assertFalse(bank.getTransactions("account1").contains(payment)); // Die Transaktion sollte nicht mehr im Konto vorhanden sein
    }

    /**
     * Testfall zum Überprüfen, ob eine Transaktion in einem Konto existiert.
     * Verifiziert, dass eine existierende Transaktion gefunden wird und eine nicht existierende Transaktion nicht gefunden wird.
     *
     * @throws Exception Wenn beim Setup ein Fehler auftritt.
     */
    @Test
    public void testContainsTransaction() throws Exception {
        bank.createAccount("account1");
        Transaction payment = new Payment("2024-11-30", 100.0, "Test Payment", 0.05, 0.02);
        bank.addTransaction("account1", payment);

        assertTrue(bank.containsTransaction("account1", payment)); // Die Transaktion sollte im Konto vorhanden sein
        assertFalse(bank.containsTransaction("account1", new Payment("2024-11-29", 50.0, "Other Payment", 0.05, 0.02))); // Eine nicht existierende Transaktion sollte nicht gefunden werden
    }

    /**
     * Testfall zum Abrufen von Transaktionen eines bestimmten Typs.
     * Verifiziert, dass nur positive Transaktionen korrekt zurückgegeben werden.
     *
     * @throws Exception Wenn beim Setup ein Fehler auftritt.
     */
    @Test
    public void testGetTransactionsByType() throws Exception {
        bank.createAccount("account1");
        Transaction payment = new Payment("2024-11-30", 100.0, "Test Payment", 0.05, 0.02);
        bank.addTransaction("account1", payment);

        List<Transaction> positiveTransactions = bank.getTransactionsByType("account1", true);
        assertEquals(1, positiveTransactions.size()); // Nur die positive Transaktion sollte zurückgegeben werden
    }

    /**
     * Testfall zum Abrufen von Transaktionen, die nach Betrag sortiert sind.
     * Verifiziert, dass Transaktionen korrekt in aufsteigender oder absteigender Reihenfolge sortiert werden.
     *
     * @throws Exception Wenn beim Setup ein Fehler auftritt.
     */
    @Test
    public void testGetTransactionsSorted() throws Exception {
        bank.createAccount("account1");
        Transaction payment1 = new Payment("2024-11-30", 100.0, "Test Payment 1", 0.05, 0.02);
        Transaction payment2 = new Payment("2024-11-30", 50.0, "Test Payment 2", 0.05, 0.02);
        bank.addTransaction("account1", payment1);
        bank.addTransaction("account1", payment2);

        List<Transaction> sortedTransactionsAsc = bank.getTransactionsSorted("account1", true);
        assertEquals(payment2, sortedTransactionsAsc.get(0)); // Die kleinere Transaktion sollte zuerst kommen

        List<Transaction> sortedTransactionsDesc = bank.getTransactionsSorted("account1", false);
        assertEquals(payment1, sortedTransactionsDesc.get(0)); // Die größere Transaktion sollte zuerst kommen
    }
}
