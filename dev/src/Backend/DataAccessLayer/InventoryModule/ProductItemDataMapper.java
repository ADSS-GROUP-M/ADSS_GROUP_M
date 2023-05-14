package Backend.DataAccessLayer.InventoryModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.InventoryModule.ProductItem;
import Backend.DataAccessLayer.SuppliersModule.ProductsDataMapper;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductItemDataMapper extends AbstractDataMapper {
    private static ProductItemDataMapper instance = null;

    private ProductItemDataMapper() {
        super("product_items", new String[]{"serial_number", "is_defective", "defection_date", "supplier_id", "supplier_price", "supplier_discount", "sold_price", "expiration_date", "location", "catalog_number", "branch"});
    }

    public static ProductItemDataMapper getInstance() {
        if (instance == null)
            instance = new ProductItemDataMapper();
        return instance;
    }

    public void insert(String serial_number, int is_defective, String defection_date, String supplier_id, double supplier_price, double supplier_discount, double sold_price, LocalDateTime expiration_date, String location, String catalog_number, String branch) throws SQLException {
        if(!isExists(serial_number, catalog_number, branch)){
            String columnsString = String.join(", ", columns);
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %d, '%s', '%s', %f, %f, %f, '%s', '%s', '%s', '%s')",
                    tableName, columnsString, serial_number, is_defective, defection_date, supplier_id, supplier_price, supplier_discount,
                    sold_price, expiration_date.toString(), location, catalog_number, branch));
        }
    }

    public void update(String serial_number, int is_defective, LocalDateTime defection_date, String supplier_id, double supplier_price, double supplier_discount, double sold_price, LocalDateTime expiration_date, String location, String catalog_number, String branch) throws SQLException {
        if(isExists(serial_number, catalog_number, branch)){
            if(is_defective != -1)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET is_defective = '%s' WHERE catalog_number = '%s' and branch = '%s'", tableName,is_defective,catalog_number,branch));
            if(defection_date != null)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET defection_date = '%s' WHERE catalog_number = '%s' and branch = '%s'", tableName,defection_date.toString(),catalog_number,branch));
            if(supplier_id != null)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET supplier_id = '%s' WHERE catalog_number = '%s' and branch = '%s'", tableName,supplier_id,catalog_number,branch));
            if(supplier_price != -1)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET supplier_price = '%s' WHERE catalog_number = '%s' and branch = '%s'", tableName,supplier_price,catalog_number,branch));
            if(supplier_discount != -1)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET supplier_discount = '%s' WHERE catalog_number = '%s' and branch = '%s'", tableName,supplier_discount,catalog_number,branch));
            if(sold_price != -1)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET sold_price = '%s' WHERE catalog_number = '%s' and branch = '%s'", tableName,sold_price,catalog_number,branch));
            if(expiration_date != null)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET expiration_date = '%s' WHERE catalog_number = '%s' and branch = '%s'", tableName,expiration_date.toString(),catalog_number,branch));
            if(location != null)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET location = '%s' WHERE catalog_number = '%s' and branch = '%s'", tableName,location,catalog_number,branch));
        }
    }

    public void delete(String serial_number, String catalog_number, String branch) throws SQLException {
        if (isExists(serial_number, catalog_number, branch)) {
            sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE serial_number = '%s' AND catalog_number = '%s' AND branch = '%s'", tableName, serial_number, catalog_number, branch));
        }
    }

    public Map<String, List<ProductItem>> initializeCache() throws SQLException {
        Map<String, List<ProductItem>> cachedItems = new HashMap<>();
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s", columnsString, tableName));
        while (resultSet.next()) {
            ProductItem item = new ProductItem(resultSet.getString("serial_number"),
                    resultSet.getString("supplier_id"),
                    resultSet.getDouble("supplier_price"),
                    resultSet.getDouble("supplier_discount"),
                    resultSet.getString("location"),
                    LocalDateTime.parse(resultSet.getString("expiration_date")),
                    resultSet.getString("catalog_number"),
                    Branch.valueOf(resultSet.getString("branch")));
            item.setIntIsDefective(resultSet.getInt("is_defective"), LocalDateTime.parse(resultSet.getString("defection_date")));
            item.setSoldPrice(resultSet.getDouble("sold_price"));

            String catalog_num = resultSet.getString("catalog_number");
            cachedItems.put(catalog_num, new ArrayList<>());
            // add the item to the existing list in the map
            List<ProductItem> productList = cachedItems.get(catalog_num);
            productList.add(item);
        }
        return cachedItems;
    }

    public boolean isExists(String catalog_number, String serial_number, String branch) throws SQLException {
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT COUNT(*) as count FROM %s WHERE catalog_number = '%s' AND serial_number = '%s' AND branch = '%s'", tableName, catalog_number, serial_number, branch));
        return resultSet.getInt("count") > 0;
    }

}
