package service;

import model.Order;

public class ReportManager {

    private final ReportService reportService;

    public ReportManager() {
        this.reportService = new ReportService();
    }

    public void generateFinancialReport(Order[] orders) {
        reportService.bestSellingMovies();
        reportService.peakHours();
        reportService.concessionReport();
    }

    public boolean checkBookingExists(String targetId) {
        return false; 
    }
}