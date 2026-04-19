package model;

public class Booking {

    private String bookingId;
    private Order order;

    // Constructor with validation
    public Booking(String bookingId, Order order) {

        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be empty");
        }

        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        this.bookingId = bookingId.trim();
        this.order = order;
    }

    // Getter for booking ID
    public String getBookingId() {
        return bookingId;
    }

    // Getter for order
    public Order getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId + 
               ", Order ID: " + order.getOrderId();
    }
}