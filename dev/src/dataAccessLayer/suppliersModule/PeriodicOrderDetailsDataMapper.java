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

    public void insert(int orderId, String bnNumber ,int day, Branch branch) throws DalException {
        String columnsString = String.join(", ", columns);
        try {
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES(%d, '%s', %d, '%s')",tableName, columnsString, orderId,
                    bnNumber, day, branch.name()));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void delete(int orderId) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE order_id = %d", tableName, orderId));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public int findDay(int orderId) throws DalException {
        OfflineResultSet resultSet = null;
        try {
            resultSet = sqlExecutor.executeRead(String.format("SELECT day FROM %s WHERE order_id = %d", tableName, orderId));
            return resultSet.next() ? resultSet.getInt("day") : 0;
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public String findBnNumber(int orderId) throws DalException {
        try {
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT bn_number FROM %s WHERE order_id = %d", tableName, orderId));
            return resultSet.next() ? resultSet.getString("bn_number") : null;
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public Branch findBranch(int orderId) throws DalException {
        try {
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT branch FROM %s WHERE order_id = %d", tableName, orderId));
            return resultSet.next() ? Branch.valueOf(resultSet.getString("branch")) : Branch.branch1;
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void update(int orderId, int day) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET day = %d WHERE order_id = %d", tableName,
                    day, orderId));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
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
