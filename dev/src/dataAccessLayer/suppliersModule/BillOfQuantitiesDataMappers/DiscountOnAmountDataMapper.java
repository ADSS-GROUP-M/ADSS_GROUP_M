package dataAccessLayer.suppliersModule.BillOfQuantitiesDataMappers;

import businessLayer.suppliersModule.Discounts.CashDiscount;
import businessLayer.suppliersModule.Discounts.Discount;
import businessLayer.suppliersModule.Discounts.PercentageDiscount;
import businessLayer.suppliersModule.Pair;
import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.*;

public class DiscountOnAmountDataMapper  extends AbstractDataMapper {
    public DiscountOnAmountDataMapper(SQLExecutor sqlExecutor) throws DalException {
        super(sqlExecutor, "discounts_on_amount", new String[] {"bn_number", "amount_to_reach", "percentage", "cash"});
    }

    public void insert(String bnNumber, int amountToReach, Discount discount) throws DalException {
        double percentage = -1,cash = -1;
        if(discount instanceof PercentageDiscount) {
            percentage = ((PercentageDiscount) discount).getPercentage();
        } else {
            cash = ((CashDiscount)discount).getAmountOfDiscount();
        }
        String columnsString = String.join(", ", columns);
        try {
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %d, %f, %f)",tableName, columnsString, bnNumber,
                    amountToReach, percentage, cash));
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

    public void updateAmountToReach(String bnNumber, double amountToReach) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET amount_to_reach = %d WHERE bn_number = '%s'", tableName, amountToReach, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updateDiscount(String bnNumber, Discount discount) throws DalException {
        double percentage = -1,cash = -1;
        if(discount instanceof PercentageDiscount) {
            percentage = ((PercentageDiscount) discount).getPercentage();
        } else {
            cash = ((CashDiscount)discount).getAmountOfDiscount();
        }
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET cash = %f, percentage = %f  WHERE bn_number = '%s'", tableName, cash, percentage, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public Pair<Integer, Discount> find(String bnNumber) throws DalException {
        try {
            String columnsString = String.join(", ", columns);
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'", columnsString, tableName, bnNumber));
            if(resultSet.next()) {
                Discount discount = null;
                if (resultSet.getDouble("percentage") != -1) {
                    discount = new PercentageDiscount(resultSet.getDouble("percentage"));
                } else {
                    discount = new CashDiscount(resultSet.getDouble("cash"));
                }
                return new Pair<>(resultSet.getInt("amount_to_reach"), discount);
            }
            return null;
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
                .addColumn("amount_to_reach", ColumnType.INTEGER)
                .addColumn("percentage", ColumnType.REAL)
                .addColumn("cash", ColumnType.REAL)
                .addForeignKey("bn_number", "suppliers","bn_number", ON_UPDATE.CASCADE, ON_DELETE.CASCADE);
    }
}
