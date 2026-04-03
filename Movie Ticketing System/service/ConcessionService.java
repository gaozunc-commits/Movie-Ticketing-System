package service;

import model.ConcessionItem;
import util.FileHandler;

import java.util.ArrayList;
import java.util.List;

public class ConcessionService {
    private static final String FILE_PATH = "data/inventory.txt";
    private final List<ConcessionItem> items;

    public ConcessionService() {
        items = new ArrayList<>();
        load();
    }

    public void createItem(ConcessionItem item) {
        items.add(item);
        save();
    }

    public List<ConcessionItem> readAllItems() {
        return new ArrayList<>(items);
    }

    public ConcessionItem readItemByIndex(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IllegalArgumentException("Concession index out of range.");
        }
        return items.get(index);
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

    public void deductStock(int index, int quantity) {
        ConcessionItem item = readItemByIndex(index);
        if (item.getStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock for " + item.getName());
        }
        item.setStock(item.getStock() - quantity);
        save();
    }

    public void deleteItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IllegalArgumentException("Concession index out of range.");
        }
        items.remove(index);
        save();
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (ConcessionItem item : items) {
            lines.add(item.getName() + "|" + item.getPrice() + "|" + item.getStock());
        }
        FileHandler.overwriteFile(FILE_PATH, lines);
    }

    private void load() {
        items.clear();
        List<String> lines = FileHandler.readFromFile(FILE_PATH);
        for (String line : lines) {
            try {
                String[] parts = line.split("\\|");
                if (parts.length != 3) {
                    continue;
                }
                items.add(new ConcessionItem(parts[0], Double.parseDouble(parts[1]), Integer.parseInt(parts[2])));
            } catch (Exception ex) {
                System.out.println("Skipping invalid concession record: " + line);
            }
        }
    }
}
