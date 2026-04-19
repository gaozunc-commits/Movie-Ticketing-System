package model;

public class Showtime {

    private String showtimeId;
    private Movie movie;
    private Hall hall;
    private String date;
    private String startTime;
    private String endTime;

    // booking state (TRUE = booked)
    private boolean[][] seats;

    public Showtime(String showtimeId, Movie movie, Hall hall,
                    String date, String startTime, String endTime) {

        this.showtimeId = showtimeId;
        this.movie = movie;
        this.hall = hall;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;

        this.seats = new boolean[hall.getSeatMap().length][hall.getSeatMap()[0].length];
    }

    // ================= GETTERS =================

    public String getShowtimeId() {
        return showtimeId;
    }

    public Movie getMovie() {
        return movie;
    }

    public Hall getHall() {
        return hall;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean[][] getSeats() {
        return seats;
    }

    // ================= SETTERS (needed by BookingService update) =================

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    // ================= SEAT BOOKING (IMPORTANT FIX) =================

    public boolean bookSeat(String seat) {

        int[] pos = parseSeat(seat);
        int row = pos[0];
        int col = pos[1];

        if (seats[row][col]) {
            return false; // already booked
        }

        seats[row][col] = true;
        return true;
    }

    public void releaseSeat(String seat) {

        int[] pos = parseSeat(seat);
        int row = pos[0];
        int col = pos[1];

        seats[row][col] = false;
    }

    // ================= SEAT PARSER (A1 -> [0][0]) =================

    private int[] parseSeat(String seat) {

        if (seat == null || seat.length() < 2) {
            throw new IllegalArgumentException("Invalid seat format");
        }

        seat = seat.toUpperCase().trim();

        int row = seat.charAt(0) - 'A';
        int col = Integer.parseInt(seat.substring(1)) - 1;

        if (row < 0 || row >= seats.length ||
            col < 0 || col >= seats[0].length) {
            throw new IllegalArgumentException("Seat out of range");
        }

        return new int[]{row, col};
    }

    // ================= DISPLAY =================

    public void displaySeats() {
        hall.displaySeatMap(seats);

        System.out.println("\nBooked Seats:");
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j]) {
                    System.out.print((char) ('A' + i) + "" + (j + 1) + " ");
                }
            }
        }
        System.out.println();
    }
}