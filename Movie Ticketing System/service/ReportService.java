package service;

import util.Config;
import util.FileHandler;

public class ReportService {
    private static final String ORDER_FILE = Config.ORDER_FILE;
    private static final String SHOWTIME_FILE = Config.SHOWTIME_FILE;

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

        System.out.println("\n=== BEST SELLING MOVIES ===");
        if (movieTitles.length == 0) {
            System.out.println("No sales data available.");
            return;
        }
        for (int i = 0; i < movieTitles.length; i++) {
            System.out.println(movieTitles[i] + " -> " + movieCounts[i] + " orders");
        }
    }
      public void concessionReport() {

        String[] orders = FileHandler.readFromFile(ORDER_FILE);

        int foodQty = 0;
        int drinkQty = 0;
        int snackQty = 0;

        double foodSales = 0;
        double drinkSales = 0;
        double snackSales = 0;

        System.out.println("\n=== CONCESSION SALES REPORT ===");

        for (String line : orders) {
            String[] parts = line.split("\\|");

            // MUST be at least 7 columns for concession lines
            if (parts.length < 7) continue;

            String type = parts[3];

            // ❗ IMPORTANT: skip ticket / total lines
            if (!type.equalsIgnoreCase("FOOD")
                    && !type.equalsIgnoreCase("DRINK")
                    && !type.equalsIgnoreCase("SNACK")) {
                continue;
            }

            String itemName = parts[4];

            int qty;
            double price;

            try {
                qty = Integer.parseInt(parts[5]);
                price = Double.parseDouble(parts[6]);
            } catch (Exception e) {
                continue; // skip bad data like A3 etc
            }

            double total = qty * price;

            switch (type.toUpperCase()) {
                case "FOOD":
                    foodQty += qty;
                    foodSales += total;
                    System.out.println("FOOD | " + itemName + " x" + qty + " = RM " + total);
                    break;

                case "DRINK":
                    drinkQty += qty;
                    drinkSales += total;
                    System.out.println("DRINK | " + itemName + " x" + qty + " = RM " + total);
                    break;

                case "SNACK":
                    snackQty += qty;
                    snackSales += total;
                    System.out.println("SNACK | " + itemName + " x" + qty + " = RM " + total);
                    break;
            }
        }

        System.out.println("\n=== SUMMARY ===");
        System.out.println("FOOD  : " + foodQty + " items | RM " + foodSales);
        System.out.println("DRINK : " + drinkQty + " items | RM " + drinkSales);
        System.out.println("SNACK : " + snackQty + " items | RM " + snackSales);
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

        System.out.println("\n=== PEAK HOURS ===");
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
