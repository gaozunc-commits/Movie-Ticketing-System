package main;

import model.*;
import service.*;
import util.InputValidator;

import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final MovieService MOVIE_SERVICE = new MovieService();
    private static final ConcessionService CONCESSION_SERVICE = new ConcessionService();
    private static final UserService USER_SERVICE = new UserService();
    private static final BookingService BOOKING_SERVICE = new BookingService(MOVIE_SERVICE);
    private static final PaymentService PAYMENT_SERVICE = new PaymentService();
    private static final ReportService REPORT_SERVICE = new ReportService();

    private static final AdminMenuHandler ADMIN_MENU =
            new AdminMenuHandler(SCANNER, MOVIE_SERVICE, CONCESSION_SERVICE, USER_SERVICE, BOOKING_SERVICE, REPORT_SERVICE);

    private static final StaffMenuHandler STAFF_MENU =
            new StaffMenuHandler(SCANNER, BOOKING_SERVICE, CONCESSION_SERVICE);

    private static final CustomerMenuHandler CUSTOMER_MENU =
            new CustomerMenuHandler(SCANNER, MOVIE_SERVICE, CONCESSION_SERVICE, BOOKING_SERVICE, PAYMENT_SERVICE);

    public static void main(String[] args) {

        System.out.println("=== CINEMA TICKETING & CONCESSION SYSTEM ===");

        boolean running = true;

        while (running) {
            try {
                int choice = showMainMenu();

                if (choice == 0) {
                    System.out.println("Exiting system...");
                    running = false;
                    continue;
                }

                User currentUser = login();

                // for  null crash
                if (currentUser == null) {
                    continue;
                }

                // got a change 
                switch (currentUser.getRole().toUpperCase()) {
                    case "ADMIN" -> ADMIN_MENU.run();
                    case "STAFF" -> STAFF_MENU.run();
                    case "CUSTOMER" -> CUSTOMER_MENU.run((Customer) currentUser);
                    default -> System.out.println("Unknown role.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // login（with dismiss crash）
    private static User login() {
        System.out.println("\n--- LOGIN ---");

        System.out.print("Username: ");
        String username = SCANNER.nextLine();

        System.out.print("Password: ");
        String password = SCANNER.nextLine();

        User user = USER_SERVICE.authenticate(username, password);

        if (user == null) {
            System.out.println("Invalid username or password.");
            return null;
        }

        System.out.println("Welcome, " + user.getName() + " (" + user.getRole() + ")");
        return user;
    }

    //  validation
    private static int showMainMenu() {
        while (true) {
            try {
                System.out.println("\n=== CINEMA SYSTEM ===");
                System.out.println("1. Admin Login");
                System.out.println("2. Staff Login");
                System.out.println("3. Customer Login");
                System.out.println("0. Exit");
                System.out.print("Choose: ");

                return InputValidator.parseIntInRange(
                        SCANNER.nextLine(), 0, 3
                );

            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number between 0 and 3.");
            }
        }
    }
}