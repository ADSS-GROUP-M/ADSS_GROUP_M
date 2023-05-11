package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.Product;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SupplierProductDataMapper extends AbstractDataMapper {
    public SupplierProductDataMapper(String tableName, String columns) {
        super("suppliers_products", new String[]{"bn_number", "catalog_number", "suppliers_catalog_number", "quantity", "price"});
    }

    public void insert(String bnNumber, String catalogNumber, String suppliersCatalogNumber, int quantity, double price) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES(%s, %s, %s, %d, %f)",tableName, columnsString, bnNumber,
                catalogNumber, suppliersCatalogNumber, quantity, price));
    }

    public void delete(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s", tableName, bnNumber));
    }

    public void delete(String bnNumber, String catalogNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s and catalog_number = %s", tableName, bnNumber, catalogNumber));
    }

    public List<Product> find(String bnNumber) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = %s", columnsString, tableName, bnNumber));
        List<Product> products = new LinkedList<>();
        while (resultSet.next()){
            products.add(new Product(resultSet.getString("catalog_number"), resultSet.getString("suppliers_catalog_number"),
                    resultSet.getDouble("price"), resultSet.getInt("quantity")));
        }
        return products;
    }
}
