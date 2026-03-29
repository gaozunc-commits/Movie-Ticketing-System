package model;

public class Showtime {
    private Movie movie;
    private Hall hall;
    private String time;
    private boolean[][] seats; // true = booked, false = available

    // Constructor
    public Showtime(Movie movie, Hall hall, String time, int rows, int cols) {
        this.movie = movie;
        this.hall = hall;
        this.time = time;
        this.seats = new boolean[rows][cols];
    }

    // Getters
    public Movie getMovie() {
        return movie;
    }

    public Hall getHall() {
        return hall;
    }

    public String getTime() {
        return time;
    }

    public boolean[][] getSeats() {
        return seats;
    }

    // Display seat map
    public void displaySeats() {
        System.out.println("\n===== SEAT MAP =====");
        System.out.println("O = Available | X = Booked");

        for (int i = 0; i < seats.length; i++) {
            System.out.print("Row " + (i + 1) + ": ");
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j]) {
                    System.out.print("X ");
                } else {
                    System.out.print("O ");
                }
            }
            System.out.println();
        }
    }

    // Book a seat
    public boolean bookSeat(int row, int col) {
        if (row < 0 || row >= seats.length || col < 0 || col >= seats[0].length) {
            System.out.println("Invalid seat position!");
            return false;
        }

        if (seats[row][col]) {
            System.out.println("Seat already booked!");
            return false;
        }

        seats[row][col] = true;
        return true;
    }
}