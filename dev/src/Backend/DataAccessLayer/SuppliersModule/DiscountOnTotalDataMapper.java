package Backend.DataAccessLayer.SuppliersModule;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

public class DiscountOnTotalDataMapper  extends AbstractDataMapper {
    public DiscountOnTotalDataMapper(String tableName, String columns) {
        super("discount_on_total", new String[] {"bn_number", "price_to_reach", "percentage", "cash"});
    }
}
