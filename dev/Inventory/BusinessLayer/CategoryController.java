package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryController {
    public Map<String,Category> categories;
    public Map<String,List<CategoryDiscount>> categoriesDiscount;

    //should be singleton?
    public CategoryController(){
        this.categories = new HashMap<String,Category>();
//        this.categoriesDiscount = new HashMap<S>();
    }

    //should type be enum? subC or Category
    public void createCategory(String name, String type){
        if(!categories.containsKey(name))
            categories.put(name,new Category(name,type));
    }

    public void updateDiscountPerCategory(String name, int discount, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }

    public List<Product> getStockProduct(List<Category> categories, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }
}
