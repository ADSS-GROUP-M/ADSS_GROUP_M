package Backend.DataAccessLayer.InventoryModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.InventoryModule.ProductItem;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductItemDataMapper extends AbstractDataMapper {
    private Map<String, List<ProductItem>> cachedItems;

    public ProductItemDataMapper() {
        super("product_items", new String[]{"serial_number", "is_defective", "defection_date", "supplier_id", "supplier_price", "supplier_discount", "sold_price", "expiration_date", "location", "catalog_number", "branch"});
        cachedItems = new HashMap<>();
    }

    public void insert(String serial_number, int is_defective, String defection_date, String supplier_id, double supplier_price, double supplier_discount, double sold_price, LocalDateTime expiration_date, String location, String catalog_number, String branch) throws SQLException {
        if(!isExists(serial_number, catalog_number, branch)){
            String columnsString = String.join(", ", columns);
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %d, '%s', '%s', %f, %f, %f, '%s', '%s', '%s', '%s')",
                    tableName, columnsString, serial_number, is_defective, defection_date, supplier_id, supplier_price, supplier_discount,
                    sold_price, expiration_date.toString(), location, catalog_number, branch));
            ProductItem productItem = new ProductItem(serial_number, supplier_id, supplier_price, supplier_discount, location, expiration_date, catalog_number, Branch.valueOf(branch));
            if (!cachedItems.containsKey(catalog_number)) {
                cachedItems.put(catalog_number, new ArrayList<>());
            }

            // add the item to the existing list in the map
            List<ProductItem> productList = cachedItems.get(catalog_number);
            productList.add(productItem);

        }
    }

    public void delete(String serial_number, String catalog_number, String branch) throws SQLException {
        if (isExists(serial_number, catalog_number, branch)) {
            // remove the product item from the corresponding list
            List<ProductItem> productList = cachedItems.get(catalog_number);
            if (productList != null) {
                productList.removeIf(productItem -> productItem.getSerial_number().equals(serial_number));
            }
            // remove the row from the database
            sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE serial_number = '%s' AND catalog_number = '%s' AND branch = '%s'", tableName, serial_number, catalog_number, branch));
        }
    }

    public ProductItem find(String serial_number, String catalog_number, String branch){
        for (List<ProductItem> productItemList : cachedItems.values()) {
            for (ProductItem productItem : productItemList) {
                if (productItem.getSerial_number().equals(serial_number) &&
                        productItem.getCatalog_number().equals(catalog_number) &&
                        productItem.getBranch().equals(Branch.valueOf(branch))) {
                    return productItem;
                }
            }
        }
        return null;
    }

    public void initializeCache() throws SQLException {
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
            // check if catalog_number is missing in the map
            if (!cachedItems.containsKey(catalog_num)) {
                cachedItems.put(catalog_num, new ArrayList<>());
            }

            // add the item to the existing list in the map
            List<ProductItem> productList = cachedItems.get(catalog_num);
            productList.add(item);
        }
    }

    public List<ProductItem> getDefectiveProducts() {
        List<ProductItem> defectiveProducts = new ArrayList<>();
        for (List<ProductItem> productList : cachedItems.values()) {
            for (ProductItem productItem : productList) {
                if (productItem.isDefective()) {
                    defectiveProducts.add(productItem);
                }
            }
        }
        return defectiveProducts;
    }

    private boolean isExists(String serial_number, String catalog_number, String branch) {
        for (List<ProductItem> productItemList: cachedItems.values()) {
            for(ProductItem productItem: productItemList){
                if (productItem.getSerial_number().equals(serial_number) &&
                        productItem.getCatalog_number().equals(catalog_number) &&
                        productItem.getBranch().equals(Branch.valueOf(branch))) {
                    return true;
                }
            }
        }
        return false;
    }

}
