package model;

public class Payment {
    private String paymentId;
    private double amount;
    private String method;

    // Parameterized constructor to initialize payment details.
    public Payment(String paymentId, double amount, String method) {
        this.paymentId = paymentId;
        setAmount(amount);
        setMethod(method);
    }

    // Getter for payment ID.
    public String getPaymentId() {
        return paymentId;
    }

    // Getter for payment amount.
    public double getAmount() {
        return amount;
    }

    // Setter for payment amount with validation.
    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new ArrayIndexOutOfBoundsException("Payment amount must be positive.");
        }
        this.amount = amount;
    }

    // Getter for payment method.
    public String getMethod() {
        return method;
    }

    // Setter for payment method with validation.
    public void setMethod(String method) {
        if (method == null || method.trim().isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Payment method cannot be empty.");
        }
        this.method = method.trim();
    }

    public void processPayment() {
        System.out.println("Processing " + method + " payment: RM " + String.format("%.2f", amount));
        System.out.println("Payment successful. ID: " + paymentId);
    }
}