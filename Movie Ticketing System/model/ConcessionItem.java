package model;

public class ConcessionItem {
    private String name;
    private double price;
    private int stock;

    // Parameterized constructor to initialize a concession item.
    public ConcessionItem(String name, double price, int stock) {
        setName(name);
        setPrice(price);
        setStock(stock);
    }

    // Getter for item name.
    public String getName() {
        return name;
    }

    // Setter for item name with validation.
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Item name cannot be empty.");
        }
        this.name = name.trim();
    }

    // Getter for item price.
    public double getPrice() {
        return price;
    }

    // Setter for item price with validation.
    public void setPrice(double price) {
        if (price < 0) {
            throw new ArrayIndexOutOfBoundsException("Price cannot be negative.");
        }
        this.price = price;
    }

    // Getter for available stock.
    public int getStock() {
        return stock;
    }

    // Setter for stock quantity with validation.
    public void setStock(int stock) {
        if (stock < 0) {
            throw new ArrayIndexOutOfBoundsException("Stock cannot be negative.");
        }
        this.stock = stock;
    }

    public String toString() {
        return String.format("%s - RM %.2f | Stock: %d", name, price, stock);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConcessionItem)) {
            return false;
        }
        ConcessionItem other = (ConcessionItem) obj;
        if (Double.compare(price, other.price) != 0 || stock != other.stock) {
            return false;
        }
        return name == null ? other.name == null : name.equals(other.name);
    }

    public int hashCode() {
        int result = 17;
        result = 31 * result + (name == null ? 0 : name.hashCode());
        long priceBits = Double.doubleToLongBits(price);
        result = 31 * result + (int) (priceBits ^ (priceBits >>> 32));
        result = 31 * result + stock;
        return result;
    }
}