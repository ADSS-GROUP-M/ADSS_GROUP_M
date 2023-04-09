package dev.Inventory.BusinessLayer;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String nameCategory;
    private List<ProductType> productsRelated;
    private String categoryType;

    public Category(String nameCategory, String categoryType){
        this.nameCategory = nameCategory;
        this.categoryType = categoryType;
        this.productsRelated = new ArrayList<ProductType>();
    }

}
