package Backend.ServiceLayer.InventoryModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.InventoryModule.CategoryController;
import Backend.BusinessLayer.InventoryModule.DiscountController;
import Backend.BusinessLayer.InventoryModule.ProductController;

import java.util.List;
import java.util.Optional;

public class CategoriesService {
    ProductController productController;
    DiscountController discountController;
    CategoryController categoryController;

    public CategoriesService() {
        productController = ProductController.ProductController();
        categoryController = CategoryController.CategoryController();
        discountController = DiscountController.DiscountController();
    }

    public Response createCategory(String name, Optional<List<String>> subcategories) {
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

    public Response addProductToCategory(String name, String catalog_number){
        try {
            categoryController.addProductToCategory(name,catalog_number);
            return new Response<>(String.format("catalog number: %s added to category: %s successfully",catalog_number,name));
        } catch (Exception e) {
            return Response.createErrorResponse("Error removing category: " + e.getMessage());
        }
    }

    public Response removeProductFromCategory(String name, String catalog_number){
        try {
            categoryController.removeProductFromCategory(name,catalog_number);
            return new Response<>(String.format("catalog number: %s added to category: %s successfully",catalog_number,name));
        } catch (Exception e) {
            return Response.createErrorResponse("Error removing category: " + e.getMessage());
        }
    }

    public Response getCategoryReport(String branch, List<String> categories){
        try {
            categoryController.getProductsPerCategory(categories, Branch.valueOf(branch));
            return new Response<>("Succeed");
        } catch (Exception e) {
            return Response.createErrorResponse("Error " + e.getMessage());
        }
    }


}
