package dataAccessLayer.suppliersModule;

import businessLayer.businessLayerUsage.Branch;
import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class PeriodicOrderDetailsDataMapper extends AbstractDataMapper {


    public PeriodicOrderDetailsDataMapper(SQLExecutor sqlExecutor) throws DalException {
        super(sqlExecutor, "periodic_order_details", new String[]{"order_id", "bn_number", "day", "branch"});
    }

    public void insert(int orderId, String bnNumber ,int day, Branch branch) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES(%d, '%s', %d, '%s')",tableName, columnsString, orderId,
                bnNumber, day, branch.name()));
    }

    public void delete(int orderId) throws SQLException {
        sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE order_id = %d", tableName, orderId));
    }

    public int findDay(int orderId) throws SQLException {
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT day FROM %s WHERE order_id = %d", tableName, orderId));
        return resultSet.next() ? resultSet.getInt("day") : 0;
    }

    public String findBnNumber(int orderId) throws SQLException {
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT bn_number FROM %s WHERE order_id = %d", tableName, orderId));
        return resultSet.next() ? resultSet.getString("bn_number") : null;
    }

    public Branch findBranch(int orderId) throws SQLException {
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT branch FROM %s WHERE order_id = %d", tableName, orderId));
        return resultSet.next() ? Branch.valueOf(resultSet.getString("branch")) : Branch.branch1;
    }

    public void update(int orderId, int day) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET day = %d WHERE order_id = %d", tableName,
                day, orderId));
    }

    /**
     * Used to insert data into {@link AbstractDataMapper#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    @Override
    protected void initializeCreateTableQueryBuilder() {
        createTableQueryBuilder
                .addColumn("order_id", ColumnType.INTEGER, ColumnModifier.PRIMARY_KEY)
                .addColumn("bn_number", ColumnType.TEXT)
                .addColumn("day", ColumnType.INTEGER)
                .addColumn("branch", ColumnType.TEXT)
                .addForeignKey("bn_number", "suppliers", "bn_number");
    }
}
