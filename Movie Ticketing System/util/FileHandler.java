package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    // Generic method to save a list of strings to a file
    public static void saveToFile(String fileName, List<String> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    // Generic method to read all lines from a file
    public static List<String> readFromFile(String fileName) {
        List<String> data = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return data;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return data;
    }
}