package bank.exceptions;

/**
 * Diese Ausnahme wird ausgelöst, wenn eine bestimmte Transaktion nicht existiert.
 */
public class TransactionDoesNotExistException extends Exception {
    /**
     * Konstruktor für die Ausnahme mit einer benutzerdefinierten Fehlermeldung.
     *
     * @param message Die Fehlermeldung, die die Ursache beschreibt.
     */
    public TransactionDoesNotExistException(String message) {
        super(message); // Ruft den Konstruktor der Basisklasse auf
    }
}
