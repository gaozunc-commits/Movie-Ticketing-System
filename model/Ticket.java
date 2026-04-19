package model;

import java.util.Objects;

public abstract class Ticket implements LinePriced {

    private String seatNumber;
    private double price;
    private String ticketType;

    protected Ticket(String seatNumber, double price, String ticketType) {
        setSeatNumber(seatNumber);
        setPrice(price);
        setTicketType(ticketType);
    }

    public abstract double calculatePrice();

    @Override
    public double lineTotal() {
        return calculatePrice();
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public double getPrice() {
        return price;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setSeatNumber(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Seat number cannot be empty.");
        }
        this.seatNumber = seatNumber.trim().toUpperCase();
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
        this.price = price;
    }

    public void setTicketType(String ticketType) {
        if (ticketType == null || ticketType.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket type cannot be empty.");
        }
        this.ticketType = ticketType.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return ticketType + " ticket | seat " + seatNumber + " | base RM " + String.format("%.2f", price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Ticket other = (Ticket) obj;
        return Double.compare(price, other.price) == 0
                && Objects.equals(seatNumber, other.seatNumber)
                && Objects.equals(ticketType, other.ticketType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatNumber, ticketType, price);
    }
}
