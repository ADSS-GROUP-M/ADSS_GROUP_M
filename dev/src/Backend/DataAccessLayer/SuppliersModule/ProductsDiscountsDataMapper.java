package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.Discounts.CashDiscount;
import Backend.BusinessLayer.SuppliersModule.Discounts.Discount;
import Backend.BusinessLayer.SuppliersModule.Discounts.PercentageDiscount;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ProductsDiscountsDataMapper extends AbstractDataMapper {
    /**
     * maps between supplier's bn number - to map between product's catalog number - to its map of discounts
     */
    public ProductsDiscountsDataMapper() {
        super("products_discounts", new String[]{"bn_number","catalog_number", "amount", "percentage", "cash"});
    }

    public void insert(String bnNumber, String catalogNumber, int amount, Discount discount) throws SQLException {
        double percentage = -1,cash = -1;
        if(discount instanceof PercentageDiscount)
            percentage = ((PercentageDiscount) discount).getPercentage();
        else
            cash = ((CashDiscount)discount).getAmountOfDiscount();
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES(%s, %s, %d, %f, %f)",tableName, columnsString, bnNumber,
                catalogNumber, amount, percentage, cash));
    }

    public void delete(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s", tableName, bnNumber));
    }

    public void delete(String bnNumber, String catalogNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s and catalog_number = %s", tableName, bnNumber, catalogNumber));
    }

    public void delete(String bnNumber, String catalogNumber, int amount) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s and catalog_number = %s and amount = %d", tableName, bnNumber, catalogNumber, amount));
    }

    public Map<String, Map<Integer, Discount>> find(String bnNumber) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s WHERE bn_number = %s", columnsString, bnNumber));
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
}
