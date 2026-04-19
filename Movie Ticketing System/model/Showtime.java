package model;

import java.util.Objects;

public class Showtime {

    private String showtimeId;
    private Movie movie;
    private Hall hall;
    private String date;
    private String time;
    private boolean[][] seatMap;

    public Showtime(String showtimeId, Movie movie, Hall hall, String date, String time) {
        setShowtimeId(showtimeId);
        setMovie(movie);
        setHall(hall);
        setDate(date);
        setTime(time);

        this.seatMap = new boolean[hall.getSeatMap().length][hall.getSeatMap()[0].length];
    }

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

    public String getTime() {
        return time;
    }

    public int getSeatRowCount() {
        return seatMap.length;
    }

    public int getSeatColumnCount() {
        return seatMap[0].length;
    }

    /**
     * Snapshot of seat occupancy (defensive copy). Mutating the returned array does not change this showtime.
     */
    public boolean[][] copySeatOccupancy() {
        boolean[][] copy = new boolean[seatMap.length][seatMap[0].length];
        for (int r = 0; r < seatMap.length; r++) {
            System.arraycopy(seatMap[r], 0, copy[r], 0, seatMap[r].length);
        }
        return copy;
    }

    /**
     * Replaces occupancy from a snapshot (e.g. after loading from disk). Dimensions must match the hall layout.
     */
    public void importSeatOccupancy(boolean[][] grid) {
        if (grid == null) {
            throw new IllegalArgumentException("Seat grid cannot be null.");
        }
        if (grid.length != seatMap.length || grid[0].length != seatMap[0].length) {
            throw new IllegalArgumentException("Seat grid dimensions do not match hall layout.");
        }
        for (int r = 0; r < seatMap.length; r++) {
            System.arraycopy(grid[r], 0, seatMap[r], 0, seatMap[r].length);
        }
    }

    public void setShowtimeId(String showtimeId) {
        if (showtimeId == null || showtimeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Showtime ID cannot be empty");
        }
        this.showtimeId = showtimeId.trim();
    }

    public void setMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null");
        }
        this.movie = movie;
    }

    public void setHall(Hall hall) {
        if (hall == null) {
            throw new IllegalArgumentException("Hall cannot be null");
        }
        this.hall = hall;
    }

    public void setDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Date cannot be empty");
        }
        this.date = date.trim();
    }

    public void setTime(String time) {
        if (time == null || time.trim().isEmpty()) {
            throw new IllegalArgumentException("Time cannot be empty");
        }
        this.time = time.trim();
    }

    public void displaySeats() {
        hall.displaySeatMap(seatMap);
    }

    public String getSeatType(String seatNumber) {
        int[] pos = getSeatPosition(seatNumber);
        return hall.getSeatMap()[pos[0]][pos[1]];
    }

    public synchronized boolean bookSeat(String seatNumber) {
        int[] pos = getSeatPosition(seatNumber);
        if (seatMap[pos[0]][pos[1]]) {
            return false;
        }
        seatMap[pos[0]][pos[1]] = true;
        return true;
    }

    public synchronized void releaseSeat(String seatNumber) {
        int[] pos = getSeatPosition(seatNumber);
        seatMap[pos[0]][pos[1]] = false;
    }

    @Override
    public String toString() {
        return showtimeId + " | " + (movie != null ? movie.getTitle() : "?") + " | " + date + " " + time;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Showtime)) {
            return false;
        }
        Showtime other = (Showtime) obj;
        return Objects.equals(showtimeId, other.showtimeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(showtimeId);
    }

    private int[] getSeatPosition(String seatNumber) {
        String s = seatNumber.trim().toUpperCase();
        int row = s.charAt(0) - 'A';
        int col = Integer.parseInt(s.substring(1)) - 1;
        return new int[]{row, col};
    }
}
