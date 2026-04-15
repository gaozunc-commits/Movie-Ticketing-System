package model;

public class Staff extends User {
    private int staffId;

    // Parameterized constructor for staff user creation.
    public Staff(String username, String password, String name, int staffId) {
        super(username, password, name, "STAFF");
        setStaffId(staffId);
    }

    // Getter for staff ID.
    public int getStaffId() {
        return staffId;
    }

    // Setter for staff ID with validation.
    public void setStaffId(int staffId) {
        if (staffId <= 0) {
            throw new ArrayIndexOutOfBoundsException("Staff ID must be positive.");
        }
        this.staffId = staffId;
    }

    public void displayMenu() {
        System.out.println("\n--- STAFF MENU ---");
        System.out.println("1. Validate Ticket");
        System.out.println("2. Update Concession Stock");
        System.out.println("0. Logout");
    }
}