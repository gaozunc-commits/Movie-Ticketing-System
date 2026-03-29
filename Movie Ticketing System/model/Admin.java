package model;

public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password, "ADMIN");
    }

    public void generateReport() {
        System.out.println("Generating Best Selling Movies Report...");
        // Logic for Task 4 would go here
    }
}