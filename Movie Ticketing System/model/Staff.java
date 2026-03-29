package model;

public class Staff extends User {
    private int staffID; // Encapsulated private variable

    public Staff(String username, String password, int staffID) {
        // Calls the constructor of User.java
        super(username, password, "STAFF");
        this.staffID = staffID;
    }

    // --- ENCAPSULATION ---
    public int getStaffID() {
        return staffID;
    }

    // --- POLYMORPHISM (Method Overriding) ---
    @Override
    public void displayMenu() {
        System.out.println("\n--- STAFF MENU (ID: " + staffID + ") ---");
        System.out.println("1. View Movie Schedules");
        System.out.println("2. Validate Ticket");
        System.out.println("3. Manage Concession Stock");
    }

    public void validateTicket(String bookingID) {
        // In a real system, you'd check this ID against your FileHandler/Database
        System.out.println("Staff " + staffID + " is validating ticket: " + bookingID);
        System.out.println("Ticket Status: VALID ✅");
    }
}