package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.InventoryModule.ProductController;
import Backend.BusinessLayer.InventoryModule.ProductItem;
import Backend.DataAccessLayer.InventoryModule.ProductDAO;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class ProductsDataMapper  extends AbstractDataMapper {
//    private List<ProductDAO> cachedProducts;
    private static ProductsDataMapper instance = null;

    private ProductsDataMapper() {
        super("products", new String[]{"catalog_number", "name", "manufacture", "category"});
//        cachedProducts = new ArrayList<>();
    }

    public static ProductsDataMapper getInstance() {
        if (instance == null)
            instance = new ProductsDataMapper();
        return instance;
    }

    public void insert(String catalog_number, String name, String manufacture) throws SQLException {
        try {
            if (!isExists(catalog_number)) {
                String columnsString = String.join(", ", columns);
                sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %s, '%s')",
                        tableName, columnsString, name, catalog_number, manufacture));
//            ProductDAO productDAO = new ProductDAO(catalog_number, name);
//            productDAO.setManufacture(manufacture);
//            cachedProducts.add(productDAO);
            }
        }
        catch (SQLException e){
            //TODO: Handle the exception appropriately
        }
    }

    public void update(String catalog_number, String newName, String newManufacture) throws SQLException {
        if (!isExists(catalog_number)){
            if(newName != null)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET name = '%s' WHERE catalog_number = '%s'", tableName,newName,catalog_number));
            if(newManufacture != null)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET manufacture = '%s' WHERE catalog_number = '%s'", tableName,newManufacture,catalog_number));
        }
    }

    public void delete(String catalog_number) throws SQLException {
        if (isExists(catalog_number)) {
            sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE catalog_number = %s", tableName, catalog_number));
//            cachedProducts.removeIf(productDAO -> productDAO.getCatalog_number().equals(catalog_number));
        }
    }

    public List<ProductDAO> initializeCache() throws SQLException {
        List<ProductDAO> cachedProducts = new ArrayList<>();
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
        return cachedProducts;
    }

    public boolean isExists(String catalogNumber) throws SQLException {
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT COUNT(*) as count FROM %s WHERE catalog_number = '%s'", tableName, catalogNumber));
        return resultSet.getInt("count") > 0;
    }

//    public List<ProductDAO> getCachedProductsPairBranch() {
//        return cachedProducts;
//    }
}
