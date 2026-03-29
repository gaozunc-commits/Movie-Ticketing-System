package model;

public class StandardTicket extends Ticket {
    
    // Constructor passes the seat and a default base price (e.g., RM 15.00) to the parent
    public StandardTicket(String seatNumber) {
        super(seatNumber, 15.00);
    }

    @Override
    public double calculateFinalPrice() {
        return basePrice; // No extra charges for standard
    }
}