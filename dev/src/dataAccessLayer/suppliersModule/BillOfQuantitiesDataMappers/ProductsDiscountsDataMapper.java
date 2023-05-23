package dataAccessLayer.suppliersModule.BillOfQuantitiesDataMappers;

import businessLayer.suppliersModule.Discounts.CashDiscount;
import businessLayer.suppliersModule.Discounts.Discount;
import businessLayer.suppliersModule.Discounts.PercentageDiscount;
import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.*;

public class ProductsDiscountsDataMapper extends AbstractDataMapper {
    /**
     * maps between supplier's bn number - to map between product's catalog number - to its map of discounts
     */
    public ProductsDiscountsDataMapper(SQLExecutor sqlExecutor) throws DalException {
        super(sqlExecutor, "products_discounts", new String[]{"bn_number","catalog_number", "amount", "percentage", "cash"});
    }

    public void insert(String bnNumber, String catalogNumber, int amount, Discount discount) throws SQLException {
        double percentage = -1,cash = -1;
        if(discount instanceof PercentageDiscount)
            percentage = ((PercentageDiscount) discount).getPercentage();
        else
            cash = ((CashDiscount)discount).getAmountOfDiscount();
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', '%s', %d, %f, %f)",tableName, columnsString, bnNumber,
                catalogNumber, amount, percentage, cash));
    }

    public void delete(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s'", tableName, bnNumber));
    }

    public void delete(String bnNumber, String catalogNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s' and catalog_number = '%s'", tableName, bnNumber, catalogNumber));
    }

    public void delete(String bnNumber, String catalogNumber, int amount) throws SQLException {
        sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s' and catalog_number = '%s' and amount = %d", tableName, bnNumber, catalogNumber, amount));
    }

    public Map<String, Map<Integer, Discount>> find(String bnNumber) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'", columnsString,
                tableName, bnNumber));
        Map<String, Map<Integer, Discount>> suppliersProductDiscounts = new HashMap<>();
        while (resultSet.next()) {
            String catalogNumber = resultSet.getString("catalog_number");
            int amount = resultSet.getInt("amount");
            if(!suppliersProductDiscounts.containsKey(catalogNumber))
                suppliersProductDiscounts.put(catalogNumber, new HashMap<>());
            if (resultSet.getDouble("percentage") == -1)
                suppliersProductDiscounts.get(catalogNumber).put(amount, new CashDiscount(resultSet.getDouble("cash")));
            else
                suppliersProductDiscounts.get(catalogNumber).put(amount, new PercentageDiscount(resultSet.getDouble("percentage")));
        }
        return suppliersProductDiscounts;
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
                .addColumn("bn_number", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn("catalog_number", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn("amount", ColumnType.INTEGER, ColumnModifier.PRIMARY_KEY)
                .addColumn("percentage", ColumnType.REAL)
                .addColumn("cash", ColumnType.REAL)
                .addForeignKey("catalog_number", "products","catalog_number", ON_UPDATE.CASCADE, ON_DELETE.CASCADE)
                .addForeignKey("bn_number", "suppliers","bn_number", ON_UPDATE.CASCADE, ON_DELETE.CASCADE);
    }
}
