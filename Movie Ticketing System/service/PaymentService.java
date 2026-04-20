package service;

import util.InputValidator;

import java.util.Objects;
import java.util.Scanner;

public class PaymentService {

    public Payment processPayment(String method, double amount) {
        Payment payment = new Payment("PAY-" + System.currentTimeMillis(), amount, method);
        payment.processPayment();
        return payment;
    }

    public Payment processPaymentByChoice(double amount, Scanner scanner) {
        System.out.println("\n=== PAYMENT ===");
        System.out.println("Select payment method:");
        System.out.println("1. TNG eWallet");
        System.out.println("2. Cash");
        System.out.println("3. Card");
        int methodOption = InputValidator.promptIntInRange(scanner, "Choose payment option: ", 1, 3);
        String paymentMethod;
        switch (methodOption) {
            case 1:
                paymentMethod = "TNG";
                break;

            case 2:
                paymentMethod = "Cash";
                break;

            case 3:
                paymentMethod = "Card";
                break;

            default:
                paymentMethod = "Unknown";
                break;
        }

        if ("TNG".equals(paymentMethod)) {
            validateTngPayment(amount, scanner);
        }

        if ("Cash".equals(paymentMethod)) {
            validateCashPayment(amount, scanner);
        }

        return processPayment(paymentMethod, amount);
    }

    /**
     * TNG only: confirm pay now (1) or cancel order (0).
     */
    private void validateTngPayment(double amountDue, Scanner scanner) {
        System.out.println("\n=== TNG PAYMENT VALIDATION ===");
        System.out.println("Total due: RM " + String.format("%.2f", amountDue));
        System.out.println("1. Pay now");
        System.out.println("0. Cancel order");
        int choice = InputValidator.promptIntInRange(scanner, "Choose: ", 0, 1);
        if (choice == 0) {
            throw new IllegalStateException("Order cancelled.");
        }
    }

    private void validateCashPayment(double amountDue, Scanner scanner) {
        System.out.println("\n=== CASH PAYMENT VALIDATION ===");

        while (true) {
            System.out.println("Total due: RM " + String.format("%.2f", amountDue));
            double tendered = InputValidator.promptPositiveDouble(scanner, "Amount tendered (RM): ");

            if (tendered >= amountDue) {
                if (tendered > amountDue) {
                    System.out.println("Change: RM " + String.format("%.2f", tendered - amountDue));
                }
                return;
            }

            System.out.println("Payment unsuccessful. Insufficient amount.");
            System.out.println("0. Cancel order");
            System.out.println("1. Continue");
            int next = InputValidator.promptIntInRange(scanner, "Choose: ", 0, 1);

            if (next == 0) {
                throw new IllegalStateException("Order cancelled.");
            }
        }
    }

    public static final class Payment {

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
            return "Payment ID: " + paymentId
                    + " | Method: " + method
                    + " | Amount: RM " + String.format("%.2f", amount);
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
}
