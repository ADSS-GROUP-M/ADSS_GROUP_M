package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.DAO;
import objects.transportObjects.ItemList;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemListsDAO extends DAO<ItemList> {
    public ItemListsDAO() throws DalException {
        super("item_lists",
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
    public ItemListsDAO(String dbName) throws DalException {
        super(dbName,
                "item_lists",
                new String[]{"id"},
                "id",
                "loading_type",
                "item_name",
                "amount");
    }

    /**
     * Initialize the table if it doesn't exist
     */
    @Override
    protected void initTable() throws DalException {
        String query = """
                CREATE TABLE IF NOT EXISTS item_lists (
                    id INTEGER PRIMARY KEY,
                    loading_type TEXT NOT NULL,
                    item_name TEXT NOT NULL,
                    amount INTEGER NOT NULL,
                    PRIMARY KEY (id)
                );
                """;
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to initialize item_lists table", e);
        }
    }

    /**
     * @param object@return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public ItemList select(ItemList object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE id = '%s';", TABLE_NAME, object.id());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select item list", e);
        }
        if(resultSet.next()) {
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
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        OfflineResultSet resultSet;
        try{
            resultSet = cursor.executeRead(query);
        } catch(SQLException e){
            throw new DalException("Failed to select all item lists");
        }
        LinkedList<ItemList> itemLists = new LinkedList<>();
        while(resultSet.next()){
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
        insertMapToDB(object.id(), "loading", object.load());
        insertMapToDB(object.id(), "unloading", object.unload());
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(ItemList object) throws DalException {

    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(ItemList object) throws DalException {

    }

    @Override
    protected ItemList getObjectFromResultSet(OfflineResultSet resultSet) {
        return null;
    }

    private void insertMapToDB(int id,String loadingType, Map<String,Integer> map) throws DalException {
        for(var list : map.entrySet()){
            String itemName = list.getKey();
            int amount = list.getValue();
            String query = String.format("INSERT INTO %s VALUES ('%s', '%s', '%s', '%s');",
                    TABLE_NAME,
                    id,
                    loadingType,
                    itemName,
                    amount);
            try {
                cursor.executeWrite(query);
            } catch (SQLException e) {
                recoverFromError(id);
                throw new DalException("Failed to insert item list", e);
            }
        }
    }

    private void recoverFromError(int id) throws DalException{
        String query = String.format("DELETE FROM %s WHERE id = '%s';", TABLE_NAME, id);
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to recover from error", e);
        }
    }
}
