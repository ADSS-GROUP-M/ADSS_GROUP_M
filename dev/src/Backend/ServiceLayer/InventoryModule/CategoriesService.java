package Backend.ServiceLayer.InventoryModule;

import Backend.BusinessLayer.InventoryModule.CategoryController;
import Backend.BusinessLayer.InventoryModule.DiscountController;
import Backend.BusinessLayer.InventoryModule.ProductController;

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

    public Response getCategoryReport(String branch, List<String> categories){
        try {
            categoryController.getProductsPerCategory(categories,branch);
            return new Response<>("Succeed");
        } catch (Exception e) {
            return Response.createErrorResponse("Error " + e.getMessage());
        }
    }


}
