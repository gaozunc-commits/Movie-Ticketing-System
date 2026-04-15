package main;

import model.Admin;
import model.ConcessionItem;
import model.Customer;
import model.Hall;
import model.Movie;
import model.Showtime;
import model.Staff;
import service.BookingService;
import service.ConcessionService;
import service.MovieService;
import service.ReportService;
import service.UserService;
import util.InputValidator;

import java.util.Scanner;

public class AdminMenuHandler {
    private final Scanner scanner;
    private final MovieService movieService;
    private final ConcessionService concessionService;
    private final UserService userService;
    private final BookingService bookingService;
    private final ReportService reportService;

    public AdminMenuHandler(
        Scanner scanner,
        MovieService movieService,
        ConcessionService concessionService,
        UserService userService,
        BookingService bookingService,
        ReportService reportService
    ) {
        this.scanner = scanner;
        this.movieService = movieService;
        this.concessionService = concessionService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.reportService = reportService;
    }

    public void run() {
        boolean keepAdminMenuOpen = true;
        do {
            System.out.println(this);
            int choice = readInt("Select: ");
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
                        reportService.bestSellingMovies();
                        reportService.peakHours();
                        reportService.concessionReport();
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

    public String toString() {
        return "\n--- ADMIN DASHBOARD ---\n"
            + "1. Movie CRUD\n"
            + "2. Showtime CRUD\n"
            + "3. Concession CRUD\n"
            + "4. User CRUD\n"
            + "5. Reports\n"
            + "0. Logout";
    }

    private void movieCrudMenu() {
        System.out.println("\n======================== MOVIE CRUD ========================");
        System.out.println("1. Create Movie");
        System.out.println("2. View Movies");
        System.out.println("3. Update Movie");
        System.out.println("4. Delete Movie");
        System.out.println("============================================================");

        int option = readInt("Choose movie action: ");
        switch (option) {
            case 1:
                System.out.println("\n[Create Movie]");
                movieService.createMovie(
                    new Movie(
                        readText("Title: "),
                        readText("Genre: "),
                        readInt("Duration: "),
                        readText("Age Rating: ")
                    )
                );
                System.out.println("Movie created successfully.");
                break;
            case 2:
                System.out.println("\n[View Movies]");
                movieService.displayMovies();
                bookingService.displayShowtimes();
                break;
            case 3:
                System.out.println("\n[Update Movie]");
                movieService.displayMovies();
                int indexUpdate = chooseIndex("Movie index: ", movieService.readAllMovies().length);
                movieService.updateMovie(
                    indexUpdate,
                    readText("New title: "),
                    readText("New genre: "),
                    readInt("New duration: "),
                    readText("New age rating: ")
                );
                System.out.println("Movie updated successfully.");
                break;
            case 4:
                System.out.println("\n[Delete Movie]");
                movieService.displayMovies();
                int indexDelete = chooseIndex("Movie index: ", movieService.readAllMovies().length);
                movieService.deleteMovie(indexDelete);
                System.out.println("Movie deleted successfully.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void showtimeCrudMenu() {
        System.out.println("\n--- SHOWTIME CRUD ---");
        System.out.println("1. Create 2. Read 3. Update 4. Delete");

        int option = readInt("Select: ");
        switch (option) {
            case 1:
                movieService.displayMovies();
                Movie movie = movieService.readMovieByIndex(chooseIndex("Movie index: ", movieService.readAllMovies().length));
                int hallNo = readInt("Hall number: ");
                int rows = readInt("Rows: ");
                int cols = readInt("Cols: ");
                Hall hall = new Hall(hallNo, rows * cols, readText("Screen type: "), rows, cols);
                String time = readText("Time (HH:mm): ");
                Showtime showtime = new Showtime("ST-" + System.currentTimeMillis(), movie, hall, time);
                bookingService.createShowtime(showtime);
                break;
            case 2:
                bookingService.displayShowtimes();
                break;
            case 3:
                bookingService.displayShowtimes();
                int updateIndex = chooseIndex("Showtime index: ", bookingService.readAllShowtimes().length);
                String newTime = readText("New time: ");
                bookingService.updateShowtimeTime(updateIndex, newTime);
                break;
            case 4:
                bookingService.displayShowtimes();
                int deleteIndex = chooseIndex("Showtime index: ", bookingService.readAllShowtimes().length);
                bookingService.deleteShowtime(deleteIndex);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void concessionCrudMenu() {
        System.out.println("\n--- CONCESSION CRUD ---");
        System.out.println("1. Create 2. Read 3. Update 4. Delete");

        int option = readInt("Select: ");
        switch (option) {
            case 1:
    System.out.println("\n[Create Concession Item]");

    concessionService.createItem(
        new ConcessionItem(
            readText("Name: "),
            readDouble("Price: "),
            readInt("Stock: "),
            readText("Category (FOOD/DRINK/SNACK): ").toUpperCase()
        )
    );
    System.out.println("Concession item created successfully.");
    break;
            case 2:
                concessionService.displayConcessions();
                break;
            case 3:
    concessionService.displayConcessions();
    int updateIndex = chooseIndex("Concession index: ", concessionService.readAllItems().length);

    concessionService.updateItem(
        updateIndex,
        readText("New name: "),
        readDouble("New price: "),
        readInt("New stock: "),
        readText("New category (FOOD/DRINK/SNACK): ").toUpperCase()
    );

    System.out.println("Concession updated successfully.");
    
                break;
            case 4:
                concessionService.displayConcessions();
                int deleteIndex = chooseIndex("Concession index: ", concessionService.readAllItems().length);
                concessionService.deleteItem(deleteIndex);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void userCrudMenu() {
        System.out.println("\n--- USER CRUD ---");
        System.out.println("1. Create User");
        System.out.println("2. View Users");
        System.out.println("3. Update User Name");
        System.out.println("4. Delete User\n");

        int option = readInt("Choose user action: ");
        switch (option) {
            case 1:
                System.out.println("\n[Create User]");
                String role = readText("Role (ADMIN/STAFF/CUSTOMER): ").toUpperCase();
                String username = readText("Username: ");
                String password = readText("Password: ");
                String name = readText("Name: ");

                switch (role) {
                    case "ADMIN":
                        userService.createUser(new Admin(username, password, name));
                        break;
                    case "STAFF":
                        int staffId = readInt("Staff ID: ");
                        userService.createUser(new Staff(username, password, name, staffId));
                        break;
                    default:
                        userService.createUser(new Customer(username, password, name, true));
                }
                System.out.println("User created successfully.");
                break;
            case 2:
                System.out.println("\n[View Users]");
                userService.displayUsers();
                break;
            case 3:
                System.out.println("\n[Update User Name]");
                userService.displayUsers();
                int updateIndex = chooseIndex("User index: ", userService.readAllUsers().length);
                userService.updateUserName(updateIndex, readText("New name: "));
                System.out.println("User updated successfully.");
                break;
            case 4:
                System.out.println("\n[Delete User]");
                userService.displayUsers();
                int deleteIndex = chooseIndex("User index: ", userService.readAllUsers().length);
                userService.deleteUser(deleteIndex);
                System.out.println("User deleted successfully.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private int readInt(String prompt) {
        boolean validIntegerReceived = false;
        int parsedValue = 0;
        do {
            try {
                System.out.print(prompt);
                parsedValue = InputValidator.parseIntInRange(scanner.nextLine(), Integer.MIN_VALUE, Integer.MAX_VALUE);
                validIntegerReceived = true;
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        } while (!validIntegerReceived);
        return parsedValue;
    }

    private double readDouble(String prompt) {
        boolean validDoubleReceived = false;
        double parsedValue = 0.0;
        do {
            try {
                System.out.print(prompt);
                parsedValue = InputValidator.parsePositiveDouble(scanner.nextLine());
                validDoubleReceived = true;
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        } while (!validDoubleReceived);
        return parsedValue;
    }

    private String readText(String prompt) {
        boolean validTextReceived = false;
        String parsedText = "";
        do {
            try {
                System.out.print(prompt);
                parsedText = InputValidator.requireText(scanner.nextLine(), "Input");
                validTextReceived = true;
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        } while (!validTextReceived);
        return parsedText;
    }

    private int chooseIndex(String prompt, int length) throws ArrayIndexOutOfBoundsException {
        if (length <= 0) {
            throw new ArrayIndexOutOfBoundsException("No records available.");
        }
        int selected = readInt(prompt) - 1;
        if (selected < 0 || selected >= length) {
            throw new ArrayIndexOutOfBoundsException("Index out of range.");
        }
        return selected;
    }
}
