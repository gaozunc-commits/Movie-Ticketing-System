package model;

public class ConcessionItem {

    private String name;
    private double price;
    private int stock;
    private String category; // FOOD / DRINK / SNACK

// git change on here
    private static final String FOOD = "FOOD";
    private static final String DRINK = "DRINK";
    private static final String SNACK = "SNACK";

    public ConcessionItem(String name, double price, int stock, String category) {
        setName(name);
        setPrice(price);
        setStock(stock);
        setCategory(category);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty.");
        }
        this.name = name.trim();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null ||
                !(category.equalsIgnoreCase(FOOD) ||
                  category.equalsIgnoreCase(DRINK) ||
                  category.equalsIgnoreCase(SNACK))) {

            throw new IllegalArgumentException("Category must be FOOD, DRINK, or SNACK.");
        }

        this.category = category.toUpperCase();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - RM %.2f | Stock: %d",
                category, name, price, stock);
    }
}