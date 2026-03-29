package main;

import model.*;
import service.*;
import util.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static MovieManager movieManager = new MovieManager();
    private static InventoryManager inventoryManager = new InventoryManager();
    private static ReportManager reportManager = new ReportManager();
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("=== WELCOME TO THE CINEMA MANAGEMENT SYSTEM ===");
        loginMenu();

        if (currentUser != null) {
            int choice; // Declare outside so the 'while' condition can access it
            
            do {
                displayMainMenu();
                try {
                    System.out.print("\nSelect Option (0 to Exit): ");
                    choice = Integer.parseInt(scanner.nextLine());

                    if (choice == 1) {
                        movieManager.displayMovies();
                    } else if (choice == 2) {
                        handleBooking();
                    } else if (choice == 3) {
                        handleConcessions(); // Moved logic to a method for cleanliness
                    } else if (choice == 4) {
                        handleStaffActions();
                    } else if (choice == 0) {
                        System.out.println("Exiting... Data saved.");
                    } else {
                        System.out.println("Invalid option. Try again.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("⚠️ ERROR: Please enter a valid number.");
                    choice = -1; // Set to a non-zero value so the loop continues after an error
                }
                
            } while (choice != 0); // The loop continues as long as choice is NOT 0
        }
        
    }

    private static void loginMenu() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        // Hardcoded for demo, but Task 4 person should link this to FileHandler
        if (user.equals("admin") && pass.equals("123")) {
            currentUser = new Admin(user, pass);
            System.out.println("Logged in as ADMIN.");
        } else {
            currentUser = new Staff(user, pass, 101);
            System.out.println("Logged in as STAFF.");
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. View Now Showing Movies");
        System.out.println("2. Book a Ticket (Customer Flow)");
        System.out.println("3. Concession Management");
        System.out.println("4. Staff/Admin Dashboard");
        System.out.println("0. Exit");
    }

    private static void handleBooking() {
        System.out.println("\n--- STARTING BOOKING ---");
        movieManager.displayMovies();
        
        try {
            System.out.print("Select Movie Number: ");
            int movieIdx = Integer.parseInt(scanner.nextLine()) - 1;
            Movie selectedMovie = movieManager.getMovie(movieIdx);

            if (selectedMovie == null) {
                System.out.println("❌ Invalid movie selection!");
                return;
            }

            // 1. VISUAL REPRESENTATION: Show the 2D Map
            selectedMovie.getHall().displaySeatMap();

            // 2. TICKET & SEAT SELECTION
            System.out.print("Select Ticket Type (1. Standard / 2. VIP): ");
            int type = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Seat Number (e.g., A1): ");
            String seatInput = scanner.nextLine().toUpperCase();

            // 3. SEAT LOGIC: Check if seat is available and mark it 'X'
            // Note: You must add the bookSeat method to Hall.java first!
            if (!selectedMovie.getHall().bookSeat(seatInput)) {
                return; // Exit if seat is taken or invalid
            }

            Booking newBooking = new Booking("BK-" + System.currentTimeMillis(), selectedMovie);

            if (type == 2) {
                newBooking.addTicket(new VIPTicket(seatInput));
            } else {
                newBooking.addTicket(new StandardTicket(seatInput));
            }

            // 4. DYNAMIC CONCESSIONS
            System.out.print("Would you like to see the Snack Menu? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                inventoryManager.displaySnackMenu(); 
                System.out.print("Select Snack Number: ");
                int snackChoice = Integer.parseInt(scanner.nextLine()) - 1;
                System.out.print("How many? ");
                int qty = Integer.parseInt(scanner.nextLine());

                ConcessionItem itemFromMenu = inventoryManager.getItem(snackChoice, qty);
                if (itemFromMenu != null) {
                    newBooking.addConcession(itemFromMenu);
                    System.out.println("✅ Added to your order!");
                }
            }

            // 5. PAYMENT & RECEIPT
            System.out.println("\n--- PAYMENT ---");
            double amount = newBooking.getTotalAmount();
            System.out.println("Total Amount Due: RM " + amount);
            System.out.print("Enter Payment Method (Credit Card/E-Wallet): ");
            String method = scanner.nextLine();

            Payment payment = new Payment("PAY-" + System.currentTimeMillis(), amount, method);
            payment.processPayment();

            newBooking.printReceipt();

            // 6. SAVE DATA: Using the real seat input instead of the word "Seat"
            String bookingData = newBooking.getBookingID() + "|" + 
                                selectedMovie.getTitle() + "|" + 
                                seatInput + "|" + 
                                amount;

            FileHandler.saveToFile("data/bookings.txt", Arrays.asList(bookingData));

        } catch (Exception e) {
            System.out.println("⚠️ Booking failed: " + e.getMessage());
        } 
    }

    private static void handleStaffActions() {
        if (currentUser instanceof Admin) {
        System.out.println("\n--- ADMIN DASHBOARD ---");
        System.out.println("1. Generate Financial Report");
        System.out.println("2. Add New Movie");
        System.out.print("Select: ");
        
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 1) {
            reportManager.generateFinancialReport();
        } else if (choice == 2) {
            // Logic to add a movie if you want to implement it
            System.out.println("Feature coming soon...");
        }
        } else if (currentUser instanceof Staff) {
            System.out.println("\n--- STAFF DASHBOARD ---");
            System.out.println("1. Validate Ticket (Check ID)");
            System.out.print("Select: ");
            
            if (scanner.nextLine().equals("1")) {
                System.out.print("Enter Booking ID to verify: ");
                String id = scanner.nextLine();
                // This checks if the ID exists in your bookings.txt
                boolean isValid = reportManager.checkBookingExists(id);
                if (isValid) {
                    System.out.println("✅ TICKET VALID. Entry Allowed.");
                } else {
                    System.out.println("❌ INVALID TICKET. Entry Denied.");
                }
            }
        }
    }

    private static void handleConcessions() {
        if (currentUser instanceof Admin) {
            System.out.println("1. Add New Snack | 2. View Inventory");
            int subChoice = Integer.parseInt(scanner.nextLine());
            if (subChoice == 1) {
                System.out.print("Enter Snack Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter Price: ");
                double price = Double.parseDouble(scanner.nextLine());
                inventoryManager.addSnackType(name, price, 100); // Using the manager we built
            }
        } 
        
        else {
            inventoryManager.displaySnackMenu(); // Staff can only view
        }
    }
}