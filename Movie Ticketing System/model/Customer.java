package model;

public class Customer extends User {
    private boolean loyaltyMember;
    private int loyaltyPoints;

    // Parameterized constructor for customer user creation.
    public Customer(String username, String password, String name, boolean loyaltyMember) {
        super(username, password, name, "CUSTOMER");
        this.loyaltyMember = loyaltyMember;
        this.loyaltyPoints = 0;
    }

    public void displayMenu() {
        System.out.println("\n--- CUSTOMER MENU ---");
        System.out.println("1. Browse Movies");
        System.out.println("2. Book Ticket");
        System.out.println("3. View My Orders");
        System.out.println("0. Logout");
    }

    // Getter for loyalty membership status.
    public boolean isLoyaltyMember() {
        return loyaltyMember;
    }

    // Setter for loyalty membership status.
    public void setLoyaltyMember(boolean loyaltyMember) {
        this.loyaltyMember = loyaltyMember;
    }

    // Getter for current loyalty points.
    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void addLoyaltyPoints(int points) {
        if (points > 0) {
            this.loyaltyPoints += points;
        }
    }
}
