package service;

import model.Payment;
import java.util.Scanner;

public class PaymentService {
    public Payment processPayment(String method, double amount) {
        Payment payment = new Payment("PAY-" + System.currentTimeMillis(), amount, method);
        payment.processPayment();
        return payment;
    }

    public Payment processPaymentByChoice(double amount, Scanner scanner) {
        boolean validPaymentMethodSelected = false;
        String paymentMethod = "";
        do {
            try {
                System.out.println("\nSelect payment method:");
                System.out.println("1. Ewallet");
                System.out.println("2. Cash");
                System.out.println("3. Card");
                System.out.print("Choose payment option: ");
                int methodOption = Integer.parseInt(scanner.nextLine());
                if (methodOption == 1) {
                    paymentMethod = "Ewallet";
                    validPaymentMethodSelected = true;
                } else if (methodOption == 2) {
                    paymentMethod = "Cash";
                    validPaymentMethodSelected = true;
                } else if (methodOption == 3) {
                    paymentMethod = "Card";
                    validPaymentMethodSelected = true;
                } else {
                    System.out.println("Invalid payment option. Please choose 1, 2, or 3.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
            }
        } while (!validPaymentMethodSelected);

        return processPayment(paymentMethod, amount);
    }
}
