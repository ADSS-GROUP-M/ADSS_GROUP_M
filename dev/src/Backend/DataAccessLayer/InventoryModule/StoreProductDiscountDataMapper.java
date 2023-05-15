package Backend.DataAccessLayer.InventoryModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.InventoryModule.Category;
import Backend.BusinessLayer.InventoryModule.ProductItem;
import Backend.BusinessLayer.InventoryModule.ProductStoreDiscount;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class StoreProductDiscountDataMapper extends AbstractDataMapper {

    private static StoreProductDiscountDataMapper instance = null;
    public StoreProductDiscountDataMapper() {
        super("store_product_discount", new String[]{"catalog_number", "start_date", "end_date", "discount","branch"});
    }

    public static StoreProductDiscountDataMapper getInstance(){
        if(instance == null){
            return new StoreProductDiscountDataMapper();
        } else {
            return instance;
        }
    }
    public void insert(String catalog_number, String start_date, String end_date, double discount, String branch) throws SQLException {
        try {
            String columnsString = String.join(", ", columns);
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', '%s', '%s', '%s')",
                    tableName, columnsString, catalog_number, start_date, end_date, discount, branch));
        }
        catch (SQLException e){
            //TODO: Handle the exception appropriately
        }
    }

    public Map<Branch, Map<String, List<ProductStoreDiscount>>> initializeCache() throws SQLException {
        Map<Branch, Map<String,List<ProductStoreDiscount>>> cachedDiscount = new HashMap<Branch, Map<String,List<ProductStoreDiscount>>>();
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s", columnsString, tableName));
        while (resultSet.next()) {
            ProductStoreDiscount productStoreDiscount = new ProductStoreDiscount(resultSet.getString("catalog_number"),
                    Branch.valueOf(resultSet.getString("branch")),
                    LocalDateTime.parse(resultSet.getString("start_date")),
                    LocalDateTime.parse(resultSet.getString("end_date")),
                    Double.parseDouble("discount"));
            Branch branch = productStoreDiscount.getBranch();
            String catalog_number = productStoreDiscount.getCatalog_number();
            if(!cachedDiscount.containsKey(branch)) {
                Map<String, List<ProductStoreDiscount>> mapProductDiscount = new HashMap<String, List<ProductStoreDiscount>>();
                cachedDiscount.put(branch,mapProductDiscount);
            }
            if(!cachedDiscount.get(branch).containsKey(catalog_number)){
                List<ProductStoreDiscount> listProductStoreDiscount = new ArrayList<ProductStoreDiscount>();
                cachedDiscount.get(branch).put(catalog_number,listProductStoreDiscount);
            }
            cachedDiscount.get(branch).get(catalog_number).add(productStoreDiscount);
        }
        return cachedDiscount;
    }


}
