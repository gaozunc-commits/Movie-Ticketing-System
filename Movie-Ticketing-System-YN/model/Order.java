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

    // Parameterized constructor to create a new order.
    public Order(String orderId, String customerUsername, String showtimeId) throws ArrayIndexOutOfBoundsException {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Order ID cannot be empty.");
        }
        if (customerUsername == null || customerUsername.trim().isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Customer username cannot be empty.");
        }
        if (showtimeId == null || showtimeId.trim().isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Showtime ID cannot be empty.");
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

    public void addTicket(Ticket ticket) throws ArrayIndexOutOfBoundsException {
        if (ticket == null) {
            throw new ArrayIndexOutOfBoundsException("Ticket cannot be null.");
        }
        tickets = appendTicket(tickets, ticket);
        ticketCount++;
        totalPrice += ticket.calculatePrice();
    }

    public void addConcessionItem(ConcessionItem item, int quantity)
        throws ArrayIndexOutOfBoundsException {

    if (item == null) {
        throw new ArrayIndexOutOfBoundsException("Concession item cannot be null.");
    }

    if (quantity <= 0) {
        throw new ArrayIndexOutOfBoundsException("Quantity must be positive.");
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

    public void setTotalPrice(double totalPrice) throws ArrayIndexOutOfBoundsException {
        if (totalPrice < 0) {
            throw new ArrayIndexOutOfBoundsException("Total price cannot be negative.");
        }
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            this.paymentMethod = "Unknown";
            return;
        }
        this.paymentMethod = paymentMethod.trim();
    }

    public String getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(String purchasedAt) {
        if (purchasedAt == null || purchasedAt.trim().isEmpty()) {
            this.purchasedAt = "N/A";
            return;
        }
        this.purchasedAt = purchasedAt.trim();
    }

    public String toString() {
        return orderId + " | Customer: " + customerUsername + " | Showtime: " + showtimeId + " | Total: RM " + String.format("%.2f", totalPrice);
    }

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
