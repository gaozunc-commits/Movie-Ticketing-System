package main;

import java.util.Scanner;

import service.BookingService;
import service.ConcessionService;
import service.MovieService;
import service.ReportService;
import service.UserService;

public class AdminMenuHandler {

    private final AdminCrud adminCrud;
    private final ReportService reportService;

    public AdminMenuHandler(
            Scanner scanner,
            MovieService movieService,
            ConcessionService concessionService,
            UserService userService,
            BookingService bookingService,
            ReportService reportService
    ) {
        this.adminCrud = new AdminCrud(scanner, movieService, bookingService, concessionService, userService);
        this.reportService = reportService;
    }

    public void run() {
        boolean keepAdminMenuOpen = true;
        do {
            System.out.println(this);
            int choice = adminCrud.readIntRange("Select: ", 0, 5);
            try {
                switch (choice) {
                    case 1:
                        movieCrudMenu();
                        break;
                    case 2:
                        showtimeCrudMenu();
                        break;
                    case 3:
                        concessionCrudMenu();
                        break;
                    case 4:
                        userCrudMenu();
                        break;
                    case 5:
                        reportMenu();
                        break;
                    case 0:
                        System.out.println("Logging out...");
                        keepAdminMenuOpen = false;
                        break;
                    default:
                        System.out.println("Invalid menu option.");
                }
            } catch (Exception e) {
                System.out.println("Admin flow error: " + e.getMessage());
            }
        } while (keepAdminMenuOpen);
    }

    @Override
    public String toString() {
        return "\n=== ADMIN DASHBOARD ===\n"
                + "1. Movie CRUD\n"
                + "2. Showtime CRUD\n"
                + "3. Concession CRUD\n"
                + "4. User CRUD\n"
                + "5. Reports\n"
                + "0. Logout";
    }

    // Submenus (presentation + routing only)

    private void movieCrudMenu() {
        while (true) {
            System.out.println("\n=== MOVIE CRUD ===");
            System.out.println("1. Create Movie");
            System.out.println("2. View Movies");
            System.out.println("3. Update Movie");
            System.out.println("4. Delete Movie");
            System.out.println("0. Back");

            int option = adminCrud.readIntRange("Choose movie action: ", 0, 4);
            if (option == 0) {
                return;
            }
            try {
                switch (option) {
                    case 1:
                        adminCrud.createMovieWithReview();
                        break;
                    case 2:
                        adminCrud.displayMoviesAndShowtimes();
                        break;
                    case 3:
                        adminCrud.updateMovieWithReview();
                        break;
                    case 4:
                        adminCrud.deleteMovieByIndexChoice();
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Movie CRUD error: " + e.getMessage());
            }
        }
    }

    private void showtimeCrudMenu() {
        while (true) {
            System.out.println("\n=== SHOWTIME CRUD ===");
            System.out.println("1. Create Showtime");
            System.out.println("2. View Showtimes");
            System.out.println("3. Update Showtime");
            System.out.println("4. Delete Showtime");
            System.out.println("0. Back");

            int option = adminCrud.readIntRange("Choose showtime action: ", 0, 4);
            if (option == 0) {
                return;
            }
            try {
                switch (option) {
                    case 1:
                        adminCrud.createShowtimeFromConsole();
                        break;
                    case 2:
                        adminCrud.displayShowtimesList();
                        break;
                    case 3:
                        adminCrud.updateShowtimeTimeFromConsole();
                        break;
                    case 4:
                        adminCrud.deleteShowtimeFromConsole();
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Showtime CRUD error: " + e.getMessage());
            }
        }
    }

    private void reportMenu() {
        while (true) {
            System.out.println("\n=== REPORTS ===");
            System.out.println("1. Best Selling Movies");
            System.out.println("2. Peak Hours");
            System.out.println("3. Concession Report");
            System.out.println("0. Back");

            int choice = adminCrud.readIntRange("Choose report action: ", 0, 3);
            if (choice == 0) {
                return;
            }
            try {
                switch (choice) {
                    case 1:
                        reportService.bestSellingMovies();
                        break;
                    case 2:
                        reportService.peakHours();
                        break;
                    case 3:
                        reportService.concessionReport();
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Report error: " + e.getMessage());
            }
        }
    }

    private void concessionCrudMenu() {
        while (true) {
            System.out.println("\n=== CONCESSION CRUD ===");
            System.out.println("1. Create Concession Item");
            System.out.println("2. View Concession Items");
            System.out.println("3. Update Concession Item");
            System.out.println("4. Delete Concession Item");
            System.out.println("0. Back");

            int option = adminCrud.readIntRange("Choose concession action: ", 0, 4);
            if (option == 0) {
                return;
            }
            try {
                switch (option) {
                    case 1:
                        adminCrud.createConcessionWithReview();
                        break;
                    case 2:
                        adminCrud.displayConcessionsList();
                        break;
                    case 3:
                        adminCrud.updateConcessionWithReview();
                        break;
                    case 4:
                        adminCrud.deleteConcessionByIndexChoice();
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Concession CRUD error: " + e.getMessage());
            }
        }
    }

    private void userCrudMenu() {
        while (true) {
            System.out.println("\n=== USER CRUD ===");
            System.out.println("1. Create User");
            System.out.println("2. View Users");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("0. Back");

            int option = adminCrud.readIntRange("Choose user action: ", 0, 4);
            if (option == 0) {
                return;
            }
            try {
                switch (option) {
                    case 1:
                        adminCrud.createUserFromConsole();
                        break;
                    case 2:
                        adminCrud.displayUsersCategorySubmenu();
                        break;
                    case 3:
                        adminCrud.updateUserWithReview();
                        break;
                    case 4:
                        adminCrud.deleteUserWithConfirmation();
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("User CRUD error: " + e.getMessage());
            }
        }
    }
}
