package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoryController {
    public List<Category> categories;

    //should be singleton?
    public CategoryController(){
        this.categories = new ArrayList<Category>();
    }

    //should type be enum? subC or Category
    public void createCategory(String name, String type){
        //TODO: need to implement
        throw new RuntimeException();
    }

    //should we remove category? >> if yes we need to update alk products that related to this category
    public void removeCategory(String name, String type){
        //TODO: need to implement
        throw new RuntimeException();
    }

    public void updateDiscount(String name, int discount, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }

    public List<Product> getStockProduct(List<Category> categories, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }
}
