package BusinessLayer;
import ServiceLayer.JsonSerializer;

import java.util.HashMap;
import java.util.TreeSet;
public class ItemListsController {
    TreeSet<ItemList> itemLists;
    public ItemListsController(){
        itemLists = new TreeSet<>();
    }
    public ItemList createItemList(HashMap<String, Integer> items){
        int id =0;
        ItemList itemList = new ItemList(id, items);
        itemLists.add(itemList);
        id++;
        return itemList;
    }
    public ItemList createItemList(JsonSerializer items){
       return null;
    }
    public ItemList getItemList(int id){
        for (ItemList itemList : itemLists) {
            if (itemList.getId() == id) {
                return itemList;
            }
        }
        return null;
    }
    public ItemList removeItemList(int id) throws Exception {
        ItemList itemList = getItemList(id);
        if(itemList != null){
            itemLists.remove(itemList);
            return itemList;
        }
        throw new Exception("Item list not found");

    }
    public boolean updateItemList(int id, ItemList newItemList){
        ItemList itemList = getItemList(id);
        if(itemList != null){
            itemLists.remove(itemList);
            itemLists.add(newItemList);
            return true;
        }
        return false;
    }


}
