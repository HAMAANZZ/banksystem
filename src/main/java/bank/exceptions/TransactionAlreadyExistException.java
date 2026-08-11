package bank.exceptions;

/**
 * Diese Ausnahme wird ausgelöst, wenn versucht wird, eine bereits existierende Transaktion hinzuzufügen.
 */
public class TransactionAlreadyExistException extends Exception {
    /**
     * Konstruktor für die Ausnahme mit einer benutzerdefinierten Fehlermeldung.
     *
     * @param message Die Fehlermeldung, die die Ursache beschreibt.
     */
    public TransactionAlreadyExistException(String message) {
        super(message); // Ruft den Konstruktor der Basisklasse auf
    }
}
