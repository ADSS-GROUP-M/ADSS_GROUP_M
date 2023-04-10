package dev.Inventory.BusinessLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Category {
    private String nameCategory;
    private List<ProductType> productsRelated;
    //subCategory or Category
    private String categoryType;
    public List<CategoryDiscount> categoriesDiscount;

    public Category(String nameCategory, String categoryType){
        this.nameCategory = nameCategory;
        this.categoryType = categoryType;
        this.productsRelated = new ArrayList<ProductType>();
        this.categoriesDiscount = new ArrayList<CategoryDiscount>();
    }

    public void addProduct(ProductType product){
        productsRelated.add(product);
    }

}
