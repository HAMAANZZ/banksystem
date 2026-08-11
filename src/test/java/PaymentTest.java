import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    private Payment payment;


    /**
     * Setup-Methode, die vor jedem Test ausgeführt wird.
     * Hier kannst du Instanzen von Objekten vorbereiten, die für alle Tests benötigt werden.
     */
    @BeforeEach
    public void setUp() throws NegativeAmountException, InvalidIncomingInterestException, InvalidOutgoingInterestException {
        // Beispiel: Initialisierung eines Payment-Objekts, das für mehrere Tests genutzt wird
        // payment = new Payment("2024-11-30", 100.0, "Test Payment", 0.1, 0.05);
    }

    /**
     * Cleanup-Methode, die nach jedem Test ausgeführt wird.
     * Hier kannst du Ressourcen freigeben oder Zustände zurücksetzen.
     */
    @AfterEach
    public void tearDown() {
        // Bereinigungslogik, falls notwendig, z.B. Nullsetzen von Objekten
        // payment = null;
    }


    /**
     * Test für den Konstruktor mit 3 Parametern.
     * Verifiziert, dass die Attribute des Payments korrekt gesetzt werden.
     *
     * @throws NegativeAmountException Wenn der Betrag negativ ist.
     */
    @Test
    public void testConstructor() throws NegativeAmountException {
        payment = new Payment("2024-11-30", 100.0, "Test Payment");
        assertEquals("2024-11-30", payment.getDate());
        assertEquals(100.0, payment.getAmount());
        assertEquals("Test Payment", payment.getDescription());
        assertEquals(0.0, payment.getIncomingInterest());
        assertEquals(0.0, payment.getOutgoingInterest());
    }

    /**
     * Test für den Konstruktor mit 5 Parametern (einschließlich eingehendem und ausgehendem Zinssatz).
     * Verifiziert, dass die Attribute des Payments korrekt gesetzt werden.
     *
     * @throws NegativeAmountException          Wenn der Betrag negativ ist.
     * @throws InvalidIncomingInterestException Wenn der eingehende Zinssatz ungültig ist.
     * @throws InvalidOutgoingInterestException Wenn der ausgehende Zinssatz ungültig ist.
     */
    @Test
    void testConstructorWithInterest() throws NegativeAmountException, InvalidIncomingInterestException, InvalidOutgoingInterestException {
        Payment payment = new Payment("2024-11-30", 100.0, "Test Payment", 0.05, 0.02);
        assertEquals("2024-11-30", payment.getDate());
        assertEquals(100.0, payment.getAmount());
        assertEquals("Test Payment", payment.getDescription());
        assertEquals(0.05, payment.getIncomingInterest());
        assertEquals(0.02, payment.getOutgoingInterest());
    }

    /**
     * Test für den Copy-Konstruktor.
     * Verifiziert, dass eine Kopie des Payment-Objekts korrekt erstellt wird.
     *
     * @throws NegativeAmountException          Wenn der Betrag negativ ist.
     * @throws InvalidIncomingInterestException Wenn der eingehende Zinssatz ungültig ist.
     * @throws InvalidOutgoingInterestException Wenn der ausgehende Zinssatz ungültig ist.
     */
    @Test
    void testCopyConstructor() throws NegativeAmountException, InvalidIncomingInterestException, InvalidOutgoingInterestException {
        Payment original = new Payment("2024-11-30", 100.0, "Original Payment", 0.05, 0.02);
        Payment copy = new Payment(original);
        assertEquals(original, copy);                                           // Beide Objekte sollten gleich sein
        assertNotSame(original, copy);                                          // Sie sollten unterschiedliche Instanzen sein
    }

    /**
     * Test für die Berechnungsmethode mit eingehendem Zinssatz.
     * Verifiziert, dass der Betrag nach Berechnung mit eingehendem Zinssatz korrekt ist.
     *
     * @throws NegativeAmountException          Wenn der Betrag negativ ist.
     * @throws InvalidIncomingInterestException Wenn der eingehende Zinssatz ungültig ist.
     * @throws InvalidOutgoingInterestException Wenn der ausgehende Zinssatz ungültig ist.
     */
    @Test
    void testCalculateWithIncomingInterest() throws NegativeAmountException, InvalidIncomingInterestException, InvalidOutgoingInterestException {
        Payment payment = new Payment("2024-11-30", 100.0, "Test Payment", 0.1, 0.05);
        assertEquals(90.0, payment.calculate(), 0.001); // 100 - 10% Eingehender Zins
    }

    /**
     * Test für die Berechnungsmethode mit ausgehendem Zinssatz.
     * Verifiziert, dass der Betrag nach Berechnung mit ausgehendem Zinssatz korrekt ist.
     *
     * @throws NegativeAmountException          Wenn der Betrag negativ ist.
     * @throws InvalidIncomingInterestException Wenn der eingehende Zinssatz ungültig ist.
     * @throws InvalidOutgoingInterestException Wenn der ausgehende Zinssatz ungültig ist.
     */
    @Test
    void testCalculateWithOutgoingInterest() throws NegativeAmountException, InvalidIncomingInterestException, InvalidOutgoingInterestException {
        Payment payment = new Payment("2024-11-30", -100.0, "Test Payment", 0.1, 0.05);
        assertEquals(-105.0, payment.calculate(), 0.001); // -100 + 5% Ausgehender Zins
    }

    /**
     * Test für die equals-Methode.
     * Verifiziert, dass zwei Payment-Objekte korrekt miteinander verglichen werden.
     *
     * @throws NegativeAmountException          Wenn der Betrag negativ ist.
     * @throws InvalidIncomingInterestException Wenn der eingehende Zinssatz ungültig ist.
     * @throws InvalidOutgoingInterestException Wenn der ausgehende Zinssatz ungültig ist.
     */
    @Test
    void testEquals() throws NegativeAmountException, InvalidIncomingInterestException, InvalidOutgoingInterestException {
        Payment payment1 = new Payment("2024-11-30", 100.0, "Test Payment", 0.1, 0.05);
        Payment payment2 = new Payment("2024-11-30", 100.0, "Test Payment", 0.1, 0.05);
        Payment payment3 = new Payment("2024-11-30", 200.0, "Different Payment", 0.1, 0.05);
        assertEquals(payment1, payment2); // Beide Transfers sollten gleich sein
        assertNotEquals(payment1, payment3); // Die Payments sollten nicht gleich sein
    }

    /**
     * Test für die toString-Methode.
     * Verifiziert, dass die String-Darstellung eines Payments korrekt ist.
     *
     * @throws NegativeAmountException          Wenn der Betrag negativ ist.
     * @throws InvalidIncomingInterestException Wenn der eingehende Zinssatz ungültig ist.
     * @throws InvalidOutgoingInterestException Wenn der ausgehende Zinssatz ungültig ist.
     */
    @Test
    void testToString() throws NegativeAmountException, InvalidIncomingInterestException, InvalidOutgoingInterestException {
        Payment payment = new Payment("2024-11-30", 100.0, "Test Payment", 0.1, 0.05);
        String expected = "Transaction{ date= 2024-11-30, amount= 100.0, Calculate= 90.0, description= Test Payment}\n" +
                "Payment{ incomingInterest= 0.1, outgoingInterest= 0.05}\n";
        assertEquals(expected, payment.toString()); // Die toString-Darstellung sollte korrekt sein
    }

    /**
     * Test, ob bei ungültigem eingehendem Zinssatz die entsprechende Ausnahme ausgelöst wird.
     * Überprüft, ob der eingehende Zinssatz den Bereich [0, 1] einhält.
     */
    @Test
    void testInvalidIncomingInterest() {
        Exception exception = assertThrows(InvalidIncomingInterestException.class, () ->
                new Payment("2024-11-30", 100.0, "Test Payment", 1.5, 0.05)
        );
        assertEquals("Incoming interest or incomingInterest is out of range 0-1", exception.getMessage());
    }

    /**
     * Test, ob bei ungültigem ausgehendem Zinssatz die entsprechende Ausnahme ausgelöst wird.
     * Überprüft, ob der ausgehende Zinssatz den Bereich [0, 1] einhält.
     */
    @Test
    void testInvalidOutgoingInterest() {
        Exception exception = assertThrows(InvalidOutgoingInterestException.class, () ->
                new Payment("2024-11-30", 100.0, "Test Payment", 0.05, 1.5)
        );
        assertEquals("Outgoing interest or incomingInterest is out of range 0-1", exception.getMessage());
    }
}
