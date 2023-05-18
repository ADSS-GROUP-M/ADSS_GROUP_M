package dataAccessLayer.suppliersModule;

import businessLayer.businessLayerUsage.Branch;
import dataAccessLayer.dalUtils.AbstractDataMapper;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;

public class PeriodicOrderDetailsDataMapper extends AbstractDataMapper {


    public PeriodicOrderDetailsDataMapper(){
        super("periodic_order_details", new String[]{"order_id", "bn_number", "day", "branch"});
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

}
