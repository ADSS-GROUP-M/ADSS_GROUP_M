package dataAccessLayer.dalAbstracts;

import dataAccessLayer.dalUtils.Cache;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;

public abstract class DAOBase<T> implements DAO<T> {

    protected final SQLExecutor cursor;
    protected final String TABLE_NAME;
    protected final Cache<T> cache;
    protected CreateTableQueryBuilder createTableQueryBuilder;

    protected DAOBase(SQLExecutor cursor, String tableName) throws DalException {
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
     * Used to insert data into {@link DAOBase#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    protected abstract void initializeCreateTableQueryBuilder();

    protected abstract T getObjectFromResultSet(OfflineResultSet resultSet);

    /**
     * used for testing
     */
    public void clearTable() {
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
