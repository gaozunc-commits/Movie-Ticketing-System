package service;

/**
 * Thin facade over {@link BookingService} for booking lookups (keeps optional coupling low for callers).
 */
public class BookingManager {

    private final BookingService bookingService;

    public BookingManager(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /** Default wiring when no shared {@link BookingService} is supplied (e.g. standalone tools). */
    public BookingManager() {
        this(new BookingService(new MovieService()));
    }

    public boolean checkBookingExists(String orderId) {
        return bookingService.existsByOrderOrBookingId(orderId);
    }
}
