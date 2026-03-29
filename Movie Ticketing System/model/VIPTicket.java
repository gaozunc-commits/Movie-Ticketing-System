package model;

public class VIPTicket extends Ticket {
    
    public VIPTicket(String seatNumber) {
        super(seatNumber, 25.00); // VIP starts with a higher base price
    }

    @Override
    public double calculateFinalPrice() {
        // Polymorphism in action: VIP has a different calculation logic
        double tax = 0.06; // Example 6% SST
        return (basePrice + 10.00) * (1 + tax); 
    }
}