package Backend.DataAccessLayer.InventoryModule;

import Backend.BusinessLayer.InventoryModule.ProductItem;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductItemDataMapper extends AbstractDataMapper {
    public ProductItemDataMapper() {
        super("product_items", new String[]{"serial_number", "is_defective", "defection_date", "supplier_id", "supplier_price", "supplier_discount", "sold_price", "expiration_date", "location", "catalog_number", "branch"});
    }

    public void insert(String serial_number, int is_defective, String defection_date, String supplier_id, double supplier_price, double supplier_discount, double sold_price, String expiration_date, String location, String catalog_number, String branch) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %d, '%s', '%s', %f, %f, %f, '%s', '%s', '%s', '%s')",
                tableName, columnsString, serial_number, is_defective, defection_date, supplier_id, supplier_price, supplier_discount,
                sold_price, expiration_date, location, catalog_number, branch));
    }

    public void delete(String serial_number, String catalog_number, String branch) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE serial_number = %s and catalog_number = %s and branch = %s", tableName, serial_number, catalog_number, branch));
    }

    public ProductItem find(String serial_number, String catalog_number, String branch) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE serial_number = '%s' AND catalog_number = '%s' AND branch = '%s'",
                columnsString, tableName, serial_number, catalog_number, branch));
        ProductItem item = new ProductItem(resultSet.getString("serial_number"),
                resultSet.getString("supplier_id"),
                resultSet.getString("expiration_date"),
                LocalDateTime.parse(resultSet.getString("location")));
        item.setIntIsDefective(resultSet.getInt("is_defective"), LocalDateTime.parse(resultSet.getString("defection_date")));
        item.setSupplierPrice(resultSet.getDouble("supplier_price"));
        item.setSupplierDiscount(resultSet.getDouble("supplier_discount"));
        item.setSoldPrice(resultSet.getDouble("sold_price"));
        return item;
    }

    public List<ProductItem> getAllItems() throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s", columnsString,
                tableName));
        List<ProductItem> itemsList = new ArrayList<>();
        while (resultSet.next()) {
            ProductItem item = new ProductItem(resultSet.getString("serial_number"),
                    resultSet.getString("supplier_id"),
                    resultSet.getString("expiration_date"),
                    LocalDateTime.parse(resultSet.getString("location")));
            item.setIntIsDefective(resultSet.getInt("is_defective"), LocalDateTime.parse(resultSet.getString("defection_date")));
            item.setSupplierPrice(resultSet.getDouble("supplier_price"));
            item.setSupplierDiscount(resultSet.getDouble("supplier_discount"));
            item.setSoldPrice(resultSet.getDouble("sold_price"));
            itemsList.add(item);
        }
        return itemsList;
    }

    public List<ProductItem> findAllDefectiveItems() throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE is_defective = 1", columnsString,
                tableName));
        List<ProductItem> itemsList = new ArrayList<>();
        while (resultSet.next()) {
            ProductItem item = new ProductItem(resultSet.getString("serial_number"),
                    resultSet.getString("supplier_id"),
                    resultSet.getString("expiration_date"),
                    LocalDateTime.parse(resultSet.getString("location")));
            item.setIntIsDefective(resultSet.getInt("is_defective"), LocalDateTime.parse(resultSet.getString("defection_date")));
            item.setSupplierPrice(resultSet.getDouble("supplier_price"));
            item.setSupplierDiscount(resultSet.getDouble("supplier_discount"));
            item.setSoldPrice(resultSet.getDouble("sold_price"));
            itemsList.add(item);
        }
        return itemsList;
    }

}
