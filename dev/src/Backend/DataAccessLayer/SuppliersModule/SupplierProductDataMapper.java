package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.Product;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SupplierProductDataMapper extends AbstractDataMapper {
    public SupplierProductDataMapper() {
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

    public void updateBnNumber(String bnNumber,String newBnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET bn_number = %s WHERE bn_number = %s", tableName, newBnNumber, bnNumber));
    }

    public void updateSuppliersCatalogNumber(String bnNumber, String catalogNumber, String suppliersCatalogNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET suppliers_catalog_number = %s WHERE bn_number = %s and catalog_number = %s", tableName, suppliersCatalogNumber, bnNumber, catalogNumber));
    }

    public void updateQuantity(String bnNumber, String catalogNumber, int quantity) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET quantity = %d WHERE bn_number = %s and catalog_number = %s", tableName, catalogNumber, bnNumber));
    }

    public void updatePrice(String bnNumber, String catalogNumber, double price) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET price = %f WHERE bn_number = %s and catalog_number = %s", tableName, price, catalogNumber, bnNumber));
    }
}
