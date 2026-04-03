package util;

public class InputValidator {
    public static int parseIntInRange(String value, int min, int max) {
        try {
            int number = Integer.parseInt(value.trim());
            if (number < min || number > max) {
                throw new IllegalArgumentException("Value must be between " + min + " and " + max + ".");
            }
            return number;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Please enter a valid integer.");
        }
    }

    public static double parsePositiveDouble(String value) {
        try {
            double number = Double.parseDouble(value.trim());
            if (number <= 0) {
                throw new IllegalArgumentException("Value must be positive.");
            }
            return number;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Please enter a valid decimal number.");
        }
    }

    public static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    public static void validateSeatFormat(String seat) {
        if (seat == null || !seat.trim().toUpperCase().matches("^[A-Z][0-9]{1,2}$")) {
            throw new IllegalArgumentException("Seat format must be like A1.");
        }
    }
}
