package bank;

import bank.exceptions.*;

/**
 * Von abstract können wir keine objekte davon machen.
 * deswegen wenn wir implements machen, dann müssen wir die methode in
 * CalculateBill nicht überschreiben.
 * denn das wird in KinderClasse gemacht.
 * wenn wir eine methode aus der CalculateBill aufrufen, dann wird die methode
 * aus der kinderClasse aufgerufen.
 * <p>
 * <p>
 * Abstrakte Klasse, die allgemeine Eigenschaften und Methoden einer Transaktion
 * definiert.
 * Von dieser Klasse können keine Objekte direkt erstellt werden.
 */
public abstract class Transaction implements CalculateBill {
    /**
     * date = datum
     * amount = preis
     * description = beschreibung
     */
    protected String date;
    protected double amount;
    protected String description = "";

    /**
     * Konstruktor zur Erstellung einer neuen Transaktion.
     *
     * @param date        das Datum der Transaktion
     * @param amount      der Betrag der Transaktion
     * @param description die Beschreibung der Transaktion
     */

    public Transaction(String date, double amount, String description) throws NegativeAmountException {
        this.date = date;
        this.setAmount(amount);
        this.description = description;
    }

    /**
     * Gibt eine String-Darstellung der Transaktion zurück, inklusive Datum, Betrag,
     * berechneter Wert und Beschreibung.
     *
     * @return eine textuelle Darstellung der Transaktion
     */
    
    @Override
    public String toString() {
        return "Transaction{ " + "date= " + date + ", amount= " + amount + ", Calculate= " + calculate()
                + ", description= " + description + '}';
    }

    /**
     * Vergleicht diese Transaktion mit einem anderen Objekt auf Gleichheit.
     *
     * @param obj das zu vergleichende Objekt
     * @return true, wenn die Objekte gleich sind, andernfalls false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true; // Prüft, ob es sich um dasselbe Objekt handelt
        if (obj == null || getClass() != obj.getClass()) { // Prüft auf null und gleiche Klasse
            return false;
        }
        Transaction tr = (Transaction) obj;

        return tr.getDate().equals(this.getDate()) &&
                tr.getAmount() == this.getAmount() &&
                tr.getDescription().equals(this.getDescription());

    }

    /**
     * Gibt das Datum der Transaktion zurück.
     *
     * @return das Datum der Transaktion als String
     */
    public String getDate() {
        return date;
    }

    /**
     * Setzt das Datum der Transaktion.
     *
     * @param date das neue Datum der Transaktion
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Setzt den Betrag der Transaktion.
     * Muss in den Unterklassen implementiert werden.
     *
     * @param Amount der neue Betrag der Transaktion
     */
    public abstract void setAmount(double Amount) throws NegativeAmountException;

    /**
     * Gibt den Betrag der Transaktion zurück.
     *
     * @return der Betrag der Transaktion
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Gibt die Beschreibung der Transaktion zurück.
     *
     * @return die Beschreibung als String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setzt die Beschreibung der Transaktion.
     *
     * @param description die neue Beschreibung der Transaktion
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
