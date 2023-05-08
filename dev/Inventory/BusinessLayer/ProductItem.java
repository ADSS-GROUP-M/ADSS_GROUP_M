package dev.Inventory.BusinessLayer;
import java.time.LocalDateTime;

public class ProductItem {
    private String serial_number;
    private productPair<Boolean, LocalDateTime> isDefective;
    private Boolean isSold;
    private int supplierID;
    private double supplierPrice;
    //default value -1
    private double supplierDiscount;
    private double soldPrice;
    private String location;
    private LocalDateTime expirationDate;

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
    public ProductItem(String serial_number, int supplierID, double supplierPrice, String location, Double supplierDiscount, LocalDateTime expirationDate) {
        this.serial_number = serial_number;
        this.supplierID = supplierID;
        this.supplierPrice = supplierPrice;
        this.location = location;
        this.isSold = false;
        this.soldPrice = -1;
        this.isDefective = null;
        this.expirationDate = expirationDate;
        if(supplierDiscount == null) {
            this.supplierDiscount = supplierDiscount.doubleValue();
        } else
        {
            this.supplierDiscount = 0;
        }
    }
    public boolean isDefective(){
        if (isDefective == null)
            return false;
        return true;
    }

    public void reportAsDefective() {
        if (isDefective == null) {
            this.isDefective = new productPair<Boolean, LocalDateTime>(true, LocalDateTime.now());
        }
    }
    public LocalDateTime getDefectiveDate() {
        return isDefective.getDefectiveDate();
    }

    //TODO: update soldPrice
    public void reportAsSold(double soldPrice) {
        this.isSold = true;
        this.soldPrice = soldPrice;
    }

    public void setLocation(String newLocation){this.location = newLocation;}
    public void setSupplierPrice(double newPrice){this.supplierPrice = newPrice;}
    public void setSupplierID(int newSupplierID){this.supplierID = newSupplierID;}
    public void setSoldPrice(double newSoldPrice){this.soldPrice = newSoldPrice;}
    public String getSerial_number(){return this.serial_number;}
    public String getLocation(){return this.location;}
}
