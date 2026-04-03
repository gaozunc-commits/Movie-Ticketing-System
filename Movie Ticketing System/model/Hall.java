package model;

public class Hall {
    private int hallNumber;
    private int capacity;
    private String screenType;
    private String[][] seatMap;

    public Hall(int hallNumber, int capacity, String screenType, int rows, int cols) {
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

    public int getHallNumber() {
        return hallNumber;
    }

    public void setHallNumber(int hallNumber) {
        if (hallNumber <= 0) {
            throw new IllegalArgumentException("Hall number must be positive.");
        }
        this.hallNumber = hallNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        this.capacity = capacity;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        if (screenType == null || screenType.trim().isEmpty()) {
            throw new IllegalArgumentException("Screen type cannot be empty.");
        }
        this.screenType = screenType.trim();
    }

    public String[][] getSeatMap() {
        return seatMap;
    }

    public void displaySeatMap(boolean[][] bookedSeats) {
        System.out.println("\n--- SCREEN THIS WAY ---");
        System.out.println("VIP/STD with O(open) or X(booked)\n");
        for (int i = 0; i < seatMap.length; i++) {
            char rowLabel = (char) ('A' + i);
            System.out.print(rowLabel + " ");
            for (int j = 0; j < seatMap[i].length; j++) {
                String marker = bookedSeats[i][j] ? "X" : "O";
                System.out.print(seatMap[i][j] + "-" + marker + "   ");
            }
            System.out.println();
        }
        System.out.print("  ");
        for (int j = 0; j < seatMap[0].length; j++) {
            System.out.print((j + 1) + "       ");
        }
        System.out.println();
    }
}