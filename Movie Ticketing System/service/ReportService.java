package service;

import util.FileHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private static final String ORDER_FILE = "data/orders.txt";
    private static final String SHOWTIME_FILE = "data/showtimes.txt";

    public void bestSellingMovies() {
        Map<String, Integer> movieCount = new HashMap<>();
        List<String> orders = FileHandler.readFromFile(ORDER_FILE);
        List<String> showtimes = FileHandler.readFromFile(SHOWTIME_FILE);
        Map<String, String> showtimeMovie = new HashMap<>();

        for (String line : showtimes) {
            String[] parts = line.split("\\|");
            if (parts.length >= 2) {
                showtimeMovie.put(parts[0], parts[1]);
            }
        }

        for (String line : orders) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4 && "TOTAL".equals(parts[3])) {
                String showtimeId = parts[2];
                String movieTitle = showtimeMovie.getOrDefault(showtimeId, "Unknown");
                movieCount.put(movieTitle, movieCount.getOrDefault(movieTitle, 0) + 1);
            }
        }

        System.out.println("\n--- BEST SELLING MOVIES ---");
        if (movieCount.isEmpty()) {
            System.out.println("No sales data available.");
            return;
        }
        for (Map.Entry<String, Integer> entry : movieCount.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " orders");
        }
    }

    public void peakHours() {
        Map<String, Integer> hourCount = new HashMap<>();
        List<String> orders = FileHandler.readFromFile(ORDER_FILE);
        List<String> showtimes = FileHandler.readFromFile(SHOWTIME_FILE);
        Map<String, String> showtimeTime = new HashMap<>();

        for (String line : showtimes) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                showtimeTime.put(parts[0], parts[3]);
            }
        }

        for (String line : orders) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4 && "TOTAL".equals(parts[3])) {
                String time = showtimeTime.getOrDefault(parts[2], "Unknown");
                hourCount.put(time, hourCount.getOrDefault(time, 0) + 1);
            }
        }

        System.out.println("\n--- PEAK HOURS ---");
        if (hourCount.isEmpty()) {
            System.out.println("No booking data available.");
            return;
        }
        for (Map.Entry<String, Integer> entry : hourCount.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " orders");
        }
    }
}
