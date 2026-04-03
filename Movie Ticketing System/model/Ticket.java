package model;

public abstract class Ticket {
    private String seatNumber;
    private double price;
    private String ticketType;

    protected Ticket(String seatNumber, double price, String ticketType) {
        setSeatNumber(seatNumber);
        setPrice(price);
        this.ticketType = ticketType;
    }

    public abstract double calculatePrice();

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Seat number cannot be empty.");
        }
        this.seatNumber = seatNumber.trim().toUpperCase();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
        this.price = price;
    }

    public String getTicketType() {
        return ticketType;
    }
}