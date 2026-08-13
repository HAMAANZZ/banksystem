package bank;

import bank.exceptions.*;
import com.google.gson.*;

import java.io.*;
import java.util.*;

/**
 * die Klasse entspricht eine Privatbank.
 * AltV1: in getAccountBalance wird Klassen IncomingTransfer und
 * OutgoingTransfer Benutzt.
 */
public class PrivateBank implements Bank {

    /**
     * Name: Bank Name
     * incomingInterest: Einkommen Zinsen satz
     * outgoingInterest: Ausgaben Zinsen satz
     * accountsToTransactions: Account name als Schlüssen und eine Liste von
     * Transaction
     * Transaction könnte Payment; Transfer; IncomingTransfer und OutcomingTransfer
     * sein.
     * gson: gson vorlage
     */
    private String name;
    private double incomingInterest;
    private double outgoingInterest;
    private final Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();
    private String directoryName = "json/";
    private Gson gson = new GsonBuilder()
            // Ermöglicht das Registrieren eines benutzerdefinierten TypeAdapters für die
            // Serialisierung oder Deserialisierung eines bestimmten Typs.
            .registerTypeAdapter(Transaction.class, new TransactionAdapter())
            .setPrettyPrinting() // setPrettyPrinting(): Formatiert die JSON-Ausgabe für eine bessere Lesbarkeit.
            .create();
    // serializeNulls(): Gibt an, ob Null-Werte in der JSON-Ausgabe serialisiert
    // werden sollen.
    // disableHtmlEscaping(): Verhindert das Escaping von HTML-Zeichen.

    /**
     * Konstruktor
     *
     * @param name
     * @param incomingInterest
     * @param outgoingInterest
     * @directoryName ist der Pfard zu datein.
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest, String directoryName)
            throws InvalidIncomingInterestException, InvalidOutgoingInterestException, IOException {
        this.name = name;
        this.directoryName = directoryName;
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
        // Prüfen, ob der JSON Ordner existiert
        File directory = new File(directoryName);

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException(
                        "Der Ordner konnte nicht erstellt werden: "
                                + directoryName);
            }
        }

        // Vorhandene Accounts aus JSON laden
        readAccounts();

    }

    /**
     * kopierkonstruktor
     *
     * @param pb
     */
    public PrivateBank(PrivateBank pb) throws InvalidIncomingInterestException, InvalidOutgoingInterestException {
        // Kopiert die ersten drei Attribute
        this.setName(pb.getName());
        setIncomingInterest(pb.getIncomingInterest());
        setOutgoingInterest(pb.getOutgoingInterest());

    }

    /**
     * Überschreibt die `toString`-Methode, um eine String-Darstellung des Objekts
     * bereitzustellen.
     * Diese Methode fasst die wichtigsten Attribute der Klasse zusammen,
     * was nützlich ist für Debugging, Protokollierung oder Ausgabe.
     *
     * @return Eine lesbare String-Darstellung des PrivateBank-Objekts.
     */
    @Override
    public String toString() {
        return "PrivateBank{ " +
                "name= " + name +
                ", incomingInterest= " + incomingInterest +
                ", outgoingInterest= " + outgoingInterest +
                ", accountsToTransactions= " + accountsToTransactions +
                '}';
    }

