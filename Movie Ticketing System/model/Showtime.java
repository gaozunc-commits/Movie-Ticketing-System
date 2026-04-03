package model;

public class Showtime {
    private String showtimeId;
    private Movie movie;
    private Hall hall;
    private String time;
    private boolean[][] seatMap;

    public Showtime(String showtimeId, Movie movie, Hall hall, String time) {
        this.showtimeId = showtimeId;
        this.movie = movie;
        this.hall = hall;
        this.time = time;
        this.seatMap = new boolean[hall.getSeatMap().length][hall.getSeatMap()[0].length];
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public Movie getMovie() {
        return movie;
    }

    public Hall getHall() {
        return hall;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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
            throw new IllegalArgumentException("Seat format must be like A1.");
        }
        String normalized = seatNumber.trim().toUpperCase();
        int row = normalized.charAt(0) - 'A';
        int col = Integer.parseInt(normalized.substring(1)) - 1;

        if (row < 0 || row >= seatMap.length || col < 0 || col >= seatMap[0].length) {
            throw new IllegalArgumentException("Seat is out of hall range.");
        }
        return new int[] { row, col };
    }
}