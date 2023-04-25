package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.DAO;
import objects.transportObjects.ItemList;

import java.sql.SQLException;
import java.util.List;

public class ItemListsDAO extends DAO<ItemList> {

    public static final String[] types = {"INTEGER"};
    public static final String[] primary_keys = {"id"};
    private final ItemListsItemsDAO itemListsItemsDAO;

    public ItemListsDAO() throws DalException {
        super("item_lists",
                types,
                primary_keys,
                "id"
        );
        itemListsItemsDAO = new ItemListsItemsDAO();
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public ItemListsDAO(String dbName) throws DalException {
        super(dbName,
                "item_lists",
                types,
                primary_keys,
                "id"
        );
        itemListsItemsDAO = new ItemListsItemsDAO(dbName);
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public ItemList select(ItemList object) throws DalException {

        if(cache.contains(object)) {
            return cache.get(object);
        }

        ItemList selected = itemListsItemsDAO.select(object);
        cache.put(selected);
        return selected;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<ItemList> selectAll() throws DalException {
        List<ItemList> itemLists = itemListsItemsDAO.selectAll();
        cache.putAll(itemLists);
        return itemLists;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(ItemList object) throws DalException {
        String query = String.format("INSERT INTO %s (id) VALUES (%d);", TABLE_NAME, object.id());
        try {
            if(cursor.executeWrite(query) == 1){
                itemListsItemsDAO.insert(object);
                cache.put(object);
            } else {
                throw new RuntimeException("Unexpected error while inserting item list");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert item list", e);
        } catch (RuntimeException e) {
            try{
                itemListsItemsDAO.delete(object);
            } catch (DalException ignored) {}
            throw new RuntimeException("Unexpected error while inserting item list");
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(ItemList object) throws DalException {
        itemListsItemsDAO.update(object);
        cache.put(object);
    }

    /**
     * @param object getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(ItemList object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE id = %d;", TABLE_NAME, object.id());
        try {
            if(cursor.executeWrite(query) == 1){
                itemListsItemsDAO.delete(object);
                cache.remove(object);
            } else {
                throw new DalException("No item list with id " + object.id() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete item list", e);
        }
    }

    @Override
    protected ItemList getObjectFromResultSet(OfflineResultSet resultSet) {
        return itemListsItemsDAO.getObjectFromResultSet(resultSet);
    }
}
