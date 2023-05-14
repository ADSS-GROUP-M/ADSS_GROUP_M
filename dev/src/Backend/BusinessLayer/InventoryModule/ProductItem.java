package Backend.BusinessLayer.InventoryModule;
import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.SuppliersModule.OrderController;

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
    private Branch branch;
    private String catalog_number;

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
    public ProductItem(String serial_number,String supplierID, double supplierPrice, double supplierDiscount, String location, LocalDateTime expirationDate, String catalog_number, Branch branch) {
        this.serial_number = serial_number;
        this.supplierID = supplierID;
        this.location = location;
        this.isSold = false;
        this.soldDate = null;
        this.soldPrice = -1;
        this.isDefective = null;
        this.expirationDate = expirationDate;
        this.supplierPrice = supplierPrice;
        this.supplierDiscount = supplierDiscount;
        this.branch = branch;
        this.catalog_number = catalog_number;
    }
    public boolean isDefective(){
        if(isDefective == null)
            return false;
        else return isDefective.getIsDefective();
    }
    public boolean isSold(){
        if (this.isSold == null)
            return false;
        return true;
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

    public void reportAsSold(double soldPrice) {
        this.isSold = true;
        this.soldPrice = soldPrice;
        this.soldDate = LocalDateTime.now();
    }

    public void setLocation(String newLocation){this.location = newLocation;}
    public void setIntIsDefective(int newIsDefective, LocalDateTime defective_date) {
        if(newIsDefective == 0){
            isDefective = new productPair<Boolean, LocalDateTime>(false, defective_date);
        } else
            isDefective = new productPair<Boolean, LocalDateTime>(true, defective_date);
    }
    public void setSupplierPrice(double newPrice){this.supplierPrice = newPrice;}
    public void setSupplierID(String newSupplierID){this.supplierID = newSupplierID;}
    public void setSoldPrice(double newSoldPrice){
        if(!isSold)
            reportAsSold(newSoldPrice);
        else
            this.soldPrice = newSoldPrice;
    }
    public void setSupplierDiscount(double newDiscount) {this.supplierDiscount = newDiscount;}
    public double getSupplierPrice(){return this.supplierPrice;}
    public double getSupplierDiscount(){return this.supplierDiscount;}
    public String getSerial_number(){return this.serial_number;}
    public String getLocation(){return this.location;}
    public String getCatalog_number() {return this.catalog_number;}
    public Branch getBranch() {return this.branch;}
}
