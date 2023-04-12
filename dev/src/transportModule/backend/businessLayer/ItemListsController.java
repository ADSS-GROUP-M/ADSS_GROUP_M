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
        idCounter = 1; //TODO: currently not in use. this will have to be restored from the DB in the future.
    }

    /**
     * Adds a new item list to the system.
     *
     * @param itemList The item list to be added.
     * @return The ID of the added item list.
     * @throws IOException If the item list already exists in the system.
     */
    public Integer addItemList(ItemList itemList) throws IOException{

        //TODO: remove support for pre-defined IDs and move to auto-incrementing IDs.
        // currently, the ID is set to -1 if it is not pre-defined.
        // this is a temporary solution until the DB is implemented.

        if(listExists(itemList.id()) == true)
            throw new IOException("An item list with this id already exists");

        //<TEMPORARY SOLUTION>
        if(itemList.id() != -1){

            //TODO: uncomment this when pre-defined IDs are no longer supported
            //throw new UnsupportedOperationException("Pre-defined IDs are not supported");

            itemLists.put(itemList.id(), itemList);
            return itemList.id();
        }
        //<TEMPORARY SOLUTION/>
        else{
            ItemList toAdd = new ItemList(idCounter++, itemList);
            itemLists.put(toAdd.id(), toAdd);
            return toAdd.id();
        }
    }

    /**
     * Retrieves an item list with the specified ID.
     *
     * @param id The ID of the item list to retrieve.
     * @return The item list with the specified ID.
     * @throws IOException If the item list with the specified ID is not found.
     */
    public ItemList getItemList(int id) throws IOException{
        if(listExists(id) == false)
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
        if (listExists(id) == false)
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
        if(listExists(id) == false)
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

    public boolean listExists(int id) {
        return itemLists.containsKey(id);
    }
}
