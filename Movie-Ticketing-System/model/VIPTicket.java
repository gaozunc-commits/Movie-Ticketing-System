package model;

public class VIPTicket extends Ticket {
    // Parameterized constructor for VIP ticket creation.
    public VIPTicket(String seatNumber) {
        super(seatNumber, 35.0, "VIP");
    }

    @Override
    public double calculatePrice() {
        return getPrice();
    }
}