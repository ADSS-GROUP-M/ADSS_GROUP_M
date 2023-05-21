package dataAccessLayer.transportModule;

import dataAccessLayer.dalAbstracts.DAOBase;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import domainObjects.transportModule.ItemList;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class ItemListsItemsDAO extends DAOBase<ItemList> {
    public static final String[] primaryKey = {"id", "loading_type", "item_name"};
    public static final String tableName = "item_lists_items";
    private static final String PARENT_TABLE_NAME = ItemListsDAO.tableName;

    private enum LoadingType {
        loading,
        unloading
    }

    public ItemListsItemsDAO(SQLExecutor cursor) throws DalException {
        super(cursor, tableName);
    }

    /**
     * Used to insert data into {@link DAOBase#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    @Override
    protected void initializeCreateTableQueryBuilder() {
        createTableQueryBuilder
                .addColumn("id", ColumnType.INTEGER, ColumnModifier.PRIMARY_KEY)
                .addColumn("loading_type", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn("item_name", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn("amount", ColumnType.INTEGER)
                .addForeignKey("id", ItemListsDAO.tableName, ItemListsDAO.primaryKey);
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

        String idsQuery = String.format("SELECT id FROM %s where id != -1;", PARENT_TABLE_NAME);
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

        StringBuilder query = new StringBuilder();

        query.append(insertMapToDBQuery(object.id(), LoadingType.loading, object.load()));
        query.append(insertMapToDBQuery(object.id(), LoadingType.unloading, object.unload()));

        try {
            if(cursor.executeWrite(query.toString()) != query.toString().split("\n").length){
                throw new RuntimeException("Unexpected error while inserting item list");
            }
        } catch (SQLException e) {
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

        StringBuilder query = new StringBuilder();
        query.append(updateFromMapsQuery(object.id(), oldObject.unload(), object.unload(), LoadingType.unloading));
        query.append(updateFromMapsQuery(object.id(), oldObject.load(), object.load(), LoadingType.loading));

        try {
            if(cursor.executeWrite(query.toString()) == 0){
                throw new DalException("No item list with id " + object.id() + " was found");
            }
        } catch (SQLException e) {
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
            if(cursor.executeWrite(query) == 0){
                throw new DalException("No item list with id " + object.id() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete item list", e);
        }
    }

    @Override
    public boolean exists(ItemList object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE id = %d LIMIT 1;", TABLE_NAME, object.id());
        try {
            return cursor.executeRead(query).isEmpty() == false;
        } catch (SQLException e) {
            throw new DalException("Failed to check if item list exists", e);
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

    private String insertMapToDBQuery(int id, LoadingType loadingType, Map<String,Integer> map) {
        StringBuilder query = new StringBuilder();
        
        for(var item : map.entrySet()){
            String itemName = item.getKey();
            int amount = item.getValue();
            query.append(String.format("INSERT INTO %s VALUES ('%s', '%s', '%s', %d);\n",
                    TABLE_NAME,
                    id,
                    loadingType,
                    itemName,
                    amount));
        }

        return query.toString();
    }

    private String updateFromMapsQuery(int id, Map<String,Integer> oldMap, Map<String,Integer> newMap, LoadingType loadingType){

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

        StringBuilder query = new StringBuilder();
        for(String itemName: itemsToRemove){
            query.append(String.format("DELETE FROM %s WHERE id = '%s' AND loading_type = '%s' AND item_name = '%s';\n",
                    TABLE_NAME,
                    id,
                    loadingType,
                    itemName));
        }

        for(String itemName: itemsToUpdate) {
            query.append(String.format("UPDATE %s SET amount = %d WHERE id = '%s' AND loading_type = '%s' AND item_name = '%s';\n",
                    TABLE_NAME,
                    newMap.get(itemName),
                    id,
                    loadingType,
                    itemName));
        }

        for(String itemName: itemsToAdd){
            query.append(String.format("INSERT INTO %s VALUES ('%s', '%s', '%s', %d);\n",
                    TABLE_NAME,
                    id,
                    loadingType,
                    itemName,
                    newMap.get(itemName)));
        }

        return query.toString();
    }
}
