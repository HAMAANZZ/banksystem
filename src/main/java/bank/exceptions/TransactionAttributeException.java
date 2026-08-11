package bank.exceptions;

/**
 * Diese Ausnahme wird ausgelöst, wenn eine Transaktion ungültige oder unzulässige Attribute enthält.
 */
public class TransactionAttributeException extends Exception {
    /**
     * Konstruktor für die Ausnahme mit einer benutzerdefinierten Fehlermeldung.
     *
     * @param message Die Fehlermeldung, die das Problem beschreibt.
     */
    public TransactionAttributeException(String message) {
        super(message); // Ruft den Konstruktor der Basisklasse auf
    }
}
