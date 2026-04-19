package model;

public class Order {

    private String orderId;
    private String customerUsername;
    private String showtimeId;
    private Ticket[] tickets;
    private int ticketCount;
    private ConcessionItem[] concessionItems;
    private int concessionItemCount;
    private double totalPrice;
    private String paymentMethod;
    private String purchasedAt;

    public Order(String orderId, String customerUsername, String showtimeId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be empty.");
        }
        if (customerUsername == null || customerUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer username cannot be empty.");
        }
        if (showtimeId == null || showtimeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Showtime ID cannot be empty.");
        }
        this.orderId = orderId.trim();
        this.customerUsername = customerUsername.trim();
        this.showtimeId = showtimeId.trim();
        this.tickets = new Ticket[0];
        this.ticketCount = 0;
        this.concessionItems = new ConcessionItem[0];
        this.concessionItemCount = 0;
        this.totalPrice = 0.0;
        this.paymentMethod = "Unknown";
        this.purchasedAt = "N/A";
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

    public Ticket[] getTickets() {
        Ticket[] copied = new Ticket[ticketCount];
        for (int i = 0; i < ticketCount; i++) {
            copied[i] = tickets[i];
        }
        return copied;
    }

    public ConcessionItem[] getConcessionItems() {
        ConcessionItem[] copied = new ConcessionItem[concessionItemCount];
        for (int i = 0; i < concessionItemCount; i++) {
            copied[i] = concessionItems[i];
        }
        return copied;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPurchasedAt() {
        return purchasedAt;
    }

    public void setTotalPrice(double totalPrice) {
        if (totalPrice < 0) {
            throw new IllegalArgumentException("Total price cannot be negative.");
        }
        this.totalPrice = totalPrice;
    }

    public void setPaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            this.paymentMethod = "Unknown";
            return;
        }
        this.paymentMethod = paymentMethod.trim();
    }

    public void setPurchasedAt(String purchasedAt) {
        if (purchasedAt == null || purchasedAt.trim().isEmpty()) {
            this.purchasedAt = "N/A";
            return;
        }
        this.purchasedAt = purchasedAt.trim();
    }

    public void addTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null.");
        }
        tickets = appendTicket(tickets, ticket);
        ticketCount++;
        totalPrice += ticket.calculatePrice();
    }

    public void addConcessionItem(ConcessionItem item, int quantity) {
        if (item == null) {
            throw new IllegalArgumentException("Concession item cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        ConcessionItem orderedItem = new ConcessionItem(
                item.getName(),
                item.getPrice(),
                quantity,
                item.getCategory()
        );

        concessionItems = appendConcessionItem(concessionItems, orderedItem);
        concessionItemCount++;

        totalPrice += item.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return orderId + " | Customer: " + customerUsername + " | Showtime: " + showtimeId + " | Total: RM "
                + String.format("%.2f", totalPrice);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Order)) {
            return false;
        }
        Order other = (Order) obj;
        return orderId == null ? other.orderId == null : orderId.equals(other.orderId);
    }

    @Override
    public int hashCode() {
        return 31 + (orderId == null ? 0 : orderId.hashCode());
    }

    private Ticket[] appendTicket(Ticket[] source, Ticket ticket) {
        Ticket[] expanded = new Ticket[source.length + 1];
        for (int i = 0; i < source.length; i++) {
            expanded[i] = source[i];
        }
        expanded[source.length] = ticket;
        return expanded;
    }

    private ConcessionItem[] appendConcessionItem(ConcessionItem[] source, ConcessionItem item) {
        ConcessionItem[] expanded = new ConcessionItem[source.length + 1];
        for (int i = 0; i < source.length; i++) {
            expanded[i] = source[i];
        }
        expanded[source.length] = item;
        return expanded;
    }
}
