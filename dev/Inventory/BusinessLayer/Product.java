package dev.Inventory.BusinessLayer;
import javafx.util.Pair;
import java.time.LocalDateTime;
import java.util.Date;

public class Product {
    private int productID;
    private productPair<Boolean, LocalDateTime> isDefective;
    private Boolean isSold;
    private int supplierID;
    private double supplierPrice;
    //default value -1
    private double soldPrice;
    private String location;

    protected class productPair<F, S> {
        private final F first;
        private final S second;

        protected productPair(F first, S second) {
            this.first = first;
            this.second = second;
        }
        public F getIsDefective() {
            return first;
        }
        public S getDefectiveDate() {
            return second;
        }
    }
    public Product(int productID, int supplierID, double supplierPrice, String location) {
        this.productID = productID;
        this.supplierID = supplierID;
        this.supplierPrice = supplierPrice;
        this.location = location;
        this.isSold = false;
        this.soldPrice = -1;
        this.isDefective = null;
    }
    public boolean isDefective(){
        if (isDefective == null)
            return false;
        return true;
    }

    public void setIsDefective() {
        if (isDefective == null) {
            this.isDefective = new productPair<Boolean, LocalDateTime>(true, LocalDateTime.now());
        }
    }
    public LocalDateTime getDefectiveDate() {
        return isDefective.getDefectiveDate();
    }

    //TODO: update soldPrice
    public void updateIsSold(double soldPrice) {
        this.isSold = true;
        this.soldPrice = soldPrice;
    }

    public void setLocation(String newLocation){this.location = newLocation;}
    public void setSupplierPrice(double newPrice){this.supplierPrice = newPrice;}
    public void setSupplierID(int newSupplierID){this.supplierID = newSupplierID;}
    public void setSoldPrice(double newSoldPrice){this.soldPrice = newSoldPrice;}


}
