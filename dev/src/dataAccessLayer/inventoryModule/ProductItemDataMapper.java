package dataAccessLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.ProductItem;
import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProductItemDataMapper extends AbstractDataMapper {
    private static ProductItemDataMapper instance = null;

    private ProductItemDataMapper() {
        super("product_items", new String[]{"serial_number", "is_defective", "defection_date", "supplier_id", "supplier_price", "supplier_discount", "sold_price", "expiration_date", "location", "catalog_number", "branch", "is_sold", "sold_date"});
    }

    public static ProductItemDataMapper getInstance() {
        if (instance == null)
            instance = new ProductItemDataMapper();
        return instance;
    }

    public void insert(String serial_number, int is_defective, String defection_date, String supplier_id, double supplier_price, double supplier_discount, double sold_price, LocalDateTime expiration_date, String location, String catalog_number, String branch) throws SQLException {
        if(!isExists(serial_number, catalog_number, branch)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String columnsString = String.join(", ", columns);
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %d, '%s', '%s', %f, %f, %f, '%s', '%s', '%s', '%s', 0, '')",
                    tableName, columnsString, serial_number, is_defective, defection_date, supplier_id, supplier_price, supplier_discount,
                    sold_price, expiration_date.format(formatter), location, catalog_number, branch));
        }
    }

    public void update(String serial_number, int is_defective, LocalDateTime defection_date, String supplier_id, double supplier_price, double supplier_discount, double sold_price, LocalDateTime expiration_date, String location, String catalog_number, String branch) throws SQLException {
        if(isExists(catalog_number, serial_number, branch)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            if(is_defective != -1)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET is_defective = '%s' WHERE catalog_number = '%s' and branch = '%s' and serial_number = '%s'", tableName,is_defective,catalog_number,branch, serial_number));
            if(defection_date != null)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET defection_date = '%s' WHERE catalog_number = '%s' and branch = '%s' and serial_number = '%s'", tableName,defection_date.format(formatter),catalog_number,branch, serial_number));
            if(supplier_id != null)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET supplier_id = '%s' WHERE catalog_number = '%s' and branch = '%s' and serial_number = '%s'", tableName,supplier_id,catalog_number,branch, serial_number));
            if(supplier_price != -1)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET supplier_price = '%s' WHERE catalog_number = '%s' and branch = '%s' and serial_number = '%s'", tableName,supplier_price,catalog_number,branch, serial_number));
            if(supplier_discount != -1)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET supplier_discount = '%s' WHERE catalog_number = '%s' and branch = '%s' and serial_number = '%s'", tableName,supplier_discount,catalog_number,branch, serial_number));
            if(sold_price != -1)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET sold_price = '%s' WHERE catalog_number = '%s' and branch = '%s' and serial_number = '%s'", tableName,sold_price,catalog_number,branch, serial_number));
            if(expiration_date != null)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET expiration_date = '%s' WHERE catalog_number = '%s' and branch = '%s' and serial_number = '%s'", tableName,expiration_date.format(formatter),catalog_number,branch, serial_number));
            if(location != null)
                sqlExecutor.executeWrite(String.format("UPDATE %s SET location = '%s' WHERE catalog_number = '%s' and branch = '%s' and serial_number = '%s'", tableName,location,catalog_number,branch, serial_number));
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        while (resultSet.next()) {
            ProductItem item = new ProductItem(resultSet.getString("serial_number"),
                    resultSet.getString("supplier_id"),
                    resultSet.getDouble("supplier_price"),
                    resultSet.getDouble("supplier_discount"),
                    resultSet.getString("location"),
                    LocalDateTime.parse(resultSet.getString("expiration_date"),formatter),
                    resultSet.getString("catalog_number"),
                    Branch.valueOf(resultSet.getString("branch")));
            if(!Objects.equals(resultSet.getString("defection_date"), "")) {
                if(resultSet.getString("defection_date") != null)
                    item.setIntIsDefective(resultSet.getInt("is_defective"), LocalDateTime.parse(resultSet.getString("defection_date"), formatter));
//                    item.setIntIsDefective(resultSet.getInt("is_defective"), formatter));
            }
            item.setSoldPrice(resultSet.getDouble("sold_price"));
            String catalog_num = resultSet.getString("catalog_number");
            item.setIsSold(resultSet.getInt("is_sold"));
            if (!Objects.equals(resultSet.getString("sold_date"), "")) {
                if(resultSet.getString("sold_date") != null)
                    item.setSoldDate(LocalDateTime.parse(resultSet.getString("sold_date"), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            }
            cachedItems.computeIfAbsent(catalog_num, k -> new ArrayList<>());
            // add the item to the existing list in the map
            cachedItems.get(catalog_num).add(item);
        }
        return cachedItems;
    }

    public boolean isExists(String catalog_number, String serial_number, String branch) throws SQLException {
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT COUNT(*) as count FROM %s WHERE catalog_number = '%s' AND serial_number = '%s' AND branch = '%s'", tableName, catalog_number, serial_number, branch));
        if (resultSet.next())
            return resultSet.getInt("count") > 0;
        return false;    }

}
