package transportModule.backend.businessLayer;

import transportModule.records.ItemList;

import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * The ItemListsController class is responsible for managing item lists in the transport module.
 * It provides methods to add, retrieve, update, and remove item lists from the system.
 */
public class ItemListsController {

    private final TreeMap<Integer, ItemList> itemLists;
    private int idCounter;

    public ItemListsController(){
        itemLists = new TreeMap<>();
        idCounter = 1; // currently not in use.
                // this will have to be restored from the DB in the future.
    }

    /**
     * Adds a new item list to the system.
     *
     * @param itemList The item list to be added.
     * @throws IOException If the item list already exists in the system.
     */
    public void addItemList(ItemList itemList) throws IOException{
        if(itemLists.containsKey(itemList.id()) == true)
            throw new IOException("Item list already exists");

        itemLists.put(itemList.id(), itemList);
    }

    /**
     * Retrieves an item list with the specified ID.
     *
     * @param id The ID of the item list to retrieve.
     * @return The item list with the specified ID.
     * @throws IOException If the item list with the specified ID is not found.
     */
    public ItemList getItemList(int id) throws IOException{
        if(itemLists.containsKey(id) == false)
            throw new IOException("Item list not found");

        return itemLists.get(id);
    }

    /**
     * Removes an item list with the specified ID from the system.
     *
     * @param id The ID of the item list to remove.
     * @throws IOException If the item list with the specified ID is not found.
     */
    public void removeItemList(int id) throws IOException {
        if (itemLists.containsKey(id) == false)
            throw new IOException("Item list not found");

        itemLists.remove(id);
    }

    /**
     * Updates an item list with the specified ID with a new item list.
     *
     * @param id The ID of the item list to update.
     * @param newItemList The new item list to update with.
     * @throws IOException If the item list with the specified ID is not found.
     */
    public void updateItemList(int id, ItemList newItemList) throws IOException{
        if(itemLists.containsKey(id) == false)
            throw new IOException("Item list not found");

        itemLists.put(id, newItemList);
    }

    /**
     * Retrieves all item lists in the system as a linked list.
     *
     * @return A linked list of all item lists in the system.
     */
    public LinkedList<ItemList> getAllItemLists() {
        return new LinkedList<>(itemLists.values());
    }
}
