package bank.exceptions;

/**
 * Exception thrown when the outgoing interest rate is not between 0 and 1.
 */
public class InvalidOutgoingInterestException extends Exception {
    public InvalidOutgoingInterestException(String message) {
        super(message);
    }
}