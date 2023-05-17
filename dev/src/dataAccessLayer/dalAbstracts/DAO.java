package dataAccessLayer.dalAbstracts;

import dataAccessLayer.dalUtils.Cache;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DAO<T> {

    protected final SQLExecutor cursor;
    protected final String TABLE_NAME;
    protected final String[] ALL_COLUMNS;
    protected final String[] PRIMARY_KEYS;
    protected final String[] TYPES;

    protected final Cache<T> cache;

    protected DAO(SQLExecutor cursor ,String tableName,String[] types, String[] primaryKeys, String ... allColumns) {
        this.cursor = cursor;
        this.TABLE_NAME = tableName;
        this.PRIMARY_KEYS = primaryKeys;
        this.ALL_COLUMNS = allColumns;
        this.TYPES = types;
        this.cache = new Cache<>();
    }

    /**
     * Initialize the table if it doesn't exist
     */
    protected void initTable() throws DalException{
        CreateTableQueryBuilder queryBuilder = new CreateTableQueryBuilder(TABLE_NAME);

        // columns
        for (int i = 0; i < ALL_COLUMNS.length; i++) {
            ArrayList<ColumnModifier> modifiers = new ArrayList<>();

            // basic modifiers
            modifiers.add(ColumnModifier.NOT_NULL);

            // primary key
            if(Arrays.asList(PRIMARY_KEYS).contains(ALL_COLUMNS[i])) {
                modifiers.add(ColumnModifier.PRIMARY_KEY);
            }

            queryBuilder.addColumn(ALL_COLUMNS[i], ColumnType.valueOf(TYPES[i]), modifiers.toArray(new ColumnModifier[0]));
        }

        String query = queryBuilder.buildQuery();
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to initialize table "+TABLE_NAME, e);
        }
    }

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
            cache.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearCache() {
    	cache.clear();
    }
}
