package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DiscountController {
    public List<ProductDiscountSupplier> supplierDiscount;
    public List<ProductStoreDiscount> storeDiscounts;

    //should be singleton?
    public DiscountController() {
        this.supplierDiscount = new ArrayList<ProductDiscountSupplier>();
        this.storeDiscounts = new ArrayList<ProductStoreDiscount>();
    }

    public void createSupplierDiscount(int productID, double discount, int supplierID, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public void createStoreDiscount(int productID, double discount, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public double getTodayBiggestStoreDiscountI(int productId){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public double getTodaySupplierDiscountI(int productId){
        //TODO: need to implement
        throw new RuntimeException();
    }

}
