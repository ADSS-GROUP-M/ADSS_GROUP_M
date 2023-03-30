package TransportModule.BusinessLayer;

import TransportModule.BusinessLayer.Records.ItemList;

import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;
public class ItemListsController {

    private TreeMap<Integer, ItemList> itemLists;
    private int idCounter;

    public ItemListsController(){
        itemLists = new TreeMap<>();
        idCounter = 0; // this will have to be restored from the DB in the future
    }
    public void addItemList(ItemList itemList) throws IOException{
        if(itemLists.containsKey(itemList.id()) == true)
            throw new IOException("Item list already exists");

        itemLists.put(idCounter++, itemList);
    }

    public ItemList createItemList(String json){
        ItemList itemList = ItemList.parse(idCounter++,json);
        itemLists.put(itemList.id(), itemList);
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

    public LinkedList<ItemList> getAllItemLists() {
        LinkedList<ItemList> list = new LinkedList<>();
        for (ItemList i : itemLists.values())
            list.add(i);
        return list;
    }
}
