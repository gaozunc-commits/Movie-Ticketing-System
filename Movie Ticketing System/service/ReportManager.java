package service;

import util.FileHandler;
import java.util.List;

public class ReportManager {
    private final String BOOKING_FILE = "data/bookings.txt";

    public void generateFinancialReport() {
        List<String> lines = FileHandler.readFromFile(BOOKING_FILE);
        double totalRevenue = 0;
        int totalTickets = 0;

        System.out.println("\n--- OFFICIAL CINEMA FINANCIAL REPORT ---");
        System.out.println("ID          | Movie           | Amount");
        System.out.println("---------------------------------------");

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                String id = parts[0];
                String movie = parts[1];
                double price = Double.parseDouble(parts[3]);

                System.out.printf("%-11s | %-15s | RM %.2f\n", id, movie, price);
                totalRevenue += price;
                totalTickets++;
            }
        }

        System.out.println("---------------------------------------");
        System.out.println("Total Tickets Sold: " + totalTickets);
        System.out.println("TOTAL REVENUE   : RM " + String.format("%.2f", totalRevenue));
        System.out.println("---------------------------------------");
    }

    public boolean checkBookingExists(String targetId) {
        List<String> lines = FileHandler.readFromFile(BOOKING_FILE);
        
        for (String line : lines) {
            String[] parts = line.split("\\|");
            // parts[0] is the Booking ID (e.g., BK-1774...)
            if (parts.length > 0 && parts[0].equalsIgnoreCase(targetId)) {
                return true; // ID found!
            }
        }
        return false; // ID not found after checking the whole file
    }
}