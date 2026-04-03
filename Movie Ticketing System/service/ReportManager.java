package service;

public class ReportManager {
    private final ReportService reportService;

    public ReportManager() {
        this.reportService = new ReportService();
    }

    public void generateFinancialReport() {
        reportService.bestSellingMovies();
        reportService.peakHours();
    }

    public boolean checkBookingExists(String targetId) {
        return false;
    }
}