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
import java.time.LocalDate;
import java.time.LocalTime;
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
            int choice = readIntRange("Select: ", 0, 5);
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
        System.out.println("0. Back");
        System.out.println("============================================================");

        int option = readInt("Choose movie action: ");
        switch (option) {
            case 0:return;
            case 1:
    System.out.println("\n[Create Movie]");
    System.out.println("Enter movie details:");

    String title = readMovieTitle();
    String genre = readGenre();
    int duration = readDuration();
    String age = readAgeRating();
    if (age == null) return;

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
            case 2:
                System.out.println("\n[View Movies]");
                movieService.displayMovies();
                bookingService.displayShowtimes();
                break;

     case 3:
    System.out.println("\n[Update Movie]");

    movieService.displayMovies();
    if (movieService.readAllMovies().length == 0) {
        System.out.println("No movies available.");
        return;
    }

    int indexUpdate = chooseIndex("Movie index: ", movieService.readAllMovies().length);
    if (indexUpdate < 0) return;

    Movie temp = movieService.readMovieByIndex(indexUpdate);

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
            case 4:
                System.out.println("\n[Delete Movie]");
                movieService.displayMovies();
                if (movieService.readAllMovies().length == 0) {
                    System.out.println("No movies available.");
                    return;
                }
                int indexDelete = chooseIndex("Movie index: ", movieService.readAllMovies().length);
                if (indexDelete < 0) return;
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
                if (movieService.readAllMovies().length == 0) {
                    System.out.println("No movies available.");
                    return;
                }
                int movieIndex = chooseIndex("Movie index: ", movieService.readAllMovies().length);
                if (movieIndex < 0) return;

            Movie movie = movieService.readMovieByIndex(movieIndex);
              

                int hallNo = readInt("Hall number: ");
                int rows = readInt("Rows: ");
                int cols = readInt("Cols: ");

                Hall hall = new Hall(hallNo, rows * cols, readText("Screen type: "), rows, cols);


                String date = readDate();
                String time = readTime();

                Showtime showtime = new Showtime(
                        "ST-" + System.currentTimeMillis(),
                        movie,
                        hall,
                        date,
                        time
                );

                bookingService.createShowtime(showtime);
                System.out.println("Showtime created successfully.");
                break;

            case 2:
                bookingService.displayShowtimes();
                break;

            case 3:
                bookingService.displayShowtimes();
                if (bookingService.readAllShowtimes().length == 0) {
                    System.out.println("No showtimes available.");
                    return;
                }
                int updateIndex = chooseIndex("Showtime index: ", bookingService.readAllShowtimes().length);
                if (updateIndex < 0) return;
                String newTime = readText("New time: ");
                bookingService.updateShowtimeTime(updateIndex, newTime);
                System.out.println("Showtime updated successfully.");
                break;

            case 4:
                bookingService.displayShowtimes();
                if (bookingService.readAllShowtimes().length == 0) {
                    System.out.println("No showtimes available.");
                    return;
                }
                int deleteIndex = chooseIndex("Showtime index: ", bookingService.readAllShowtimes().length);
                if (deleteIndex < 0) return;
                bookingService.deleteShowtime(deleteIndex);
                System.out.println("Showtime deleted successfully.");
                break;

            default:
                System.out.println("Invalid option.");
        }
    }