    /**
     * Überschreibt die `equals`-Methode, um die Gleichheit zweier
     * `PrivateBankV2`-Objekte zu überprüfen.
     * Zwei Objekte gelten als gleich, wenn:
     * - Sie der gleichen Klasse angehören
     * - Alle relevanten Attribute (name, incomingInterest, outgoingInterest,
     * accountsToTransactions) übereinstimmen
     *
     * @param obj Das Objekt, mit dem verglichen wird.
     * @return `true`, wenn die Objekte gleich sind, andernfalls `false`.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PrivateBank pb = (PrivateBank) obj;
        return Double.compare(pb.incomingInterest, incomingInterest) == 0 &&
                Double.compare(pb.outgoingInterest, outgoingInterest) == 0 &&
                Objects.equals(name, pb.name) &&
                Objects.equals(accountsToTransactions, pb.accountsToTransactions);
    }

    /**
     * Löscht ein Konto aus der Bank.
     * <p>
     * Falls das Konto nicht existiert, wird eine
     * {@link AccountDoesNotExistException} ausgelöst.
     * Diese Methode kann auch eine {@link IOException} werfen, falls ein zugrunde
     * liegender
     * Ein-/Ausgabefehler auftritt.
     *
     * @param account der Name des zu löschenden Kontos
     * @throws AccountDoesNotExistException wenn das zu löschende Konto nicht
     *                                      existiert
     * @throws IOException                  wenn ein Ein-/Ausgabefehler während des
     *                                      Löschvorgangs auftritt
     */
    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        // Überprüfen, ob das Konto existiert
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Das Konto existiert nicht: " + account);
        }

        // Konto aus der Map entfernen
        accountsToTransactions.remove(account);

        // Dateipfad für die JSON-Datei des Kontos erstellen
        String filePath = directoryName + "Konto_" + account + ".json";

        // Versuchen, die Datei zu löschen
        File file = new File(filePath);
        if (file.exists() && !file.delete()) {
            throw new IOException("Die Datei konnte nicht gelöscht werden: " + filePath);
        }

        System.out.println("Das Konto '" + account + "' wurde erfolgreich gelöscht.");
    }

    /**
     * Gibt eine Liste aller Konten zurück, die von der Bank verwaltet werden.
     * <p>
     * Die Liste enthält die Namen aller derzeit im System verfügbaren Konten.
     *
     * @return eine {@link List} von {@link String}, die die Namen aller Konten
     *         repräsentiert
     */
    @Override
    public List<String> getAllAccounts() {
        ArrayList<String> accounts = new ArrayList<>();
        for (Map.Entry<String, List<Transaction>> entry : accountsToTransactions.entrySet()) {
            accounts.add(entry.getKey());
        }
        return accounts;
    }

    /**
     * Diese Methode soll alle vorhandenen Konten vom
     * Dateisystem lesen und im accountsToTransactions zur Verfügung stellen
     *
     * @throws IOException Fehler aufgetreten beim Lesen
     */
    public void readAccounts() throws IOException {

        File directory = new File(this.directoryName);

        // Alle Dateien suchen, die wie Konto_NAME.json heißen
        File[] accountFiles = directory.listFiles(
                (dir, fileName) -> fileName.startsWith("Konto_")
                        && fileName.endsWith(".json"));

        // Falls keine Dateien vorhanden sind
        if (accountFiles == null) {
            return;
        }

        // Map leeren, bevor neu geladen wird
        accountsToTransactions.clear();

        for (File file : accountFiles) {

            String fileName = file.getName();

            // Beispiel:
            // Konto_MusterMann.json
            // wird zu:
            // MusterMann
            String accountName = fileName.substring(
                    "Konto_".length(),
                    fileName.length() - ".json".length());

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                Transaction[] transactions = gson.fromJson(
                        reader,
                        Transaction[].class);

                List<Transaction> transactionList = new ArrayList<>();

                // Eine leere JSON Datei kann null ergeben
                if (transactions != null) {
                    transactionList.addAll(
                            Arrays.asList(transactions));
                }

                accountsToTransactions.put(
                        accountName,
                        transactionList);

                System.out.println(
                        "Account geladen: "
                                + accountName
                                + " ("
                                + transactionList.size()
                                + " Transaktionen)");

            } catch (JsonParseException e) {

                throw new IOException(
                        "Fehler beim Lesen von "
                                + file.getName()
                                + ": "
                                + e.getMessage(),
                        e);
            }
        }
    }

    /**
     * Diese Methode soll das angegebene Konto im
     * Dateisystem persistieren (serialisieren und anschließend speichern).
     * Die Datei wird überschrieben.
     *
     * @param account Account Name
     * @throws IOException                  Fehler aufgetreten beim Schreiben
     * @throws AccountDoesNotExistException wenn der Account existiert.
     */
    public void writeAccount(String account) throws IOException, AccountDoesNotExistException {
        // Überprüfen, ob das Konto existiert
        if (!(accountsToTransactions.containsKey(account))) {
            throw new AccountDoesNotExistException("Das Konto existiert nicht: " + account);
        }

        // Hole die Transaktionsliste für das Konto
        // List zu einer Array machen, weil der Json nur Array akzeptiert
        Transaction[] transactionArray = getTransactions(account).toArray(Transaction[]::new);
        /*
         * // Wenn keine Transaktionen vorhanden sind, keine Datei erstellen
         * if (transactions.isEmpty()) {
         * System.out.println("Keine Transaktionen für das Konto vorhanden.");
         * return;
         * }
         */

        // Erstelle den Dateipfad
        String filePath = this.directoryName + "Konto_" + account + ".json";

        // Konvertiere die Transaktionsliste in JSON
        String json = gson.toJson(transactionArray);

        // Schreibe den JSON-String in eine Datei
        // es wird Überschrieben

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(json);
            System.out.println("Transaktionen für Konto " + account + " wurden erfolgreich gespeichert.");

        } catch (IOException e) {
            throw new IOException("Fehler beim Schreiben der Datei: " + e.getMessage());
        }
    }

    /**
     * Adds an account to the bank.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    @Override
    public void createAccount(String account)
            throws AccountAlreadyExistsException,
            IOException,
            AccountDoesNotExistException {

        // Accountnamen prüfen und bereinigen
        account = validateAccountName(account);

        // Prüfen, ob Account bereits existiert
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account existiert bereits: " + account);
        }

        // Zusätzlich Groß/Kleinschreibung beachten
        for (String existingAccount : accountsToTransactions.keySet()) {

            if (existingAccount.equalsIgnoreCase(account)) {
                throw new AccountAlreadyExistsException(
                        "Ein Account mit diesem Namen existiert bereits: " + existingAccount);
            }
        }

        // Account erstellen
        accountsToTransactions.put(account, new ArrayList<>());

        // JSON Datei speichern
        writeAccount(account);
    }

    /**
     * Adds an account (with specified transactions) to the bank.
     * Important: duplicate transactions must not be added to the account!
     * Fehler: Konto existiert schon, Transaktion ist doppelt oder ungültig
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be
     *                     added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws TransactionAttributeException    if the validation check for certain
     *                                          attributes fail
     * @throws AccountDoesNotExistException
     */

    @Override
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException,
            IOException, AccountDoesNotExistException {

        account = validateAccountName(account);

        // Überprüfe, ob das Konto bereits existiert
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account already exists: " + account);
        }

        // Neue Liste für Transaktionen erstellen
        List<Transaction> transactionList = new ArrayList<>();

        for (Transaction transaction : transactions) {
            // contains: Überprüfen, ob die Transaktion bereits in der Liste ist
            if (transactionList.contains(transaction)) {
                throw new TransactionAlreadyExistException("Duplicate transaction detected: " + transaction);
            }
            /*
             * Überprüfe, ob die Transaktion gültig ist
             * Z.B
             * Description und Date darf nicht null und "" (leer) sein.
             */
            if (transaction.getDescription() == null || transaction.getDescription().isEmpty()
                    || transaction.getDate() == null || transaction.getDate().isEmpty()) {
                throw new TransactionAttributeException("Invalid transaction attributes: " + transaction);
            }

            // Transaktion hinzufügen
            transactionList.add(transaction);
        }

        // Konto mit Transaktionen hinzufügen
        accountsToTransactions.put(account, transactionList);
        this.writeAccount(account);
    }

    /**
     * Adds a transaction to an already existing account.
     * Fügt eine Transaktion zu einem bestehenden Konto hinzu
     * Fehler: Konto existiert nicht, Transaktion doppelt oder ungültig.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified
     *                    account
     * @throws TransactionAlreadyExistException
     * @throws AccountDoesNotExistException
     * @throws TransactionAttributeException
     * @throws InvalidIncomingInterestException
     * @throws InvalidOutgoingInterestException
     * @throws IOException
     */

    @Override
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException,
            InvalidIncomingInterestException, InvalidOutgoingInterestException, IOException {

        // wenn die datei nicht existiert dann haben wir eine Exception
        if (!(accountsToTransactions.containsKey(account))) {
            throw new AccountDoesNotExistException("Das angegebene Konto existiert nicht.");
        }

        /*
         * Überprüfe, ob die Transaktion gültig ist
         * Z.B
         * Description und Date darf nicht null und "" (leer) sein.
         */
        if (transaction.getDescription() == null || transaction.getDescription().isEmpty()
                || transaction.getDate() == null || transaction.getDate().isEmpty()) {
            throw new TransactionAttributeException("Invalid transaction attributes: " + transaction);
        }

        // Überprüfen, ob die Transaktion bereits existiert
        List<Transaction> transactionList = accountsToTransactions.get(account); // Liste der Transaktionen für das
                                                                                 // Konto
        if (transactionList.contains(transaction)) {
            throw new TransactionAlreadyExistException("Die Transaktion existiert bereits im Konto '" + account + "'.");
        }
        // Wenn es sich um ein Payment handelt, incomingInterest und outgoingInterest
        // setzen
        if (transaction instanceof Payment) {
            Payment payment = (Payment) transaction;
            payment.setIncomingInterest(this.getIncomingInterest()); // Zinsen der Bank setzen
            payment.setOutgoingInterest(this.getOutgoingInterest()); // Zinsen der Bank setzen
        }

        // Transaktion hinzufügen
        transactionList.add(transaction);
        this.writeAccount(account);
    }

    /**
     * Removes a transaction from an account. If the transaction does not exist, an
     * exception is
     * thrown.
     * Entfernt eine Transaktion von einem Konto.
     * Fehler: Konto existiert nicht, Transaktion existiert nicht
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified
     *                    account
     * @throws AccountDoesNotExistException     if the specified account does not
     *                                          exist
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     */

    @Override
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {

        // Überprüfen, ob das Konto existiert
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("The account does not exist: " + account);
        }

        // Holen der Transaktionsliste für das Konto
        List<Transaction> transactionList = accountsToTransactions.get(account);

        // Überprüfen, ob die Transaktion in der Liste vorhanden ist
        if (!transactionList.contains(transaction)) {
            throw new TransactionDoesNotExistException("The transaction does not exist in the account: " + transaction);
        }

        // Transaktion aus der Liste entfernen
        transactionList.remove(transaction);

        System.out.println("Transaction removed successfully from account: " + account);
        this.writeAccount(account);
    }

    /**
     * Checks whether the specified transaction for a given account exists.
     * Prüft, ob eine bestimmte Transaktion in einem Konto existiert.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        // Überprüfen, ob das Konto existiert
        if (!accountsToTransactions.containsKey(account)) {
            return false; // Konto existiert nicht, daher Transaktion auch nicht enthalten
        }

        // Holen der Transaktionsliste für das Konto
        List<Transaction> transactionList = accountsToTransactions.get(account);

        // Überprüfen, ob die Transaktion in der Liste enthalten ist
        return transactionList.contains(transaction);
    }

    /**
     * Calculates and returns the current account balance.
     * Berechnet und gibt den Kontostand zurück.
     *
     * @param account the selected account
     * @return the current account balance
     */

    @Override
    public double getAccountBalance(String account) throws AccountDoesNotExistException {
        double balance = 0.0;
        // Überprüfen, ob das Konto existiert
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("The account does not exist: " + account);
        }

        for (Transaction transaction : accountsToTransactions.get(account)) {
            balance += transaction.calculate();
        }
        return balance;
    }

    /**
     * Returns a list of transactions for an account.
     * Gibt eine Liste aller Transaktionen eines Kontos zurück.
     *
     * @param account the selected account
     * @return the list of all transactions for the specified account
     */
    @Override
    public List<Transaction> getTransactions(String account) {
        // Überprüfen, ob das Konto existiert
        if (!accountsToTransactions.containsKey(account)) {
            return List.of(); // Leere Liste zurückgeben, wenn das Konto nicht existiert
        }

        // Transaktionsliste zurückgeben
        return new ArrayList<>(accountsToTransactions.get(account)); // Kopie der Liste erstellen
    }

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific
     * account. Sorts the list either in ascending or descending order
     * (or empty).
     * Gibt die Transaktionen eines Kontos sortiert zurück (aufsteigend/absteigend).
     * aufsteigend => True => 10;20;30
     * absteigend => Flase => 30;20;10
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or
     *                descending order
     * @return the sorted list of all transactions for the specified account
     */
    @Override
    public List<Transaction> getTransactionsSorted(
            String account,
            boolean asc) {

        // Prüfen, ob Account existiert
        if (!accountsToTransactions.containsKey(account)) {
            return List.of();
        }

        // Kopie erstellen
        List<Transaction> transactions = new ArrayList<>(
                accountsToTransactions.get(account));

        // Nach dem tatsächlich berechneten Betrag sortieren
        transactions.sort((t1, t2) -> {

            int comparison = Double.compare(
                    t1.calculate(),
                    t2.calculate());

            return asc
                    ? comparison
                    : -comparison;
        });

        return transactions;
    }

    /**
     * Returns a list of either positive or negative transactions (-> calculated
     * amounts).
     * Gibt nur positive (z. B. Einnahmen) oder negative (z. B. Ausgaben)
     * Transaktionen zurück.
     *
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return the list of all transactions by type
     */

    @Override
    public List<Transaction> getTransactionsByType(
            String account,
            boolean positive) {

        // Prüfen, ob Account existiert
        if (!accountsToTransactions.containsKey(account)) {
            return List.of();
        }

        return accountsToTransactions
                .get(account)
                .stream()
                .filter(transaction -> {

                    double value = transaction.calculate();

                    if (positive) {
                        return value > 0;
                    } else {
                        return value < 0;
                    }
                })
                .toList();
    }

    /**
     * Überweist Geld von einem Konto auf ein anderes Konto.
     *
     * Beim Sender wird ein OutgoingTransfer gespeichert.
     * Beim Empfänger wird ein IncomingTransfer gespeichert.
     */
    public void transfer(
            String sender,
            String recipient,
            String date,
            double amount,
            String description) throws AccountDoesNotExistException,
            NegativeAmountException,
            TransactionAlreadyExistException,
            TransactionAttributeException,
            IOException {

        // --------------------------------------------------------
        // 1. Prüfen, ob Sender existiert
        // --------------------------------------------------------

        if (!accountsToTransactions.containsKey(sender)) {
            throw new AccountDoesNotExistException(
                    "Senderkonto existiert nicht: " + sender);
        }

        // --------------------------------------------------------
        // 2. Prüfen, ob Empfänger existiert
        // --------------------------------------------------------

        if (!accountsToTransactions.containsKey(recipient)) {
            throw new AccountDoesNotExistException(
                    "Empfängerkonto existiert nicht: " + recipient);
        }

        // --------------------------------------------------------
        // 3. Sender und Empfänger dürfen nicht gleich sein
        // --------------------------------------------------------

        if (sender.equals(recipient)) {
            throw new TransactionAttributeException(
                    "Sender und Empfänger dürfen nicht gleich sein.");
        }

        // --------------------------------------------------------
        // 4. Betrag überprüfen
        // --------------------------------------------------------

        if (amount <= 0) {
            throw new TransactionAttributeException(
                    "Der Betrag muss größer als 0 sein.");
        }

        // --------------------------------------------------------
        // 5. Datum überprüfen
        // --------------------------------------------------------

        if (date == null || date.isBlank()) {
            throw new TransactionAttributeException(
                    "Das Datum darf nicht leer sein.");
        }

        // --------------------------------------------------------
        // 6. Beschreibung überprüfen die Darf leer sein
        // --------------------------------------------------------

        // if (description == null || description.isBlank()) {
        // throw new TransactionAttributeException(
        // "Die Beschreibung darf nicht leer sein.");
        // }

        // --------------------------------------------------------
        // 7. Prüfen, ob genug Geld vorhanden ist
        // --------------------------------------------------------

        double balance = getAccountBalance(sender);

        if (balance < amount) {
            throw new TransactionAttributeException(
                    "Nicht genügend Guthaben.");
        }

        // --------------------------------------------------------
        // 8. Zwei Transaktionen erzeugen
        // --------------------------------------------------------

        OutgoingTransfer outgoingTransfer = new OutgoingTransfer(
                date,
                amount,
                description,
                sender,
                recipient);

        IncomingTransfer incomingTransfer = new IncomingTransfer(
                date,
                amount,
                description,
                sender,
                recipient);

        // --------------------------------------------------------
        // 9. Prüfen, ob Transaktionen schon existieren
        // --------------------------------------------------------

        if (containsTransaction(sender, outgoingTransfer)) {
            throw new TransactionAlreadyExistException(
                    "OutgoingTransfer existiert bereits.");
        }

        if (containsTransaction(recipient, incomingTransfer)) {
            throw new TransactionAlreadyExistException(
                    "IncomingTransfer existiert bereits.");
        }

        // --------------------------------------------------------
        // 10. Sender bekommt negative Transaktion
        // --------------------------------------------------------

        accountsToTransactions
                .get(sender)
                .add(outgoingTransfer);

        // --------------------------------------------------------
        // 11. Empfänger bekommt positive Transaktion
        // --------------------------------------------------------

        accountsToTransactions
                .get(recipient)
                .add(incomingTransfer);

        // --------------------------------------------------------
        // 12. Beide Accounts speichern
        // --------------------------------------------------------

        writeAccount(sender);
        writeAccount(recipient);

        System.out.println(
                amount
                        + " Euro von "
                        + sender
                        + " an "
                        + recipient
                        + " überwiesen.");
    }

    /**
     * Prüft und bereinigt einen Accountnamen.
     */
    private String validateAccountName(String account) {

        // null verhindern
        if (account == null) {
            throw new IllegalArgumentException(
                    "Der Accountname darf nicht null sein.");
        }

        // Leerzeichen vorne und hinten entfernen
        String cleanName = account.trim();

        // Leeren Namen verhindern
        if (cleanName.isEmpty()) {
            throw new IllegalArgumentException(
                    "Der Accountname darf nicht leer sein.");
        }

        // Zu lange Namen verhindern
        if (cleanName.length() > 50) {
            throw new IllegalArgumentException(
                    "Der Accountname darf maximal 50 Zeichen haben.");
        }

        // Zeichen verhindern, die unter Windows
        // in Dateinamen problematisch sind
        if (cleanName.matches(".*[\\\\/:*?\"<>|].*")) {
            throw new IllegalArgumentException(
                    "Der Accountname enthält ungültige Zeichen.");
        }

        return cleanName;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Getter- und Setter-Methoden für die ersten drei Attribute

    /**
     * @return
     */
    public String getDirectoryName() {
        return directoryName;
    }

    /**
     * @param directoryName
     */
    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    /**
     * Getter-Methode für den Namen der Bank.
     *
     * @return Der Name der Bank als String.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter-Methode für den Namen der Bank.
     *
     * @param name Der neue Name der Bank.
     */
    public void setName(String name) {
        this.name = name; // Aktualisiert den Namen der Bank
    }

    /**
     * Getter-Methode für den Zinssatz von eingehenden Transaktionen.
     *
     * @return Der Zinssatz für eingehende Transaktionen als double.
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }

    /**
     * Getter-Methode für den Zinssatz von ausgehenden Transaktionen.
     *
     * @return Der Zinssatz für ausgehende Transaktionen als double.
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     * Setzt den eingehenden Zinssatz. Überprüft, ob der Wert zwischen 0 und 1
     * liegt.
     *
     * @param incomingInterest der neue eingehende Zinssatz, muss zwischen 0 und 1
     *                         liegen
     */
    public void setIncomingInterest(double incomingInterest) throws InvalidIncomingInterestException {
        if (incomingInterest > 1 || incomingInterest < 0) {
            throw new InvalidIncomingInterestException("Incoming interest or incomingInterest is out of range 0-1");
        } else {
            this.incomingInterest = incomingInterest;
        }
    }

    /**
     * Setzt den ausgehenden Zinssatz. Überprüft, ob der Wert zwischen 0 und 1
     * liegt.
     *
     * @param outgoingInterest der neue ausgehende Zinssatz, muss zwischen 0 und 1
     *                         liegen
     */
    public void setOutgoingInterest(double outgoingInterest) throws InvalidOutgoingInterestException {
        if (outgoingInterest > 1 || outgoingInterest < 0) {
            throw new InvalidOutgoingInterestException("Outgoing interest or incomingInterest is out of range 0-1");
        } else {
            this.outgoingInterest = outgoingInterest;
        }

    }
}