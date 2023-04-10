package dev.Inventory.BusinessLayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //default value -1
    private int notificationMin;
    private Map<Integer,Product> products;
    private String branch;

    public ProductType(int productTypeID, String name, String manufacturer, int storeAmount, int warehouseAmount, double originalSupplierPrice, double originalStorePrice, String branch){
        this.productTypeID = productTypeID;
        this.name = name;
        this.manufacturer = manufacturer;
        this.storeAmount = storeAmount;
        this.warehouseAmount = warehouseAmount;
        this.originalSupplierPrice = originalSupplierPrice;
        this.originalStorePrice = originalStorePrice;
        this.category = null;
        this.subCategory = new ArrayList<Category>();
        this.notificationMin = -1;
        this.products = new HashMap<Integer,Product>();
        this.branch = branch;
    }

    public void updateDefective(List<Integer> productsID){
        for(Integer defectiveProductID: productsID){
            products.get(defectiveProductID).updateIsDefective();
        }
    }
    // TODO: should add soldPrice and check with the discount table
    public void updateSold(List<Integer> productsID){
        for(Integer soldProductID: productsID){
            products.get(soldProductID).updateIsSold();
        }
    }
    public void updateNotificationMin(int newVal){
        this.notificationMin = newVal;
    }
    public void updateCategory(Category category){
        this.category = category;
        category.addProduct(this);
    }
    public void addSubCategory(Category category){
        subCategory.add(category);
        category.addProduct(this);
    }

    public void addProduct(int productID, int supplierID, double supplierPrice, String location){
        products.put(productID,new Product(productID,supplierID,supplierPrice,location));
    }

    public List<Product> getDefectiveProducts(List<Product> defectiveList){
        for(Product product: products.values()){
            if(product.isDefective())
                defectiveList.add(product);
        }
        return defectiveList;
    }
    //TODO : verify we want to return all products (maybe not defective and sold)
    public Map<Integer,List<Product>> getAllProducts(Map<Integer,List<Product>> allProducts){
        List<Product> currentProducts = new ArrayList<Product>();
        for(Product product: products.values()){
            currentProducts.add(product);
        }
        allProducts.put(productTypeID, currentProducts);
        return allProducts;
    }
    public Boolean productIsShortage(){
        if(storeAmount+warehouseAmount < notificationMin)
            return true;
        return false;
    }
    public int getProductTypeID(){return productTypeID;}
}
