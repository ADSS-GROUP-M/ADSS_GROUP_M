package dev.Inventory.BusinessLayer.InventoryModule;
import java.time.LocalDateTime;

public class ProductItem {
    private String serial_number;
    private productPair<Boolean, LocalDateTime> isDefective;
    private Boolean isSold;
    private LocalDateTime soldDate;
    private String supplierID;
    private double supplierPrice;
    private double supplierDiscount;
    //default value -1
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
    public ProductItem(String serial_number,String supplierID, String location, LocalDateTime expirationDate) {
        this.serial_number = serial_number;
        this.supplierID = supplierID;
        this.location = location;
        this.isSold = false;
        this.soldDate = null;
        this.soldPrice = -1;
        this.isDefective = null;
        this.expirationDate = expirationDate;
        // TODO: connect to supplier controller and update supplier price and discount
        // this.supplierPrice this.supplierDiscount
//        this.supplierPrice = 0;
//        this.supplierDiscount = 0;

    }
    public boolean isDefective(){return isSold();}
    public boolean isSold(){
        return isSold;
    }

    public LocalDateTime getSoldDate(){
        return this.soldDate;
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
    public void setSupplierID(String newSupplierID){this.supplierID = newSupplierID;}
    public void setSoldPrice(double newSoldPrice){
        this.soldPrice = newSoldPrice;
        this.soldDate = LocalDateTime.now();
    }
    public double getSupplierPrice(){return this.supplierPrice;}
    public double getSupplierDiscount(){return this.supplierDiscount;}
    public String getSerial_number(){return this.serial_number;}
    public String getLocation(){return this.location;}
}
