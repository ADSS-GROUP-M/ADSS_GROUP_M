package BusinessLayer.transportModule;

import Objects.transportObjects.ItemList;

import utils.transportUtils.TransportException;
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
     * @throws TransportException If the item list already exists in the system.
     */
    public Integer addItemList(ItemList itemList){

        if(itemList.id() != -1){
            throw new UnsupportedOperationException("Pre-defined IDs are not supported");
        }

        ItemList toAdd = new ItemList(idCounter++, itemList);
        itemLists.put(toAdd.id(), toAdd);
        return toAdd.id();
    }

    /**
     * Retrieves an item list with the specified ID.
     *
     * @param id The ID of the item list to retrieve.
     * @return The item list with the specified ID.
     * @throws TransportException If the item list with the specified ID is not found.
     */
    public ItemList getItemList(int id) throws TransportException{
        if(listExists(id) == false) {
            throw new TransportException("Item list not found");
        }

        return itemLists.get(id);
    }

    /**
     * Removes an item list with the specified ID from the system.
     *
     * @param id The ID of the item list to remove.
     * @throws TransportException If the item list with the specified ID is not found.
     */
    public void removeItemList(int id) throws TransportException {
        if (listExists(id) == false) {
            throw new TransportException("Item list not found");
        }

        itemLists.remove(id);
    }

    /**
     * Updates an item list with the specified ID with a new item list.
     *
     * @param id The ID of the item list to update.
     * @param newItemList The new item list to update with.
     * @throws TransportException If the item list with the specified ID is not found.
     */
    public void updateItemList(int id, ItemList newItemList) throws TransportException{
        if(listExists(id) == false) {
            throw new TransportException("Item list not found");
        }

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
