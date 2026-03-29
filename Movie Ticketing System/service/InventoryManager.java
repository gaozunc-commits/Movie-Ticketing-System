package service;

import model.ConcessionItem;
import util.FileHandler;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<ConcessionItem> availableItems;
    private final String FILE_PATH = "data/inventory.txt";

    public InventoryManager() {
        this.availableItems = new ArrayList<>();
        loadInventory();
    }

    // --- ADD THIS METHOD (Main.java needs this!) ---
    public ConcessionItem getItem(int index, int qty) {
        if (index >= 0 && index < availableItems.size()) {
            ConcessionItem original = availableItems.get(index);
            // Return a new item with the specific quantity the customer wants
            return new ConcessionItem(original.getName(), original.getPrice(), qty);
        }
        return null;
    }

    // CREATE / ADD new snack type
    public void addSnackType(String name, double price, int stock) {
        availableItems.add(new ConcessionItem(name, price, stock));
        saveInventory();
    }

    // READ / DISPLAY Menu
    public void displaySnackMenu() {
        if (availableItems.isEmpty()) {
            System.out.println("⚠️ Snack inventory is empty!");
            return;
        }
        System.out.println("\n--- SNACK BAR MENU ---");
        for (int i = 0; i < availableItems.size(); i++) {
            ConcessionItem item = availableItems.get(i);
            System.out.println((i + 1) + ". " + item.getName() + " - RM " + item.getPrice());
        }
    }

    private void saveInventory() {
        List<String> data = new ArrayList<>();
        for (ConcessionItem item : availableItems) {
            data.add(item.getName() + "|" + item.getPrice() + "|" + item.getQuantity());
        }
        // Use the local FILE_PATH variable
        FileHandler.saveToFile(FILE_PATH, data);
    }

    private void loadInventory() {
        availableItems.clear(); // Clear existing to avoid duplicates on reload
        List<String> lines = FileHandler.readFromFile(FILE_PATH);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length == 3) {
                availableItems.add(new ConcessionItem(parts[0], 
                                   Double.parseDouble(parts[1]), 
                                   Integer.parseInt(parts[2])));
            }
        }
    }
}