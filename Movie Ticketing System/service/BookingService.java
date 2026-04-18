package service;

import model.ConcessionItem;
import model.Hall;
import model.Movie;
import model.Order;
import model.Showtime;
import model.StandardTicket;
import model.Ticket;
import model.VIPTicket;
import util.FileHandler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingService {
    private static final String SHOWTIME_FILE = "data/showtimes.txt";
    private static final String ORDER_FILE = "data/orders.txt";
    private Showtime[] showtimes;
    private int showtimeCount;
    private Order[] orders;
    private int orderCount;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public BookingService(MovieService movieService) {
        showtimes = new Showtime[0];
        showtimeCount = 0;
        orders = new Order[0];
        orderCount = 0;
        loadShowtimes(movieService);
        loadOrders();
    }

    public void createShowtime(Showtime showtime) {
        showtimes = appendShowtime(showtimes, showtime);
        showtimeCount++;
        saveShowtimes();
    }

    public Showtime[] readAllShowtimes() {
        Showtime[] copied = new Showtime[showtimeCount];
        for (int i = 0; i < showtimeCount; i++) {
            copied[i] = showtimes[i];
        }
        return copied;
    }

    public Showtime readShowtimeByIndex(int index) {
        if (index < 0 || index >= showtimeCount) {
            throw new ArrayIndexOutOfBoundsException("Showtime index out of range.");
        }
        return showtimes[index];
    }

    public void updateShowtimeTime(int index, String newTime) {
        Showtime showtime = readShowtimeByIndex(index);
        showtime.setTime(newTime);
        saveShowtimes();
    }

    public void deleteShowtime(int index) {
        if (index < 0 || index >= showtimeCount) {
            throw new ArrayIndexOutOfBoundsException("Showtime index out of range.");
        }
        showtimes = removeShowtimeAt(showtimes, index);
        showtimeCount--;
        saveShowtimes();
    }

    public Order createOrder(String customerUsername, Showtime showtime) throws ArrayIndexOutOfBoundsException {
        if (showtime == null) {
            throw new ArrayIndexOutOfBoundsException("Showtime cannot be null when creating order.");
        }
        if (!showtimeExists(showtime.getShowtimeId())) {
            throw new ArrayIndexOutOfBoundsException("Showtime is not registered in the system.");
        }
        Order order = new Order("OR-" + System.currentTimeMillis(), customerUsername, showtime.getShowtimeId());
        orders = appendOrder(orders, order);
        orderCount++;
        return order;
    }

    public Ticket createTicketForSeat(Showtime showtime, String seatNumber) throws ArrayIndexOutOfBoundsException {
        if (showtime == null) {
            throw new ArrayIndexOutOfBoundsException("Showtime cannot be null.");
        }
        String seatType = showtime.getSeatType(seatNumber);
        if ("VIP".equalsIgnoreCase(seatType)) {
            return new VIPTicket(seatNumber);
        }
        return new StandardTicket(seatNumber);
    }

    public void persistOrder(Order order) throws ArrayIndexOutOfBoundsException {
        if (order == null) {
            throw new ArrayIndexOutOfBoundsException("Order cannot be null.");
        }
        if (order.getTickets().length == 0 && order.getConcessionItems().length == 0) {
            throw new ArrayIndexOutOfBoundsException("Order must contain at least one ticket or concession item.");
        }
        Ticket[] tickets = order.getTickets();
        ConcessionItem[] concessionItems = order.getConcessionItems();
        String[] lines = new String[tickets.length + concessionItems.length + 1];
        int lineIndex = 0;
        for (Ticket ticket : tickets) {
            lines[lineIndex++] = order.getOrderId() + "|" + order.getCustomerUsername() + "|" + order.getShowtimeId() + "|TICKET|" + ticket.getTicketType() + "|" + ticket.getSeatNumber() + "|" + ticket.calculatePrice();
        }
        for (ConcessionItem item : concessionItems) {
            lines[lineIndex++] = order.getOrderId() + "|" + order.getCustomerUsername() + "|" + order.getShowtimeId() + "|SNACK|" + item.getName() + "|" + item.getStock() + "|" + item.getPrice();
        }
        if ("N/A".equals(order.getPurchasedAt())) {
            order.setPurchasedAt(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        }
        lines[lineIndex] = order.getOrderId() + "|" + order.getCustomerUsername() + "|" + order.getShowtimeId() + "|TOTAL|" + order.getTotalPrice() + "|" + order.getPaymentMethod() + "|" + order.getPurchasedAt();
        FileHandler.saveToFile(ORDER_FILE, lines);
    }

    public Order[] readAllOrders() {
        Order[] copied = new Order[orderCount];
        for (int i = 0; i < orderCount; i++) {
            copied[i] = orders[i];
        }
        return copied;
    }

    public boolean validateTicket(String orderId) {
        for (int i = 0; i < orderCount; i++) {
            if (orders[i].getOrderId().equalsIgnoreCase(orderId)) {
                return true;
            }
        }
        String[] lines = FileHandler.readFromFile(ORDER_FILE);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length > 0 && parts[0].equalsIgnoreCase(orderId)) {
                return true;
            }
        }
        return false;
    }

    private void saveShowtimes() {
        String[] lines = new String[showtimeCount];
        for (int i = 0; i < showtimeCount; i++) {
            Showtime showtime = showtimes[i];
            lines[i] = showtime.getShowtimeId() + "|" + showtime.getMovie().getTitle() + "|" + showtime.getHall().getHallNumber() + "|" + showtime.getTime() + "|" + showtime.getHall().getCapacity() + "|" + showtime.getHall().getScreenType() + "|" + showtime.getSeatMap().length + "|" + showtime.getSeatMap()[0].length;
        }
        FileHandler.overwriteFile(SHOWTIME_FILE, lines);
    }

    private void loadShowtimes(MovieService movieService) {
        showtimes = new Showtime[0];
        showtimeCount = 0;
        String[] lines = FileHandler.readFromFile(SHOWTIME_FILE);
        Movie[] movies = movieService.readAllMovies();
        for (String line : lines) {
            try {
                String[] parts = line.split("\\|");
                if (parts.length < 8) {
                    continue;
                }
                Movie movie = null;
                for (Movie m : movies) {
                    if (m.getTitle().equalsIgnoreCase(parts[1])) {
                        movie = m;
                        break;
                    }
                }
                if (movie == null) {
                    continue;
                }
                Hall hall = new Hall(Integer.parseInt(parts[2]), Integer.parseInt(parts[4]), parts[5], Integer.parseInt(parts[6]), Integer.parseInt(parts[7]));
                showtimes = appendShowtime(showtimes, new Showtime(parts[0], movie, hall, parts[3]));
                showtimeCount++;
            } catch (Exception e) {
                System.out.println("Skipping invalid showtime record: " + line);
            }
        }
    }

    private void loadOrders() {
        orders = new Order[0];
        orderCount = 0;
        String[] lines = FileHandler.readFromFile(ORDER_FILE);
        for (String line : lines) {
            try {
                String[] parts = line.split("\\|");
                if (parts.length < 4) {
                    continue;
                }
                if ("TOTAL".equals(parts[3])) {
                    Order loadedOrder = new Order(parts[0], parts[1], parts[2]);
                    if (parts.length >= 5) {
                        loadedOrder.setTotalPrice(Double.parseDouble(parts[4]));
                    }
                    if (parts.length >= 6) {
                        loadedOrder.setPaymentMethod(parts[5]);
                    }
                    if (parts.length >= 7) {
                        loadedOrder.setPurchasedAt(parts[6]);
                    }
                    orders = appendOrder(orders, loadedOrder);
                    orderCount++;
                }
            } catch (Exception e) {
                System.out.println("Skipping invalid order record: " + line);
            }
        }
    }

    private boolean showtimeExists(String showtimeId) {
        for (int i = 0; i < showtimeCount; i++) {
            if (showtimes[i].getShowtimeId().equalsIgnoreCase(showtimeId)) {
                return true;
            }
        }
        return false;
    }

    private Showtime[] appendShowtime(Showtime[] source, Showtime showtime) {
        Showtime[] expanded = new Showtime[source.length + 1];
        for (int i = 0; i < source.length; i++) {
            expanded[i] = source[i];
        }
        expanded[source.length] = showtime;
        return expanded;
    }

    private Showtime[] removeShowtimeAt(Showtime[] source, int index) {
        Showtime[] reduced = new Showtime[source.length - 1];
        int target = 0;
        for (int i = 0; i < source.length; i++) {
            if (i == index) {
                continue;
            }
            reduced[target++] = source[i];
        }
        return reduced;
    }

    private Order[] appendOrder(Order[] source, Order order) {
        Order[] expanded = new Order[source.length + 1];
        for (int i = 0; i < source.length; i++) {
            expanded[i] = source[i];
        }
        expanded[source.length] = order;
        return expanded;
    }

    public void displayShowtimes() {
        Showtime[] allShowtimes = readAllShowtimes();
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-10s %-10s %-20s%n", "No.", "Movie", "Hall", "Time", "Showtime ID");
        System.out.println("--------------------------------------------------------------------------------");
        if (allShowtimes.length == 0) {
            System.out.println("No showtimes available.");
            System.out.println("--------------------------------------------------------------------------------");
            return;
        }
        for (int i = 0; i < allShowtimes.length; i++) {
            Showtime s = allShowtimes[i];
            String hallLabel = "Hall " + s.getHall().getHallNumber();
            System.out.printf("%-5d %-25s %-10s %-10s %-20s%n", i + 1, s.getMovie().getTitle(), hallLabel, s.getTime(), s.getShowtimeId());
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    public void displayOrderReceipts() {
        Order[] allOrders = readAllOrders();
        for (Order order : allOrders) {
            printOrderReceipt(order);
        }
    }

    private void printOrderReceipt(Order order) {
        System.out.println("\n========================================");
        System.out.println("           MOVIE ORDER RECEIPT");
        System.out.println("========================================");
        System.out.println("Order ID      : " + order.getOrderId());
        System.out.println("Customer      : " + order.getCustomerUsername());
        System.out.println("Showtime ID   : " + order.getShowtimeId());
        System.out.println("Purchased At  : " + order.getPurchasedAt());
        System.out.println("Payment Method: " + order.getPaymentMethod());
        System.out.println("----------------------------------------");
        System.out.println("Total Amount  : RM " + String.format("%.2f", order.getTotalPrice()));
        System.out.println("Status        : PAID");
        System.out.println("========================================");
    }
}
