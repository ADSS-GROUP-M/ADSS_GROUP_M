package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;
import objects.transportObjects.ItemList;

import java.sql.SQLException;
import java.util.*;

public class ItemListsItemsDAO extends ManyToManyDAO<ItemList> {

    private enum LoadingType {
        loading,
        unloading
    }

    public static final String[] types = new String[]{"INTEGER", "TEXT", "TEXT", "INTEGER"};

    public ItemListsItemsDAO() throws DalException {
        super("item_lists_items",
                new String[]{"item_lists"},
                types,
                new String[]{"id","loading_type","item_name"},
                new String[]{"id"},
                new String[]{"id"},
                "id",
                "loading_type",
                "item_name",
                "amount");
    }

    /**
     *  used for testing
     *  @param dbName the name of the database to connect to
     */
    public ItemListsItemsDAO(String dbName) throws DalException {
        super(dbName,
                "item_lists_items",
                new String[]{"item_lists"},
                types,
                new String[]{"id","loading_type","item_name"},
                new String[]{"id"},
                new String[]{"Id"},
                "id",
                "loading_type",
                "item_name",
                "amount");
    }

    /**
     * @param object@return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public ItemList select(ItemList object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE id = %d;", TABLE_NAME, object.id());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select item list", e);
        }
        if(resultSet.isEmpty() == false) {
            return getObjectFromResultSet(resultSet);
        } else {
            throw new DalException("No item list with id " + object.id() + " was found");
        }
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<ItemList> selectAll() throws DalException {
        OfflineResultSet resultSet;

        String idsQuery = String.format("SELECT id FROM %s;", PARENT_TABLE_NAME);
        try{
            resultSet = cursor.executeRead(idsQuery);
        } catch(SQLException e){
            throw new DalException("Failed to select all item lists");
        }

        LinkedList<Integer> ids = new LinkedList<>();
        while(resultSet.next()){
            ids.add(resultSet.getInt("id"));
        }

        LinkedList<ItemList> itemLists = new LinkedList<>();
        for(int id: ids){
            String query = String.format("SELECT * FROM %s WHERE id = %d;", TABLE_NAME, id);
            try{
                resultSet = cursor.executeRead(query);
            } catch(SQLException e){
                throw new DalException("Failed to select all item lists");
            }
            itemLists.add(getObjectFromResultSet(resultSet));
        }
        return itemLists;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(ItemList object) throws DalException {
        try {
            insertMapToDB(object.id(), LoadingType.loading, object.load());
            insertMapToDB(object.id(), LoadingType.unloading, object.unload());
        } catch (SQLException e) {
            recoverFromInsertError(object.id());
            throw new DalException("Failed to insert item list", e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(ItemList object) throws DalException {
        ItemList oldObject = select(object);
        try {
            updateFromMaps(object.id(), oldObject.load(), object.load(), LoadingType.loading);
            updateFromMaps(object.id(), oldObject.unload(), object.unload(), LoadingType.unloading);
        } catch (SQLException e) {
            recoverFromUpdateError(object);
            throw new DalException("Failed to update item list", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(ItemList object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE id = '%s';", TABLE_NAME, object.id());
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to delete item list", e);
        }
    }

    @Override
    protected ItemList getObjectFromResultSet(OfflineResultSet resultSet) {
        HashMap<String,Integer> load = new HashMap<>();
        HashMap<String,Integer> unload = new HashMap<>();
        while(resultSet.next()){
            String loadingType = resultSet.getString("loading_type");
            String itemName = resultSet.getString("item_name");
            int amount = resultSet.getInt("amount");
            if(loadingType.equals(LoadingType.loading.toString())){
                load.put(itemName, amount);
            } else {
                unload.put(itemName, amount);
            }
        }
        return new ItemList(resultSet.getInt("id"), load, unload);
    }

    //================================================================================ |
    //============================ Helper Methods ==================================== |
    //================================================================================ |

    private void insertMapToDB(int id, LoadingType loadingType, Map<String,Integer> map) throws SQLException {
        for(var item : map.entrySet()){
            String itemName = item.getKey();
            int amount = item.getValue();
            String query = String.format("INSERT INTO %s VALUES ('%s', '%s', '%s', %d);",
                    TABLE_NAME,
                    id,
                    loadingType,
                    itemName,
                    amount);
            cursor.executeWrite(query);
        }
    }

    private void updateFromMaps(int id, Map<String,Integer> oldMap,Map<String,Integer> newMap, LoadingType loadingType) throws SQLException {

        List<String> itemsToRemove = oldMap.keySet().stream()
                .filter(itemName -> newMap.containsKey(itemName) == false)
                .toList();
        List<String> itemsToUpdate = newMap.keySet().stream()
                .filter(oldMap::containsKey)
                .filter(itemName -> newMap.get(itemName).equals(oldMap.get(itemName)) == false)
                .toList();
        List<String> itemsToAdd = newMap.keySet().stream()
                .filter(itemName -> oldMap.containsKey(itemName) == false)
                .toList();

        for(String itemName: itemsToRemove){
            String removeQuery = String.format("DELETE FROM %s WHERE id = '%s' AND loading_type = '%s' AND item_name = '%s';",
                    TABLE_NAME,
                    id,
                    loadingType,
                    itemName);
            cursor.executeWrite(removeQuery);
        }

        for(String itemName: itemsToUpdate) {
            String updateQuery = String.format("UPDATE %s SET amount = %d WHERE id = '%s' AND loading_type = '%s' AND item_name = '%s';",
                    TABLE_NAME,
                    newMap.get(itemName),
                    id,
                    loadingType,
                    itemName);
            cursor.executeWrite(updateQuery);
        }

        for(String itemName: itemsToAdd){
            String addQuery = String.format("INSERT INTO %s VALUES ('%s', '%s', '%s', %d);",
                    TABLE_NAME,
                    id,
                    loadingType,
                    itemName,
                    newMap.get(itemName));
            cursor.executeWrite(addQuery);
        }
    }

    private void recoverFromInsertError(int id) throws DalException{
        String query = String.format("DELETE FROM %s WHERE id = '%s';", TABLE_NAME, id);
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to recover from error", e);
        }
    }

    private void recoverFromUpdateError(ItemList object) throws DalException {
        recoverFromInsertError(object.id());
        insert(object);
    }
}
