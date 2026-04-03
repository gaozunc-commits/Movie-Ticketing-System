package model;

public class StandardTicket extends Ticket {
    public StandardTicket(String seatNumber) {
        super(seatNumber, 18.0, "STANDARD");
    }

    @Override
    public double calculatePrice() {
        return getPrice();
    }
}