package util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public final class InputValidator {

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

    /** Inclusive min/max (e.g. price with min 0). */
    public static double parseDoubleInRange(String value, double min, double max) {
        try {
            double number = Double.parseDouble(value.trim());
            if (number < min || number > max) {
                throw new IllegalArgumentException("Value must be between " + min + " and " + max + ".");
            }
            return number;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Please enter a valid decimal number.");
        }
    }
    public static String calculateEndTime(String startTime, int durationMinutes) {

    java.time.LocalTime start = java.time.LocalTime.parse(startTime);
    java.time.LocalTime end = start.plusMinutes(durationMinutes);

    return end.toString();
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

    // -------------------------------------------------------------------------
    // Domain string validation (admin / movie / concession / user)
    // -------------------------------------------------------------------------

    public static String validateMovieTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        return title.trim();
    }

    public static String validateGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be empty.");
        }
        String trimmed = genre.trim();
        if (!trimmed.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Invalid genre! Only letters and space allowed.");
        }
        return trimmed;
    }

    /** ISO-8601 date {@code yyyy-MM-dd}. */
    public static String parseIsoDate(String raw) {
        LocalDate date = LocalDate.parse(raw.trim());
        return date.toString();
    }

    /** 24-hour {@code HH:mm}. */
    public static String parseTimeHHmm(String raw) {
        LocalTime time = LocalTime.parse(raw.trim());
        return time.toString();
    }

    /** Normalised: FOOD, DRINK, or SNACK. */
    public static String validateConcessionCategory(String raw) {
        String category = requireText(raw, "Category").toUpperCase();
        if (!category.equals("FOOD") && !category.equals("DRINK") && !category.equals("SNACK")) {
            throw new IllegalArgumentException("Category must be FOOD, DRINK, or SNACK.");
        }
        return category;
    }

    /** Normalised: ADMIN, STAFF, CUSTOMER. */
    public static String validateUserRole(String raw) {
        String role = requireText(raw, "Role").toUpperCase();
        if (!role.equals("ADMIN") && !role.equals("STAFF") && !role.equals("CUSTOMER")) {
            throw new IllegalArgumentException("Role must be ADMIN, STAFF, or CUSTOMER.");
        }
        return role;
    }

    /** Length 2–12, no spaces. */
    public static String validatePassword(String password) {
        String pw = requireText(password, "Password");
        if (pw.length() < 2) {
            throw new IllegalArgumentException("Password too short!");
        }
        if (pw.length() > 12) {
            throw new IllegalArgumentException("Password too long! Max 12 characters.");
        }
        if (pw.contains(" ")) {
            throw new IllegalArgumentException("Password cannot contain spaces!");
        }
        return pw;
    }

    // -------------------------------------------------------------------------
    // Interactive prompts (Scanner + stdout); re-ask until valid
    // -------------------------------------------------------------------------

    public static int promptInt(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return parseIntInRange(scanner.nextLine(), Integer.MIN_VALUE, Integer.MAX_VALUE);
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }

    public static int promptIntInRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            int value = promptInt(scanner, prompt);
            if (value < min || value > max) {
                System.out.println("Must be between " + min + " and " + max);
                continue;
            }
            return value;
        }
    }

    public static double promptPositiveDouble(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return parsePositiveDouble(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }
    public static double promptPrice2Decimal(Scanner scanner, String prompt) {
    while (true) {
        try {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            double value = Double.parseDouble(input);

            if (value < 0) {
                throw new IllegalArgumentException("Price cannot be negative.");
            }

            // strict 2 decimal check
            if (input.contains(".")) {
                int decimals = input.split("\\.")[1].length();
                if (decimals > 2) {
                    throw new IllegalArgumentException("Only 2 decimal places allowed.");
                }
            }

            return Math.round(value * 100.0) / 100.0;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
    
    public static double parsePrice2Decimal(String value) {
    try {
        double number = Double.parseDouble(value.trim());

        if (number < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        // check 2 decimal places max
        String[] parts = value.trim().split("\\.");

        if (parts.length == 2 && parts[1].length() > 2) {
            throw new IllegalArgumentException("Price only allows 2 decimal places.");
        }

        return Math.round(number * 100.0) / 100.0;

    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid price format.");
    }
}
    public static double promptDoubleInRange(Scanner scanner, String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                return parseDoubleInRange(scanner.nextLine(), min, max);
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }

    public static String promptText(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return requireText(scanner.nextLine(), "Input");
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }

    public static int promptOneBasedIndex(Scanner scanner, String prompt, int length) {
        while (true) {
            if (length <= 0) {
                System.out.println("No records available.");
                return -1;
            }
            int selected = promptInt(scanner, prompt) - 1;
            if (selected >= 0 && selected < length) {
                return selected;
            }
            System.out.println("Index out of range. Try again.");
        }
    }

    public static String promptIsoDate(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return parseIsoDate(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid date format! Use YYYY-MM-DD.");
            }
        }
    }

    public static String promptTimeHHmm(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return parseTimeHHmm(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid time format! Use HH:mm (24-hour).");
            }
        }
    }

    public static boolean isExit(String input) {
        return input != null && input.trim().equalsIgnoreCase("exit");
    }
    public static String promptMovieTitle(Scanner scanner, String prompt) {
    while (true) {
        try {
            String input = promptText(scanner, prompt).trim();

            if (isExit(input)) {
                return null;
            }

            return validateMovieTitle(input);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}  
    public static String promptGenre(Scanner scanner, String prompt) {
        while (true) {
            try {
                return validateGenre(promptText(scanner, prompt));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public static String promptConcessionCategory(Scanner scanner, String prompt) {
        while (true) {
            try {
                return validateConcessionCategory(promptText(scanner, prompt));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String promptUserRole(Scanner scanner, String prompt) {
        while (true) {
            try {
                return validateUserRole(promptText(scanner, prompt));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String promptPassword(Scanner scanner, String prompt) {
        while (true) {
            try {
                return validatePassword(promptText(scanner, prompt));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String promptMovieAgeRating(Scanner scanner) {
        while (true) {
            System.out.println("\n=== AGE RATING ===");
            System.out.println("1. U (General)");
            System.out.println("2. P13 (Parental Guidance 13)");
            System.out.println("3. 18 (18+)");
            System.out.println("0. Cancel");

            int choice = promptIntInRange(scanner, "Choose: ", 0, 3);

            switch (choice) {
                case 1:
                    return "U";
                case 2:
                    return "P13";
                case 3:
                    return "18";
                case 0:
                    System.out.println("Cancelled");
                    return null;
                default:
                    // Exhaustive for 0–3; kept for defensive clarity if promptIntInRange changes.
                    System.out.println("Invalid option");
            }
        }
    }
    public static String promptTimeSlotHHmm(Scanner scanner, String msg) {

    String[] validSlots = {
        "10:00","10:30","11:00","11:30",
        "12:00","12:30","13:00","13:30",
        "14:00","14:30","15:00","15:30",
        "16:00","16:30","17:00","17:30",
        "18:00","18:30","19:00","19:30",
        "20:00"
    };

    while (true) {
        System.out.print(msg);
        String input = scanner.nextLine();

        for (String slot : validSlots) {
            if (slot.equals(input)) {
                return input;
            }
        }

        System.out.println(" Invalid time slot! Use 30-minute interval (10:00 - 20:00).");
    }
}
}
