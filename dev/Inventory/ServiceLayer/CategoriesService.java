package dev.Inventory.ServiceLayer;

import dev.Inventory.BusinessLayer.CategoryController;
import dev.Inventory.BusinessLayer.DiscountController;
import dev.Inventory.BusinessLayer.ProductController;

import java.util.List;

public class CategoriesService {
    ProductController productController;
    DiscountController discountController;
    CategoryController categoryController;

    public CategoriesService() {
        productController = ProductController.ProductController();
        categoryController = CategoryController.CategoryController();
        discountController = DiscountController.DiscountController();
    }

//    public Response getStockProductsByCategory(){
//        categoryController.getProductsPerCategory()
//    }

    public Response createCategory(String name, List<String> subcategories) {
        try {
            categoryController.createCategory(name,subcategories);
            return new Response<>("Category created successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error creating catagory: " + e.getMessage());
        }
    }


    public Response removeCategory(String name) {
        try {

            categoryController.removeCategory(name);
            return new Response<>("Category removed successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error removing category: " + e.getMessage());
        }
    }

    public Response removeSubCategory(String name, List<String> subcategories){
        try {
            //TODO
            categoryController.removeSubcategory(name,subcategories);
            return new Response<>("Subcategories removed successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error removing category: " + e.getMessage());
        }
    }

    public Response getCategoryReport(String branch, List<String> categories){
        try {
            categoryController.getProductsPerCategory(categories,branch);
            return new Response<>("Subcategories removed successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error removing category: " + e.getMessage());
        }
    }

    public Response addSubCategory(String name, List<String> subcategories){
        try {
            categoryController.addSubcategory(name,subcategories);
            return new Response<>("Subcategory added successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error removing category: " + e.getMessage());
        }
    }


}
