package bank;

import bank.exceptions.NegativeAmountException;

/**
 * Die Klasse ist für Ausgaben nach einer Überweisung.
 */
public class OutgoingTransfer extends Transfer {
    //private double outgoingInterest = 0.0;

    /**
     * Berechnet und gibt den Betrag der Überweisung zurück.
     * Der Betrag wird unverändert zurückgegeben.
     *
     * @return der Betrag der Überweisung
     */
    //TODO: in Praktikum fragen: hat Transfer eigentlich Zinsen?
    @Override
    public double calculate() {
        //return -(super.calculate() + (super.calculate() * outgoingInterest)); // amount in Transaction
        return -(super.calculate());
    }


    /**
     * Konstruktor für eine Überweisung mit Datum, Betrag und Beschreibung.
     *
     * @param date        das Datum der Überweisung
     * @param amount      der Betrag der Überweisung (muss positiv sein)
     * @param description die Beschreibung der Überweisung
     */
    public OutgoingTransfer(String date, double amount, String description) throws NegativeAmountException {
        super(date, amount, description);
    }

    /**
     * Konstruktor für eine Überweisung mit Datum, Betrag, Beschreibung, Sender und Empfänger.
     *
     * @param date        das Datum der Überweisung
     * @param amount      der Betrag der Überweisung (muss positiv sein)
     * @param description die Beschreibung der Überweisung
     * @param sender      der Sender der Überweisung
     * @param recipient   der Empfänger der Überweisung
     */
    public OutgoingTransfer(String date, double amount, String description, String sender, String recipient) throws NegativeAmountException {
        super(date, amount, description, sender, recipient);
    }

    /**
     * Copy-Konstruktor für die Erstellung einer neuen Überweisung basierend auf einer existierenden Überweisung.
     *
     * @param t die zu kopierende Überweisung
     */
    public OutgoingTransfer(Transfer t) throws NegativeAmountException {
        super(t);
    }

}
