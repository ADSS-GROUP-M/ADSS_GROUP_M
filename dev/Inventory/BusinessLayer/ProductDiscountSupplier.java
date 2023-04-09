package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;

public class ProductDiscountSupplier {
    private int productTypeID;
    private String branch;
    private double discount;
    private int supplierID;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public ProductDiscountSupplier(int productTypeID, String branch, LocalDateTime startDate, LocalDateTime endDate, double discount, int supplierID){
        this.productTypeID= productTypeID;
        this.branch = branch;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discount = discount;
        this.supplierID = supplierID;
    }

    //should be in the controller?
    public double getDiscount(int productID, String branch){
        //TODO: need to implement
        throw new RuntimeException();
    }
}
