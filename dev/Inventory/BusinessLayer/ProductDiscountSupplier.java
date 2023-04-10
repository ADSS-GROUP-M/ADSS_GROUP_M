package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;
import java.util.List;

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

    public Boolean isDateInRange(LocalDateTime dateToValidate){
        return dateToValidate.isAfter(startDate) && dateToValidate.isBefore(endDate);
    }
    public double getDiscountPerDate(LocalDateTime dateToValidate, int productTypeID){
        if(isDateInRange(dateToValidate) && productTypeID == this.productTypeID)
            return discount;
        else
            return -1;
    }
    public double getDiscount(int productTypeID){
        if(productTypeID == this.productTypeID)
            return discount;
        else
            return -1;
    }

    public List<ProductDiscountSupplier> addDiscountSupplier(List<ProductDiscountSupplier> discountSuppliersList,LocalDateTime dateToValidate, int productTypeID){
        if(isDateInRange(dateToValidate) && productTypeID == this.productTypeID)
            discountSuppliersList.add(this);
        return discountSuppliersList;
    }

}
