package bank;

import bank.exceptions.*;

import java.io.IOException;
import java.util.List;

;

/**
 * Interface for a generic bank. Provides multiple methods to handle the interaction between
 * accounts and transactions.
 */
public interface Bank {
    /**
     * Löscht ein Konto aus der Bank.
     *
     * Falls das Konto nicht existiert, wird eine {@link AccountDoesNotExistException} ausgelöst.
     * Diese Methode kann auch eine {@link IOException} werfen, falls ein zugrunde liegender
     * Ein-/Ausgabefehler auftritt.
     *
     * @param account der Name des zu löschenden Kontos
     * @throws AccountDoesNotExistException wenn das zu löschende Konto nicht existiert
     * @throws IOException                  wenn ein Ein-/Ausgabefehler während des Löschvorgangs auftritt
     */
    void deleteAccount(String account) throws AccountDoesNotExistException, IOException;

    /**
     * Gibt eine Liste aller Konten zurück, die von der Bank verwaltet werden.
     *
     * Die Liste enthält die Namen aller derzeit im System verfügbaren Konten.
     *
     * @return eine {@link List} von {@link String}, die die Namen aller Konten repräsentiert
     */
    List<String> getAllAccounts();


    /**
     * Adds an account to the bank.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    void createAccount(String account) throws AccountAlreadyExistsException, IOException, AccountDoesNotExistException;

    /**
     * Adds an account (with specified transactions) to the bank.
     * Important: duplicate transactions must not be added to the account!
     *
     * Fehler: Konto existiert schon, Transaktion ist doppelt oder ungültig
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     *
     */
    void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, IOException, AccountDoesNotExistException;

    /**
     * Adds a transaction to an already existing account.
     * Fügt eine Transaktion zu einem bestehenden Konto hinzu
     * Fehler: Konto existiert nicht, Transaktion doppelt oder ungültig.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, InvalidIncomingInterestException, InvalidOutgoingInterestException, IOException;

        /**
         * Removes a transaction from an account. If the transaction does not exist, an exception is
         * thrown.
         * Entfernt eine Transaktion von einem Konto.
         * Fehler: Konto existiert nicht, Transaktion existiert nicht
         *
         *
         * @param account     the account from which the transaction is removed
         * @param transaction the transaction which is removed from the specified account
         * @throws AccountDoesNotExistException     if the specified account does not exist
         * @throws TransactionDoesNotExistException if the transaction cannot be found
         */
    void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException;

    /**
     * Checks whether the specified transaction for a given account exists.
     * Prüft, ob eine bestimmte Transaktion in einem Konto existiert.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     */
    boolean containsTransaction(String account, Transaction transaction);

    /**
     * Calculates and returns the current account balance.
     * Berechnet und gibt den Kontostand zurück.
     *
     * @param account the selected account
     * @return the current account balance
     */
    double getAccountBalance(String account) throws AccountDoesNotExistException;

    /**
     * Returns a list of transactions for an account.
     * Gibt eine Liste aller Transaktionen eines Kontos zurück.
     *
     * @param account the selected account
     * @return the list of all transactions for the specified account
     */
    List<Transaction> getTransactions(String account);

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account. Sorts the list either in ascending or descending order
     * (or empty).
     * Gibt die Transaktionen eines Kontos sortiert zurück (aufsteigend/absteigend).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return the sorted list of all transactions for the specified account
     */
    List<Transaction> getTransactionsSorted(String account, boolean asc);

    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     * Gibt nur positive (z. B. Einnahmen) oder negative (z. B. Ausgaben) Transaktionen zurück.
     *
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return the list of all transactions by type
     */
    List<Transaction> getTransactionsByType(String account, boolean positive);
}
