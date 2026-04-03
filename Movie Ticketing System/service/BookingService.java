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

import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private static final String SHOWTIME_FILE = "data/showtimes.txt";
    private static final String ORDER_FILE = "data/orders.txt";
    private final List<Showtime> showtimes;
    private final List<Order> orders;

    public BookingService(MovieService movieService) {
        showtimes = new ArrayList<>();
        orders = new ArrayList<>();
        loadShowtimes(movieService);
        loadOrders();
    }

    public void createShowtime(Showtime showtime) {
        showtimes.add(showtime);
        saveShowtimes();
    }

    public List<Showtime> readAllShowtimes() {
        return new ArrayList<>(showtimes);
    }

    public Showtime readShowtimeByIndex(int index) {
        if (index < 0 || index >= showtimes.size()) {
            throw new IllegalArgumentException("Showtime index out of range.");
        }
        return showtimes.get(index);
    }

    public void updateShowtimeTime(int index, String newTime) {
        Showtime showtime = readShowtimeByIndex(index);
        showtime.setTime(newTime);
        saveShowtimes();
    }

    public void deleteShowtime(int index) {
        if (index < 0 || index >= showtimes.size()) {
            throw new IllegalArgumentException("Showtime index out of range.");
        }
        showtimes.remove(index);
        saveShowtimes();
    }

    public Order createOrder(String customerUsername, Showtime showtime) {
        Order order = new Order("OR-" + System.currentTimeMillis(), customerUsername, showtime.getShowtimeId());
        orders.add(order);
        return order;
    }

    public Ticket createTicketForSeat(Showtime showtime, String seatNumber) {
        String seatType = showtime.getSeatType(seatNumber);
        if ("VIP".equalsIgnoreCase(seatType)) {
            return new VIPTicket(seatNumber);
        }
        return new StandardTicket(seatNumber);
    }

    public void persistOrder(Order order) {
        List<String> lines = new ArrayList<>();
        for (Ticket ticket : order.getTickets()) {
            lines.add(order.getOrderId() + "|" + order.getCustomerUsername() + "|" + order.getShowtimeId() + "|TICKET|" + ticket.getTicketType() + "|" + ticket.getSeatNumber() + "|" + ticket.calculatePrice());
        }
        for (ConcessionItem item : order.getConcessionItems()) {
            lines.add(order.getOrderId() + "|" + order.getCustomerUsername() + "|" + order.getShowtimeId() + "|SNACK|" + item.getName() + "|" + item.getStock() + "|" + item.getPrice());
        }
        lines.add(order.getOrderId() + "|" + order.getCustomerUsername() + "|" + order.getShowtimeId() + "|TOTAL|" + order.getTotalPrice() + "|" + order.getCouponCode() + "|" + order.getQrCode());
        FileHandler.saveToFile(ORDER_FILE, lines);
    }

    public List<Order> readAllOrders() {
        return new ArrayList<>(orders);
    }

    public boolean validateTicket(String orderId) {
        for (Order order : orders) {
            if (order.getOrderId().equalsIgnoreCase(orderId)) {
                return true;
            }
        }
        List<String> lines = FileHandler.readFromFile(ORDER_FILE);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length > 0 && parts[0].equalsIgnoreCase(orderId)) {
                return true;
            }
        }
        return false;
    }

    private void saveShowtimes() {
        List<String> lines = new ArrayList<>();
        for (Showtime showtime : showtimes) {
            lines.add(showtime.getShowtimeId() + "|" + showtime.getMovie().getTitle() + "|" + showtime.getHall().getHallNumber() + "|" + showtime.getTime() + "|" + showtime.getHall().getCapacity() + "|" + showtime.getHall().getScreenType() + "|" + showtime.getSeatMap().length + "|" + showtime.getSeatMap()[0].length);
        }
        FileHandler.overwriteFile(SHOWTIME_FILE, lines);
    }

    private void loadShowtimes(MovieService movieService) {
        showtimes.clear();
        List<String> lines = FileHandler.readFromFile(SHOWTIME_FILE);
        List<Movie> movies = movieService.readAllMovies();
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
                showtimes.add(new Showtime(parts[0], movie, hall, parts[3]));
            } catch (Exception ex) {
                System.out.println("Skipping invalid showtime record: " + line);
            }
        }
    }

    private void loadOrders() {
        orders.clear();
        List<String> lines = FileHandler.readFromFile(ORDER_FILE);
        for (String line : lines) {
            try {
                String[] parts = line.split("\\|");
                if (parts.length < 4) {
                    continue;
                }
                if ("TOTAL".equals(parts[3])) {
                    orders.add(new Order(parts[0], parts[1], parts[2]));
                }
            } catch (Exception ex) {
                System.out.println("Skipping invalid order record: " + line);
            }
        }
    }
}
