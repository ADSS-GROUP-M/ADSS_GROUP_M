package CMDApp;

import java.util.HashMap;

import static CMDApp.Main.getInt;
import static CMDApp.Main.getString;

public class ItemListsManagement {

    static void manageItemLists() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Item lists management");
            System.out.println("Please select an option:");
            System.out.println("1. Create new item list");
            System.out.println("2. Update item list");
            System.out.println("3. Remove item list");
            System.out.println("4. View full item list information");
            System.out.println("5. View all item lists");
            System.out.println("6. Return to previous menu");
            int option = getInt();
            switch (option){
                case 1:
                    createItemList();
                    break;
                case 2:
                    updateItemList();
                    break;
                case 3:
                    removeItemList();
                    break;
                case 4:
                    viewItemList();
                    break;
                case 5:
                    viewAllItemLists();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void createItemList() {

    }

    private static void updateItemList() {

    }

    private static void removeItemList() {

    }

    private static void viewItemList() {

    }

    private static void viewAllItemLists() {

    }

    //==========================================================================|
    //========================== Helper methods ================================|
    //==========================================================================|


    private static void itemListTemp() {
        HashMap<String,Integer> items = new HashMap<>();
        System.out.println("Enter items details:");
        System.out.println("To finish adding items, enter \"done\" in the item name");
        while(true){
            String item = getString("Item name: ");
            if(item.equalsIgnoreCase("done")){
                break;
            }
            int quantity = getInt("Quantity: ");
            items.put(item,quantity);
        }
    }
}
