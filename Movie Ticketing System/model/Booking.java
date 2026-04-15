package model;

public class Booking {
    private String bookingId;
    private Order order;

    // Parameterized constructor to bind a booking ID with an order.
    public Booking(String bookingId, Order order) {
        this.bookingId = bookingId;
        this.order = order;
    }

    // Getter for booking ID.
    public String getBookingId() {
        return bookingId;
    }

    // Getter for linked order object.
    public Order getOrder() {
        return order;
    }
}