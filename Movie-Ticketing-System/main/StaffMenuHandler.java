package main;

import java.util.Scanner;
import model.User;
import service.BookingService;
import service.ConcessionService;
import util.InputValidator;

public class StaffMenuHandler {

    private final Scanner scanner;
    private final BookingService bookingService;
    private final ConcessionService concessionService;

    public StaffMenuHandler(Scanner scanner,
                            BookingService bookingService,
                            ConcessionService concessionService) {
        this.scanner = scanner;
        this.bookingService = bookingService;
        this.concessionService = concessionService;
    }

    public void run(Staff staff) {
        boolean keepStaffMenuOpen = true;

        while (keepStaffMenuOpen) {
            staff.displayMenu();
            int choice = InputValidator.promptIntInRange(scanner, "Select: ", 0, 4);

            try {
   switch (choice) {

    case 1:
        System.out.println("\n=== VALIDATE TICKET ===");
        String orderId = InputValidator.promptText(scanner, "Enter order ID: ");

        boolean valid = bookingService.existsByOrderOrBookingId(orderId);

        System.out.println(valid ? "Ticket VALID." : "Ticket INVALID.");
        break;

    case 2:
        updateStock(true);
        break;

    case 3:
        updateStock(false);
        break;

    case 4:
        System.out.println("\n=== CONCESSION LIST ===");
        concessionService.displayConcessions();
        break;

    case 0:
        System.out.println("Logging out...");
        keepStaffMenuOpen = false;
        break;

    default:
        System.out.println("Invalid menu option.");
}

} catch (Exception e) {
    System.out.println("Staff flow error: " + e.getMessage());
}
        }
    }
    private void updateStock(boolean isAdd) {

    String action = isAdd ? "ADD" : "DEDUCT";

    System.out.println("\n=== " + action + " CONCESSION STOCK ===");
    concessionService.displayConcessions();

    if (concessionService.readAllItems().length == 0) {
        System.out.println("No concession items available.");
        return;
    }

    int index = InputValidator.promptOneBasedIndex(
            scanner,
            "Select concession index: ",
            concessionService.readAllItems().length
    );

    if (index == -1) return;

    int currentStock = concessionService.readAllItems()[index].getStock();

    int qty = InputValidator.promptIntInRange(
            scanner,
            "Enter quantity:1-1000 ",
            1,
            1000
    );

    if (!isAdd && qty > currentStock) {
        System.out.println(" Cannot deduct more than current stock (" + currentStock + ")");
        return;
    }

    if (isAdd) {
        concessionService.addStock(index, qty);
    } else {
        concessionService.deductStock(index, qty);
    }

    System.out.println("Stock updated successfully.");
}
    public static final class Staff extends User {
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
            System.out.println("\n=== STAFF MENU ===");
            System.out.println("1. Validate Ticket");
            System.out.println("2. Add Concession Stock");
            System.out.println("3. Deduct Concession Stock");
            System.out.println("4. View Concession List");
            System.out.println("0. Logout");
        }
        }
    }

