package org.example;

import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.Payment;
import bank.PrivateBank;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        PrivateBank bank;

        try {
            System.out.println("try1");

            bank = new PrivateBank("bank", 0.1, 0.1, "json/");
            bank.createAccount("test");
            bank.addTransaction("test", new Payment("Date", 100, "Beschreibung"));
            bank.addTransaction("test", new IncomingTransfer("Date", 50, "Beschreibung"));
            bank.addTransaction("test", new OutgoingTransfer("Date", 50, "Beschreibung"));
        } catch (Exception e) {


        }
    }
}