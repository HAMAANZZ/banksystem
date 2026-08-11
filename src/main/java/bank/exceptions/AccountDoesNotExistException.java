package bank.exceptions;

/**
 * Diese Ausnahme wird ausgelöst, wenn ein Konto, auf das zugegriffen werden soll, nicht existiert.
 */
public class AccountDoesNotExistException extends Exception {
    /**
     * Konstruktor für die Ausnahme mit einer benutzerdefinierten Fehlermeldung.
     *
     * @param message Die Fehlermeldung, die das Problem beschreibt.
     */
    public AccountDoesNotExistException(String message) {
        super(message); // Ruft den Konstruktor der Basisklasse auf
    }
}
