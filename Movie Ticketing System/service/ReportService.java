package service;

import util.FileHandler;

public class ReportService {
    private static final String ORDER_FILE = "data/orders.txt";
    private static final String SHOWTIME_FILE = "data/showtimes.txt";

    public void bestSellingMovies() {
        String[] orders = FileHandler.readFromFile(ORDER_FILE);
        String[] showtimes = FileHandler.readFromFile(SHOWTIME_FILE);
        String[] movieTitles = new String[0];
        int[] movieCounts = new int[0];

        for (String line : orders) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4 && "TOTAL".equals(parts[3])) {
                String showtimeId = parts[2];
                String movieTitle = getMovieTitleByShowtimeId(showtimes, showtimeId);
                int existingIndex = indexOf(movieTitles, movieTitle);
                if (existingIndex >= 0) {
                    movieCounts[existingIndex]++;
                } else {
                    movieTitles = appendString(movieTitles, movieTitle);
                    movieCounts = appendInt(movieCounts, 1);
                }
            }
        }

        System.out.println("\n--- BEST SELLING MOVIES ---");
        if (movieTitles.length == 0) {
            System.out.println("No sales data available.");
            return;
        }
        for (int i = 0; i < movieTitles.length; i++) {
            System.out.println(movieTitles[i] + " -> " + movieCounts[i] + " orders");
        }
    }

    public void peakHours() {
        String[] orders = FileHandler.readFromFile(ORDER_FILE);
        String[] showtimes = FileHandler.readFromFile(SHOWTIME_FILE);
        String[] hours = new String[0];
        int[] hourCounts = new int[0];

        for (String line : orders) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4 && "TOTAL".equals(parts[3])) {
                String time = getShowtimeTimeByShowtimeId(showtimes, parts[2]);
                int existingIndex = indexOf(hours, time);
                if (existingIndex >= 0) {
                    hourCounts[existingIndex]++;
                } else {
                    hours = appendString(hours, time);
                    hourCounts = appendInt(hourCounts, 1);
                }
            }
        }

        System.out.println("\n--- PEAK HOURS ---");
        if (hours.length == 0) {
            System.out.println("No booking data available.");
            return;
        }
        for (int i = 0; i < hours.length; i++) {
            System.out.println(hours[i] + " -> " + hourCounts[i] + " orders");
        }
    }

    private String getMovieTitleByShowtimeId(String[] showtimes, String showtimeId) {
        for (String line : showtimes) {
            String[] parts = line.split("\\|");
            if (parts.length >= 2 && parts[0].equals(showtimeId)) {
                return parts[1];
            }
        }
        return "Unknown";
    }

    private String getShowtimeTimeByShowtimeId(String[] showtimes, String showtimeId) {
        for (String line : showtimes) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4 && parts[0].equals(showtimeId)) {
                return parts[3];
            }
        }
        return "Unknown";
    }

    private int indexOf(String[] data, String value) {
        for (int i = 0; i < data.length; i++) {
            if (data[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    private String[] appendString(String[] source, String value) {
        String[] expanded = new String[source.length + 1];
        for (int i = 0; i < source.length; i++) {
            expanded[i] = source[i];
        }
        expanded[source.length] = value;
        return expanded;
    }

    private int[] appendInt(int[] source, int value) {
        int[] expanded = new int[source.length + 1];
        for (int i = 0; i < source.length; i++) {
            expanded[i] = source[i];
        }
        expanded[source.length] = value;
        return expanded;
    }
}
