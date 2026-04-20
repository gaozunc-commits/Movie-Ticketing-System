package main;

import java.util.Scanner;

import model.*;
import service.MovieService.Movie;
import service.*;

import util.InputValidator;

public final class AdminCrud {

    private final Scanner scanner;
    private final MovieService movieService;
    private final BookingService bookingService;
    private final ConcessionService concessionService;
    private final UserService userService;

    public AdminCrud(
            Scanner scanner,
            MovieService movieService,
            BookingService bookingService,
            ConcessionService concessionService,
            UserService userService
    ) {
        this.scanner = scanner;
        this.movieService = movieService;
        this.bookingService = bookingService;
        this.concessionService = concessionService;
        this.userService = userService;
    }

    // Movie CRUD

    public void createMovieWithReview() {
        System.out.println("\n=== CREATE MOVIE ===");
        System.out.println("Enter movie details (or type 'exit' to cancel):");
        
        String title = readMovieTitle();
        if (title == null) {
            System.out.println("Movie creation cancelled due to invalid title.");
            return;
        }
        String genre = readGenre();
        int duration = readDuration();
        String age = readAgeRating();
        if (age == null) {
            return;
        }

        while (true) {
            System.out.println("\n=== REVIEW MOVIE ===");
            System.out.println("Press Number to Edit");
            System.out.println("1. Title: " + title);
            System.out.println("2. Genre: " + genre);
            System.out.println("3. Duration: " + duration);
            System.out.println("4. Age Rating: " + age);
            System.out.println("5. Confirm & Save");
            System.out.println("0. Cancel");

            int edit = readIntRange("Select: ", 0, 5);

            switch (edit) {
                case 1:
                    title = readMovieTitle();
                    break;
                
                case 2:
                    genre = readGenre();
                    break;
                case 3:
                    duration = readDuration();
                    break;
                case 4:
                    age = readAgeRating();
                    break;
                case 5:
                    Movie movie = new Movie(title, genre, duration, age);
                    movieService.createMovie(movie);
                    System.out.println("Movie created successfully.");
                    return;
                case 0:
                    System.out.println("Cancelled.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /** Lists movies and showtimes (admin read path). */
    public void displayMoviesAndShowtimes() {
        System.out.println("\n=== VIEW MOVIES ===");
        movieService.displayMovies();
        bookingService.displayShowtimes();
    }

    public void updateMovieWithReview() {
        System.out.println("\n=== UPDATE MOVIE ===");

        movieService.displayMovies();
        if (movieService.readAllMovies().length == 0) {
            System.out.println("No movies available.");
            return;
        }

        int indexUpdate = chooseIndex("Movie index: ", movieService.readAllMovies().length);
        if (indexUpdate < 0) {
            return;
        }

        Movie temp = movieService.readMovieByIndex(indexUpdate);
            if (movieService.readAllMovies().length == 0) {
        System.out.println("No movies available.");
        return;
    }

        while (true) {
            System.out.println("\n=== REVIEW UPDATE MOVIE ===");
            System.out.println("1. Title: " + temp.getTitle());
            System.out.println("2. Genre: " + temp.getGenre());
            System.out.println("3. Duration: " + temp.getDuration());
            System.out.println("4. Age Rating: " + temp.getAgeRating());
            System.out.println("5. Confirm Update");
            System.out.println("0. Cancel");

            int edit = readIntRange("Select: ", 0, 5);

            switch (edit) {
                case 1:
                    temp.setTitle(readMovieTitle());
                    break;
                case 2:
                    temp.setGenre(readGenre());
                    break;
                case 3:
                    temp.setDuration(readDuration());
                    break;
                case 4:
                    temp.setAgeRating(readAgeRating());
                    break;
                case 5:
                    movieService.updateMovie(
                            indexUpdate,
                            temp.getTitle(),
                            temp.getGenre(),
                            temp.getDuration(),
                            temp.getAgeRating()
                    );
                    System.out.println("Movie updated successfully.");
                    return;

                case 0:
                    System.out.println("Cancelled.");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public void deleteMovieByIndexChoice() {
        System.out.println("\n=== DELETE MOVIE ===");
        movieService.displayMovies();
        if (movieService.readAllMovies().length == 0) {
            System.out.println("No movies available.");
            return;
        }
        int indexDelete = chooseIndex("Movie index: ", movieService.readAllMovies().length);
        if (indexDelete < 0) {
            return;
        }
        movieService.deleteMovie(indexDelete);
        System.out.println("Movie deleted successfully.");
    }

    // Showtime CRUD

    public void createShowtimeFromConsole() {

    System.out.println("\n=== CREATE SHOWTIME ===");

    movieService.displayMovies();

    if (movieService.readAllMovies().length == 0) {
        System.out.println("No movies available.");
        return;
    }

    int movieIndex = chooseIndex("Movie index: ", movieService.readAllMovies().length);
    if (movieIndex < 0) return;

    Movie movie = movieService.readMovieByIndex(movieIndex);

    String date = InputValidator.promptIsoDate(scanner, "Date (YYYY-MM-DD): ");

    bookingService.displayTimetableMatrix(date);

    int hallNo = readInt("Hall number: ");

    String startTime = InputValidator.promptTimeSlotHHmm(scanner, "Time (HH:mm): ");
    String endTime = InputValidator.calculateEndTime(startTime, movie.getDuration());

if (endTime.compareTo("20:00") > 0) {
    System.out.println("Movie exceeds cinema operating hours (10:00–20:00)");
    return;
}

    if (!bookingService.isHallAvailable(hallNo, date, startTime, endTime)) {
        System.out.println(" Time slot already occupied!");
        return;
    }

    Hall hall = new Hall(hallNo);

    Showtime showtime = new Showtime(
        "ST-" + System.currentTimeMillis(),
        movie,
        hall,
        date,
        startTime,
        endTime
    );

    bookingService.createShowtime(showtime);
        System.out.println("Showtime created successfully.");
}

    public void displayShowtimesList() {
        bookingService.displayShowtimes();
    }
    
    public void updateShowtimeTimeFromConsole() {
        System.out.println("\n=== UPDATE SHOWTIME ===");
        bookingService.displayShowtimes();
        if (bookingService.readAllShowtimes().length == 0) {
            System.out.println("No showtimes available.");
            return;
        }
        int updateIndex = chooseIndex("Showtime index: ", bookingService.readAllShowtimes().length);
        if (updateIndex < 0) {
            return;
        }
        String startTime = InputValidator.promptTimeHHmm(scanner, "Start Time (HH:mm): ");

        String newEnd = InputValidator.calculateEndTime(
        startTime,
        bookingService.readShowtimeByIndex(updateIndex).getMovie().getDuration()
);
    bookingService.updateShowtimeTime(updateIndex, startTime, newEnd);
        System.out.println("Showtime updated successfully.");
    }

    public void deleteShowtimeFromConsole() {
        System.out.println("\n=== DELETE SHOWTIME ===");
        bookingService.displayShowtimes();
        if (bookingService.readAllShowtimes().length == 0) {
            System.out.println("No showtimes available.");
            return;
        }
        int deleteIndex = chooseIndex("Showtime index: ", bookingService.readAllShowtimes().length);
        if (deleteIndex < 0) {
            return;
        }
        bookingService.deleteShowtime(deleteIndex);
        System.out.println("Showtime deleted successfully.");
    }

    // Concession CRUD

    public void createConcessionWithReview() {
        System.out.println("\n=== CREATE CONCESSION ===");

        String name = InputValidator.promptText(scanner, "Name: ");
        double price = InputValidator.promptPrice2Decimal(scanner, "Price: ");
        int stock = InputValidator.promptIntInRange(scanner, "Stock: ", 0, 1000);
        String category = readCategory();

        while (true) {
            System.out.println("\n=== REVIEW CONCESSION ITEM ===");
            System.out.println("Press Number to Edit");
            System.out.println("1. Name: " + name);
            System.out.println("2. Price: " + price);
            System.out.println("3. Stock: " + stock);
            System.out.println("4. Category: " + category);
            System.out.println("5. Confirm & Save");
            System.out.println("0. Cancel");

            int edit = readIntRange("Select: ", 0, 5);

            switch (edit) {
                case 1:
                    name = InputValidator.promptText(scanner, "New Name: ");
                    break;

                case 2:
                    price = InputValidator.promptPrice2Decimal(scanner, "New Price: ");
                    break;

                case 3:
                    stock = InputValidator.promptIntInRange(scanner, "New Stock: ", 0, 1000);
                    break;

                case 4:
                    category = readCategory();
                    break;

                case 5:
                    concessionService.createItem(
                            new ConcessionItem(name, price, stock, category)
                    );
                    System.out.println("Concession item created successfully.");
                    return;

                case 0:
                    System.out.println("Cancelled.");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public void displayConcessionsList() {
        concessionService.displayConcessions();
    }

    public void updateConcessionWithReview() {
        System.out.println("\n=== UPDATE CONCESSION ===");

        concessionService.displayConcessions();
        if (concessionService.readAllItems().length == 0) {
            System.out.println("No concession items available.");
            return;
        }

        int updateIndex = chooseIndex("Concession index: ", concessionService.readAllItems().length);
        if (updateIndex < 0) {
            return;
        }

        ConcessionItem temp = concessionService.readAllItems()[updateIndex];

        String nameU = temp.getName();
        double priceU = temp.getPrice();
        int stockU = temp.getStock();
        String categoryU = temp.getCategory();

        while (true) {
            System.out.println("\n=== REVIEW UPDATE CONCESSION ===");
            System.out.println("Press Number to Edit");
            System.out.println("1. Name: " + nameU);
            System.out.println("2. Price: " + priceU);
            System.out.println("3. Stock: " + stockU);
            System.out.println("4. Category: " + categoryU);
            System.out.println("5. Confirm Update");
            System.out.println("0. Cancel");

            int edit = readIntRange("Select: ", 0, 5);

            switch (edit) {
                case 1:
                    nameU = InputValidator.promptText(scanner, "New Name: ");
                    break;

                case 2:
                    priceU = InputValidator.promptPrice2Decimal(scanner, "New Price: ");
                    break;

                case 3:
                    stockU = InputValidator.promptIntInRange(scanner, "New Stock: ", 0, 1000);
                    break;

                case 4:
                    categoryU = readCategory();
                    break;

                case 5:
                    concessionService.updateItem(updateIndex, nameU, priceU, stockU, categoryU);
                    System.out.println("Concession updated successfully.");
                    return;

                case 0:
                    System.out.println("Cancelled.");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public void deleteConcessionByIndexChoice() {
        System.out.println("\n=== DELETE CONCESSION ===");
        concessionService.displayConcessions();
        if (concessionService.readAllItems().length == 0) {
            System.out.println("No concession items available.");
            return;
        }

        int deleteIndex = chooseIndex("Concession index: ", concessionService.readAllItems().length);
        if (deleteIndex < 0) {
            return;
        }

        concessionService.deleteItem(deleteIndex);
        System.out.println("Concession deleted successfully.");
    }

    // User CRUD

    public void createUserFromConsole() {
        System.out.println("\n=== CREATE USER ===");
        String role = readRole();
        String username;
        while (true) {
            username = InputValidator.promptText(scanner, "Username: ");
            if (userService.usernameExists(username)) {
                System.out.println("Username already exists!");
            } else {
                break;
            }
        }
        String password = readPassword();
        String name = InputValidator.promptText(scanner, "Name: ");

        switch (role) {
            case "ADMIN":
                userService.createUser(new AdminMenuHandler.AdminUser(username, password, name));
                System.out.println("Admin created successfully.");
                break;

            case "STAFF":
                int staffId = readInt("Staff ID: ");
                userService.createUser(new StaffMenuHandler.Staff(username, password, name, staffId));
                System.out.println("Staff created successfully.");
                break;

            case "CUSTOMER":
                userService.createUser(new CustomerMenuHandler.Customer(username, password, name));
                System.out.println("Customer created successfully.");
                break;

            default:
                System.out.println("Invalid role.");
        }
    }

    /** Inner menu: filter by role; 0 = back. */
    public void displayUsersCategorySubmenu() {
        System.out.println("\n=== VIEW USERS ===");
        while (true) {
            System.out.println("\n=== USER VIEW ===");
            System.out.println("1. Admins");
            System.out.println("2. Staff");
            System.out.println("3. Customers");
            System.out.println("4. All Users");
            System.out.println("0. Back");

            int choice = readIntRange("Select: ", 0, 4);
            if (choice == 0) {
                break;
            }
            System.out.println();
            userService.displayUsersByCategory(choice);
        }
    }

    public void updateUserWithReview() {
        System.out.println("\n=== UPDATE USER ===");
        userService.displayUsersByCategory(4);

        if (userService.readAllUsers().length == 0) {
            System.out.println("No users available.");
            return;
        }

        int index = selectUserIndexFromList();
        if (index < 0) {
            return;
        }

        User user = userService.readUserByIndex(index);

        while (true) {
            System.out.println("\n=== EDIT USER ===");
            System.out.println("1. Name: " + user.getName());
            System.out.println("2. Password: " + user.getPassword());
            System.out.println("3. Role: " + getRoleLabel(user));
            System.out.println("0. Confirm & Save");

            int edit = readIntRange("Select: ", 0, 3);

            switch (edit) {
                case 1:
                    user.setName(InputValidator.promptText(scanner, "New name: "));
                    break;

                case 2:
                    user.setPassword(readPassword());
                    break;

                case 3:
                    changeUserRole(index, user);
                    break;

                case 0:
                    userService.replaceUser(index, user);
                    System.out.println("User updated successfully.");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public void deleteUserWithConfirmation() {
        System.out.println("\n=== DELETE USER ===");

        int deleteIndex = selectUserIndexFromList();
        if (deleteIndex < 0) {
            return;
        }

        System.out.print("Confirm delete? (Y/N): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Cancelled.");
            return;
        }

        userService.deleteUser(deleteIndex);
        System.out.println("User deleted successfully.");
    }
    
    public int readInt(String prompt) {
        return InputValidator.promptInt(scanner, prompt);
    }

    public int readIntRange(String prompt, int min, int max) {
        return InputValidator.promptIntInRange(scanner, prompt, min, max);
    }

    public double readDoubleRange(String prompt, double min, double max) {
        return InputValidator.promptDoubleInRange(scanner, prompt, min, max);
    }

    public String readText(String prompt) {
        return InputValidator.promptText(scanner, prompt);
    }

    private String readCategory() {
        return InputValidator.promptConcessionCategory(scanner, "Category (FOOD/DRINK/SNACK): ");
    }

    private int chooseIndex(String prompt, int length) {
        return InputValidator.promptOneBasedIndex(scanner, prompt, length);
    }

    private String readAgeRating() {
        return InputValidator.promptMovieAgeRating(scanner);
    }

    private int readDuration() {
        return InputValidator.promptIntInRange(scanner, "Duration (1–300 mins): ", 1, 300);
    }

    private String readMovieTitle() {
        return InputValidator.promptMovieTitle(scanner, "Title: ");
    }

    private String readGenre() {
        return InputValidator.promptGenre(scanner, "Genre: ");
    }

    private String readRole() {
        return InputValidator.promptUserRole(scanner, "Role (ADMIN/STAFF/CUSTOMER): ");
    }

    private String readPassword() {
        return InputValidator.promptPassword(scanner, "Password: ");
    }

    private int selectUserIndexFromList() {
        System.out.println("\n=== SELECT USER TYPE ===");
        System.out.println("1. Admin");
        System.out.println("2. Staff");
        System.out.println("3. Customer");
        System.out.println("4. All");

        int type = InputValidator.promptIntInRange(scanner, "Select: ", 1, 4);

        int[] map = userService.getFilteredIndexes(type);

        if (map.length == 0) {
            System.out.println("No users.");
            return -1;
        }

        for (int i = 0; i < map.length; i++) {
            User listedUser = userService.readUserByIndex(map[i]);
            System.out.println((i + 1) + ". " + listedUser.getUsername());
        }

        int selected = InputValidator.promptIntInRange(scanner, "Select: ", 1, map.length);

        return map[selected - 1];
    }

    private static String getRoleLabel(User user) {
        if (user instanceof AdminMenuHandler.AdminUser) {
            return "ADMIN";
        }
        if (user instanceof StaffMenuHandler.Staff) {
            return "STAFF";
        }
        if (user instanceof CustomerMenuHandler.Customer) {
            return "CUSTOMER";
        }
        return "UNKNOWN";
    }

    private void changeUserRole(int index, User oldUser) {

        System.out.println("\n=== SELECT NEW ROLE ===");
        System.out.println("1. Admin");
        System.out.println("2. Staff");
        System.out.println("3. Customer");

        int choice = InputValidator.promptIntInRange(scanner, "Select: ", 1, 3);

        User newUser;

        String username = oldUser.getUsername();
        String password = oldUser.getPassword();
        String name = oldUser.getName();

        switch (choice) {
            case 1:
                newUser = new AdminMenuHandler.AdminUser(username, password, name);
                break;

            case 2:
                int staffId = InputValidator.promptInt(scanner, "Staff ID: ");
                newUser = new StaffMenuHandler.Staff(username, password, name, staffId);
                break;

            case 3:
                newUser = new CustomerMenuHandler.Customer(username, password, name);
                break;

            default:
                return;
        }

        userService.replaceUser(index, newUser);
    }
}
