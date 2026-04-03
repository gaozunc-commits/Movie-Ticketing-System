package model;

public class Booking {
    private String bookingId;
    private Order order;

    public Booking(String bookingId, Order order) {
        this.bookingId = bookingId;
        this.order = order;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Order getOrder() {
        return order;
    }
}