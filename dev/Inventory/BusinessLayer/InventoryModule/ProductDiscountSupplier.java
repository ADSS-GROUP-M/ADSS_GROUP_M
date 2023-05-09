package dev.Inventory.BusinessLayer.InventoryModule;

import java.time.LocalDateTime;
import java.util.List;

public class ProductDiscountSupplier {
    private String catalog_number;
    private String branch;
    private double discount;
    private int supplierID;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public ProductDiscountSupplier(String catalog_number, String branch, LocalDateTime startDate, LocalDateTime endDate, double discount, int supplierID){
        this.catalog_number= catalog_number;
        this.branch = branch;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discount = discount;
        this.supplierID = supplierID;
    }

    public Boolean isDateInRange(LocalDateTime dateToValidate){
        return dateToValidate.isAfter(startDate) && dateToValidate.isBefore(endDate);
    }
    public double getDiscountPerDate(LocalDateTime dateToValidate, String catalog_number){
        if(isDateInRange(dateToValidate) && catalog_number == this.catalog_number)
            return discount;
        else
            return -1;
    }
    public double getDiscount(String catalog_number){
        if(catalog_number == this.catalog_number)
            return discount;
        else
            return -1;
    }

    public Boolean isDateInRange(LocalDateTime startDate, LocalDateTime endDate){
        return isDateInRange(startDate) && isDateInRange(endDate);
    }
    public double getDiscountInRange(LocalDateTime startDate, LocalDateTime endDate){
        if(isDateInRange(startDate) && isDateInRange(endDate))
            return discount;
        else
            return -1;
    }

    public List<ProductDiscountSupplier> addDiscountSupplier(List<ProductDiscountSupplier> discountSuppliersList,LocalDateTime startDate, LocalDateTime endDate, String catalog_number){
        if(isDateInRange(startDate,endDate) && catalog_number == this.catalog_number)
            discountSuppliersList.add(this);
        return discountSuppliersList;
    }

}
