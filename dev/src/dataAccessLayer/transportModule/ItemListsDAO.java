package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.DAO;

import java.util.List;

public class ItemListsDAO<T> extends DAO<T> {

    public ItemListsDAO() throws DalException {
        super("item_lists",
                new String[]{"INTEGER"},
                new String[]{"id"},
                "id"
        );
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public ItemListsDAO(String dbName) throws DalException {
        super(dbName,
                "item_lists",
                new String[]{"INTEGER"},
                new String[]{"id"},
                "id"
        );
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public T select(T object) throws DalException {
        return null;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<T> selectAll() throws DalException {
        return null;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(T object) throws DalException {

    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(T object) throws DalException {

    }

    /**
     * @param object getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(T object) throws DalException {

    }

    @Override
    protected T getObjectFromResultSet(OfflineResultSet resultSet) {
        return null;
    }

}
