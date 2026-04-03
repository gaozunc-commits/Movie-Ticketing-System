package model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private String customerUsername;
    private String showtimeId;
    private List<Ticket> tickets;
    private List<ConcessionItem> concessionItems;
    private double totalPrice;
    private String couponCode;
    private String qrCode;

    public Order(String orderId, String customerUsername, String showtimeId) {
        this.orderId = orderId;
        this.customerUsername = customerUsername;
        this.showtimeId = showtimeId;
        this.tickets = new ArrayList<>();
        this.concessionItems = new ArrayList<>();
        this.totalPrice = 0.0;
        this.couponCode = "";
        this.qrCode = "QR-" + orderId;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        totalPrice += ticket.calculatePrice();
    }

    public void addConcessionItem(ConcessionItem item, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        concessionItems.add(new ConcessionItem(item.getName(), item.getPrice(), quantity));
        totalPrice += item.getPrice() * quantity;
    }

    public void applyCoupon(String couponCode, double discountRate) {
        if (discountRate <= 0 || discountRate >= 1) {
            throw new IllegalArgumentException("Discount rate must be between 0 and 1.");
        }
        this.couponCode = couponCode;
        totalPrice = totalPrice * (1.0 - discountRate);
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public List<ConcessionItem> getConcessionItems() {
        return concessionItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public String getQrCode() {
        return qrCode;
    }
}
