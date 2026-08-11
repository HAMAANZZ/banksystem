package bank.exceptions;

/**
 * Exception thrown when the amount is negative.
 */

public class NegativeAmountException extends Exception {
    public NegativeAmountException(String message) {
        super(message);
    }
}
