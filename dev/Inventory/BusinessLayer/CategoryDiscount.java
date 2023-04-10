package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;

public class CategoryDiscount {
    private double discount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String categoryName;
    private  String branch;

    public CategoryDiscount(LocalDateTime startDate, LocalDateTime endDate, double discount, String categoryName, String branch){
        this.startDate = startDate;
        this.endDate = endDate;
        this.discount = discount;
        this.categoryName = categoryName;
        this.branch = branch;
    }
    public Boolean isDateInRange(LocalDateTime dateToValidate){
        return dateToValidate.isAfter(startDate) && dateToValidate.isBefore(endDate);
    }
    public Boolean isDateInRange(LocalDateTime startDate, LocalDateTime endDate){
        return isDateInRange(startDate) && isDateInRange(endDate);
    }
    public double getDiscount(LocalDateTime dateToValidate){
        if(isDateInRange(dateToValidate))
            return discount;
        else
            return -1;
    }
    public double getDiscountInRange(LocalDateTime startDate, LocalDateTime endDate){
        if(isDateInRange(startDate) && isDateInRange(endDate))
            return discount;
        else
            return -1;
    }
}
