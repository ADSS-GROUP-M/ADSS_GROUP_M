import presentationLayer.inventoryModule.InventoryMain;
import presentationLayer.suppliersModule.SupplierMain;

import java.util.Scanner;

public class Main_suppliersInventory {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        while (run) {
            System.out.println("HI!\nwhat menu would you like?\n\t1. Suppliers\n\t2. Inventory");
            int choice = Integer.parseInt(sc.nextLine());
            if (choice == 1) {
                new SupplierMain().run();
            } else if (choice == 2) {
                new InventoryMain().run();
            }
            System.out.println("would you like to go back to main menu? y/n");
            String input = sc.nextLine();
            run = input.equals("y");
        }

    }
}
