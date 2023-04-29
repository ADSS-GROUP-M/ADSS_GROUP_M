package presentationLayer.transportModule;

import objects.transportObjects.ItemList;
import serviceLayer.transportModule.ItemListsService;
import utils.JsonUtils;
import utils.Response;

import java.util.HashMap;

public class ItemListsManagement {

    private final UiData uiData;
    private final ItemListsService ils;

    ItemListsManagement(UiData uiData, ItemListsService ils){
        this.uiData = uiData;
        this.ils = ils;
    }

    void manageItemLists() {
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
            int option = uiData.readInt();
            switch (option) {
                case 1 -> createItemList();
                case 2 -> updateItemList();
                case 3 -> removeItemList();
                case 4 -> viewItemList();
                case 5 -> viewAllItemLists();
                case 6 -> {
                    return;
                }
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private void createItemList() {
        System.out.println("=========================================");
        System.out.println("Enter items details:");
        ItemList list = new ItemList(new HashMap<>(), new HashMap<>());
        System.out.println("\nItem loading list:");
        itemEditor(list.load());
        System.out.println("\nItem unloading list:");
        itemEditor(list.unload());
        String json = list.toJson();
        String responseJson = ils.addItemList(json);
        Response response = JsonUtils.deserialize(responseJson, Response.class);
        if(response.success()) {
            int id = response.dataToInt();
            list = list.newId(id);
            uiData.itemLists().put(id,list);
        }
        System.out.println("\n"+response.message());
    }

    private void updateItemList() {
        while (true) {
            System.out.println("=========================================");
            Integer id = getListID();
            if (id == null) {
                return;
            }

            ItemList oldList = uiData.itemLists().get(id);
            ItemList newList = new ItemList(id, oldList.load(), oldList.unload());

            //unloading item list
            System.out.println("\nItem unloading list:");
            for (String key  : newList.load().keySet()){
                System.out.println("  "+key+" : "+newList.load().get(key));
            }
            itemEditor(newList.load());

            //loading item list
            System.out.println("\nItem loading list:");
            for (String key  : newList.unload().keySet()){
                System.out.println("  "+key+" : "+newList.unload().get(key));
            }
            itemEditor(newList.unload());
            String json = newList.toJson();
            String responseJson = ils.updateItemList(json);
            Response response = JsonUtils.deserialize(responseJson, Response.class);
            if(response.success()) {
                uiData.itemLists().put(id,newList);
            }
            System.out.println("\n"+response.message());
        }
    }

    private void removeItemList() {
        while (true) {
            System.out.println("=========================================");
            Integer id = getListID();
            if (id == null) {
                return;
            }
            ItemList list = uiData.itemLists().get(id);
            printItemList(list);
            System.out.println("Are you sure you want to remove this item list? (y/n)");
            String option = uiData.readLine();
            switch(option){
                case "y" ->{
                    String json = list.toJson();
                    String responseJson = ils.removeItemList(json);
                    Response response = JsonUtils.deserialize(responseJson, Response.class);
                    if(response.success()) {
                        uiData.itemLists().remove(id);
                    }
                    System.out.println("\n"+response.message());
                }
                case "n" ->{
                    return;
                }
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private void viewItemList() {
        System.out.println("=========================================");
        System.out.println("Select item list to view:");
        Integer id = getListID();
        if (id == null) {
            return;
        }
        ItemList list = uiData.itemLists().get(id);
        printItemList(list);
        System.out.println("\nEnter 'done!' to return to previous menu");
        uiData.readLine();
    }

    private void viewAllItemLists() {
        System.out.println("=========================================");
        System.out.println("All item lists:");
        for (ItemList list : uiData.itemLists().values()) {
            System.out.println("-----------------------------------------");
            printItemList(list);
        }
        System.out.println("\nEnter 'done!' to return to previous menu");
        uiData.readWord();
    }

    private void itemEditor(HashMap<String,Integer> items) {
        System.out.println("\nTo remove items from the list enter the item name and 0 in the quantity");
        System.out.println("To finish adding items, enter \"done!\" in the item name");
        while (true) {
            String itemQuantity = uiData.readLine("<Item name> <Quantity>: ").toLowerCase();

            if(itemQuantity.equalsIgnoreCase("done!")){
                break;
            }

            String[] itemQuantityArray = itemQuantity.split(" ");
            StringBuilder item = new StringBuilder();
            for(int i = 0; i < itemQuantityArray.length-1; i++){
                item.append(itemQuantityArray[i]);
                if(i != itemQuantityArray.length-2) {
                    item.append(" ");
                }
            }
            int quantity = Integer.parseInt(itemQuantityArray[itemQuantityArray.length-1]);

            if(quantity <= 0) {
                items.remove(item.toString());
            }
            else {
                items.put(item.toString(),quantity);
            }
        }
    }

    private Integer getListID() {
        System.out.println();
        int id = uiData.readInt("Enter item list ID (enter '-1' to return to the previous menu): ");
        if (id == -1) {
            return null;
        }
        if(uiData.itemLists().containsKey(id) == false){
            System.out.println("Item list with ID "+id+" does not exist!");
            return null;
        }
        return id;
    }

    private void printItemList(ItemList list) {
        System.out.println("Item list ID: " + list.id());
        System.out.println("Items loading list:");
        for (String key : list.load().keySet()) {
            System.out.println("  "+key + " : " + list.load().get(key));
        }
        System.out.println("Items unloading list:");
        for (String key : list.unload().keySet()) {
            System.out.println("  " + key + " : " + list.unload().get(key));
        }
    }
}
