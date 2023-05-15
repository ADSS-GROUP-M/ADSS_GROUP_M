package Backend.DataAccessLayer.SuppliersModule.OrderHistoryDataMappers;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class NumberOfOrdersCounterDataMapper extends AbstractDataMapper {
    private Map<String, Integer> suppliersCounters;

    public NumberOfOrdersCounterDataMapper(){
        super("number_of_order_counter", new String[]{"bn_number", "order_number_counter"});
        suppliersCounters = new HashMap<>();
    }

    public void init(String bnNumber) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %d)",tableName, columnsString, bnNumber,
                0));
        suppliersCounters.put(bnNumber, 0);
    }

    public void delete(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s'", tableName, bnNumber));
    }

    public void update(String bnNumber, int numberOfOrders) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET order_number_counter = %d WHERE bn_number = '%s'", tableName, numberOfOrders, bnNumber));
        suppliersCounters.put(bnNumber, numberOfOrders);
    }

    public Integer getOrderNumber(String bnNumber) throws SQLException {
        if(suppliersCounters.containsKey(bnNumber))
            return suppliersCounters.get(bnNumber);
        Integer orderNumber = find(bnNumber);
        if(orderNumber == null) {
            init(bnNumber);
            orderNumber = 0;
        }
        return orderNumber;
    }

    public Integer find(String bnNumber) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'", columnsString,
                tableName, bnNumber));
        if(resultSet.next())
            return resultSet.getInt("order_number_counter");
        return null;
    }

    public int getAndIncrement(String bnNumber) throws SQLException {
        int orderId = getOrderNumber(bnNumber);
        update(bnNumber, orderId + 1);
        return orderId;
    }
}
