package model;

public class VIPTicket extends Ticket {
    public VIPTicket(String seatNumber) {
        super(seatNumber, 35.0, "VIP");
    }

    @Override
    public double calculatePrice() {
        return getPrice();
    }
}