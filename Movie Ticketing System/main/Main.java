package main;

import java.util.Scanner;
import model.*;
import service.*;
import util.InputValidator;

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

        System.out.println("#######################################################");
        System.out.println("#         _____ _                                     #");
        System.out.println("#        / ____(_)                                    #");
        System.out.println("#       | |     _ _ __   ___ _ __ ___   __ _          #");
        System.out.println("#       | |    | | '_ \\ / _ \\ '_ ` _ \\ / _` |         #");
        System.out.println("#       | |____| | | | |  __/ | | | | | (_| |         #");
        System.out.println("#        \\_____|_|_| |_|\\___|_| |_| |_|\\__,_|         #");
        System.out.println("#                                                     #");
        System.out.println("#               CINEMA TICKETING SYSTEM               #");
        System.out.println("#                                                     #");
        System.out.println("#     Now Showing       Book Tickets      Trailers    #");
        System.out.println("#                                                     #");
        System.out.println("#######################################################\n");

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
                    case "ADMIN":
                        ADMIN_MENU.run();
                        break;

                    case "STAFF":
                        STAFF_MENU.run((Staff) currentUser);
                        break;

                    case "CUSTOMER":
                        CUSTOMER_MENU.run((Customer) currentUser);
                        break;

                    default:
                        System.out.println("Unknown role.");
                        break;
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // login（with dismiss crash）
    private static User login() {
        System.out.println("\n=== LOGIN ===");

        String username = InputValidator.promptText(SCANNER, "Username: ");
        String password = InputValidator.promptText(SCANNER, "Password: ");

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
        System.out.println("\n=== CINEMA SYSTEM ===");
        System.out.println("1. Admin Login");
        System.out.println("2. Staff Login");
        System.out.println("3. Customer Login");
        System.out.println("0. Exit");
        return InputValidator.promptIntInRange(SCANNER, "Choose: ", 0, 3);
    }
}