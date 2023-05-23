package dataAccessLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.ProductStoreDiscount;
import exceptions.DalException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountManagerMapper {

    StoreProductDiscountDataMapper storeProductDiscountDataMapper;
    public static DiscountManagerMapper instance = null;
    Map<Branch, Map<String, List<ProductStoreDiscount>>> cached_discounts;

    private DiscountManagerMapper() throws DalException {
        storeProductDiscountDataMapper = StoreProductDiscountDataMapper.getInstance();
        try {
            cached_discounts = storeProductDiscountDataMapper.initializeCache();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public static DiscountManagerMapper getInstance() throws DalException {
        if (instance == null) {
            return new DiscountManagerMapper();
        } else {
            return instance;
        }
    }

    public void createDiscount(String catalog_number, String branch, LocalDateTime startDate, LocalDateTime endtDate, double discount){
        try {
            storeProductDiscountDataMapper.insert(catalog_number,startDate.toString(),endtDate.toString(),discount,branch);
            ProductStoreDiscount productStoreDiscount = new ProductStoreDiscount(catalog_number,Branch.valueOf(branch),startDate,endtDate,discount);
            cached_discounts.computeIfAbsent(Branch.valueOf(branch), k -> new HashMap<>())
                    .computeIfAbsent(catalog_number, k -> new ArrayList<>())
                    .add(productStoreDiscount);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Map<Branch, Map<String, List<ProductStoreDiscount>>> getCached_discounts() {
        return cached_discounts;
    }
}
