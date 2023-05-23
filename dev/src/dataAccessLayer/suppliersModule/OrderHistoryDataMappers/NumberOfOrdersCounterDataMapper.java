package dataAccessLayer.suppliersModule.OrderHistoryDataMappers;

import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class NumberOfOrdersCounterDataMapper extends AbstractDataMapper {
    private final Map<String, Integer> suppliersCounters;

    public NumberOfOrdersCounterDataMapper(SQLExecutor sqlExecutor) throws DalException {
        super(sqlExecutor, "number_of_order_counter", new String[]{"bn_number", "order_number_counter"});
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
                .addColumn("bn_number", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn("order_number_counter", ColumnType.INTEGER)
                .addForeignKey("bn_number", "suppliers", "bn_number");
    }
}
