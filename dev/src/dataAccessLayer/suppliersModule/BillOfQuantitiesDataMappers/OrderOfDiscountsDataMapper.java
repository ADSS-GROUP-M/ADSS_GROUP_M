package dataAccessLayer.suppliersModule.BillOfQuantitiesDataMappers;

import dataAccessLayer.dalUtils.AbstractDataMapper;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;

public class OrderOfDiscountsDataMapper extends AbstractDataMapper {
    public OrderOfDiscountsDataMapper() {
        super("order_of_discounts", new String[]{"bn_number", "amount_before_total"});
    }

    public void insert(String bnNumber, boolean amountBeforeTotal) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %d)",tableName, columnsString, bnNumber,
                amountBeforeTotal? 1 : 0));
    }
    public void update(String bnNumber, boolean amountBeforeTotal) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET amount_before_total = %d WHERE bn_number = '%s'", tableName, amountBeforeTotal? 1: 0, bnNumber));
    }
    public Boolean find(String bnNumber) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'", columnsString, tableName, bnNumber));
        if(resultSet.next())
            return resultSet.getInt("amount_before_total") == 1;
        return null;
    }

    public void delete(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s'", tableName, bnNumber));
    }
}
