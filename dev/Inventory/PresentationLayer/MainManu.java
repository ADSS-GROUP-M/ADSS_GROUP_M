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
        System.out.println("1. Manage reports");
        System.out.println("5. Exit");
        int option = appData.readInt();
        switch (option) {
            case 1 -> transportsManagement.manageTransports();
            case 2 -> itemListsManagement.manageItemLists();
            case 3 -> manageResources();
            case 4 -> appData.generateData();
            case 5 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void all_menu() {
        System.out.println("Please select an option:");
        System.out.println("1. Manage reports");
        System.out.println("2. Manage inventory's products");
        System.out.println("3. Manage Discounts");
        System.out.println("4. Manage Catagories");
        System.out.println("5. Exit");
        int option = appData.readInt();
        switch (option) {
            case 1 -> transportsManagement.manageTransports();
            case 2 -> itemListsManagement.manageItemLists();
            case 3 -> manageResources();
            case 4 -> appData.generateData();
            case 5 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }
}
