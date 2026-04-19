package model;

public class Hall {

    private int hallNumber;
    private int capacity;
    private String screenType;
    private String[][] seatMap;

    // Constructor
    public Hall(int hallNumber, int capacity, String screenType, int rows, int cols) {

        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Rows and columns must be positive.");
        }

        if (capacity != rows * cols) {
            throw new IllegalArgumentException("Capacity must match rows x cols.");
        }

        setHallNumber(hallNumber);
        setCapacity(capacity);
        setScreenType(screenType);

        this.seatMap = new String[rows][cols];
        initializeSeats();
    }

    // VIP / STD
    private void initializeSeats() {
        for (int i = 0; i < seatMap.length; i++) {
            for (int j = 0; j < seatMap[i].length; j++) {
                if (i < 2) {
                    seatMap[i][j] = "VIP";
                } else {
                    seatMap[i][j] = "STD";
                }
            }
        }
    }

    public int getHallNumber() {
        return hallNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getScreenType() {
        return screenType;
    }

    public String[][] getSeatMap() {
        return seatMap;
    }

    public void setHallNumber(int hallNumber) {
        if (hallNumber <= 0) {
            throw new IllegalArgumentException("Hall number must be positive.");
        }
        this.hallNumber = hallNumber;
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        this.capacity = capacity;
    }

    public void setScreenType(String screenType) {
        if (screenType == null || screenType.trim().isEmpty()) {
            throw new IllegalArgumentException("Screen type cannot be empty.");
        }
        this.screenType = screenType.trim();
    }

    // Display hall seating 
    public void displaySeatMap(boolean[][] bookedSeats) {

        System.out.println("\n============================== SCREEN ==============================");
        System.out.println("Hall: " + hallNumber + " | Screen Type: " + screenType);
        System.out.println("Legend: VIP-O = Available | VIP-X = Booked");
        System.out.println("        STD-O = Available | STD-X = Booked");
        System.out.println("--------------------------------------------------------------------");

        // Column numbers
        System.out.print("      ");
        for (int j = 0; j < seatMap[0].length; j++) {
            System.out.printf("%-8d", j + 1);
        }
        System.out.println();
        System.out.println("--------------------------------------------------------------------");

        // Rows
        for (int i = 0; i < seatMap.length; i++) {
            char rowLabel = (char) ('A' + i);
            System.out.printf("Row %-2s", rowLabel);

            for (int j = 0; j < seatMap[i].length; j++) {
                String marker = bookedSeats[i][j] ? "X" : "O";
                System.out.printf("%-8s", seatMap[i][j] + "-" + marker);
            }

            System.out.println();
        }

        System.out.println("====================================================================");
    }
}