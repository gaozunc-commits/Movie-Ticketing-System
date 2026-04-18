package model;

public class Customer extends User {

    // Parameterized constructor for customer user creation.
    public Customer(String username, String password, String name) {
        super(username, password, name, "CUSTOMER");
    }

    public void displayMenu() {
        System.out.println("\n--- CUSTOMER MENU ---");
        System.out.println("1. Browse Movies");
        System.out.println("2. Book Ticket");
        System.out.println("3. View My Orders");
        System.out.println("0. Logout");
    }

}
