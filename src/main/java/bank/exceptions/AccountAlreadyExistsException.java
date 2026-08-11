package bank.exceptions;

/**
 * Diese Ausnahme wird ausgelöst, wenn versucht wird, ein Konto hinzuzufügen, das bereits existiert.
 */
public class AccountAlreadyExistsException extends Exception {
    /**
     * Konstruktor für die Ausnahme mit einer benutzerdefinierten Fehlermeldung.
     *
     * @param message Die Fehlermeldung, die die Ursache beschreibt.
     */
    public AccountAlreadyExistsException(String message) {
        super(message); // Ruft den Konstruktor der Basisklasse auf
    }
}
