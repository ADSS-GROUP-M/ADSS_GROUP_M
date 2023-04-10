package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountController {
    public Map<String, List<ProductDiscountSupplier>> supplierDiscount;
    public Map<String, List<ProductStoreDiscount>> storeDiscounts;

    //should be singleton?
    public DiscountController() {
        this.storeDiscounts = new HashMap<String, List<ProductStoreDiscount>>();
        this.supplierDiscount = new HashMap<String, List<ProductDiscountSupplier>>();
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

}
