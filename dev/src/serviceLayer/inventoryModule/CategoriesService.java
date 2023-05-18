package serviceLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.CategoryController;
import businessLayer.inventoryModule.DiscountController;
import businessLayer.inventoryModule.ProductController;

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

    public Response createCategory(Branch branch, List<String> subcategories, String name) {
        try {
            categoryController.createCategory(branch, subcategories, name);
            return new Response<>("Category created successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error creating catagory: " + e.getMessage());
        }
    }


    public Response removeCategory(Branch branch, String name) {
        try {
            categoryController.removeCategory(branch, name);
            return new Response<>("Category removed successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error removing category: " + e.getMessage());
        }
    }

    public Response addProductToCategory(Branch branch, String catalog_number, String name){
        try {
            categoryController.addProductToCategory(branch, catalog_number, name);
            return new Response<>(String.format("catalog number: %s added to category: %s successfully",catalog_number,name));
        } catch (Exception e) {
            return Response.createErrorResponse("Error removing category: " + e.getMessage());
        }
    }

    public Response removeProductFromCategory(Branch branch, String catalog_number, String name){
        try {
            categoryController.removeProductFromCategory(branch,catalog_number, name);
            return new Response<>(String.format("catalog number: %s added to category: %s successfully",catalog_number,name));
        } catch (Exception e) {
            return Response.createErrorResponse("Error removing category: " + e.getMessage());
        }
    }

    public Response getCategoryReport(String branch, List<String> categories){
        try {
            return new Response<>(categoryController.getProductsPerCategory(categories, Branch.valueOf(branch)));
        } catch (Exception e) {
            return Response.createErrorResponse("Error " + e.getMessage());
        }
    }


}
