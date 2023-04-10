package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {
    Map<String, List<ProductType>> productTypes;

    //should create as singleton?
    public ProductController(){
        this.productTypes = new HashMap<String, List<ProductType>>();
    }

    // Add new product
    public void createProduct(int productID, int productTypeID, String branch) {
        //TODO: need to implement
    }
    // Add new product type
    public void createProductType(int productTypeID, String branch){
        //TODO: need to implement
    }
    // update products to defective
    public void updateDefectiveProduct(int typeID, List<Integer> productsID, String branch){
        //TODO: need to implement
    }
    // update products status to sold
    public void updateSoldProduct(int typeID, List<Integer> productsID, String branch){
        //TODO: need to implement
    }
    public void updateDiscount(int typeID, int discount, LocalDateTime startDate,LocalDateTime endDate, String branch){
        //TODO: need to implement
    }
    public void updateProductNotificationMin(int typeID, int newVal, String branch){
        //TODO: need to implement
    }
    public double getProductPrice(int typeID, String branch){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public List<Product> getDefectiveProducts(LocalDateTime startDate, LocalDateTime endDate, String branch){
        //TODO: need to implement
        throw new RuntimeException();
    }

    public List<Product> getInventoryShortages(LocalDateTime startDate, LocalDateTime endDate, String branch){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public List<Product> getStockProductsDetails(String branch){
        //TODO: need to implement
        throw new RuntimeException();
    }

}
