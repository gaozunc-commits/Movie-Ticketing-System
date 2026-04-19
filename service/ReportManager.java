package service;

import model.Order;

/**
 * Coordinates reporting and booking checks without duplicating file logic.
 */
public class ReportManager {

    private final ReportService reportService;
    private final BookingService bookingService;

    public ReportManager(ReportService reportService, BookingService bookingService) {
        this.reportService = reportService;
        this.bookingService = bookingService;
    }

    public ReportManager() {
        this(new ReportService(), new BookingService(new MovieService()));
    }

    public void generateFinancialReport(Order[] orders) {
        reportService.bestSellingMovies();
        reportService.peakHours();
        reportService.concessionReport();
    }

    public boolean checkBookingExists(String targetId) {
        return bookingService.existsByOrderOrBookingId(targetId);
    }
}
