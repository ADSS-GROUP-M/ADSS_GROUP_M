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
        // Map<productID, product>
        this.products = new HashMap<Integer,Product>();
        this.branch = branch;
    }

    public Product getProduct(int productID){
        if(products.containsKey(productID))
            return products.get(productID);
        else
            throw new RuntimeException(String.format("Product does not exist with the ID : %s",productID));
    }
    public void setDefective(List<Integer> productsID){
        for(Integer defectiveProductID: productsID){
            products.get(defectiveProductID).setIsDefective();
        }
    }
    public void setDefective(int productID){
        products.get(productID).setIsDefective();
    }
    // TODO: should add soldPrice and check with the discount table
    public void setToSold(List<Integer> productsID){
        for(Integer soldProductID: productsID){
            products.get(soldProductID).updateIsSold();
        }
    }
    public void setNotificationMin(int newVal){
        this.notificationMin = newVal;
    }
    public void setCategory(Category category){
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

    public void setName(String newName){this.name = newName;}
    public void setManufacturer(String newManufacturer){this.manufacturer =newManufacturer;}
    public void setStoreAmount(int newStoreAmount){this.storeAmount = newStoreAmount;}
    public void setWarehouseAmount(int newWarehouseAmount){this.warehouseAmount = newWarehouseAmount;}
    public void setOriginalStorePrice(double newPrice ){this.originalStorePrice = newPrice;}
    public void setOriginalSupplierPrice(double newPrice){this.originalSupplierPrice = newPrice;}
}
