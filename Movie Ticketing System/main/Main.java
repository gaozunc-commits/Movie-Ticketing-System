package main;

import model.*;
import service.*;
import util.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final MovieService MOVIE_SERVICE = new MovieService();
    private static final ConcessionService CONCESSION_SERVICE = new ConcessionService();
    private static final UserService USER_SERVICE = new UserService();
    private static final BookingService BOOKING_SERVICE = new BookingService(MOVIE_SERVICE);
    private static final PaymentService PAYMENT_SERVICE = new PaymentService();
    private static final ReportService REPORT_SERVICE = new ReportService();

    public static void main(String[] args) {
        initializeSampleData();
        System.out.println("=== CINEMA TICKETING & CONCESSION SYSTEM ===");
        while (true) {
            try {
                User currentUser = login();
                if (currentUser instanceof Admin) {
                    adminMenu();
                } else if (currentUser instanceof Staff) {
                    staffMenu();
                } else if (currentUser instanceof Customer) {
                    customerMenu((Customer) currentUser);
                }
            } catch (Exception ex) {
                System.out.println("Login failed: " + ex.getMessage());
            }
        }
    }

    private static User login() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = SCANNER.nextLine();
        System.out.print("Password: ");
        String password = SCANNER.nextLine();
        User user = USER_SERVICE.authenticate(username, password);
        System.out.println("Welcome, " + user.getName() + " (" + user.getRole() + ")");
        return user;
    }

    private static void customerMenu(Customer customer) {
        while (true) {
            customer.displayMenu();
            System.out.println("4. Upcoming Trailers");
            int choice = readInt("Select: ");
            try {
                if (choice == 1) {
                    showMoviesAndShowtimes();
                } else if (choice == 2) {
                    placeOrder(customer);
                } else if (choice == 3) {
                    listOrders();
                } else if (choice == 4) {
                    showTrailers();
                } else if (choice == 0) {
                    break;
                } else {
                    System.out.println("Invalid menu option.");
                }
            } catch (Exception ex) {
                System.out.println("Customer flow error: " + ex.getMessage());
            }
        }
    }

    private static void staffMenu() {
        while (true) {
            System.out.println("\n--- STAFF DASHBOARD ---");
            System.out.println("1. Validate Ticket");
            System.out.println("2. Update Concession Stock");
            System.out.println("0. Logout");
            int choice = readInt("Select: ");
            try {
                if (choice == 1) {
                    String orderId = readText("Enter order ID: ");
                    boolean valid = BOOKING_SERVICE.validateTicket(orderId);
                    System.out.println(valid ? "Ticket VALID." : "Ticket INVALID.");
                } else if (choice == 2) {
                    listConcessions();
                    int index = readInt("Select concession index: ") - 1;
                    int stock = readInt("New stock: ");
                    CONCESSION_SERVICE.updateStock(index, stock);
                    System.out.println("Stock updated.");
                } else if (choice == 0) {
                    break;
                } else {
                    System.out.println("Invalid menu option.");
                }
            } catch (Exception ex) {
                System.out.println("Staff flow error: " + ex.getMessage());
            }
        }
    }

    private static void adminMenu() {
        while (true) {
        System.out.println("\n--- ADMIN DASHBOARD ---");
            System.out.println("1. Movie CRUD");
            System.out.println("2. Showtime CRUD");
            System.out.println("3. Concession CRUD");
            System.out.println("4. User CRUD");
            System.out.println("5. Reports");
            System.out.println("0. Logout");
            int choice = readInt("Select: ");
            try {
                if (choice == 1) {
                    movieCrudMenu();
                } else if (choice == 2) {
                    showtimeCrudMenu();
                } else if (choice == 3) {
                    concessionCrudMenu();
                } else if (choice == 4) {
                    userCrudMenu();
                } else if (choice == 5) {
                    REPORT_SERVICE.bestSellingMovies();
                    REPORT_SERVICE.peakHours();
                } else if (choice == 0) {
                    break;
                } else {
                    System.out.println("Invalid menu option.");
                }
            } catch (Exception ex) {
                System.out.println("Admin flow error: " + ex.getMessage());
            }
        }
    }

    private static void movieCrudMenu() {
        System.out.println("\n--- MOVIE CRUD ---");
        System.out.println("1. Create 2. Read 3. Update 4. Delete");
        int option = readInt("Select: ");
        if (option == 1) {
            MOVIE_SERVICE.createMovie(new Movie(readText("Title: "), readText("Genre: "), readInt("Duration: "), readText("Age Rating: ")));
        } else if (option == 2) {
            showMoviesAndShowtimes();
        } else if (option == 3) {
            listMovies();
            int index = readInt("Movie index: ") - 1;
            MOVIE_SERVICE.updateMovie(index, readText("New title: "), readText("New genre: "), readInt("New duration: "), readText("New age rating: "));
        } else if (option == 4) {
            listMovies();
            MOVIE_SERVICE.deleteMovie(readInt("Movie index: ") - 1);
        }
    }

    private static void showtimeCrudMenu() {
        System.out.println("\n--- SHOWTIME CRUD ---");
        System.out.println("1. Create 2. Read 3. Update 4. Delete");
        int option = readInt("Select: ");
        if (option == 1) {
            listMovies();
            Movie movie = MOVIE_SERVICE.readMovieByIndex(readInt("Movie index: ") - 1);
            int hallNo = readInt("Hall number: ");
            int rows = readInt("Rows: ");
            int cols = readInt("Cols: ");
            Hall hall = new Hall(hallNo, rows * cols, readText("Screen type: "), rows, cols);
            String time = readText("Time (HH:mm): ");
            Showtime showtime = new Showtime("ST-" + System.currentTimeMillis(), movie, hall, time);
            BOOKING_SERVICE.createShowtime(showtime);
        } else if (option == 2) {
            listShowtimes();
        } else if (option == 3) {
            listShowtimes();
            BOOKING_SERVICE.updateShowtimeTime(readInt("Showtime index: ") - 1, readText("New time: "));
        } else if (option == 4) {
            listShowtimes();
            BOOKING_SERVICE.deleteShowtime(readInt("Showtime index: ") - 1);
        }
    }

    private static void concessionCrudMenu() {
        System.out.println("\n--- CONCESSION CRUD ---");
        System.out.println("1. Create 2. Read 3. Update 4. Delete");
        int option = readInt("Select: ");
        if (option == 1) {
            CONCESSION_SERVICE.createItem(new ConcessionItem(readText("Name: "), readDouble("Price: "), readInt("Stock: ")));
        } else if (option == 2) {
            listConcessions();
        } else if (option == 3) {
            listConcessions();
            int index = readInt("Concession index: ") - 1;
            CONCESSION_SERVICE.updateItem(index, readText("New name: "), readDouble("New price: "), readInt("New stock: "));
        } else if (option == 4) {
            listConcessions();
            CONCESSION_SERVICE.deleteItem(readInt("Concession index: ") - 1);
        }
    }

    private static void userCrudMenu() {
        System.out.println("\n--- USER CRUD ---");
        System.out.println("1. Create 2. Read 3. Update 4. Delete");
        int option = readInt("Select: ");
        if (option == 1) {
            String role = readText("Role (ADMIN/STAFF/CUSTOMER): ").toUpperCase();
            String username = readText("Username: ");
            String password = readText("Password: ");
            String name = readText("Name: ");
            if ("ADMIN".equals(role)) {
                USER_SERVICE.createUser(new Admin(username, password, name));
            } else if ("STAFF".equals(role)) {
                USER_SERVICE.createUser(new Staff(username, password, name, readInt("Staff ID: ")));
            } else {
                USER_SERVICE.createUser(new Customer(username, password, name, true));
            }
        } else if (option == 2) {
            listUsers();
        } else if (option == 3) {
            listUsers();
            USER_SERVICE.updateUserName(readInt("User index: ") - 1, readText("New name: "));
        } else if (option == 4) {
            listUsers();
            USER_SERVICE.deleteUser(readInt("User index: ") - 1);
        }
    }

    private static void placeOrder(Customer customer) {
        listShowtimes();
        Showtime showtime = BOOKING_SERVICE.readShowtimeByIndex(readInt("Select showtime index: ") - 1);
        showtime.displaySeats();
        String seat = readText("Choose seat (e.g., A1): ").toUpperCase();
        InputValidator.validateSeatFormat(seat);
        if (!showtime.bookSeat(seat)) {
            System.out.println("Seat not available.");
            return;
        }

        Order order = BOOKING_SERVICE.createOrder(customer.getUsername(), showtime);
        Ticket ticket = BOOKING_SERVICE.createTicketForSeat(showtime, seat);
        order.addTicket(ticket);

        System.out.print("Add concession item? (y/n): ");
        if (SCANNER.nextLine().equalsIgnoreCase("y")) {
            listConcessions();
            int index = readInt("Concession index: ") - 1;
            int quantity = readInt("Quantity: ");
            ConcessionItem item = CONCESSION_SERVICE.readItemByIndex(index);
            CONCESSION_SERVICE.deductStock(index, quantity);
            order.addConcessionItem(item, quantity);
        }

        System.out.print("Apply coupon code? (leave blank to skip): ");
        String coupon = SCANNER.nextLine().trim();
        if (!coupon.isEmpty() && "SAVE10".equalsIgnoreCase(coupon)) {
            order.applyCoupon(coupon, 0.10);
        }
        if (customer.isLoyaltyMember()) {
            order.applyCoupon("LOYALTY5", 0.05);
        }

        PAYMENT_SERVICE.processPayment(readText("Payment method: "), order.getTotalPrice());
        customer.addLoyaltyPoints((int) Math.ceil(order.getTotalPrice()));
        BOOKING_SERVICE.persistOrder(order);
        System.out.println("Order complete. Order ID: " + order.getOrderId() + " | QR: " + order.getQrCode());
    }

    private static void listMovies() {
        List<Movie> movies = MOVIE_SERVICE.readAllMovies();
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i));
        }
    }

    private static void listShowtimes() {
        List<Showtime> showtimes = BOOKING_SERVICE.readAllShowtimes();
        for (int i = 0; i < showtimes.size(); i++) {
            Showtime s = showtimes.get(i);
            System.out.println((i + 1) + ". " + s.getMovie().getTitle() + " | Hall " + s.getHall().getHallNumber() + " | " + s.getTime());
        }
    }

    private static void listConcessions() {
        List<ConcessionItem> items = CONCESSION_SERVICE.readAllItems();
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i));
        }
    }

    private static void listUsers() {
        List<User> users = USER_SERVICE.readAllUsers();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.println((i + 1) + ". " + user.getUsername() + " | " + user.getRole() + " | " + user.getName());
        }
    }

    private static void listOrders() {
        List<Order> orders = BOOKING_SERVICE.readAllOrders();
        for (Order order : orders) {
            System.out.println(order.getOrderId() + " | Customer: " + order.getCustomerUsername() + " | Total: RM " + String.format("%.2f", order.getTotalPrice()));
        }
    }

    private static void showMoviesAndShowtimes() {
        System.out.println("\n--- NOW SHOWING ---");
        listMovies();
        System.out.println("\n--- SHOWTIMES ---");
        listShowtimes();
    }

    private static void showTrailers() {
        List<String> trailers = FileHandler.readFromFile("data/trailers.txt");
        System.out.println("\n--- UPCOMING TRAILERS ---");
        if (trailers.isEmpty()) {
            System.out.println("No trailers available.");
            return;
        }
        for (String trailer : trailers) {
            System.out.println("- " + trailer);
        }
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        return InputValidator.parseIntInRange(SCANNER.nextLine(), Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        return InputValidator.parsePositiveDouble(SCANNER.nextLine());
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return InputValidator.requireText(SCANNER.nextLine(), "Input");
    }

    private static void initializeSampleData() {
        if (MOVIE_SERVICE.readAllMovies().isEmpty()) {
            MOVIE_SERVICE.createMovie(new Movie("Avengers Reassembled", "Action", 132, "PG13"));
            MOVIE_SERVICE.createMovie(new Movie("Ocean Echo", "Sci-Fi", 118, "PG"));
        }
        if (CONCESSION_SERVICE.readAllItems().isEmpty()) {
            CONCESSION_SERVICE.createItem(new ConcessionItem("Popcorn", 8.5, 100));
            CONCESSION_SERVICE.createItem(new ConcessionItem("Cola", 5.0, 120));
            CONCESSION_SERVICE.createItem(new ConcessionItem("Combo Set", 12.0, 80));
        }
        if (BOOKING_SERVICE.readAllShowtimes().isEmpty()) {
            List<Movie> movies = MOVIE_SERVICE.readAllMovies();
            if (!movies.isEmpty()) {
                Hall hall1 = new Hall(1, 40, "IMAX", 5, 8);
                Hall hall2 = new Hall(2, 30, "Standard", 5, 6);
                BOOKING_SERVICE.createShowtime(new Showtime("ST-1001", movies.get(0), hall1, "14:00"));
                BOOKING_SERVICE.createShowtime(new Showtime("ST-1002", movies.get(0), hall2, "20:00"));
                if (movies.size() > 1) {
                    BOOKING_SERVICE.createShowtime(new Showtime("ST-1003", movies.get(1), hall2, "16:30"));
                }
            }
        }
    }
}