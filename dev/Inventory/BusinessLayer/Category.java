package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category {
    private String nameCategory;
    // map<productTypeID, Product obj>
    private Map<Integer, ProductType> productsRelated;
    //0 - subCategory , 1 - main Category
    private int categoryType;
//    public List<CategoryDiscount> categoriesDiscount;

    public Category(String nameCategory, int categoryType){
        this.nameCategory = nameCategory;
        this.categoryType = categoryType;
        this.productsRelated = new HashMap<Integer,ProductType>();
    }

    public void addProduct(ProductType product){
        productsRelated.put(product.getProductTypeID(),product);
    }

//    public void addDiscountCategory(String categoryName, String branch, double discount, LocalDateTime startDate, LocalDateTime endDate){
//        categoriesDiscount.add(new CategoryDiscount(startDate,endDate,discount,categoryName,branch));
//    }

    public Boolean isProductIDRelated(int productTypeID){
        return this.productsRelated.containsKey(productTypeID);
    }

    public String getNameCategory(){return this.nameCategory;}

    public Boolean isRelatedProductEmpty(){return productsRelated.isEmpty();}

    public Map<Integer,ProductType> getProductsRelated(){return this.productsRelated;}

    public void removeProduct(int productID){
        productsRelated.remove(productID);
    }


}
