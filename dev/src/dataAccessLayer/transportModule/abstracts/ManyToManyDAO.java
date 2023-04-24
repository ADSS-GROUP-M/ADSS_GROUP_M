package dataAccessLayer.transportModule.abstracts;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;

import java.util.List;

/**
 * @param <T> The child Type
 */
public abstract class ManyToManyDAO<T>{

    protected final SQLExecutor cursor;
    protected final String TABLE_NAME;
    protected final String PARENT_TABLE_NAME;
    protected final String[] ALL_COLUMNS;
    protected final String[] PRIMARY_KEYS;
    protected final String[] FOREIGN_KEYS;

    protected final String[] REFERENCES;

    protected ManyToManyDAO(String tableName,
                            String parentTableName,
                            String[] primaryKeys,
                            String[] foreignKeys,
                            String[] references,
                            String ... allColumns) throws DalException{
        cursor = new SQLExecutor();
        TABLE_NAME = tableName;
        PARENT_TABLE_NAME = parentTableName;
        PRIMARY_KEYS = primaryKeys;
        FOREIGN_KEYS = foreignKeys;
        REFERENCES = references;
        ALL_COLUMNS = allColumns;
        initTable();
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    protected ManyToManyDAO(String dbName,
                            String tableName,
                            String parentTableName,
                            String[] primaryKeys,
                            String[] foreignKeys,
                            String[] references,
                            String ... allColumns) throws DalException{
        cursor = new SQLExecutor(dbName);
        TABLE_NAME = tableName;
        PARENT_TABLE_NAME = parentTableName;
        PRIMARY_KEYS = primaryKeys;
        FOREIGN_KEYS = foreignKeys;
        REFERENCES = references;
        ALL_COLUMNS = allColumns;
        initTable();
    }


    //TODO: Finish this
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
     * @param t_object object with the identifier to delete
     * @param t_object object with the identifier to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    public abstract void delete(T object) throws DalException;

    protected abstract T getObjectFromResultSet(OfflineResultSet resultSet);
}
