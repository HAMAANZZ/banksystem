package bank;

import bank.exceptions.*;


/**
 * Repräsentiert eine Zahlung mit Datum, Betrag und Beschreibung.
 * Zusätzlich unterstützt sie eingehende und ausgehende Zinssätze, falls angegeben.
 */

public class Payment extends Transaction {
    /**
     * incomingInterest = Positiv Zinsen 0-1 für einzahlen.
     * outgoingInterest = Positiv Zinsen 0-1 für auszahlen.
     */
    private double incomingInterest = 0.0;
    private double outgoingInterest = 0.0;

    /**
     * Berechnet und gibt den endgültigen Betrag der Zahlung basierend auf den Zinssätzen zurück.
     * Bei positiven Beträgen wird der eingehende Zinssatz angewendet, andernfalls der ausgehende.
     *
     * @return der berechnete Betrag der Zahlung nach Anwendung des Zinssatzes
     */
    @Override
    public double calculate() {
        if (amount > 0) {
            return amount - (amount * incomingInterest);
        } else {
            return amount + (amount * outgoingInterest);
        }
    }

    /**
     * Konstruktor, der eine Zahlung mit dem angegebenen Datum, Betrag und Beschreibung erstellt.
     *
     * @param date        das Datum der Zahlung
     * @param amount      der Betrag der Zahlung
     * @param description die Beschreibung der Zahlung
     */
    public Payment(String date, double amount, String description) throws NegativeAmountException {
        super(date, amount, description);
    }

    /**
     * Konstruktor, der eine Zahlung mit dem angegebenen Datum, Betrag, Beschreibung und Zinssätzen erstellt.
     *
     * @param date             das Datum der Zahlung
     * @param amount           der Betrag der Zahlung
     * @param description      die Beschreibung der Zahlung
     * @param incomingInterest der eingehende Zinssatz, muss zwischen 0 und 1 liegen
     * @param outgoingInterest der ausgehende Zinssatz, muss zwischen 0 und 1 liegen
     */
    public Payment(String date, double amount, String description, double incomingInterest, double outgoingInterest)
            throws NegativeAmountException, InvalidIncomingInterestException, InvalidOutgoingInterestException {
        this(date, amount, description);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);

    }

    /**
     * Copy-Konstruktor, der ein neues bank.Payment-Objekt aus einem bestehenden bank.Payment-Objekt erstellt.
     *
     * @param p das zu kopierende bank.Payment-Objekt
     */
    public Payment(Payment p) throws NegativeAmountException, NegativeAmountException, InvalidIncomingInterestException, InvalidOutgoingInterestException {
        this(p.getDate(), p.getAmount(), p.getDescription(), p.getIncomingInterest(), p.getOutgoingInterest());
    }

    /**
     * Gibt eine String-Darstellung der Zahlung zurück, inklusive Zinssätze.
     *
     * @return eine textuelle Darstellung der Zahlung
     */
    @Override
    public String toString() {
        return super.toString() + "\n" + "Payment{" + " incomingInterest= " + incomingInterest + ", outgoingInterest= " + outgoingInterest + "}\n";
    }

    /**
     * Vergleicht diese Zahlung mit einem anderen Objekt auf Gleichheit.
     *
     * @param obj das zu vergleichende Objekt
     * @return true, wenn die Objekte gleich sind, andernfalls false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;                       // Prüft, ob es sich um dasselbe Objekt handelt
        if (obj == null || getClass() != obj.getClass()) {  // Prüft auf null und gleiche Klasse
            return false;
        }
        Payment p = (Payment) obj;
        return super.equals(p) &&
                this.getIncomingInterest() == p.getIncomingInterest() &&
                this.getOutgoingInterest() == p.getOutgoingInterest();
    }

    /**
     * Setzt den Betrag der Zahlung.
     * Hier wird der Exception ignoriert, weil der Amount darf negativ sein.
     *
     * @param Amount der Betrag der Zahlung
     */
    @Override
    public void setAmount(double Amount) throws NegativeAmountException {
        this.amount = Amount;
    }

    /**
     * Gibt den eingehenden Zinssatz zurück.
     *
     * @return der eingehende Zinssatz
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }

    /**
     * Setzt den eingehenden Zinssatz. Überprüft, ob der Wert zwischen 0 und 1 liegt.
     *
     * @param incomingInterest der neue eingehende Zinssatz, muss zwischen 0 und 1 liegen
     */
    public void setIncomingInterest(double incomingInterest) throws InvalidIncomingInterestException {
        if (incomingInterest > 1 || incomingInterest < 0) {
            throw new InvalidIncomingInterestException("Incoming interest or incomingInterest is out of range 0-1");
        } else {
            this.incomingInterest = incomingInterest;
        }
    }

    /**
     * Gibt den ausgehenden Zinssatz zurück.
     *
     * @return der ausgehende Zinssatz
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     * Setzt den ausgehenden Zinssatz. Überprüft, ob der Wert zwischen 0 und 1 liegt.
     *
     * @param outgoingInterest der neue ausgehende Zinssatz, muss zwischen 0 und 1 liegen
     */
    public void setOutgoingInterest(double outgoingInterest) throws InvalidOutgoingInterestException {
        if (outgoingInterest > 1 || outgoingInterest < 0) {
            throw new InvalidOutgoingInterestException("Outgoing interest or incomingInterest is out of range 0-1");
        } else {
            this.outgoingInterest = outgoingInterest;
        }

    }


}