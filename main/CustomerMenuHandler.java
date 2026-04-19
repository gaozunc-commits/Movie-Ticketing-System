package main;

import model.*;
import service.*;
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
            int choice = readInt("Select: ");

            try {
                switch (choice) {
                    case 1 -> showMoviesAndShowtimes();
                    case 2 -> placeOrder(customer);
                    case 3 -> bookingService.displayOrderReceipts();
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
        return "\n--- CUSTOMER MENU ---\n"
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

            showtime = bookingService.readShowtimeByIndex(
                    safeIndex("Select showtime: ", bookingService.readAllShowtimes().length)
            );

            if (showtime == null) return;

            // UX 
            System.out.println("Enter seat (Example: A1, B2)");

            showtime.displaySeats();

            while (true) {
                System.out.print("Seat: ");
                seat = readText("").toUpperCase();

                try {
                    if (showtime.bookSeat(seat)) {
                        seatBooked = true;
                        bookingService.persistShowtimes();
                        break;
                    } else {
                        System.out.println("Seat taken. Try another.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid seat format.");
                }
            }

            Order order = bookingService.createOrder(customer.getUsername(), showtime);
            Ticket ticket = bookingService.createTicketForSeat(showtime, seat);
            order.addTicket(ticket);


            while (true) {
                System.out.print("Add concession? (y/n): ");
                String ans = readText("").trim().toLowerCase();

                if (ans.equals("n")) break;
                if (!ans.equals("y")) {
                    System.out.println("Enter only y or n");
                    continue;
                }

                concessionService.displayConcessions();

                if (concessionService.readAllItems().length == 0) {
                    System.out.println("No items available.");
                    continue;
                }

                int idx = safeIndex("Index: ", concessionService.readAllItems().length);
                if (idx == -1) continue;

                ConcessionItem item = concessionService.readItemByIndex(idx);

                if (item.getStock() <= 0) {
                    System.out.println("Out of stock");
                    continue;
                }

                int qty;
                while (true) {
                    qty = readInt("Qty: ");

                    if (qty <= 0) {
                        System.out.println("Qty must > 0");
                        continue;
                    }

                    if (qty > item.getStock()) {
                        System.out.println("Not enough stock: " + item.getStock());
                        continue;
                    }

                    break;
                }

                concessionService.deductStock(idx, qty);
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
            // rollback seat
            if (showtime != null && seatBooked) {
                showtime.releaseSeat(seat);
                bookingService.persistShowtimes();
            }
            throw e;
        }
    }

    private int safeIndex(String msg, int size) {
        if (size <= 0) {
            System.out.println("No data available");
            return -1;
        }

        while (true) {
            int i = readInt(msg) - 1;

            if (i >= 0 && i < size) return i;

            System.out.println("Invalid index, try again");
        }
    }

    private void showMoviesAndShowtimes() {
        System.out.println("\n--- MOVIES ---");
        movieService.displayMovies();

        System.out.println("\n--- SHOWTIMES ---");
        bookingService.displayShowtimes();
    }

    // InputValidator
    private int readInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return InputValidator.parseIntInRange(
                        scanner.nextLine(),
                        Integer.MIN_VALUE,
                        Integer.MAX_VALUE
                );
            } catch (Exception e) {
                System.out.println("Invalid number");
            }
        }
    }

    private String readText(String msg) {
        while (true) {
            System.out.print(msg);
            String s = scanner.nextLine();
            if (!s.trim().isEmpty()) return s;
            System.out.println("Empty input");
        }
    }
}