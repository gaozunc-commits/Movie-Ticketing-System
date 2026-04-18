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
        initializeSampleData();
        System.out.println("=== CINEMA TICKETING & CONCESSION SYSTEM ===");
        boolean keepApplicationRunning = true;
        do {
            try {
                User currentUser = login();
                if (currentUser instanceof Admin) {
                    ADMIN_MENU.run();
                } else if (currentUser instanceof Staff) {
                    STAFF_MENU.run();
                } else if (currentUser instanceof Customer) {
                    CUSTOMER_MENU.run((Customer) currentUser);
                }
            } catch (Exception e) {
                System.out.println("Login failed: " + e.getMessage());
            }
        } while (keepApplicationRunning);
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

   

   private static void initializeSampleData() {
    if (MOVIE_SERVICE.readAllMovies().length == 0) {
        MOVIE_SERVICE.createMovie(new Movie("Avengers Reassembled", "Action", 132, "PG13"));
        MOVIE_SERVICE.createMovie(new Movie("Ocean Echo", "Sci-Fi", 118, "PG"));
    }

    if (BOOKING_SERVICE.readAllShowtimes().length == 0) {
        Movie[] movies = MOVIE_SERVICE.readAllMovies();

        if (movies.length > 0) {
            Hall hall1 = new Hall(1, 40, "IMAX", 5, 8);
            Hall hall2 = new Hall(2, 30, "Standard", 5, 6);

            BOOKING_SERVICE.createShowtime(
                new Showtime(
                    "ST-1001",
                    movies[0],
                    hall1,
                    "2026-04-16",
                    "14:00"
                )
            );

            BOOKING_SERVICE.createShowtime(
                new Showtime(
                    "ST-1002",
                    movies[0],
                    hall2,
                    "2026-04-16",
                    "20:00"
                )
            );

            if (movies.length > 1) {
                BOOKING_SERVICE.createShowtime(
                    new Showtime(
                        "ST-1003",
                        movies[1],
                        hall2,
                        "2026-04-16",
                        "16:30"
                    )
                );
            }
        }
    }
}
    }
