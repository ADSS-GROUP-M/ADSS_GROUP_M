package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;

public class ProductStoreDiscount {
    private int productTypeID;
    private String branch;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double discount;

    public ProductStoreDiscount(int productTypeID, String branch, LocalDateTime startDate, LocalDateTime endDate, double discount){
        this.productTypeID= productTypeID;
        this.branch = branch;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discount = discount;
    }


    // those two should be in the controller?
    public double getDiscountPerTypeID(int productTypeID, String branch){
        //TODO: need to implement
        throw new RuntimeException();
    }

    // the meaning is today discount? or should we get range of date?
    public double getDiscountPerDate(int productTypeID, String branch){
        //TODO: need to implement
        throw new RuntimeException();
    }

}
