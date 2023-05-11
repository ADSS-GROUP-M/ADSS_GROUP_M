package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.InventoryModule.ProductItem;
import Backend.DataAccessLayer.InventoryModule.ProductDAO;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductsDataMapper  extends AbstractDataMapper {
    private List<ProductDAO> cachedProducts;

    public ProductsDataMapper() {
        super("products", new String[]{"catalog_number", "name", "manufacture", "category"});
    }

    public void insert(String catalog_number, String name, String manufacture) throws SQLException {
        if (!isExists(catalog_number)){
            String columnsString = String.join(", ", columns);
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %s, '%s')",
                    tableName, columnsString, name, catalog_number, manufacture));
            ProductDAO productDAO = new ProductDAO(catalog_number, name);
            productDAO.setManufacture(manufacture);
            cachedProducts.add(productDAO);
        }
    }

    public void delete(String catalog_number) throws SQLException {
        if (isExists(catalog_number)) {
            sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE catalog_number = %s", tableName, catalog_number));
            cachedProducts.removeIf(productDAO -> productDAO.getCatalog_number().equals(catalog_number));
        }
    }

    public void initializeCache() throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s",
                columnsString, tableName));
        while (resultSet.next()) {
            ProductDAO item = new ProductDAO(resultSet.getString("catalog_number"),
                    resultSet.getString("name"));
            item.setManufacture(resultSet.getString("manufacture"));
            item.setCategory(resultSet.getString("category"));
            cachedProducts.add(item);
        }
    }

    public boolean isExists(String catalogNumber) {
        for (ProductDAO product : cachedProducts) {
            if (product.getCatalog_number().equals(catalogNumber)) {
                return true;
            }
        }
        return false;
    }
}
