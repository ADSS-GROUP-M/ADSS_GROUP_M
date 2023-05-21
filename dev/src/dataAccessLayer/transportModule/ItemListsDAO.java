package dataAccessLayer.transportModule;

import dataAccessLayer.dalAbstracts.CounterDAO;
import dataAccessLayer.dalAbstracts.DAOBase;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import domainObjects.transportModule.ItemList;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.List;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class ItemListsDAO extends DAOBase<ItemList> implements CounterDAO {
    public static final String primaryKey = "id";
    public static final String tableName = "item_lists";
    private final ItemListsItemsDAO itemListsItemsDAO;
    private final ItemListIdCounterDAO itemListIdCounterDAO;

    public ItemListsDAO(SQLExecutor cursor, ItemListsItemsDAO itemListsItemsDAO, ItemListIdCounterDAO itemListIdCounterDAO) throws DalException {
        super(cursor, tableName);
        this.itemListsItemsDAO = itemListsItemsDAO;
        this.itemListIdCounterDAO = itemListIdCounterDAO;

        // need to add -1 to the table if it doesn't exist
        try {
            if(exists(ItemList.getLookupObject(-1)) == false){
                cursor.executeWrite("INSERT INTO %s (id) VALUES (-1);".formatted(TABLE_NAME));
            }
        } catch (SQLException e) {
            throw new DalException("Failed to initialize item lists table", e);
        }
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
        createTableQueryBuilder.addColumn("id", ColumnType.INTEGER, ColumnModifier.PRIMARY_KEY);
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
            itemListsItemsDAO.delete(object);
            if(cursor.executeWrite(query) == 1){
                cache.remove(object);
            } else {
                throw new DalException("No item list with id " + object.id() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete item list", e);
        }
    }

    @Override
    public boolean exists(ItemList object) throws DalException {

        if(cache.contains(object)) {
            return true;
        }

        String query = String.format("SELECT * FROM %s WHERE id = %d;", TABLE_NAME, object.id());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
            if(resultSet.next()) {
                ItemList selected = getObjectFromResultSet(resultSet);
                cache.put(selected);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DalException("Failed to check if item list exists", e);
        }
    }

    @Override
    protected ItemList getObjectFromResultSet(OfflineResultSet resultSet) {
        return itemListsItemsDAO.getObjectFromResultSet(resultSet);
    }

    @Override
    public void clearTable(){
        itemListsItemsDAO.clearTable();
        try {
            resetCounter();
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
        super.clearTable();
    }

    @Override
    public Integer selectCounter() throws DalException {
        return itemListIdCounterDAO.selectCounter();
    }

    @Override
    public void insertCounter(Integer value) throws DalException {
        itemListIdCounterDAO.insertCounter(value);
    }

    @Override
    public void incrementCounter() throws DalException {
        itemListIdCounterDAO.incrementCounter();
    }

    @Override
    public void resetCounter() throws DalException {
        itemListIdCounterDAO.resetCounter();
    }
}
