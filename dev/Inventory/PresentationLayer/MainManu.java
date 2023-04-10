package dev.PresentationLayer;
import java.util.*;

public class MainMenu {
    String branch;
    Scanner in;

    public MainMenu(){
        branch = "";
        in = new Scanner(System.in);
    }

    public MainMenu run() {
        System.out.println("Please select a branch:");
        branch = in.nextLine();
        System.out.println("Please select an option:");
        System.out.println("1. Manage reports");
        System.out.println("2. Manage inventory's products");
        System.out.println("3. Manage Discounts");
        System.out.println("4. Manage Catagories");
        System.out.println("5. Exit");
        int option = in.nextLine();
        switch (option) {
            case 1 -> ReportsMenu.run();
            case 2 -> InventoryProductsManu.run();
            case 3 -> DiscountsManu.run();
            case 4 -> CatagoriesManu.run();
            case 5 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }
}
