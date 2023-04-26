package businessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.transportModule.ItemListsDAO;
import objects.transportObjects.ItemList;

import utils.transportUtils.TransportException;

import java.util.List;

/**
 * The ItemListsController class is responsible for managing item lists in the transport module.
 * It provides methods to add, retrieve, update, and remove item lists from the system.
 */
public class ItemListsController {
    private final ItemListsDAO dao;

    private int idCounter;

    public ItemListsController(ItemListsDAO dao) throws TransportException{
        this.dao = dao;
        try {
            idCounter = dao.selectCounter();
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Adds a new item list to the system.
     *
     * @param itemList The item list to be added.
     * @return The ID of the added item list.
     * @throws TransportException If the item list already exists in the system.
     */
    public Integer addItemList(ItemList itemList) throws TransportException {

        if(itemList.id() != -1){
            throw new UnsupportedOperationException("Pre-defined IDs are not supported");
        }
        ItemList toAdd = new ItemList(idCounter, itemList);
        try {
            dao.insert(toAdd);
            dao.incrementCounter();
            idCounter++;
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
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

        try {
            return dao.select(ItemList.getLookupObject(id));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Removes an item list with the specified ID from the system.
     *
     * @param id The ID of the item list to remove.
     * @throws TransportException If the item list with the specified ID is not found.
     */
    public void removeItemList(int id) throws TransportException {
        if(listExists(id) == false) {
            throw new TransportException("Item list not found");
        }

        try {
            dao.delete(ItemList.getLookupObject(id));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
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

        try {
            dao.update(newItemList);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Retrieves all item lists in the system as a linked list.
     *
     * @return A linked list of all item lists in the system.
     */
    public List<ItemList> getAllItemLists() throws TransportException{
        try {
            return dao.selectAll();
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    public boolean listExists(int id) throws TransportException{
        try {
            return dao.exists(ItemList.getLookupObject(id));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }
}
