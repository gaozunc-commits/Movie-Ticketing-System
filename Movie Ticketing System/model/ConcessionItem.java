package model;

public class ConcessionItem {
    private String name;
    private double price;
    private int quantity;

    public ConcessionItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Encapsulation
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public double calculateSubtotal() { return price * quantity; }

    @Override
    public String toString() {
        return String.format("%s (x%d) - RM %.2f", name, quantity, calculateSubtotal());
    }
}