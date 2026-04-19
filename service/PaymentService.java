package service;

import model.Payment;
import util.InputValidator;

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
        System.out.println("1. Ewallet");
        System.out.println("2. Cash");
        System.out.println("3. Card");
        int methodOption = InputValidator.promptIntInRange(scanner, "Choose payment option: ", 1, 3);
        String paymentMethod;
        if (methodOption == 1) {
            paymentMethod = "Ewallet";
        } else if (methodOption == 2) {
            paymentMethod = "Cash";
        } else {
            paymentMethod = "Card";
        }
        return processPayment(paymentMethod, amount);
    }
}
