package Backend.DataAccessLayer.InventoryModule;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

public class ProductItemDataMapper extends AbstractDataMapper {
    public ProductItemDataMapper() {
        super("product_items", new String[]{"serial_number", "is_defective", "defection_date", "supplier_id", "supplier_price", "supplier_discount", "sold_price", "expiration_date", "location", "catalog_number", "branch"});
    }
}
