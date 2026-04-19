package service;

import model.ConcessionItem;

/**
 * Facade over {@link ConcessionService} for inventory-focused operations.
 */
public class InventoryManager {

    private final ConcessionService concessionService;

    public InventoryManager(ConcessionService concessionService) {
        this.concessionService = concessionService;
    }

    public InventoryManager() {
        this(new ConcessionService());
    }

    public ConcessionItem getItem(int index, int qty) {
        ConcessionItem original = concessionService.readItemByIndex(index);
        return new ConcessionItem(
                original.getName(),
                original.getPrice(),
                qty,
                original.getCategory()
        );
    }

    public void addSnackType(String name, double price, int stock, String category) {
        concessionService.createItem(
                new ConcessionItem(name, price, stock, category)
        );
    }

    public void displaySnackMenu() {
        ConcessionItem[] availableItems = concessionService.readAllItems();

        if (availableItems.length == 0) {
            System.out.println("Snack inventory is empty.");
            return;
        }

        System.out.println("\n=== SNACK BAR MENU ===");

        for (int i = 0; i < availableItems.length; i++) {
            ConcessionItem item = availableItems[i];

            if (item.getCategory().equalsIgnoreCase("SNACK")) {
                System.out.println((i + 1) + ". "
                        + item.getName() + " - RM " + item.getPrice());
            }
        }
    }
}