private void reportMenu() {
    while (true) {
        System.out.println("\n--- REPORT MENU ---");
        System.out.println("1. Best Selling Movies");
        System.out.println("2. Peak Hours");
        System.out.println("3. Concession Report");
        System.out.println("0. Back");

        int choice = readInt("Choose: ");

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
            case 0:
                return;
            default:
                System.out.println("Invalid option.");
        }
    }
}
    private void concessionCrudMenu() {
        System.out.println("\n--- CONCESSION CRUD ---");
        System.out.println("1. Create 2. Read 3. Update 4. Delete");

        int option = readIntRange("Select: ", 0, 4);
        switch (option) {
            case 0:return;
           case 1:
    System.out.println("\n[Create Concession Item]");

    String name = readText("Name: ");
    double price = readDoubleRange("Price: ", 0, Double.MAX_VALUE);
    int stock = readIntRange("Stock: ", 0, Integer.MAX_VALUE);
    String category = readCategory();
    if (category == null) return;

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
                name = readText("New Name: ");
                break;

            case 2:
                price = readDoubleRange("New Price: ", 0, Double.MAX_VALUE);
                break;

            case 3:
                stock = readIntRange("New Stock: ", 0, Integer.MAX_VALUE);
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
            case 2:
                concessionService.displayConcessions();
                break;

            case 3:
    System.out.println("\n[Update Concession Item]");

    concessionService.displayConcessions();
    if (concessionService.readAllItems().length == 0) {
        System.out.println("No concession items available.");
        return;
    }

    int updateIndex = chooseIndex("Concession index: ", concessionService.readAllItems().length);
    if (updateIndex < 0) return;

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
                nameU = readText("New Name: ");
                break;

            case 2:
                priceU = readDoubleRange("New Price: ", 0, Double.MAX_VALUE);
                break;

            case 3:
                stockU = readIntRange("New Stock: ", 0, Integer.MAX_VALUE);
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
            case 4:
                concessionService.displayConcessions();
                if (concessionService.readAllItems().length == 0) {
                    System.out.println("No concession items available.");
                    return;
                }

                int deleteIndex = chooseIndex("Concession index: ", concessionService.readAllItems().length);
                if (deleteIndex < 0) return;

                concessionService.deleteItem(deleteIndex);
                System.out.println("Concession deleted successfully.");
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
                String role = readRole();
                String username;
                while (true) {
                    username = readText("Username: ");
                    if (userService.usernameExists(username)) {
                        System.out.println("Username already exists!");
                    } else break;
                }
                username = readText("Username: ");
                String password = readPassword();
                String name = readText("Name: ");
                
                switch (role) {
                case "ADMIN":
                    userService.createUser(new Admin(username, password, name));
                      System.out.println("Admin created successfully.");
                    break;

                case "STAFF":
                    int staffId = readInt("Staff ID: ");
                    userService.createUser(new Staff(username, password, name, staffId));
                      System.out.println("Staff created successfully.");
                    break;

                case "CUSTOMER":
                    
                    userService.createUser(new Customer(username, password, name));
                      System.out.println("Customer created successfully.");
                    break;

                default:
                    System.out.println("Invalid role.");
                    return;
            }
              
                break;

            case 2:
                System.out.println("\n[View Users]");
                userService.displayUsers();
                break;

            case 3:
                System.out.println("\n[Update User Name]");
                userService.displayUsers();
                if (userService.readAllUsers().length == 0) {
                    System.out.println("No users available.");
                    return;
                }
                int updateIndex = chooseIndex("User index: ", userService.readAllUsers().length);
                if (updateIndex < 0) return;
                userService.updateUserName(updateIndex, readText("New name: "));
                System.out.println("User updated successfully.");
                break;

            case 4:
                System.out.println("\n[Delete User]");
                userService.displayUsers();
                if (userService.readAllUsers().length == 0) {
                    System.out.println("No users available.");
                    return;
                }
                int deleteIndex = chooseIndex("User index: ", userService.readAllUsers().length);
                if (deleteIndex < 0) return;
                userService.deleteUser(deleteIndex);
                System.out.println("User deleted successfully.");
                break;
                
            default:
                System.out.println("Invalid option.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return InputValidator.parseIntInRange(scanner.nextLine(), Integer.MIN_VALUE, Integer.MAX_VALUE);
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }
    private int readIntRange(String prompt, int min, int max) {
    while (true) {
        int value = readInt(prompt);

        if (value < min || value > max) {
            System.out.println("Must be between " + min + " and " + max);
            continue;
        }

        return value;
    }
}
    private double readDoubleRange(String prompt, double min, double max) {
        while (true) {
            double value = readDouble(prompt);

            if (value < min || value > max) {
                System.out.println("Must be between " + min + " and " + max);
                continue;
            }

            return value;
        }
    }
    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return InputValidator.parsePositiveDouble(scanner.nextLine());
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
    private String readCategory() {
    while (true) {
        String category = readText("Category (FOOD/DRINK/SNACK): ").toUpperCase();

        if (category.equals("FOOD") ||
            category.equals("DRINK") ||
            category.equals("SNACK")) {
            return category;
        }

        System.out.println("Invalid category! Only FOOD / DRINK / SNACK allowed.");
    }
}
    private int chooseIndex(String prompt, int length) {
        while (true) {
            if (length <= 0) {
                System.out.println("No records available.");
                return -1;
            }

            int selected = readInt(prompt) - 1;
            if (selected >= 0 && selected < length) {
                return selected;
            }

            System.out.println("Index out of range. Try again.");
        }

        
    }
    private String readAgeRating() {
    while (true) {
        System.out.println("\nSelect Age Rating:");
        System.out.println("1. U (General)");
        System.out.println("2. P13 (Parental Guidance 13)");
        System.out.println("3. 18 (18+)");
        System.out.println("0. Cancel");

        int choice = readIntRange("Choose: ", 0, 3);

        switch (choice) {
            case 1: return "U";
            case 2: return "P13";
            case 3: return "18";
            case 0:
                System.out.println("Cancelled");
                return null;
            default:
                System.out.println("Invalid option");
        }
    }
}
private int readDuration() {
    return readIntRange("Duration (60–300 mins): ", 1, 300);
}
private String readMovieTitle() {
    while (true) {
        String title = readText("Title: ");

        if (title == null || title.trim().isEmpty()) {
            System.out.println("Title cannot be empty!");
            continue;
        }

        return title.trim();
    }
}
        
    

private String readGenre() {
    while (true) {
        String genre = readText("Genre: ");

        // not null / empty check
        if (genre == null || genre.trim().isEmpty()) {
            System.out.println("Genre cannot be empty!");
            continue;
        }

        // only letters + space allowed
        if (!genre.matches("[a-zA-Z ]+")) {
            System.out.println("Invalid genre! Only letters and space allowed.");
            continue;
        }

        return genre.trim();
    }
}   
private String readDate() {
    while (true) {
        try {
            System.out.print("Date (YYYY-MM-DD): ");
            String input = scanner.nextLine();

            LocalDate date = LocalDate.parse(input); 
            return date.toString();

        } catch (Exception e) {
            System.out.println("Invalid date format! Use YYYY-MM-DD.");
        }
    }
}
private String readTime() {
    while (true) {
        try {
            System.out.print("Time (HH:mm): ");
            String input = scanner.nextLine();

            LocalTime time = LocalTime.parse(input); 
            return time.toString();

        } catch (Exception e) {
            System.out.println("Invalid time format! Use HH:mm (24-hour).");
        }
    }
}
private String readRole() {
    while (true) {
        String role = readText("Role (ADMIN/STAFF/CUSTOMER): ").toUpperCase().trim();

        if (role.equals("ADMIN") ||
            role.equals("STAFF") ||
            role.equals("CUSTOMER")) {
            return role;
        }

        System.out.println("Invalid role! Only ADMIN / STAFF / CUSTOMER allowed.");
    }
}
private String readPassword() {
    while (true) {
        String pw = readText("Password (min 2 chars): ");
        if (pw.length() < 2) {
            System.out.println("Password too short!");
            continue;
        }
        return pw;
    }
}
}