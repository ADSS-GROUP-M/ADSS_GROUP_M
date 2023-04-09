package dev.Inventory.BusinessLayer;

import java.util.Date;

public class Product {
    private int productID;
//    private Pair<Boolean, Date> isDefective;
    private Boolean isSold;
    private  int supplierID;
    private double supplierPrice;
    //default value -1
    private double soldPrice;
    private String location;

    public Product(int productID, int supplierID, double supplierPrice, String location){
        this.productID = productID;
        this.supplierID = supplierID;
        this.supplierPrice = supplierPrice;
        this.location = location;
        this.isSold = false;
        this.soldPrice = -1;
    }
}
