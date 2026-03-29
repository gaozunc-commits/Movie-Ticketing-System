package model;

public class Payment {
    private String paymentID;
    private double amount;
    private String method; // e.g., "Credit Card", "E-Wallet"

    public Payment(String paymentID, double amount, String method) {
        this.paymentID = paymentID;
        this.amount = amount;
        this.method = method;
    }

    // Getters
    
    public String getPaymentID() { return paymentID; }

    public double getAmount() { return amount; }

    public String getMethod() { return method; }

    public void processPayment() {
        // Tip: Use String.format to make the price look professional (2 decimal places)
        System.out.println("Processing " + method + " payment of RM " + String.format("%.2f", amount) + "...");
        System.out.println("Payment Successful! Transaction ID: " + paymentID);
    }
}