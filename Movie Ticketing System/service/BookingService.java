package service;

import model.Booking;
import model.ConcessionItem;
import model.Hall;
import model.Movie;
import model.Order;
import model.Showtime;
import model.StandardTicket;
import model.Ticket;
import model.VIPTicket;
import util.Config;
import util.ConsoleUi;
import util.FileHandler;
import util.OrderFileFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingService {
    private static final String SHOWTIME_FILE = Config.SHOWTIME_FILE;
    private static final String ORDER_FILE = Config.ORDER_FILE;
    private static final String BOOKING_FILE = Config.BOOKING_FILE;
    private Showtime[] showtimes;
    private int showtimeCount;
    private Order[] orders;
    private int orderCount;
    private Booking[] bookings;
    private int bookingCount;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public BookingService(MovieService movieService) {
        showtimes = new Showtime[0];
        showtimeCount = 0;
        orders = new Order[0];
        orderCount = 0;
        bookings = new Booking[0];
        bookingCount = 0;
        loadShowtimes(movieService);
        loadOrders();
        loadBookings();
    }

    public void createShowtime(Showtime showtime) {
        showtimes = appendShowtime(showtimes, showtime);
        showtimeCount++;
        saveShowtimes();
    }

    public Showtime[] readAllShowtimes() {
        sortShowtimes();
        Showtime[] copied = new Showtime[showtimeCount];
        for (int i = 0; i < showtimeCount; i++) {
            copied[i] = showtimes[i];
        }
        return copied;
    }

    public Showtime readShowtimeByIndex(int index) {
        if (index < 0 || index >= showtimeCount) {
            throw new IllegalArgumentException("Showtime index out of range.");
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
            throw new IllegalArgumentException("Showtime index out of range.");
        }
        showtimes = removeShowtimeAt(showtimes, index);
        showtimeCount--;
        saveShowtimes();
    }

    public Order createOrder(String customerUsername, Showtime showtime) {
        if (showtime == null) {
            throw new IllegalArgumentException("Showtime cannot be null when creating order.");
        }
        if (!showtimeExists(showtime.getShowtimeId())) {
            throw new IllegalArgumentException("Showtime is not registered in the system.");
        }
        Order order = new Order("OR-" + System.currentTimeMillis(), customerUsername, showtime.getShowtimeId());
        orders = appendOrder(orders, order);
        orderCount++;
        return order;
    }

    public Ticket createTicketForSeat(Showtime showtime, String seatNumber) {
        if (showtime == null) {
            throw new IllegalArgumentException("Showtime cannot be null.");
        }
        String seatType = showtime.getSeatType(seatNumber);
        if ("VIP".equalsIgnoreCase(seatType)) {
            return new VIPTicket(seatNumber);
        }
        return new StandardTicket(seatNumber);
    }

    public void persistOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        if (order.getTickets().length == 0 && order.getConcessionItems().length == 0) {
            throw new IllegalArgumentException("Order must contain at least one ticket or concession item.");
        }
        if ("N/A".equals(order.getPurchasedAt())) {
            order.setPurchasedAt(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        }
        registerBookingIfAbsent(order);
        String[] allLines = OrderFileFormat.linesForAllOrders(orders, orderCount);
        FileHandler.overwriteFile(ORDER_FILE, allLines);
    }

    /**
     * True if the id matches a persisted order, or a {@link Booking} id (e.g. BK-OR-...).
     */
    public boolean existsByOrderOrBookingId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        String key = id.trim();
        if (validateTicket(key)) {
            return true;
        }
        for (int i = 0; i < bookingCount; i++) {
            if (bookings[i].getBookingId().equalsIgnoreCase(key)) {
                return true;
            }
        }
        String[] persisted = FileHandler.readFromFile(BOOKING_FILE);
        for (String line : persisted) {
            if (line == null || line.isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\|");
            if (parts.length >= 1 && parts[0].equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
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
        Showtime s = showtimes[i];

        StringBuilder seatData = new StringBuilder();
        boolean[][] seats = s.copySeatOccupancy();

        for (int r = 0; r < seats.length; r++) {
            for (int c = 0; c < seats[r].length; c++) {
                seatData.append(seats[r][c] ? "1" : "0").append(",");
            }
        }

        lines[i] =
            s.getShowtimeId() + "|" +
            s.getMovie().getTitle() + "|" +
            s.getHall().getHallNumber() + "|" +
            s.getDate() + "|" +
            s.getTime() + "|" +
            s.getHall().getCapacity() + "|" +
            s.getHall().getScreenType() + "|" +
            s.getSeatRowCount() + "|" +
            s.getSeatColumnCount() + "|" +
            seatData;
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
            String[] p = line.split("\\|");

            Movie movie = null;
            for (Movie m : movies) {
                if (m.getTitle().equalsIgnoreCase(p[1])) {
                    movie = m;
                    break;
                }
            }

            if (movie == null) continue;

            Hall hall = new Hall(
                Integer.parseInt(p[2]),
                Integer.parseInt(p[5]),
                p[6],
                Integer.parseInt(p[7]),
                Integer.parseInt(p[8])
            );

            Showtime s = new Showtime(
                p[0],
                movie,
                hall,
                p[3], // date
                p[4]  // time
            );

            String[] seatTokens = p[9].split(",");
            int rows = s.getSeatRowCount();
            int cols = s.getSeatColumnCount();
            boolean[][] restored = new boolean[rows][cols];
            int k = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (k < seatTokens.length) {
                        restored[r][c] = "1".equals(seatTokens[k]);
                        k++;
                    }
                }
            }
            s.importSeatOccupancy(restored);

            showtimes = appendShowtime(showtimes, s);
            showtimeCount++;

        } catch (Exception e) {
            System.out.println("Skip invalid showtime: " + line);
        }
    }
}

   private void loadOrders() {
    orders = new Order[0];
    orderCount = 0;

    String[] lines = FileHandler.readFromFile(ORDER_FILE);

    java.util.HashMap<String, Order> map = new java.util.HashMap<>();

    // Pass 1: tickets and concession lines only (TOTAL mutates totalPrice and must run after line items).
    for (String line : lines) {
        try {
            String[] parts = line.split("\\|");

            if (parts.length < 4) {
                continue;
            }

            String orderId = parts[0];
            String username = parts[1];
            String showtimeId = parts[2];
            String recordType = parts[3];

            if ("TOTAL".equals(recordType)) {
                continue;
            }

            Order order = map.get(orderId);
            if (order == null) {
                order = new Order(orderId, username, showtimeId);
                map.put(orderId, order);
            }

            if ("TICKET".equals(recordType)) {
                Ticket ticket = ticketFromPersistedParts(parts);
                order.addTicket(ticket);
            } else if (isPersistedConcessionRecordType(recordType)) {
                String name = parts[4];
                int quantity = Integer.parseInt(parts[5]);
                double unitPrice = Double.parseDouble(parts[6]);
                String category = legacyConcessionCategory(recordType);
                ConcessionItem lineItem = new ConcessionItem(name, unitPrice, quantity, category);
                order.addConcessionItem(lineItem, quantity);
            }
        } catch (Exception e) {
            System.out.println("Skipping invalid order record: " + line);
        }
    }

    // Pass 2: apply persisted totals (authoritative).
    for (String line : lines) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 4 || !"TOTAL".equals(parts[3])) {
                continue;
            }
            String orderId = parts[0];
            Order order = map.get(orderId);
            if (order == null) {
                continue;
            }
            if (parts.length >= 5) {
                order.setTotalPrice(Double.parseDouble(parts[4]));
            }
            if (parts.length >= 6) {
                order.setPaymentMethod(parts[5]);
            }
            if (parts.length >= 7) {
                order.setPurchasedAt(parts[6]);
            }
        } catch (Exception e) {
            System.out.println("Skipping invalid TOTAL line: " + line);
        }
    }

    for (Order loadedOrder : map.values()) {
        orders = appendOrder(orders, loadedOrder);
        orderCount++;
    }
}

    private static boolean isPersistedConcessionRecordType(String recordType) {
        return "FOOD".equalsIgnoreCase(recordType)
                || "DRINK".equalsIgnoreCase(recordType)
                || "SNACK".equalsIgnoreCase(recordType);
    }

    /** Legacy rows used literal SNACK in column 3; new rows use FOOD/DRINK/SNACK. */
    private static String legacyConcessionCategory(String recordType) {
        return recordType.toUpperCase();
    }

    /**
     * Persisted TICKET row: ...|TICKET|TYPE|SEAT|PRICE (TYPE = VIP or STANDARD).
     * Older rows without type: ...|TICKET|SEAT|PRICE
     */
    private static Ticket ticketFromPersistedParts(String[] parts) {
        if (parts.length >= 7) {
            String seatType = parts[4];
            String seatNumber = parts[5];
            if ("VIP".equalsIgnoreCase(seatType)) {
                return new VIPTicket(seatNumber);
            }
            return new StandardTicket(seatNumber);
        }
        if (parts.length >= 6) {
            return new StandardTicket(parts[4]);
        }
        throw new IllegalArgumentException("Malformed TICKET record.");
    }

    private void sortShowtimes() {
        for (int i = 0; i < showtimeCount - 1; i++) {
            for (int j = i + 1; j < showtimeCount; j++) {

                String dateTime1 = showtimes[i].getDate() + " " + showtimes[i].getTime();
                String dateTime2 = showtimes[j].getDate() + " " + showtimes[j].getTime();

                if (dateTime1.compareTo(dateTime2) > 0) {
                    Showtime temp = showtimes[i];
                    showtimes[i] = showtimes[j];
                    showtimes[j] = temp;
                }
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

    private Booking[] appendBooking(Booking[] source, Booking booking) {
        Booking[] expanded = new Booking[source.length + 1];
        for (int i = 0; i < source.length; i++) {
            expanded[i] = source[i];
        }
        expanded[source.length] = booking;
        return expanded;
    }

    private Order findOrderById(String orderId) {
        for (int i = 0; i < orderCount; i++) {
            if (orders[i].getOrderId().equalsIgnoreCase(orderId)) {
                return orders[i];
            }
        }
        return null;
    }

    private void loadBookings() {
        bookings = new Booking[0];
        bookingCount = 0;
        String[] lines = FileHandler.readFromFile(BOOKING_FILE);
        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\|");
            if (parts.length < 2) {
                continue;
            }
            try {
                String bookingId = parts[0].trim();
                String oid = parts[1].trim();
                Order order = findOrderById(oid);
                if (order == null) {
                    continue;
                }
                if (hasBookingForOrder(oid)) {
                    continue;
                }
                bookings = appendBooking(bookings, new Booking(bookingId, order));
                bookingCount++;
            } catch (IllegalArgumentException ex) {
                System.out.println("Skip invalid booking line: " + line);
            }
        }
    }

    private void saveBookings() {
        String[] lines = new String[bookingCount];
        for (int i = 0; i < bookingCount; i++) {
            lines[i] = bookings[i].getBookingId() + "|" + bookings[i].getOrder().getOrderId();
        }
        FileHandler.overwriteFile(BOOKING_FILE, lines);
    }

    private boolean hasBookingForOrder(String orderId) {
        for (int i = 0; i < bookingCount; i++) {
            if (bookings[i].getOrder().getOrderId().equalsIgnoreCase(orderId)) {
                return true;
            }
        }
        return false;
    }

    private void registerBookingIfAbsent(Order order) {
        if (hasBookingForOrder(order.getOrderId())) {
            return;
        }
        String bookingId = "BK-" + order.getOrderId();
        bookings = appendBooking(bookings, new Booking(bookingId, order));
        bookingCount++;
        saveBookings();
    }

    public void displayShowtimes() {
        Showtime[] allShowtimes = readAllShowtimes();
        ConsoleUi.banner("SHOWTIMES");
        System.out.printf("%-5s %-25s %-10s %-12s %-10s %-20s%n",
"No.", "Movie", "Hall", "Date", "Time", "Showtime ID");
        System.out.println("---");
        if (allShowtimes.length == 0) {
            System.out.println("No showtimes available.");
            return;
        }
        for (int i = 0; i < allShowtimes.length; i++) {
            Showtime s = allShowtimes[i];
           String hallLabel = "Hall " + s.getHall().getHallNumber();

        System.out.printf("%-5d %-25s %-10s %-12s %-10s %-20s%n",
        i + 1,
        s.getMovie().getTitle(),
        hallLabel,
        s.getDate(),
        s.getTime(),
        s.getShowtimeId());
                }
            }

    public void displayOrderReceipts() {
        Order[] allOrders = readAllOrders();
        ConsoleUi.banner("YOUR ORDERS");
        if (allOrders.length == 0) {
            System.out.println("No orders.");
            ConsoleUi.lightRule();
            return;
        }
        for (Order order : allOrders) {
            printOrderReceipt(order);
        }
        ConsoleUi.lightRule();
    }

    private void printOrderReceipt(Order order) {
        ConsoleUi.banner("MOVIE ORDER RECEIPT");
        System.out.printf("%-16s %s%n", "Order ID", order.getOrderId());
        System.out.printf("%-16s %s%n", "Booking ID", bookingIdForOrder(order.getOrderId()));
        System.out.printf("%-16s %s%n", "Customer", order.getCustomerUsername());
        System.out.printf("%-16s %s%n", "Showtime ID", order.getShowtimeId());
        System.out.printf("%-16s %s%n", "Purchased At", order.getPurchasedAt());
        System.out.printf("%-16s %s%n", "Payment", order.getPaymentMethod());
        ConsoleUi.lightRule();
        System.out.println("Tickets");
        for (Ticket ticket : order.getTickets()) {
            System.out.printf("  %-10s  seat %-6s  RM %8.2f%n",
                    ticket.getTicketType(), ticket.getSeatNumber(), ticket.lineTotal());
        }
        System.out.println("Concessions");
        for (ConcessionItem item : order.getConcessionItems()) {
            System.out.printf("  %-8s  %-18s  qty %3d  RM %8.2f%n",
                    item.getCategory(), item.getName(), item.getStock(), item.lineTotal());
        }
        ConsoleUi.lightRule();
        System.out.printf("%-16s RM %8.2f%n", "TOTAL", order.getTotalPrice());
        System.out.println("Status          PAID");
    }

    private String bookingIdForOrder(String orderId) {
        for (int i = 0; i < bookingCount; i++) {
            if (bookings[i].getOrder().getOrderId().equalsIgnoreCase(orderId)) {
                return bookings[i].getBookingId();
            }
        }
        return "BK-" + orderId;
    }
    public void persistShowtimes() {
    saveShowtimes();
}
}
