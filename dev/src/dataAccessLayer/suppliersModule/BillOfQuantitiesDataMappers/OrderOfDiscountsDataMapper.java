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

    public void insert(String bnNumber, boolean amountBeforeTotal) throws DalException {
        String columnsString = String.join(", ", columns);
        try {
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %d)",tableName, columnsString, bnNumber,
                    amountBeforeTotal? 1 : 0));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }
    public void update(String bnNumber, boolean amountBeforeTotal) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET amount_before_total = %d WHERE bn_number = '%s'", tableName, amountBeforeTotal? 1: 0, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }
    public Boolean find(String bnNumber) throws DalException {
        try {
            String columnsString = String.join(", ", columns);
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'", columnsString, tableName, bnNumber));
            if(resultSet.next()) {
                return resultSet.getInt("amount_before_total") == 1;
            }
            return null;
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void delete(String bnNumber) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s'", tableName, bnNumber));
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
                .addColumn("bn_number", ColumnType.TEXT,ColumnModifier.PRIMARY_KEY)
                .addColumn("amount_before_total", ColumnType.INTEGER)
                .addForeignKey("bn_number", "suppliers", "bn_number")
                .addCheck("amount_before_total = 0 OR amount_before_total = 1");
    }
}
