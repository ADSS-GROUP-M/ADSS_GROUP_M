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
    private Map<String, Map<String, Map<Integer, Discount>>> suppliersDiscountsCached;
    public ProductsDiscountsDataMapper(String tableName, String columns) {
        super("products_discounts", new String[]{"bn_number","catalog_number", "amount", "percentage", "cash"});
        suppliersDiscountsCached = new HashMap<>();
    }

    public void insert(String bnNumber, String catalogNumber, int amount, double percentage, double cash) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES(%s, %s, %d, %f, %f)",tableName, columnsString, bnNumber,
                catalogNumber, amount, percentage, cash));
        if(!suppliersDiscountsCached.get(bnNumber).containsKey(catalogNumber))
            suppliersDiscountsCached.get(bnNumber).put(catalogNumber, new HashMap<>());
        Discount discount = percentage == -1 ? new CashDiscount(cash) : new PercentageDiscount(percentage);
        suppliersDiscountsCached.get(bnNumber).get(catalogNumber).put(amount, discount);
    }

    public void delete(String bnNumber) throws SQLException {
        boolean exist = suppliersDiscountsCached.remove(bnNumber) != null;
        if (exist)
            sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s", tableName, bnNumber));
    }

    public void delete(String bnNumber, String catalogNumber) throws SQLException {
        boolean exist = false;
        if(suppliersDiscountsCached.containsKey(bnNumber))
            exist = suppliersDiscountsCached.get(bnNumber).remove(catalogNumber) != null;
        if(exist)
            sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s and catalog_number = %s", tableName, bnNumber, catalogNumber));
    }

    public void delete(String bnNumber, String catalogNumber, int amount) throws SQLException {
        boolean exist = false;
        if(suppliersDiscountsCached.containsKey(bnNumber))
            exist = suppliersDiscountsCached.get(bnNumber).get(catalogNumber).remove(amount) != null;
        if(exist)
            sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s and catalog_number = %s and amount = %d", tableName, bnNumber, catalogNumber, amount));
    }

    public Map<String, Map<Integer, Discount>> findAllSuppliersProductDiscounts(String bnNumber) throws SQLException {
        if(suppliersDiscountsCached.containsKey(bnNumber))
            return suppliersDiscountsCached.get(bnNumber);

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
        suppliersDiscountsCached.put(bnNumber, suppliersProductDiscounts);
        return suppliersProductDiscounts;
    }
}
