package dev.Inventory.BusinessLayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category {
    private String categoryName;
    // map<productTypeID, Product obj>
    private Map<String, Product> productsRelated;
    //0 - subCategory , 1 - main Category

    private List<Category> subCategories;
    private int categoryType;
//    public List<CategoryDiscount> categoriesDiscount;

    public Category(String nameCategory, int categoryType){
        this.categoryName = nameCategory;
        this.categoryType = categoryType;
        this.productsRelated = new HashMap<String, Product>();
    }

    public void addProduct(Product product){
        productsRelated.put(product.getCatalogNumber(),product);
    }

//    public void addDiscountCategory(String categoryName, String branch, double discount, LocalDateTime startDate, LocalDateTime endDate){
//        categoriesDiscount.add(new CategoryDiscount(startDate,endDate,discount,categoryName,branch));
//    }

    public Boolean isProductIDRelated(int productTypeID){
        return this.productsRelated.containsKey(productTypeID);
    }

    public String getCategoryName(){return this.categoryName;}

    public Boolean isRelatedProductEmpty(){return productsRelated.isEmpty();}

    public Map<Integer, Product> getProductsRelated(){return this.productsRelated;}

    public void removeProduct(int productID){
        productsRelated.remove(productID);
    }

    @Override
    public String toString() {
        return "Category{" +
                "nameCategory='" + categoryName + '\'' +
                ", categoryType=" + categoryType +
                '}';
    }
}
