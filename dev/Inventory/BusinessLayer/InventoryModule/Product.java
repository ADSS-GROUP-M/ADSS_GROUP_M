package dev.Inventory.BusinessLayer.InventoryModule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
    private  String catalog_number;
    private  String name;
    private String manufacturer;
//    private double originalSupplierPrice;
    private double originalStorePrice;
    private Category category;
    private List<Category> subCategory;
    //default value -1
    private int notificationMin;
    private Map<String, ProductItem> productItems;
    private String branch;

    public Product(String catalog_number, String name, String manufacturer, double originalStorePrice, String branch){
        this.catalog_number = catalog_number;
        this.name = name;
        this.manufacturer = manufacturer;
        this.originalStorePrice = originalStorePrice;
        this.category = null;
        this.subCategory = new ArrayList<Category>();
        // TODO: notification min
        this.notificationMin = -1;
        // Map<productID, product>
        this.productItems = new HashMap<String, ProductItem>();
        this.branch = branch;
        // TODO : calc amount store and warehouse
    }

    public ProductItem getProduct(String serial_number){
        if(productItems.containsKey(serial_number))
            return productItems.get(serial_number);
        else
            throw new RuntimeException(String.format("Product does not exist with the ID : %s",serial_number));
    }
    public void reportAsDefective(List<Integer> ItemsSerialNumber){
        for(Integer defectiveSerialNumber: ItemsSerialNumber){
            productItems.get(defectiveSerialNumber).reportAsDefective();
        }
    }
    public void reportAsDefective(String serialNumber){
        productItems.get(serialNumber).reportAsDefective();
    }
    // TODO: should add soldPrice and check with the discount table
    public void reportAsSold(List<Integer> serialNumber, double soldPrice){
        for(Integer soldProductID: serialNumber){
            productItems.get(soldProductID).reportAsSold(soldPrice);
        }
        //TODO: notification min
    }
    public void setNotificationMin(int newVal){
        this.notificationMin = newVal;
    }

    public void addProductItem(String serialNumber, String supplierID, String location, LocalDateTime expirationDate){
        productItems.put(serialNumber,new ProductItem(serialNumber,supplierID,location, expirationDate));
        //TODO add min function
    }


    public List<ProductItem> getDefectiveProductItems(){
        List<ProductItem> defectiveList = new ArrayList<ProductItem>();
        for(ProductItem product: productItems.values()){
            if(product.isDefective())
                defectiveList.add(product);
        }
        return defectiveList;
    }
    //TODO : verify we want to return all products (maybe not defective and sold)
    public Map<String,List<ProductItem>> getAllProductItems(Map<String,List<ProductItem>> allProducts){
        List<ProductItem> currentProducts = new ArrayList<ProductItem>();
        for(ProductItem product: productItems.values()){
            currentProducts.add(product);
        }
        allProducts.put(catalog_number, currentProducts);
        return allProducts;
    }

    public Map<String, ProductItem> getProductItems(){return this.productItems;}

    public Boolean isProductLack(){
        if(getStoreAmount()+getWarehouseAmount() < notificationMin)
            return true;
        return false;
    }
    //TODO: get supplier info from orderController and calc minNotification val
    private int productDemandAmount(){
        int calc = 0;
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime lastWeek = current.minusDays(7);
        for(ProductItem productItem: productItems.values()){
            if(productItem.isSold()){
                if(productItem.getSoldDate().isAfter(lastWeek))
                    calc++;
            }
        }
        return calc;
    }
    public void updateMin(){}

    public String getCatalogNumber(){return catalog_number;}

    public void setName(String newName){this.name = newName;}
    public void setManufacturer(String newManufacturer){this.manufacturer =newManufacturer;}
    public int getStoreAmount(){
        int amount =0;
        for(ProductItem productItem: productItems.values()){
            if(productItem.getLocation() == "store")
                amount++;
        }
        return amount;
    }
    public int getWarehouseAmount(){
        int amount =0;
        for(ProductItem productItem: productItems.values()){
            if(productItem.getLocation() == "warehouse")
                amount++;
        }
        return amount;
    }
    public void setOriginalStorePrice(double newPrice ){this.originalStorePrice = newPrice;}
    public double getOriginalStorePrice(){return this.originalStorePrice;}
    public String getName(){return this.name;}
    public String getManufacturer(){return this.manufacturer;}
    public Category getCategory(){return this.category;}
    public List<Category> getSubCategory(){return this.subCategory;}
}
