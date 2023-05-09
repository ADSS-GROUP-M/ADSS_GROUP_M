package Backend.DataAccessLayer;

public class DiscountOnTotalDataMapper  extends AbstractDataMapper{
    public DiscountOnTotalDataMapper(String tableName, String columns) {
        super("Discount_on_total", new String[] {"bn_number", "price_to_reach", "percentage", "cash"});
    }
}
