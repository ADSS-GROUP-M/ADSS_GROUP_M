package DataAccessLayer.transportModule;

import DataAccessLayer.DalUtils.DalException;
import DataAccessLayer.transportModule.abstracts.DAO;
import transportModule.records.ItemList;

import java.sql.SQLException;
import java.util.List;

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
            throw new DalException("Failed to initialize ItemLists table", e);
        }
    }

    /**
     * @param object@return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public ItemList select(ItemList object) throws DalException {
        return null;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<ItemList> selectAll() throws DalException {
        return null;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(ItemList object) throws DalException {

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
}
