package model;

public class Hall {
    private int hallNumber;
    private int rows;
    private int cols;
    private char[][] seatMap;

    public Hall(int hallNumber, int rows, int cols) {
        this.hallNumber = hallNumber;
        this.rows = rows;
        this.cols = cols;
        this.seatMap = new char[rows][cols];
        initializeSeats();
    }

    private void initializeSeats() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                seatMap[i][j] = 'O'; // 'O' for Available, 'X' for Booked
            }
        }
    }

    public void displaySeatMap() {
        System.out.println("\n--- Screen This Way ---");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(seatMap[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean bookSeat(String seatInput) {
        try {
            // Convert "A1" -> Row 0, Col 0
            int rowIndex = Character.toUpperCase(seatInput.charAt(0)) - 'A';
            int colIndex = Character.getNumericValue(seatInput.charAt(1)) - 1;

            if (rowIndex >= 0 && rowIndex < rows && colIndex >= 0 && colIndex < cols) {
                if (seatMap[rowIndex][colIndex] == 'O') {
                    seatMap[rowIndex][colIndex] = 'X'; // Mark as booked
                    System.out.println("✅ Seat " + seatInput + " confirmed!");
                    return true;
                } else {
                    System.out.println("❌ Seat " + seatInput + " is already taken!");
                }
            } else {
                System.out.println("❌ Invalid seat coordinates!");
            }
        } catch (Exception e) {
            System.out.println("❌ Invalid format! Please use Letter+Number (e.g., A1).");
        }
        return false;
    }
}