package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Category {
    private String nameCategory;
    private List<ProductType> productsRelated;
    //0 - subCategory , 1 - main Category
    private int categoryType;
    public List<CategoryDiscount> categoriesDiscount;

    public Category(String nameCategory, int categoryType){
        this.nameCategory = nameCategory;
        this.categoryType = categoryType;
        this.productsRelated = new ArrayList<ProductType>();
        this.categoriesDiscount = new ArrayList<CategoryDiscount>();
    }

    public void addProduct(ProductType product){
        productsRelated.add(product);
    }

    public void addDiscountCategory(String categoryName, String branch, double discount, LocalDateTime startDate, LocalDateTime endDate){
        categoriesDiscount.add(new CategoryDiscount(startDate,endDate,discount,categoryName,branch));
    }

    public Boolean isProductIDRelated(int productID){
        return this.productsRelated.contains(productID);
    }

    public String getNameCategory(){return this.nameCategory;}

    public List<CategoryDiscount> getCategoriesDiscountInRange(List<CategoryDiscount> listCD, LocalDateTime startDate, LocalDateTime endDate){
        for(CategoryDiscount cd: categoriesDiscount){
            if(cd.isDateInRange(startDate,endDate))
                listCD.add(cd);
        }
        return listCD;
    }

}
