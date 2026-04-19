package main;

import model.*;
import service.*;
import util.ConsoleUi;
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
        boolean running = true;

        while (running) {
            System.out.println(this);
            int choice = InputValidator.promptIntInRange(scanner, "Select: ", 0, 4);

            try {
                switch (choice) {
                    case 1 -> showMoviesAndShowtimes();
                    case 2 -> placeOrder(customer);
                    case 3 -> bookingService.displayOrderReceipts();
                    case 4 -> showMovieTrailersPreview();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Customer flow error: " + e.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        return "\n=== CUSTOMER MENU ===\n"
                + "1. Browse Movies\n"
                + "2. Book Ticket\n"
                + "3. View Orders\n"
                + "4. Trailers\n"
                + "0. Logout";
    }

    private void placeOrder(Customer customer) {

        Showtime showtime = null;
        String seat = "";
        boolean seatBooked = false;

        try {
            bookingService.displayShowtimes();

            int showtimeIndex = InputValidator.promptOneBasedIndex(
                    scanner,
                    "Select showtime: ",
                    bookingService.readAllShowtimes().length
            );
            if (showtimeIndex < 0) {
                return;
            }

            showtime = bookingService.readShowtimeByIndex(showtimeIndex);

            System.out.println("\n=== SELECT SEAT ===");
            System.out.println("Enter seat (Example: A1, B2)");

            showtime.displaySeats();

            while (true) {
                seat = InputValidator.promptText(scanner, "Seat: ").trim().toUpperCase();
                try {
                    InputValidator.validateSeatFormat(seat);
                    if (showtime.bookSeat(seat)) {
                        seatBooked = true;
                        bookingService.persistShowtimes();
                        break;
                    }
                    System.out.println("Seat taken. Try another.");
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Invalid seat selection.");
                }
            }

            Order order = bookingService.createOrder(customer.getUsername(), showtime);
            Ticket ticket = bookingService.createTicketForSeat(showtime, seat);
            order.addTicket(ticket);

            while (true) {
                String addMore = InputValidator.promptText(scanner, "Add concession? (y/n): ").trim().toLowerCase();

                if (addMore.equals("n")) {
                    break;
                }
                if (!addMore.equals("y")) {
                    System.out.println("Enter only y or n");
                    continue;
                }

                concessionService.displayConcessions();

                if (concessionService.readAllItems().length == 0) {
                    System.out.println("No items available.");
                    continue;
                }

                int itemIndex = InputValidator.promptOneBasedIndex(
                        scanner,
                        "Index: ",
                        concessionService.readAllItems().length
                );
                if (itemIndex < 0) {
                    continue;
                }

                ConcessionItem item = concessionService.readItemByIndex(itemIndex);

                if (item.getStock() <= 0) {
                    System.out.println("Out of stock");
                    continue;
                }

                int qty = InputValidator.promptIntInRange(scanner, "Qty: ", 1, item.getStock());

                concessionService.deductStock(itemIndex, qty);
                order.addConcessionItem(item, qty);

                System.out.println("Added.");
            }

            Payment payment = paymentService.processPaymentByChoice(
                    order.getTotalPrice(), scanner
            );

            order.setPaymentMethod(payment.getMethod());
            bookingService.persistOrder(order);

            System.out.println("Order completed: " + order.getOrderId());

        } catch (Exception e) {
            if (showtime != null && seatBooked) {
                showtime.releaseSeat(seat);
                bookingService.persistShowtimes();
            }
            throw e;
        }
    }

    private void showMoviesAndShowtimes() {
        movieService.displayMovies();
        bookingService.displayShowtimes();
    }

    private void showMovieTrailersPreview() {
        Movie[] movies = movieService.readAllMovies();
        ConsoleUi.banner("TRAILERS");

        System.out.printf("%-5s %-25s %-15s %-10s %-10s %-30s%n",
                "No.", "Title", "Genre", "Duration", "Rating", "Preview");
        System.out.println("---");

        if (movies.length == 0) {
            System.out.println("No movies scheduled.");
            return;
        }

        for (int index = 0; index < movies.length; index++) {
            Movie movie = movies[index];

            String previewText = "Showing soon...";

            System.out.printf("%-5d %-25s %-15s %-10s %-10s %-30s%n",
                    index + 1,
                    movie.getTitle(),
                    movie.getGenre(),
                    movie.getDuration() + " min",
                    movie.getAgeRating(),
                    previewText);
        }
    }
}
