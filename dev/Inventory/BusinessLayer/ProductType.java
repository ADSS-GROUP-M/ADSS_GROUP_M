package dev.Inventory.BusinessLayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProductType {
    private  int productTypeID;
    private  String name;
    private String manufacturer;
    private int storeAmount;
    private int warehouseAmount;
    private double originalSupplierPrice;
    private double originalStorePrice;
    private Category category;
    private List<Category> subCategory;
    private int notidicationMin;
    private List<Product> products;
    private String branch;

    public ProductType(int productTypeID, String name, String manufacturer, int storeAmount, int warehouseAmount, double originalSupplierPrice, double originalStorePrice, int notidicationMin, String branch){
        this.productTypeID = productTypeID;
        this.name = name;
        this.manufacturer = manufacturer;
        this.storeAmount = storeAmount;
        this.warehouseAmount = warehouseAmount;
        this.originalSupplierPrice = originalSupplierPrice;
        this.originalStorePrice = originalStorePrice;
        this.category = null;
        this.subCategory = new ArrayList<Category>();
        this.notidicationMin = notidicationMin;
        this.products = new ArrayList<Product>();
        this.branch = branch;
    }

    public void updateDefective(List<Integer> productsID){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public void updateSold(List<Integer> productsID){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public void updateNotificationMin(int newVal){
        //TODO: need to implement
        throw new RuntimeException();
    }

}
