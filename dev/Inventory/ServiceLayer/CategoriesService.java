package dev.Inventory.ServiceLayer;

import dev.Inventory.BusinessLayer.CategoryController;
import dev.Inventory.BusinessLayer.DiscountController;
import dev.Inventory.BusinessLayer.ProductController;

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

    public Response createMainCategory(String name, String branch) {
        try {
            categoryController.createCategory(branch, name, 1);
            return new Response<>("Catagory created successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error creating catagory: " + e.getMessage());
        }
    }

    public Response createSubCategory(String name, String branch) {
        try {
            categoryController.createCategory(branch, name, 0);
            return new Response<>("Sub-Catagory created successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error creating sub catagory: " + e.getMessage());
        }
    }

    public Response removeMainCategory(String name, String branch) {
        try {

            categoryController.removeCategory(branch, name);
            return new Response<>("Category removed successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error removing category: " + e.getMessage());
        }
    }

    public Response removeSubCategory(String name, String branch) {
        try {
            categoryController.removeCategory(branch, name);
            return new Response<>("Sub-Category removed successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error removing category: " + e.getMessage());
        }
    }

}
