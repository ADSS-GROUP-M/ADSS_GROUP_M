package dataAccessLayer.suppliersModule.BillOfQuantitiesDataMappers;

import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class OrderOfDiscountsDataMapper extends AbstractDataMapper {
    public OrderOfDiscountsDataMapper(SQLExecutor sqlExecutor) throws DalException {
        super(sqlExecutor, "order_of_discounts", new String[]{"bn_number", "amount_before_total"});
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

    /**
     * Used to insert data into {@link AbstractDataMapper#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    @Override
    protected void initializeCreateTableQueryBuilder() throws DalException {
        createTableQueryBuilder
                .addColumn("bn_number", ColumnType.TEXT,ColumnModifier.PRIMARY_KEY)
                .addColumn("amount_before_total", ColumnType.INTEGER)
                .addForeignKey("bn_number", "suppliers", "bn_number")
                .addCheck("amount_before_total = 0 OR amount_before_total = 1");
    }
}
