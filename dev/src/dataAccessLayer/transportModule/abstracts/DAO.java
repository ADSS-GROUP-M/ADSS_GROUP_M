package dataAccessLayer.transportModule.abstracts;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import objects.transportObjects.Truck;

import java.util.List;

public abstract class DAO<T> {

    protected final SQLExecutor cursor;
    protected final String TABLE_NAME;
    protected final String[] ALL_COLUMNS;
    protected final String[] PRIMARY_KEYS;

    protected DAO(String tableName, String[] primaryKeys, String ... allColumns) throws DalException {
        this.cursor = new SQLExecutor();
        this.TABLE_NAME = tableName;
        this.PRIMARY_KEYS = primaryKeys;
        this.ALL_COLUMNS = allColumns;
        initTable();
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    protected DAO(String dbName, String tableName, String[] primaryKeys, String ... allColumns) throws DalException {
        this.cursor = new SQLExecutor(dbName);
        this.TABLE_NAME = tableName;
        this.PRIMARY_KEYS = primaryKeys;
        this.ALL_COLUMNS = allColumns;
        initTable();
    }

    /**
     * Initialize the table if it doesn't exist
     */
    protected abstract void initTable() throws DalException;

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    public abstract T select(T object) throws DalException;

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    public abstract List<T> selectAll() throws DalException;

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    public abstract void insert(T object) throws DalException;

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    public abstract void update(T object) throws DalException;

    /**
     *
     * @param object getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    public abstract void delete(T object) throws DalException;

    protected abstract T getObjectFromResultSet(OfflineResultSet resultSet);
}
