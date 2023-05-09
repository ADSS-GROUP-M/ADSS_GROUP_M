package Backend.DataAccessLayer.SuppliersModule;

public class DiscountOnAmountDataMapper  extends AbstractDataMapper{
    public DiscountOnAmountDataMapper(String tableName, String columns) {
        super("Discount_on_amount", new String[] {"bn_number", "amount_to_reach", "percentage", "cash"});
    }
}
