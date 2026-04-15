package model;

public class Showtime {
    private String showtimeId;
    private Movie movie;
    private Hall hall;
    private String time;
    private boolean[][] seatMap;

    // Parameterized constructor to initialize showtime details and seats.
    public Showtime(String showtimeId, Movie movie, Hall hall, String time) throws ArrayIndexOutOfBoundsException {
        setShowtimeId(showtimeId);
        setMovie(movie);
        setHall(hall);
        setTime(time);
        this.seatMap = new boolean[hall.getSeatMap().length][hall.getSeatMap()[0].length];
    }

    // Getter for showtime ID.
    public String getShowtimeId() {
        return showtimeId;
    }

    // Setter for showtime ID with validation.
    public void setShowtimeId(String showtimeId) {
        if (showtimeId == null || showtimeId.trim().isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Showtime ID cannot be empty.");
        }
        this.showtimeId = showtimeId.trim();
    }

    // Getter for movie associated with the showtime.
    public Movie getMovie() {
        return movie;
    }

    // Setter for associated movie with validation.
    public void setMovie(Movie movie) {
        if (movie == null) {
            throw new ArrayIndexOutOfBoundsException("Showtime must reference a valid movie.");
        }
        this.movie = movie;
    }

    // Getter for hall associated with the showtime.
    public Hall getHall() {
        return hall;
    }

    // Setter for associated hall with validation.
    public void setHall(Hall hall) {
        if (hall == null || hall.getSeatMap() == null || hall.getSeatMap().length == 0 || hall.getSeatMap()[0].length == 0) {
            throw new ArrayIndexOutOfBoundsException("Showtime must reference a valid hall.");
        }
        this.hall = hall;
    }

    // Getter for showtime clock value.
    public String getTime() {
        return time;
    }

    // Setter for showtime clock value with validation.
    public void setTime(String time) {
        if (time == null || time.trim().isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Showtime time cannot be empty.");
        }
        this.time = time.trim();
    }

    // Getter for booking state map.
    public boolean[][] getSeatMap() {
        return seatMap;
    }

    public void displaySeats() {
        hall.displaySeatMap(seatMap);
    }

    public String getSeatType(String seatNumber) {
        int[] position = getSeatPosition(seatNumber);
        return hall.getSeatMap()[position[0]][position[1]];
    }

    public boolean bookSeat(String seatNumber) {
        int[] position = getSeatPosition(seatNumber);
        int row = position[0];
        int col = position[1];
        if (seatMap[row][col]) {
            System.out.println("Seat already booked.");
            return false;
        }
        seatMap[row][col] = true;
        return true;
    }

    public void releaseSeat(String seatNumber) {
        int[] position = getSeatPosition(seatNumber);
        seatMap[position[0]][position[1]] = false;
    }

    private int[] getSeatPosition(String seatNumber) {
        if (seatNumber == null || seatNumber.length() < 2) {
            throw new ArrayIndexOutOfBoundsException("Seat format must be like A1.");
        }
        String normalized = seatNumber.trim().toUpperCase();
        int row = normalized.charAt(0) - 'A';
        int col = Integer.parseInt(normalized.substring(1)) - 1;

        if (row < 0 || row >= seatMap.length || col < 0 || col >= seatMap[0].length) {
            throw new ArrayIndexOutOfBoundsException("Seat is out of hall range.");
        }
        return new int[] { row, col };
    }

    public String toString() {
        return String.format("%s | %-20s | Hall %-3d | %s", showtimeId, movie.getTitle(), hall.getHallNumber(), time);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Showtime)) {
            return false;
        }
        Showtime other = (Showtime) obj;
        return showtimeId == null ? other.showtimeId == null : showtimeId.equals(other.showtimeId);
    }

    public int hashCode() {
        return 31 + (showtimeId == null ? 0 : showtimeId.hashCode());
    }
}