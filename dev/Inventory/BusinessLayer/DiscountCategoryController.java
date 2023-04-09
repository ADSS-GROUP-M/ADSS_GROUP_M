package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountCategoryController {
    public Map<String, List<ProductDiscountSupplier>> supplierDiscount;
    public Map<String, List<ProductStoreDiscount>> storeDiscounts;

//    public Map<String, List<CategoryDiscount>>
    public Map<String,Category> categories;

    //should be singleton?
    public DiscountCategoryController() {
        this.storeDiscounts = new HashMap<String, List<ProductStoreDiscount>>();
        this.supplierDiscount = new HashMap<String, List<ProductDiscountSupplier>>();
        this.categories = new HashMap<String, Category>();
    }

    public void createCategory(String name, String type){
        if(!categories.containsKey(name))
            categories.put(name,new Category(name,type));
    }

    public void createSupplierDiscount(int productID, String branch,double discount, int supplierID, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public void createStoreDiscount(int productID, String branch,double discount, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public double getTodayBiggestStoreDiscountI(int productID, String branch){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public double getTodaySupplierDiscountI(int productID, String branch){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public List<ProductStoreDiscount> getStoreDiscountPerDate(int productID, String branch, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public double getStoreMinDiscount(int productID, String branch){
        //TODO: need to implement
        throw new RuntimeException();
    }

    public List<ProductStoreDiscount> getSupplierDiscountPerDate(int productID, String branch, int supplierID, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public double getSupplierMinDiscount(int productID, String branch, int supplierID){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public void updateDiscountPerCategory(String name, int discount, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }

    public List<Product> getStockProduct(List<Category> categories, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }
}
