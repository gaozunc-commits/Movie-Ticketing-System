package main;

import model.*;
import service.*;

import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final MovieService MOVIE_SERVICE = new MovieService();
    private static final ConcessionService CONCESSION_SERVICE = new ConcessionService();
    private static final UserService USER_SERVICE = new UserService();
    private static final BookingService BOOKING_SERVICE = new BookingService(MOVIE_SERVICE);
    private static final PaymentService PAYMENT_SERVICE = new PaymentService();
    private static final ReportService REPORT_SERVICE = new ReportService();
    private static final AdminMenuHandler ADMIN_MENU = new AdminMenuHandler(SCANNER, MOVIE_SERVICE, CONCESSION_SERVICE, USER_SERVICE, BOOKING_SERVICE, REPORT_SERVICE);
    private static final StaffMenuHandler STAFF_MENU = new StaffMenuHandler(SCANNER, BOOKING_SERVICE, CONCESSION_SERVICE);
    private static final CustomerMenuHandler CUSTOMER_MENU = new CustomerMenuHandler(SCANNER, MOVIE_SERVICE, CONCESSION_SERVICE, BOOKING_SERVICE, PAYMENT_SERVICE);

    public static void main(String[] args) {
        System.out.println("=== CINEMA TICKETING & CONCESSION SYSTEM ===");
       do {
    try {
        int choice = showMainMenu();

        if (choice == 0) {
            System.out.println("Exiting system...");
            break;
        }

        User currentUser = login();

        switch (choice) {
            case 1:
                if (currentUser instanceof Admin) {
                    ADMIN_MENU.run();
                } else {
                    System.out.println("Access denied: Not an Admin");
                }
                break;
            case 2:
                if (currentUser instanceof Staff) {
                    STAFF_MENU.run();
                } else {
                    System.out.println("Access denied: Not Staff");
                }
                break;
            case 3:
                if (currentUser instanceof Customer) {
                    CUSTOMER_MENU.run((Customer) currentUser);
                } else {
                    System.out.println("Access denied: Not Customer");
                }
                break;
            default:
                System.out.println("Invalid option.");
        }

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
} while (true);
    }

    private static User login() throws ArrayIndexOutOfBoundsException {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = SCANNER.nextLine();
        System.out.print("Password: ");
        String password = SCANNER.nextLine();
        User user = USER_SERVICE.authenticate(username, password);
        System.out.println("Welcome, " + user.getName() + " (" + user.getRole() + ")");
        return user;
    }
private static int showMainMenu() {
    System.out.println("\n=== CINEMA SYSTEM ===");
    System.out.println("1. Admin Login");
    System.out.println("2. Staff Login");
    System.out.println("3. Customer Login");
    System.out.println("0. Exit");
    System.out.print("Choose: ");
    return Integer.parseInt(SCANNER.nextLine());
}
   

}
