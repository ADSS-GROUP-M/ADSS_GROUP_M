package dataAccessLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.ProductStoreDiscount;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountManagerMapper {

    private final StoreProductDiscountDataMapper storeProductDiscountDataMapper;
    Map<Branch, Map<String, List<ProductStoreDiscount>>> cached_discounts;

    public DiscountManagerMapper(StoreProductDiscountDataMapper storeProductDiscountDataMapper) {
        this.storeProductDiscountDataMapper = storeProductDiscountDataMapper;
        try {
            cached_discounts = this.storeProductDiscountDataMapper.initializeCache();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void createDiscount(String catalog_number, String branch, LocalDateTime startDate, LocalDateTime endtDate, double discount){
        storeProductDiscountDataMapper.insert(catalog_number,startDate.toString(),endtDate.toString(),discount,branch);
        ProductStoreDiscount productStoreDiscount = new ProductStoreDiscount(catalog_number,Branch.valueOf(branch),startDate,endtDate,discount);
        cached_discounts.computeIfAbsent(Branch.valueOf(branch), k -> new HashMap<>())
                .computeIfAbsent(catalog_number, k -> new ArrayList<>())
                .add(productStoreDiscount);
    }

    public Map<Branch, Map<String, List<ProductStoreDiscount>>> getCached_discounts() {
        return cached_discounts;
    }
}
