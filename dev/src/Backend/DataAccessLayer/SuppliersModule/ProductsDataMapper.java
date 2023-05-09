package Backend.DataAccessLayer.SuppliersModule;

public class ProductsDataMapper  extends AbstractDataMapper{
    public ProductsDataMapper(String tableName, String columns) {
        super("Products", new String[]{"catalog_number", "name", "manufacture"});
    }
}
