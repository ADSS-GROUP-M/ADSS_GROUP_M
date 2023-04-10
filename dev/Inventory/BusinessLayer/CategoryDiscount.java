package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;

public class CategoryDiscount {
    private double discount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public CategoryDiscount(LocalDateTime startDate, LocalDateTime endDate, double discount){
        this.startDate = startDate;
        this.endDate = endDate;
        this.discount = discount;
    }

    public Boolean isDateInRange(LocalDateTime time){
        //TODO: need to implement
        throw new RuntimeException();
    }
}
