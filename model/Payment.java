package model;

import java.util.Objects;

public class Payment {

    private String paymentId;
    private double amount;
    private String method;

    public Payment(String paymentId, double amount, String method) {

        if (paymentId == null || paymentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment ID cannot be empty.");
        }

        this.paymentId = paymentId.trim();

        setAmount(amount);
        setMethod(method);
    }

    public String getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive.");
        }
        this.amount = amount;
    }

    public void setMethod(String method) {
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be empty.");
        }
        this.method = method.trim();
    }

    public void processPayment() {
        System.out.println("Processing " + method + " payment: RM " + String.format("%.2f", amount));
        System.out.println("Payment successful. ID: " + paymentId);
    }

    @Override
    public String toString() {
        return "Payment ID: " + paymentId +
               " | Method: " + method +
               " | Amount: RM " + String.format("%.2f", amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) obj;
        return Double.compare(amount, other.amount) == 0
                && Objects.equals(paymentId, other.paymentId)
                && Objects.equals(method, other.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, amount, method);
    }
}