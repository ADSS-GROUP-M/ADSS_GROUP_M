package dev.Inventory.ServiceLayer;

import dev.Inventory.BusinessLayer.CategoryController;
import dev.Inventory.BusinessLayer.DiscountController;
import dev.Inventory.BusinessLayer.ProductController;

import java.time.LocalDateTime;

public class StockService {
    ProductController productController;
    DiscountController discountController;
    CategoryController categoryController;

    public StockService() {
        productController = ProductController.ProductController();
        categoryController = CategoryController.CategoryController();
        discountController = DiscountController.DiscountController();
    }

    public Response addProduct(String catalog_number, String name, String manufacturer, double store_price, String branch) {
        try {
            productController.createProduct(catalog_number, branch, name, manufacturer, store_price);
            return new Response<>("Product added successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating product type: " + e.getMessage());
        }
    }

    public Response addProductItem(String serial_number, String catalog_number, String supplier, String branch, String location, LocalDateTime expirationDate) {
        try {
            productController.createProductItem(serial_number, catalog_number, branch, supplier, location, expirationDate);
            return new Response<>("Product added successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating product type: " + e.getMessage());
        }
    }

    public Response updateProduct(String name, String catalog_number, String manufacturer, double store_price, int newMinAmount, String branch) {
        try {
            if (name != null) {
                productController.updateProduct(branch, name, catalog_number, null, -1,   -1);
                return new Response<>("Product type updated successfully");
            } else if (manufacturer != null) {
                productController.updateProduct(branch, null, catalog_number, manufacturer, -1,  -1);
                return new Response<>("Product type updated successfully");
            } else if (store_price != -1) {
                productController.updateProduct(branch, null, catalog_number, null, store_price, -1);
                return new Response<>("Product type updated successfully");
            }else if (newMinAmount != -1) {
                productController.updateProduct(branch, null, catalog_number, null, -1, newMinAmount);
                return new Response<>("Product type updated successfully");
            } else {
                return Response.createErrorResponse("Invalid input parameters");
            }
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating product type: " + e.getMessage());
        }
    }

    public Response updateProduct(int is_defective, String catalog_number, String serial_num, int is_sold, String supplier, int sold_price, String location, String branch) {
        try {
            if (is_defective != -1) {
                productController.updateProductItem(branch, is_defective, serial_num,  catalog_number,-1, null, -1, null);
                return new Response<>("Product type updated successfully");
            } else if (is_sold != -1) {
                productController.updateProductItem(branch, -1, serial_num,  catalog_number, is_sold, null, -1,  null);
                return new Response<>("Product type updated successfully");
            } else if (supplier != null) {
                productController.updateProductItem(branch, -1, serial_num,  catalog_number,-1, supplier, -1, null);
                return new Response<>("Product type updated successfully");
            } else if (sold_price != -1) {
                productController.updateProductItem(branch, -1, serial_num, catalog_number, -1, null, sold_price, null);
                return new Response<>("Product type updated successfully");
            } else if (location != null) {
                productController.updateProductItem(branch, -1, catalog_number, serial_num, -1, null, -1,  location);
                return new Response<>("Product type updated successfully");
            } else {
                return Response.createErrorResponse("Invalid input parameters");
            }
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating product type: " + e.getMessage());
        }
    }


//    public Response getInventoryShortages(){
//
//    }

    public Response getDefectiveProducts(String branch) {
        try {
            return new Response<>(productController.getDefectiveProducts(branch));
        } catch (Exception e) {
            return Response.createErrorResponse("Error creating category: " + e.getMessage());
        }
    }
    public Response getShortagesProducts(String branch) {
        try {
            return new Response<>(productController.getInventoryShortages(branch));
        } catch (Exception e) {
            return Response.createErrorResponse("Error creating category: " + e.getMessage());
        }
    }



    public Response updateDiscountPerCategory(String name, String branch, double discount, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            discountController.createCategoryDiscount(name, branch, discount, startDate, endDate);
            return new Response<>("discount added successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating discount: " + e.getMessage());
        }
    }

    public Response updateDiscountPerProduct(String catalog_number, String branch, double discount, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            discountController.createStoreDiscount(catalog_number, branch, discount, startDate, endDate);
            return new Response<>("discount added successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating discount: " + e.getMessage());
        }
    }

    public Response getProductDetails(String catalog_num, String serial_num, String branch) {
        try {
            return new Response<>(productController.getProductDetails(branch, catalog_num, serial_num));
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating discount: " + e.getMessage());
        }
    }
}
