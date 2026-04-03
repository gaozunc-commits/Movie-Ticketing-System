package model;

public class Admin extends User {
    public Admin(String username, String password, String name) {
        super(username, password, name, "ADMIN");
    }

    @Override
    public void displayMenu() {
        System.out.println("\n--- ADMIN MENU ---");
        System.out.println("1. Manage Movies");
        System.out.println("2. Manage Showtimes");
        System.out.println("3. Manage Concessions");
        System.out.println("4. Manage Users");
        System.out.println("5. Reports");
        System.out.println("0. Logout");
    }
}