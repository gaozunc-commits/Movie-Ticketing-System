package model;

public class Staff extends User {
    private int staffId;

    public Staff(String username, String password, String name, int staffId) {
        super(username, password, name, "STAFF");
        setStaffId(staffId);
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        if (staffId <= 0) {
            throw new IllegalArgumentException("Staff ID must be positive.");
        }
        this.staffId = staffId;
    }

    @Override
    public void displayMenu() {
        System.out.println("\n--- STAFF MENU ---");
        System.out.println("1. Validate Ticket");
        System.out.println("2. Update Concession Stock");
        System.out.println("0. Logout");
    }
}