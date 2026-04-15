package model;

public abstract class Ticket {
    private String seatNumber;
    private double price;
    private String ticketType;

    // Parameterized constructor used by ticket subclasses.
    protected Ticket(String seatNumber, double price, String ticketType) {
        setSeatNumber(seatNumber);
        setPrice(price);
        this.ticketType = ticketType;
    }

    public abstract double calculatePrice();

    // Getter for seat number.
    public String getSeatNumber() {
        return seatNumber;
    }

    // Setter for seat number with validation.
    public void setSeatNumber(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Seat number cannot be empty.");
        }
        this.seatNumber = seatNumber.trim().toUpperCase();
    }

    // Getter for ticket base price.
    public double getPrice() {
        return price;
    }

    // Setter for ticket base price with validation.
    public void setPrice(double price) {
        if (price <= 0) {
            throw new ArrayIndexOutOfBoundsException("Price must be positive.");
        }
        this.price = price;
    }

    // Getter for ticket type label.
    public String getTicketType() {
        return ticketType;
    }
}