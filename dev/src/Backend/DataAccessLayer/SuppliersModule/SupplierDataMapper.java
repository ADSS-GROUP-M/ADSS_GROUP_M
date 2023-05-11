package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.Supplier;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

import java.util.List;

public class SupplierDataMapper extends AbstractDataMapper {
    private List<Supplier> cachedSuppliers;

    public SupplierDataMapper(String tableName, String columns) {
        super("suppliers", new String[]{"bn_number", "name", "bank", "branch", "account_number", "payment_method"});
    }
}
