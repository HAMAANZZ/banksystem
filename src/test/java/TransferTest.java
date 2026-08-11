import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    Transfer transfer;

    @BeforeEach
    void setUp() throws NegativeAmountException {
        // Hier könntest du eine Instanz von Transfer oder anderen relevanten Objekten initialisieren
        // Beispiel: Vorbereitungen für Tests
        // transfer = new Transfer("2024-11-30", 100.0, "Test Transfer");
    }

    @AfterEach
    void tearDown() {
        // Hier kannst du Ressourcen freigeben oder Zustände zurücksetzen
        // transfer = null; // Nullsetzen der Instanz nach jedem Test
    }


    /**
     * Test für den Konstruktor der Transfer-Klasse ohne Sender und Empfänger.
     * Verifiziert, dass die Attribute des Transfers korrekt gesetzt werden.
     *
     * @throws NegativeAmountException Wenn der Betrag negativ ist.
     */
    @Test
    void testConstructor() throws NegativeAmountException {
        // Transfer ohne Sender und Empfänger
        Transfer transfer = new Transfer("2024-11-30", 100.0, "Test Transfer");
        assertEquals("2024-11-30", transfer.getDate());
        assertEquals(100.0, transfer.getAmount());
        assertEquals("Test Transfer", transfer.getDescription());
        assertEquals("", transfer.getSender());
        assertEquals("", transfer.getRecipient());
    }

    /**
     * Test für den Konstruktor der Transfer-Klasse mit Sender und Empfänger.
     * Verifiziert, dass die Attribute des Transfers korrekt gesetzt werden.
     *
     * @throws NegativeAmountException Wenn der Betrag negativ ist.
     */
    @Test
    void testConstructorWithSenderRecipient() throws NegativeAmountException {
        // Transfer mit Sender und Empfänger
        Transfer transfer = new Transfer("2024-11-30", 100.0, "Test Transfer", "Alice", "Bob");
        assertEquals("2024-11-30", transfer.getDate());
        assertEquals(100.0, transfer.getAmount());
        assertEquals("Test Transfer", transfer.getDescription());
        assertEquals("Alice", transfer.getSender());
        assertEquals("Bob", transfer.getRecipient());
    }

    /**
     * Test für den Copy-Konstruktor der Transfer-Klasse.
     * Verifiziert, dass eine Kopie des Transfer-Objekts korrekt erstellt wird.
     *
     * @throws NegativeAmountException Wenn der Betrag negativ ist.
     */
    @Test
    void testCopyConstructor() throws NegativeAmountException {
        // Originaler Transfer
        Transfer original = new Transfer("2024-11-30", 100.0, "Original Transfer", "Alice", "Bob");
        // Kopie des Transfers erstellen
        Transfer copy = new Transfer(original);
        assertEquals(original, copy); // Beide Objekte sollten gleich sein
        assertNotSame(original, copy); // Sie sollten unterschiedliche Instanzen sein
    }

    /**
     * Test für die Berechnungsmethode der Transfer-Klasse.
     * Verifiziert, dass die Methode calculate() korrekt funktioniert.
     *
     * @throws NegativeAmountException Wenn der Betrag negativ ist.
     */
    @Test
    void testCalculate() throws NegativeAmountException {
        // Test für den Standard-Transfer
        Transfer transfer = new Transfer("2024-11-30", 100.0, "Test Transfer");
        assertEquals(100.0, transfer.calculate()); // Der Betrag sollte 100.0 sein

        // Test für eingehenden Transfer
        IncomingTransfer incomingTransfer = new IncomingTransfer("2024-11-30", 150.0, "Incoming Transfer");
        assertEquals(150.0, incomingTransfer.calculate()); // Der Betrag sollte 150.0 sein

        // Test für ausgehenden Transfer
        OutgoingTransfer outgoingTransfer = new OutgoingTransfer("2024-11-30", 150.0, "Outgoing Transfer");
        assertEquals(-150.0, outgoingTransfer.calculate()); // Der Betrag sollte -150.0 sein
    }

    /**
     * Test für die equals-Methode der Transfer-Klasse.
     * Verifiziert, dass zwei Transfer-Objekte korrekt verglichen werden.
     *
     * @throws NegativeAmountException Wenn der Betrag negativ ist.
     */
    @Test
    void testEquals() throws NegativeAmountException {
        // Zwei identische Transfers
        Transfer transfer1 = new Transfer("2024-11-30", 100.0, "Test Transfer", "Alice", "Bob");
        Transfer transfer2 = new Transfer("2024-11-30", 100.0, "Test Transfer", "Alice", "Bob");
        // Ein Transfer mit unterschiedlichen Werten
        Transfer transfer3 = new Transfer("2024-11-30", 200.0, "Different Transfer", "Charlie", "David");

        assertEquals(transfer1, transfer2); // Beide Transfers sollten gleich sein
        assertNotEquals(transfer1, transfer3); // Die Transfers sollten nicht gleich sein
    }

    /**
     * Test für die toString-Methode der Transfer-Klasse.
     * Verifiziert, dass die String-Darstellung eines Transfers korrekt ist.
     *
     * @throws NegativeAmountException Wenn der Betrag negativ ist.
     */
    @Test
    void testToString() throws NegativeAmountException {
        Transfer transfer = new Transfer("2024-11-30", 100.0, "Test Transfer", "Alice", "Bob");
        String expected = "Transaction{ date= 2024-11-30, amount= 100.0, Calculate= 100.0, description= Test Transfer}\n" +
                "Transfer{ sender= Alice, recipien= Bob}\n";
        assertEquals(expected, transfer.toString());
    }
}
