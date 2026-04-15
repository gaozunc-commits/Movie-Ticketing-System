package main;

import model.ConcessionItem;
import model.Customer;
import model.Order;
import model.Payment;
import model.Showtime;
import model.Ticket;
import service.BookingService;
import service.ConcessionService;
import service.MovieService;
import service.PaymentService;
import util.FileHandler;
import util.InputValidator;

import java.util.Scanner;

public class CustomerMenuHandler {
    private final Scanner scanner;
    private final MovieService movieService;
    private final ConcessionService concessionService;
    private final BookingService bookingService;
    private final PaymentService paymentService;

    public CustomerMenuHandler(
        Scanner scanner,
        MovieService movieService,
        ConcessionService concessionService,
        BookingService bookingService,
        PaymentService paymentService
    ) {
        this.scanner = scanner;
        this.movieService = movieService;
        this.concessionService = concessionService;
        this.bookingService = bookingService;
        this.paymentService = paymentService;
    }

    public void run(Customer customer) {
        boolean keepCustomerMenuOpen = true;
        do {
            System.out.println(this);
            int choice = readInt("Select: ");
            try {
                switch (choice) {
                    case 1:
                        showMoviesAndShowtimes();
                        break;
                    case 2:
                        placeOrder(customer);
                        break;
                    case 3:
                        bookingService.displayOrderReceipts();
                        break;
                    case 4:
                        showTrailers();
                        break;
                    case 0:
                        keepCustomerMenuOpen = false;
                        break;
                    default:
                        System.out.println("Invalid menu option.");
                }
            } catch (Exception e) {
                System.out.println("Customer flow error: " + e.getMessage());
            }
        } while (keepCustomerMenuOpen);
    }

    public String toString() {
        return "\n--- CUSTOMER MENU ---\n"
            + "1. Browse Movies\n"
            + "2. Book Ticket\n"
            + "3. View My Orders\n"
            + "4. Upcoming Trailers\n"
            + "0. Logout";
    }

    private void placeOrder(Customer customer) throws ArrayIndexOutOfBoundsException {
        Showtime showtime = null;
        String seat = "";
        boolean seatBooked = false;
        try {
            bookingService.displayShowtimes();
            showtime = bookingService.readShowtimeByIndex(chooseIndex("Select showtime index: ", bookingService.readAllShowtimes().length));
            showtime.displaySeats();
            boolean validSeatChosen = false;
            do {
                try {
                    seat = readText("Choose seat (e.g., A1): ").toUpperCase();
                    InputValidator.validateSeatFormat(seat);
                    if (!showtime.bookSeat(seat)) {
                        System.out.println("Seat not available. Please choose another seat.");
                    } else {
                        seatBooked = true;
                        validSeatChosen = true;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid seat format. Please use format like A1.");
                }
            } while (!validSeatChosen);

            Order order = bookingService.createOrder(customer.getUsername(), showtime);
            Ticket ticket = bookingService.createTicketForSeat(showtime, seat);
            order.addTicket(ticket);

            boolean validConcessionChoice = false;
            do {
                System.out.println("\n==================== CONCESSION ADD-ON ====================");
                System.out.println("Would you like to add snacks/drinks to this order?");
                System.out.print("Add concession item? (y/n): ");
                String addConcessionChoice = scanner.nextLine();
                if (addConcessionChoice.equalsIgnoreCase("y")) {
                    concessionService.displayConcessions();
                    int index = chooseIndex("Concession index: ", concessionService.readAllItems().length);
                    int quantity = readInt("Quantity: ");
                    ConcessionItem item = concessionService.readItemByIndex(index);
                    concessionService.deductStock(index, quantity);
                    order.addConcessionItem(item, quantity);
                    double addOnSubtotal = item.getPrice() * quantity;
                    System.out.println("Added: " + item.getName() + " x" + quantity + " (RM " + String.format("%.2f", addOnSubtotal) + ")");
                    validConcessionChoice = true;
                } else if (addConcessionChoice.equalsIgnoreCase("n")) {
                    System.out.println("No concession item added.");
                    validConcessionChoice = true;
                } else {
                    System.out.println("Invalid choice. Please enter y or n.");
                }
            } while (!validConcessionChoice);

            Payment payment = paymentService.processPaymentByChoice(order.getTotalPrice(), scanner);
            order.setPaymentMethod(payment.getMethod());
            customer.addLoyaltyPoints((int) Math.ceil(order.getTotalPrice()));
            bookingService.persistOrder(order);
            System.out.println("Order complete. Order ID: " + order.getOrderId());
        } catch (Exception e) {
            if (showtime != null && seat != null && seatBooked) {
                showtime.releaseSeat(seat);
            }
            throw new ArrayIndexOutOfBoundsException(e.getMessage());
        } finally {
            System.out.println("Booking flow ended.");
        }
    }

    private void showMoviesAndShowtimes() {
        System.out.println("\n--- NOW SHOWING ---");
        movieService.displayMovies();
        System.out.println("\n--- SHOWTIMES ---");
        bookingService.displayShowtimes();
    }

    private void showTrailers() {
        String[] trailers = FileHandler.readFromFile("data/trailers.txt");
        System.out.println("\n--- UPCOMING TRAILERS ---");
        if (trailers.length == 0) {
            System.out.println("No trailers available.");
            return;
        }
        for (String trailer : trailers) {
            System.out.println("- " + trailer);
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
