package main;

import java.util.Scanner;
import model.Staff;
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
            int choice = InputValidator.promptIntInRange(scanner, "Select: ", 0, 2);

            try {
                switch (choice) {

                    case 1:
                        System.out.println("\n=== VALIDATE TICKET ===");
                        String orderId = InputValidator.promptText(scanner, "Enter order ID: ");

                        boolean valid = bookingService.existsByOrderOrBookingId(orderId);

                        if (valid) {
                            System.out.println("Ticket VALID.");
                        } else {
                            System.out.println("Ticket INVALID.");
                        }
                        break;

                    case 2:
                        System.out.println("\n=== UPDATE CONCESSION STOCK ===");
                        concessionService.displayConcessions();

                        if (concessionService.readAllItems().length == 0) {
                            System.out.println("No concession items available.");
                            break;
                        }

                        int index = InputValidator.promptOneBasedIndex(
                                scanner,
                                "Select concession index: ",
                                concessionService.readAllItems().length
                        );

                        if (index == -1) {
                            break;
                        }

                        int stockToAdd = InputValidator.promptIntInRange(
                                scanner,
                                "Stock to add: ",
                                1,
                                Integer.MAX_VALUE
                        );

                        concessionService.addStock(index, stockToAdd);
                        System.out.println("Stock added successfully.");
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


}
