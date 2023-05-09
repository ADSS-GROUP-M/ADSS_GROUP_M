package Backend.DataAccessLayer.SuppliersModule;

public class SupplierProductDataMapper extends AbstractDataMapper{
    public SupplierProductDataMapper(String tableName, String columns) {
        super("Suppliers_products", new String[]{"bn_number", "catalog_number", "suppliers_catalog_number", "quantity", "price"});
    }
}
