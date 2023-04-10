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

    public Boolean isDateInRange(LocalDateTime time){
        //TODO: need to implement
        throw new RuntimeException();
    }

}
