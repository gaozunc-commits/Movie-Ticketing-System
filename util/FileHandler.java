package util;

import java.io.*;

public class FileHandler {
    public static void saveToFile(String fileName, String[] data) throws ArrayIndexOutOfBoundsException {
        ensureParentDirectory(fileName);
        boolean success = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
            success = true;
        } catch (IOException e) {
            throw new ArrayIndexOutOfBoundsException("Error saving to file: " + e.getMessage());
        } finally {
            if (!success) {
                System.out.println("saveToFile finished with errors.");
            }
        }
    }

    public static void overwriteFile(String fileName, String[] data) throws ArrayIndexOutOfBoundsException {
        ensureParentDirectory(fileName);
        boolean success = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
            success = true;
        } catch (IOException e) {
            throw new ArrayIndexOutOfBoundsException("Error writing file: " + e.getMessage());
        } finally {
            if (!success) {
                System.out.println("overwriteFile finished with errors.");
            }
        }
    }

    public static String[] readFromFile(String fileName) throws ArrayIndexOutOfBoundsException {
        String[] data = new String[0];
        File file = new File(fileName);
        if (!file.exists()) {
            return data;
        }

        boolean success = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data = appendString(data, line);
            }
            success = true;
        } catch (IOException e) {
            throw new ArrayIndexOutOfBoundsException("Error reading file: " + e.getMessage());
        } finally {
            if (!success) {
                System.out.println("readFromFile finished with errors.");
            }
        }
        return data;
    }

    private static void ensureParentDirectory(String fileName) {
        File file = new File(fileName);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    private static String[] appendString(String[] source, String value) {
        String[] expanded = new String[source.length + 1];
        for (int i = 0; i < source.length; i++) {
            expanded[i] = source[i];
        }
        expanded[source.length] = value;
        return expanded;
    }
}