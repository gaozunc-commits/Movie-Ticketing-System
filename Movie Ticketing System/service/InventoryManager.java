package service;

import model.ConcessionItem;

public class InventoryManager {
    private final ConcessionService concessionService;

    public InventoryManager() {
        this.concessionService = new ConcessionService();
    }

    public ConcessionItem getItem(int index, int qty) {
        ConcessionItem original = concessionService.readItemByIndex(index);
        return new ConcessionItem(original.getName(), original.getPrice(), qty);
    }

    public void addSnackType(String name, double price, int stock) {
        concessionService.createItem(new ConcessionItem(name, price, stock));
    }

    public void displaySnackMenu() {
        ConcessionItem[] availableItems = concessionService.readAllItems();
        if (availableItems.length == 0) {
            System.out.println("Snack inventory is empty.");
            return;
        }
        System.out.println("\n--- SNACK BAR MENU ---");
        for (int i = 0; i < availableItems.length; i++) {
            ConcessionItem item = availableItems[i];
            System.out.println((i + 1) + ". " + item.getName() + " - RM " + item.getPrice());
        }
    }
}