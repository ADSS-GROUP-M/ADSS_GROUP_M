package Backend.DataAccessLayer.InventoryModule;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

import java.sql.SQLException;

public class StoreProductDiscountDataMapper extends AbstractDataMapper {
    public StoreProductDiscountDataMapper() {
        super("store_product_discount", new String[]{"catalog_number", "start_date", "end_date", "discount"});
    }

    public void insert(String category_name, String start_date, String end_date, double discount) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', '%s', '%s', '%s')",
                tableName, columnsString, category_name, start_date, end_date, discount));
    }

}
