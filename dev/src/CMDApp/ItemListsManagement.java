package CMDApp;

import TransportModule.BusinessLayer.Records.ItemList;

import java.util.HashMap;

import static CMDApp.Main.*;

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
        HashMap<String,Integer> items = new HashMap<>();
        System.out.println("=========================================");
        System.out.println("Item list ID: "+itemListIdCounter);
        System.out.println("Enter items details:");
        System.out.println("To finish adding items, enter \"done!\" in the item name");
        while(true){
            String item = getString("Item name: ").toLowerCase();
            if(item.equalsIgnoreCase("done!")){
                break;
            }
            int quantity = getInt("Quantity: ");
            items.put(item,quantity);
        }
        //TODO: code for adding item list

        System.out.println("\nItem list added successfully!");
        itemListIdCounter++;
    }

    private static void updateItemList() {
        while (true) {
            System.out.println("=========================================");
            System.out.println("Select item list to update:");
            int id = getInt("Item list ID: ");
            //TODO: code for fetching item list
            ItemList list = itemList1;
            System.out.println("\nTo remove items from the list enter the item name and 0 in the quantity");
            System.out.println("To finish updating items enter \"done!\" in the item name");


            //loading item list
            System.out.println("\nItem unloading list:");
            for (String key  : list.load().keySet()){
                System.out.println("  "+key+" : "+list.load().get(key));
            }
            while (true) {
                String item = getString("\nItem name: ");
                if(item.equalsIgnoreCase("done!")){
                    break;
                }
                int quantity = getInt("Quantity: ");
                if(quantity == 0) {
                    list.load().remove(item);
                }
                else {
                    list.load().put(item,quantity);
                }
            }

            //unloading item list
            System.out.println("\nItem loading list:");
            for (String key  : list.unload().keySet()){
                System.out.println("  "+key+" : "+list.unload().get(key));
            }
            while (true) {
                String item = getString("\nItem name: ");
                if(item.equalsIgnoreCase("done!")){

                    //TODO: code for updating item list
                    System.out.println("\nItem list updated successfully!");
                    return;
                }
                int quantity = getInt("Quantity: ");
                if(quantity == 0) {
                    list.unload().remove(item);
                }
                else {
                    list.unload().put(item,quantity);
                }
            }
        }
    }

    private static void removeItemList() {
        while (true) {
            System.out.println("=========================================");
            System.out.println("Select item list to remove:");
            int id = getInt("Item list ID: ");

            //TODO: code for fetching item list and confirming removal

            //TODO: code for removing item list

            System.out.println("\nItem list removed successfully!");
        }
    }

    private static void viewItemList() {
        System.out.println("=========================================");
        System.out.println("Select item list to view:");
        int id = getInt("Item list ID: ");
        if(id == -1){
            return;
        }
        //TODO: code for fetching item list
        ItemList list = itemList1;
        System.out.println("\nItems loading list:");
        for (String key : list.load().keySet()) {
            System.out.println("  "+key + " : " + list.load().get(key));
        }
        System.out.println("\nItems unloading list:");
        for (String key : list.unload().keySet()) {
            System.out.println("  "+key + " : " + list.unload().get(key));
        }
        System.out.println("\nPress enter to return to previous menu");
        getString();
    }

    private static void viewAllItemLists() {
        System.out.println("=========================================");
        System.out.println("All item lists:");
        //TODO: code for fetching all item lists
        ItemList[] itemLists = {itemList1};
        for (ItemList list : itemLists) {
            System.out.println("-----------------------------------------");
            System.out.println("Item list ID: " + list.id());
            System.out.println("Items loading list:");
            for (String key : list.load().keySet()) {
                System.out.println("  "+key + " : " + list.load().get(key));
            }
            System.out.println("Items unloading list:");
            for (String key : list.unload().keySet()) {
                System.out.println("  "+key + " : " + list.unload().get(key));
            }
            System.out.println();
        }
        System.out.println("\nPress enter to return to previous menu");
        getString();
    }
}
