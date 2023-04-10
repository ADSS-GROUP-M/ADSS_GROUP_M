package dev.Inventory.PresentationLayer;

import dev.Inventory.ServiceLayer.StockService;

import java.util.*;

public class MainMenu {
    String branch;
    Scanner in;
    StockService inventoryService;

    public MainMenu(){
        branch = "";
        in = new Scanner(System.in);
        inventoryService = new StockService();
    }

    public void run() {
        System.out.println("Please select a branch:");
        branch = in.nextLine();
        System.out.println("Please select an option:");
        System.out.println("1. Manage reports");
        System.out.println("2. Manage inventory's products");
        System.out.println("3. Manage Discounts");
        System.out.println("4. Manage Categories");
        System.out.println("5. Exit");
        int option = in.nextInt();
        switch (option) {
            case 1 -> new ReportsMenu().run();
            case 2 -> new InventoryProductsManu().run();
            case 3 -> new DiscountsManu().run();
            case 4 -> new CategoriesMenu().run();
            case 5 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }
}
