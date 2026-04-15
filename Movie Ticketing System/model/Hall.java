package model;

public class Hall {
    private int hallNumber;
    private int capacity;
    private String screenType;
    private String[][] seatMap;

    // Parameterized constructor to initialize hall details and seat layout.
    public Hall(int hallNumber, int capacity, String screenType, int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new ArrayIndexOutOfBoundsException("Rows and columns must be positive.");
        }
        if (capacity != rows * cols) {
            throw new ArrayIndexOutOfBoundsException("Capacity must match rows x cols.");
        }
        setHallNumber(hallNumber);
        setCapacity(capacity);
        setScreenType(screenType);
        this.seatMap = new String[rows][cols];
        initializeSeats();
    }

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

    // Getter for hall number.
    public int getHallNumber() {
        return hallNumber;
    }

    // Setter for hall number with validation.
    public void setHallNumber(int hallNumber) {
        if (hallNumber <= 0) {
            throw new ArrayIndexOutOfBoundsException("Hall number must be positive.");
        }
        this.hallNumber = hallNumber;
    }

    // Getter for hall capacity.
    public int getCapacity() {
        return capacity;
    }

    // Setter for hall capacity with validation.
    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new ArrayIndexOutOfBoundsException("Capacity must be positive.");
        }
        this.capacity = capacity;
    }

    // Getter for hall screen type.
    public String getScreenType() {
        return screenType;
    }

    // Setter for screen type with validation.
    public void setScreenType(String screenType) {
        if (screenType == null || screenType.trim().isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Screen type cannot be empty.");
        }
        this.screenType = screenType.trim();
    }

    // Getter for hall seat map template.
    public String[][] getSeatMap() {
        return seatMap;
    }

    public void displaySeatMap(boolean[][] bookedSeats) {
        System.out.println("\n============================== SCREEN ==============================");
        System.out.println("Legend: VIP-O = VIP Available | VIP-X = VIP Booked");
        System.out.println("        STD-O = Standard Available | STD-X = Standard Booked");
        System.out.println("--------------------------------------------------------------------");

        System.out.print("      ");
        for (int j = 0; j < seatMap[0].length; j++) {
            System.out.printf("%-8d", j + 1);
        }
        System.out.println();
        System.out.println("--------------------------------------------------------------------");

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