package Backend.DataAccessLayer.InventoryModule;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

import java.sql.SQLException;

public class ProductPairBranchDataMapper extends AbstractDataMapper {
    public ProductPairBranchDataMapper() {
        super("product_pair_branch", new String[]{"branch_name", "product_catalog_number", "original_store_price", "notification_min"});
    }

    public void insert(String branch_name, String product_catalog_number, double original_store_price) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %s, '%f')",
                tableName, columnsString, branch_name, product_catalog_number, original_store_price));
    }

}
