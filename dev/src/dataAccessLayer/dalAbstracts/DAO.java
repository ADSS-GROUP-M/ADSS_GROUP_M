package dataAccessLayer.dalAbstracts;

import dataAccessLayer.dalUtils.Cache;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.List;

public abstract class DAO<T> {

    protected final SQLExecutor cursor;
    protected final String TABLE_NAME;
    protected final Cache<T> cache;
    protected CreateTableQueryBuilder createTableQueryBuilder;

    protected DAO(SQLExecutor cursor, String tableName) throws DalException {
        this.cursor = cursor;
        this.TABLE_NAME = tableName;
        this.cache = new Cache<>();
        this.createTableQueryBuilder = new CreateTableQueryBuilder(tableName);
        initTable();
    }

    protected void initTable() throws DalException{
        initializeCreateTableQueryBuilder();
        String query = createTableQueryBuilder.buildQuery();
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to initialize table "+TABLE_NAME, e);
        }
    }

    /**
     * Used to insert data into {@link DAO#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    protected abstract void initializeCreateTableQueryBuilder();

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

    public abstract boolean exists(T object) throws DalException;

    protected abstract T getObjectFromResultSet(OfflineResultSet resultSet);

    /**
     * used for testing
     */
    public void clearTable(){
        String query = String.format("DELETE FROM %s", TABLE_NAME);
        try {
            cursor.executeWrite(query);
            clearCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearCache() {
    	cache.clear();
    }
}
