package Backend.BusinessLayer.InventoryModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;

import java.time.LocalDateTime;
import java.util.List;

public class ProductStoreDiscount {
    private String catalog_number;
    private Branch branch;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double discount;

    public ProductStoreDiscount(String catalog_number, Branch branch, LocalDateTime startDate, LocalDateTime endDate, double discount){
        this.catalog_number= catalog_number;
        this.branch = branch;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discount = discount;
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

    public List<ProductStoreDiscount> addDiscountStore(List<ProductStoreDiscount> discountSuppliersList, LocalDateTime startDate, LocalDateTime endDate){
        if(isDateInRange(startDate,endDate))
            discountSuppliersList.add(this);
        return discountSuppliersList;
    }

    public Branch getBranch(){return this.branch;}
    public String getCatalog_number(){return this.catalog_number;}
}
