package service;

import model.Payment;

public class PaymentService {
    public Payment processPayment(String method, double amount) {
        Payment payment = new Payment("PAY-" + System.currentTimeMillis(), amount, method);
        payment.processPayment();
        return payment;
    }
}
