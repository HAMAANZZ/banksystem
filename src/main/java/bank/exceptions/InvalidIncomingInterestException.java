package bank.exceptions;

/**
 * Exception thrown when the incoming interest rate is not between 0 and 1.
 */
public class InvalidIncomingInterestException extends Exception {
    public InvalidIncomingInterestException(String message) {
        super(message);
    }
}