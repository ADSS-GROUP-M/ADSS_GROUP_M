package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.Discounts.CashDiscount;
import Backend.BusinessLayer.SuppliersModule.Discounts.Discount;
import Backend.BusinessLayer.SuppliersModule.Discounts.PercentageDiscount;
import Backend.BusinessLayer.SuppliersModule.Pair;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;

public class DiscountOnTotalDataMapper  extends AbstractDataMapper {
    public DiscountOnTotalDataMapper(String tableName, String columns) {
        super("discount_on_total", new String[] {"bn_number", "price_to_reach", "percentage", "cash"});
    }

    public void insert(String bnNumber, double priceToReach, Discount discount) throws SQLException {
        double percentage = -1,cash = -1;
        if(discount instanceof PercentageDiscount)
            percentage = ((PercentageDiscount) discount).getPercentage();
        else
            cash = ((CashDiscount)discount).getAmountOfDiscount();
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES(%s, %d, %f, %f)",tableName, columnsString, bnNumber,
                priceToReach, percentage, cash));
    }

    public void delete(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s", tableName, bnNumber));
    }

    public void updatePriceToReach(String bnNumber, double priceToReach) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET price_to_reach = %d WHERE bn_number = %s", tableName, priceToReach, bnNumber));
    }

    public void updateDiscount(String bnNumber, Discount discount) throws SQLException {
        double percentage = -1,cash = -1;
        if(discount instanceof PercentageDiscount)
            percentage = ((PercentageDiscount) discount).getPercentage();
        else
            cash = ((CashDiscount)discount).getAmountOfDiscount();
        sqlExecutor.executeWrite(String.format("UPDATE %s SET cash = %f, percentage = %f  WHERE bn_number = %s", tableName, cash, percentage, bnNumber));
    }

    public Pair<Integer, Discount> find(String bnNumber) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = %s", columnsString, tableName, bnNumber));
        Discount discount = null;
        if(resultSet.getDouble("percentage") != -1)
            discount = new PercentageDiscount(resultSet.getDouble("percentage"));
        else
            discount = new CashDiscount(resultSet.getDouble("cash"));
        return new Pair<>(resultSet.getInt("price_to_reach"), discount);
    }
}
