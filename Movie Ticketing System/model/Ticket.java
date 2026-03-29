package model;

public abstract class Ticket {
    private String seatNumber;
    protected double basePrice;

    public Ticket(String seatNumber, double basePrice) {
        this.seatNumber = seatNumber;
        this.basePrice = basePrice;
    }

    public abstract double calculateFinalPrice();

    public double getBasePrice() { return basePrice; }

    public String getSeatNumber() { return seatNumber; }
}