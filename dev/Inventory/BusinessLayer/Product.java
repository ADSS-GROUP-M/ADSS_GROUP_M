package dev.Inventory.BusinessLayer;
import javafx.util.Pair;
import java.time.LocalDateTime;
import java.util.Date;

public class Product {
    private int productID;
    private Pair<Boolean, LocalDateTime> isDefective;
    private Boolean isSold;
    private int supplierID;
    private double supplierPrice;
    //default value -1
    private double soldPrice;
    private String location;

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
    public void updateIsDefective() {
        if (isDefective == null) {
            this.isDefective = new Pair<Boolean, LocalDateTime>(true, LocalDateTime.now());
        }
    }
    //TODO: update soldPrice
    public void updateIsSold() {
        this.isSold = true;
    }


}
