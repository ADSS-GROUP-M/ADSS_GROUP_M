package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.Supplier;

import java.util.List;

public class SupplierDataMapper extends AbstractDataMapper {
    private List<Supplier> cachedSuppliers;

    public SupplierDataMapper(String tableName, String columns) {
        super("Suppliers", new String[]{"bn_number", "name", "bank", "branch", "account_number", "payment_method"});
    }
}
