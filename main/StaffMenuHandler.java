package main;

import service.BookingService;
import service.ConcessionService;
import util.InputValidator;

import java.util.Scanner;

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

    public void run() {
        boolean keepStaffMenuOpen = true;

        while (keepStaffMenuOpen) {
            System.out.println(this);
            int choice = readInt("Select: ");

            try {
                switch (choice) {

                    case 1:
                        System.out.println("\n[Validate Ticket]");
                        String orderId = readText("Enter order ID: ");

                        boolean valid = bookingService.validateTicket(orderId);

                        if (valid) {
                            System.out.println("Ticket VALID.");
                        } else {
                            System.out.println("Ticket INVALID.");
                        }
                        break;

                    case 2:
                        System.out.println("\n[Update Concession Stock]");
                        concessionService.displayConcessions();

                        // empty check
                        if (concessionService.readAllItems().length == 0) {
                            System.out.println("No concession items available.");
                            break;
                        }

                        int index = chooseIndex(
                                "Select concession index: ",
                                concessionService.readAllItems().length
                        );

                        if (index == -1) break;

                        int stockToAdd = readInt("Stock to add: ");

                        // avoid negatif stock
                        if (stockToAdd <= 0) {
                            System.out.println("Stock must be positive.");
                            break;
                        }

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

    @Override
    public String toString() {
        return "\n--- STAFF DASHBOARD ---\n"
                + "1. Validate Ticket\n"
                + "2. Update Concession Stock\n"
                + "0. Logout";
    }

    // save InputValidator
    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return InputValidator.parseIntInRange(
                        scanner.nextLine(),
                        Integer.MIN_VALUE,
                        Integer.MAX_VALUE
                );
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }

    private String readText(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return InputValidator.requireText(scanner.nextLine(), "Input");
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }

    // change throw to loop
    private int chooseIndex(String prompt, int length) {
        if (length <= 0) {
            System.out.println("No records available.");
            return -1;
        }

        while (true) {
            int selected = readInt(prompt) - 1;

            if (selected >= 0 && selected < length) {
                return selected;
            }

            System.out.println("Index out of range. Try again.");
        }
    }
}