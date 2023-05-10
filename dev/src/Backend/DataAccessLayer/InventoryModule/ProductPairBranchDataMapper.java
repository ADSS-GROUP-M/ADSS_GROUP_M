package Backend.DataAccessLayer.InventoryModule;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

public class ProductPairBranchDataMapper extends AbstractDataMapper {
    public ProductPairBranchDataMapper() {
        super("product_pair_branch", new String[]{"branch_name", "product_catalog_number", "original_store_price", "notification_min"});
    }
}
