package TransportModule.BusinessLayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
public class ItemListsController {

    private TreeMap<Integer,ItemList> itemLists;
    private int idCounter;

    public ItemListsController(){
        itemLists = new TreeMap<>();
        idCounter = 0; // this will have to be restored from the DB in the future
    }
    public ItemList createItemList(HashMap<String, Integer> items){
        ItemList itemList = new ItemList(idCounter++, items);
        itemLists.put(itemList.getId(), itemList);
        return itemList;
    }
    public ItemList createItemList(String json){
        ItemList itemList = ItemList.parse(idCounter++,json);
        itemLists.put(itemList.getId(), itemList);
        return itemList;
    }

    public ItemList getItemList(int id) throws IOException{
        if(itemLists.containsKey(id) == false)
            throw new IOException("Item list not found");

        return itemLists.get(id);
    }
    public ItemList removeItemList(int id) throws IOException {
        if (itemLists.containsKey(id) == false)
            throw new IOException("Item list not found");

        return itemLists.remove(id);
    }
    public void updateItemList(int id, ItemList newItemList) throws IOException{
        if(itemLists.containsKey(id) == false)
            throw new IOException("Item list not found");

        itemLists.put(id, newItemList);
    }


}
