package bank;

/**
 * Schnittstelle zur Berechnung von Zahlungen.
 * Muss in allen Klassen implementiert werden, die eine Zahlung kalkulieren.
 */
public interface CalculateBill {
    /**
     * Berechnet den Betrag einer Transaktion.
     * @return der berechnete Betrag als double
     */
    double calculate();
}
