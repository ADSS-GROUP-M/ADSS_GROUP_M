package Backend.DataAccessLayer.SuppliersModule;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

public class DiscountOnAmountDataMapper  extends AbstractDataMapper {
    public DiscountOnAmountDataMapper(String tableName, String columns) {
        super("discount_on_amount", new String[] {"bn_number", "amount_to_reach", "percentage", "cash"});
    }
}
