package model;

public class Hall {
    public enum HallCategory {
    STANDARD(10, 10),
    IMAX(7, 6),
    THREE_D(10, 10);

    private final int rows;
    private final int cols;

    HallCategory(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}

    private int hallNumber;
    private HallCategory category;
    private String[][] seatMap;

    private int vipRows = 2;

    // ================= CONSTRUCTOR =================
    public Hall(int hallNumber) {
        setHallNumber(hallNumber);

        this.category = assignCategory(hallNumber);

        int rows = category.getRows();
        int cols = category.getCols();

        this.seatMap = new String[rows][cols];
        initializeSeats();
    }

    // ================= AUTO CATEGORY LOGIC =================
    private HallCategory assignCategory(int hallNumber) {

        if (hallNumber >= 1 && hallNumber <= 4) {
            return HallCategory.STANDARD;

        } else if (hallNumber >= 5 && hallNumber <= 6) {
            return HallCategory.IMAX;

        } else if (hallNumber >= 7 && hallNumber <= 8) {
            return HallCategory.THREE_D;

        } else {
            throw new IllegalArgumentException("Hall number must be between 1 and 8.");
        }
    }

    // ================= SEAT INITIALIZATION =================
    private void initializeSeats() {
        for (int i = 0; i < seatMap.length; i++) {
            for (int j = 0; j < seatMap[i].length; j++) {
                seatMap[i][j] = (i < vipRows) ? "VIP" : "STD";
            }
        }
    }

    // ================= GETTERS =================
    public int getHallNumber() {
        return hallNumber;
    }

    public HallCategory getCategory() {
        return category;
    }

    public String[][] getSeatMap() {
        return seatMap;
    }

    public String getCategoryLabel() {
        return category.name(); // STANDARD / IMAX / THREE_D
    }

    // ================= SETTER =================
    public void setHallNumber(int hallNumber) {
        if (hallNumber <= 0) {
            throw new IllegalArgumentException("Hall number must be positive.");
        }
        this.hallNumber = hallNumber;
    }

    // ================= SEAT TYPE =================
    public String getSeatType(int row, int col) {
        if (row < 0 || row >= seatMap.length || col < 0 || col >= seatMap[0].length) {
            throw new IllegalArgumentException("Invalid seat position.");
        }
        return seatMap[row][col];
    }

    // ================= DISPLAY =================
    public void displaySeatMap(boolean[][] bookedSeats) {

        if (bookedSeats == null) {
            throw new IllegalArgumentException("Booked seats cannot be null");
        }

        if (bookedSeats.length != seatMap.length ||
            bookedSeats[0].length != seatMap[0].length) {
            throw new IllegalArgumentException("Seat map mismatch");
        }

        System.out.println("\n===== SCREEN =====");
        System.out.println("Hall " + hallNumber + " (" + category + ")");
        System.out.println("VIP/STD - O = Available | X = Booked");
        System.out.println("--------------------------------");

        for (int i = 0; i < seatMap.length; i++) {

            char rowLabel = (char) ('A' + i);
            System.out.print(rowLabel + " ");

            for (int j = 0; j < seatMap[i].length; j++) {

                String status = bookedSeats[i][j] ? "X" : "O";
                System.out.print(seatMap[i][j] + "-" + status + " ");
            }

            System.out.println();
        }
    }

    public String getCategoryDisplay() {
        switch (category) {
            case STANDARD: return "Standard";
            case IMAX: return "IMAX";
            case THREE_D: return "3D";
            default: return category.name();
        }
    }
}