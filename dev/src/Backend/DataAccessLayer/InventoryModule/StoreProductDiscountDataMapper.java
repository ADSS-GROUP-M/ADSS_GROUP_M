package Backend.DataAccessLayer.InventoryModule;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

public class StoreProductDiscountDataMapper extends AbstractDataMapper {
    public StoreProductDiscountDataMapper() {
        super("store_product_discount", new String[]{"catalog_number", "start_date", "end_date", "discount"});
    }

}
