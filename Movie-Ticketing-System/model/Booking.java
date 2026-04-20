package model;

import java.util.Objects;

public class Booking {

    private String bookingId;
    private Order order;

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

    public String getBookingId() {
        return bookingId;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId + ", Order ID: " + order.getOrderId();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) obj;
        return Objects.equals(bookingId, other.bookingId)
                && Objects.equals(order, other.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, order);
    }
}
