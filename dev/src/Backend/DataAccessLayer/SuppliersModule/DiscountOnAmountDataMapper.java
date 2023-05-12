package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.Discounts.CashDiscount;
import Backend.BusinessLayer.SuppliersModule.Discounts.Discount;
import Backend.BusinessLayer.SuppliersModule.Discounts.PercentageDiscount;
import Backend.BusinessLayer.SuppliersModule.Pair;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;

public class DiscountOnAmountDataMapper  extends AbstractDataMapper {
    public DiscountOnAmountDataMapper() {
        super("discount_on_amount", new String[] {"bn_number", "amount_to_reach", "percentage", "cash"});
    }

    public void insert(String bnNumber, double amountToReach, Discount discount) throws SQLException {
        double percentage = -1,cash = -1;
        if(discount instanceof PercentageDiscount)
            percentage = ((PercentageDiscount) discount).getPercentage();
        else
            cash = ((CashDiscount)discount).getAmountOfDiscount();
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES(%s, %d, %f, %f)",tableName, columnsString, bnNumber,
                amountToReach, percentage, cash));
    }

    public void delete(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s", tableName, bnNumber));
    }

    public void updateAmountToReach(String bnNumber, double amountToReach) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET amount_to_reach = %d WHERE bn_number = %s", tableName, amountToReach, bnNumber));
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
        if(resultSet.next()) {
            Discount discount = null;
            if (resultSet.getDouble("percentage") != -1)
                discount = new PercentageDiscount(resultSet.getDouble("percentage"));
            else
                discount = new CashDiscount(resultSet.getDouble("cash"));
            return new Pair<>(resultSet.getInt("amount_to_reach"), discount);
        }
        return null;
    }


}
