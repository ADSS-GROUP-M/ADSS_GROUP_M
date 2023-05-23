package dataAccessLayer.dalAbstracts;

import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import exceptions.DalException;

import java.sql.SQLException;

public abstract class AbstractDataMapper {
    protected final String tableName;
    protected final String[] columns;
    protected final SQLExecutor sqlExecutor;
    protected final CreateTableQueryBuilder createTableQueryBuilder;

    public AbstractDataMapper(SQLExecutor sqlExecutor, String tableName, String[] columns) throws DalException{
        this.sqlExecutor = sqlExecutor;
        this.tableName = tableName;
        this.columns = columns;
        createTableQueryBuilder = new CreateTableQueryBuilder(tableName);
        initTable();
    }

    protected void initTable() throws DalException {
        initializeCreateTableQueryBuilder();
        String query = createTableQueryBuilder.buildQuery();
        try {
            sqlExecutor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to initialize table "+tableName, e);
        }
    }

    /**
     * Used to insert data into {@link AbstractDataMapper#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, CreateTableQueryBuilder.ColumnType, CreateTableQueryBuilder.ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    protected abstract void initializeCreateTableQueryBuilder();

    public String getTableName(){
        return tableName;
    }
}
