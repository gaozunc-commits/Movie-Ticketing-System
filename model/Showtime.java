package model;

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

    public void setShowtimeId(String showtimeId) {
        if (showtimeId == null || showtimeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Showtime ID cannot be empty");
        }
        this.showtimeId = showtimeId.trim();
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        if (movie == null) throw new IllegalArgumentException("Movie cannot be null");
        this.movie = movie;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        if (hall == null) throw new IllegalArgumentException("Hall cannot be null");
        this.hall = hall;
    }

    public String getDate() {  
        return date;
    }

    public void setDate(String date) {
        if (date == null || date.trim().isEmpty())
            throw new IllegalArgumentException("Date cannot be empty");
        this.date = date.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        if (time == null || time.trim().isEmpty())
            throw new IllegalArgumentException("Time cannot be empty");
        this.time = time.trim();
    }

    public boolean[][] getSeatMap() {
        return seatMap;
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
        if (seatMap[pos[0]][pos[1]]) return false;
        seatMap[pos[0]][pos[1]] = true;
        return true;
    }

    public synchronized void releaseSeat(String seatNumber) {
        int[] pos = getSeatPosition(seatNumber);
        seatMap[pos[0]][pos[1]] = false;
    }

    private int[] getSeatPosition(String seatNumber) {
        String s = seatNumber.trim().toUpperCase();
        int row = s.charAt(0) - 'A';
        int col = Integer.parseInt(s.substring(1)) - 1;
        return new int[]{row, col};
    }
}