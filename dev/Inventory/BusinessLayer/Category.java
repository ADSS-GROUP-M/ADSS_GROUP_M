package dev.Inventory.BusinessLayer;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category {
    private String categoryName;
    // map<catalog_number, Product obj>
    private Map<String, Product> productsRelated;


    //map<category_name, Category obj>
    private Map<String, Category> subCategories;


    public Category(String nameCategory, List<Category> subcategories){
        this.categoryName = nameCategory;
        this.subCategories = new HashMap<String, Category>();
        this.productsRelated = new HashMap<String, Product>();
        if(!subcategories.isEmpty()){
            for(Category category: subcategories)
                subCategories.put(category.getCategoryName(), category);
        }
    }

    public void addProductToCategory(Product product){
        productsRelated.put(product.getCatalogNumber(),product);
    }



//    public void addDiscountCategory(String categoryName, String branch, double discount, LocalDateTime startDate, LocalDateTime endDate){
//        categoriesDiscount.add(new CategoryDiscount(startDate,endDate,discount,categoryName,branch));
//    }

    public Boolean isProductIDRelated(int catalog_number){
        return this.productsRelated.containsKey(catalog_number);
    }

    public String getCategoryName(){return this.categoryName;}

    public Boolean isRelatedProductEmpty(){return productsRelated.isEmpty();}

    public Map<String, Product> getProductsRelated(){return this.productsRelated;}

    public void removeProduct(String catalog_number){
        productsRelated.remove(catalog_number);
    }

    public boolean isSubcategory(String subcategoryName){
        return subCategories.containsKey(subcategoryName);
    }

    public Map<String, Category> getSubcategories(){return subCategories;}
    public void removeSubCategory(String subcategoryName){
        if(isSubcategory(subcategoryName))
            subCategories.remove(subcategoryName);
    }

    public void addSubcategories(List<Category> subcategories){
        if(!subcategories.isEmpty()){
            for(Category category: subcategories)
                subCategories.put(category.getCategoryName(), category);
        }
    }

    private String  getSubCategoriesName(){
        if(subCategories.isEmpty())
            return "there is no subCategory";
        else{
            String ans = "[ ";
            for(Category subcategory: subCategories.values()){
                ans = ans + subcategory.getCategoryName() + " ,";
            }
            ans = ans.substring(0,ans.length()-1) + " ]";
            return ans;
        }
    }
    @Override
    public String toString() {
        return "Category{" +
                "nameCategory='" + categoryName + '\'' +
                ", subCategories=" + getSubCategoriesName() +
                '}';
    }
}
