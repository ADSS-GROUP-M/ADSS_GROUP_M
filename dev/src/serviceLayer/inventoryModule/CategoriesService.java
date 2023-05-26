package serviceLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.CategoryController;
import businessLayer.inventoryModule.DiscountController;
import businessLayer.inventoryModule.ProductController;
import businessLayer.inventoryModule.Record;
import utils.Response;

import java.util.List;

public class CategoriesService {
    private final ProductController productController;
    private final DiscountController discountController;
    private final CategoryController categoryController;

    public CategoriesService(ProductController productController, DiscountController discountController, CategoryController categoryController) {
        this.productController = productController;
        this.discountController = discountController;
        this.categoryController = categoryController;
    }

    public String createCategory(Branch branch, List<String> subcategories, String name) {
        try {
            categoryController.createCategory(branch, subcategories, name);
            return new Response("Category created successfully",true).toJson();
        } catch (Exception e) {
            return new Response("Error creating category: " + e.getMessage(),false).toJson();
        }
    }


    public String removeCategory(Branch branch, String name) {
        try {
            categoryController.removeCategory(branch, name);
            return new Response("Category removed successfully",true).toJson();
        } catch (Exception e) {
            return new Response("Error removing category: " + e.getMessage(),false).toJson();
        }
    }

    public String addProductToCategory(Branch branch, String catalog_number, String name){
        try {
            categoryController.addProductToCategory(branch, catalog_number, name);
            return new Response(String.format("catalog number: %s added to category: %s successfully",catalog_number,name),true).toJson();
        } catch (Exception e) {
            return new Response("Error removing category: " + e.getMessage(),false).toJson();
        }
    }

    public String removeProductFromCategory(Branch branch, String catalog_number, String name){
        try {
            categoryController.removeProductFromCategory(branch,catalog_number, name);
            return new Response(String.format("catalog number: %s added to category: %s successfully",catalog_number,name),true).toJson();
        } catch (Exception e) {
            return new Response("Error removing category: " + e.getMessage(),false).toJson();
        }
    }

    public String getCategoryReport(String branch, List<String> categories){
        try {
            return new Response(true,categoryController.getProductsPerCategory(categories, Branch.valueOf(branch))).toJson();
        } catch (Exception e) {
            return new Response("Error " + e.getMessage(),false).toJson();
        }
    }


}
