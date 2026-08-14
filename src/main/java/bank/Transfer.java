package bank;

import bank.exceptions.NegativeAmountException;

/**
 * Repräsentiert eine Überweisung mit Datum, Betrag, Beschreibung,
 * Sender und Empfänger.
 */
public class Transfer extends Transaction {
    /**
     * sender = Sender
     * recipient = Empfänger
     */
    private String sender = "";
    private String recipient = "";

    /**
     * Berechnet und gibt den Betrag der Überweisung zurück.
     * Der Betrag wird unverändert zurückgegeben.
     *
     * @return der Betrag der Überweisung
     */
    @Override
    public double calculate() {
        return amount; // Betrag unverändert zurückgeben
    }

    /**
     * Konstruktor für eine Überweisung mit Datum, Betrag und Beschreibung.
     *
     * @param date        das Datum der Überweisung
     * @param amount      der Betrag der Überweisung (muss positiv sein)
     * @param description die Beschreibung der Überweisung
     */
    public Transfer(String date, double amount, String description) throws NegativeAmountException {
        super(date, amount, description);
    }

    /**
     * Konstruktor für eine Überweisung mit Datum, Betrag, Beschreibung, Sender und
     * Empfänger.
     *
     * @param date        das Datum der Überweisung
     * @param amount      der Betrag der Überweisung (muss positiv sein)
     * @param description die Beschreibung der Überweisung
     * @param sender      der Sender der Überweisung
     * @param recipient   der Empfänger der Überweisung
     */
    public Transfer(String date, double amount, String description, String sender, String recipient)
            throws NegativeAmountException {
        super(date, amount, description);
        // wenn der Amount bei der set richtig gemacht würde dann setze die werte ein.
        this.sender = sender;
        this.recipient = recipient;
    }

    /**
     * Copy-Konstruktor für die Erstellung einer neuen Überweisung basierend auf
     * einer existierenden Überweisung.
     *
     * @param t die zu kopierende Überweisung
     */
    public Transfer(Transfer t) throws NegativeAmountException {
        this(t.getDate(), t.getAmount(), t.getDescription(), t.getSender(), t.getRecipient());
    }

    /**
     * Gibt eine String-Darstellung der Überweisung zurück, inklusive Sender und
     * Empfänger.
     *
     * @return eine textuelle Darstellung der Überweisung
     */
    @Override
    public String toString() {
        return super.toString() + "\n" + "Transfer{ sender= " + this.getSender() + ", recipien= " + this.getRecipient()
                + "}\n";
    }

    /**
     * Vergleicht diese Überweisung mit einem anderen Objekt auf Gleichheit.
     *
     * @param obj das zu vergleichende Objekt
     * @return true, wenn die Objekte gleich sind, andernfalls false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true; // Überprüft, ob es sich um dasselbe Objekt handelt
        if (obj == null || getClass() != obj.getClass()) { // Überprüft auf null und gleiche Klasse
            return false;
        }
        Transfer t = (Transfer) obj;
        return super.equals(t) &&
                this.getSender().equals(t.getSender()) &&
                this.getRecipient().equals(t.getRecipient());
    }

    /**
     * Setzt den Betrag der Transaktion.
     * Muss in den Unterklassen implementiert werden.
     *
     * @param Amount der neue Betrag der Transaktion
     */
    @Override
    public void setAmount(double Amount) throws NegativeAmountException {
        if (Amount < 0) {
            throw new NegativeAmountException("Negative amount");
        } else
            this.amount = Amount;
    }

    /**
     * Gibt den Sender der Überweisung zurück.
     *
     * @return der Sender der Überweisung
     */
    public String getSender() {
        return sender;
    }

    /**
     * Setzt den Sender der Überweisung.
     *
     * @param sender der neue Sender der Überweisung
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Gibt den Empfänger der Überweisung zurück.
     *
     * @return der Empfänger der Überweisung
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Setzt den Empfänger der Überweisung.
     *
     * @param recipient der neue Empfänger der Überweisung
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

}