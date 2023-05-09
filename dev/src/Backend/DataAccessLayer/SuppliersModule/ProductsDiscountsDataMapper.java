package Backend.DataAccessLayer.SuppliersModule;

public class ProductsDiscountsDataMapper extends AbstractDataMapper{
    public ProductsDiscountsDataMapper(String tableName, String columns) {
        super("Products_discounts", new String[]{"bn_number","catalog_number", "amount", "percentage", "cash"});
    }
}
