package service;

import model.ConcessionItem;
import util.FileHandler;

public class ConcessionService {
    private static final String FILE_PATH = "data/inventory.txt";
    private ConcessionItem[] items;
    private int itemCount;

    public ConcessionService() {
        items = new ConcessionItem[0];
        itemCount = 0;
        load();
    }

    public void createItem(ConcessionItem item) {
        items = appendItem(items, item);
        itemCount++;
        save();
    }

    public ConcessionItem[] readAllItems() {
        ConcessionItem[] copied = new ConcessionItem[itemCount];
        for (int i = 0; i < itemCount; i++) {
            copied[i] = items[i];
        }
        return copied;
    }

    public ConcessionItem readItemByIndex(int index) {
        if (index < 0 || index >= itemCount) {
            throw new ArrayIndexOutOfBoundsException("Concession index out of range.");
        }
        return items[index];
    }

    public void updateItem(int index, String name, double price, int stock) {
        ConcessionItem item = readItemByIndex(index);
        item.setName(name);
        item.setPrice(price);
        item.setStock(stock);
        save();
    }

    public void updateStock(int index, int stock) {
        ConcessionItem item = readItemByIndex(index);
        item.setStock(stock);
        save();
    }

    public void addStock(int index, int quantityToAdd) {
        if (quantityToAdd <= 0) {
            throw new ArrayIndexOutOfBoundsException("Stock to add must be positive.");
        }
        ConcessionItem item = readItemByIndex(index);
        item.setStock(item.getStock() + quantityToAdd);
        save();
    }

    public void deductStock(int index, int quantity) {
        ConcessionItem item = readItemByIndex(index);
        if (item.getStock() < quantity) {
            throw new ArrayIndexOutOfBoundsException("Not enough stock for " + item.getName());
        }
        item.setStock(item.getStock() - quantity);
        save();
    }

    public void deleteItem(int index) {
        if (index < 0 || index >= itemCount) {
            throw new ArrayIndexOutOfBoundsException("Concession index out of range.");
        }
        items = removeItemAt(items, index);
        itemCount--;
        save();
    }

    private void save() {
        String[] lines = new String[itemCount];
        for (int i = 0; i < itemCount; i++) {
            ConcessionItem item = items[i];
            lines[i] = item.getName() + "|" + item.getPrice() + "|" + item.getStock();
        }
        FileHandler.overwriteFile(FILE_PATH, lines);
    }

    private void load() {
        items = new ConcessionItem[0];
        itemCount = 0;
        String[] lines = FileHandler.readFromFile(FILE_PATH);
        for (String line : lines) {
            try {
                String[] parts = line.split("\\|");
                if (parts.length != 3) {
                    continue;
                }
                items = appendItem(items, new ConcessionItem(parts[0], Double.parseDouble(parts[1]), Integer.parseInt(parts[2])));
                itemCount++;
            } catch (Exception e) {
                System.out.println("Skipping invalid concession record: " + line);
            }
        }
    }

    private ConcessionItem[] appendItem(ConcessionItem[] source, ConcessionItem item) {
        ConcessionItem[] expanded = new ConcessionItem[source.length + 1];
        for (int i = 0; i < source.length; i++) {
            expanded[i] = source[i];
        }
        expanded[source.length] = item;
        return expanded;
    }

    private ConcessionItem[] removeItemAt(ConcessionItem[] source, int index) {
        ConcessionItem[] reduced = new ConcessionItem[source.length - 1];
        int target = 0;
        for (int i = 0; i < source.length; i++) {
            if (i == index) {
                continue;
            }
            reduced[target++] = source[i];
        }
        return reduced;
    }

    public void displayConcessions() {
        ConcessionItem[] allItems = readAllItems();
        System.out.println("\n----------------------------------------------------------------");
        System.out.printf("%-5s %-22s %-12s %-10s%n", "No.", "Item", "Price (RM)", "Stock");
        System.out.println("----------------------------------------------------------------");
        if (allItems.length == 0) {
            System.out.println("No concession items available.");
            System.out.println("----------------------------------------------------------------");
            return;
        }
        for (int i = 0; i < allItems.length; i++) {
            ConcessionItem item = allItems[i];
            System.out.printf("%-5d %-22s %-12.2f %-10d%n", i + 1, item.getName(), item.getPrice(), item.getStock());
        }
        System.out.println("----------------------------------------------------------------");
    }
}
