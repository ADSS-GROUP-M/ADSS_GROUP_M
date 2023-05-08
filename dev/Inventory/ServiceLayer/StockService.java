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

    public Response addProductItem(int serial_number, String catalog_number, int supplier, int supplier_price, String branch, String location, Double supplierDiscount, LocalDateTime expirationDate) {
        try {
            productController.createProductItem(serial_number, catalog_number, branch, supplier, supplier_price, location, supplierDiscount, expirationDate);
            return new Response<>("Product added successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating product type: " + e.getMessage());
        }
    }

    public Response updateProductType(String name, int catalog_number, String manufacturer, double supplier_price, double store_price, String category, String sub_category, int min_amount, String branch) {
        try {
            if (name != null) {
                productController.updateProduct(branch, name, catalog_number, null, -1, -1, null, null, -1);
                return new Response<>("Product type updated successfully");
            } else if (manufacturer != null) {
                productController.updateProduct(branch, null, catalog_number, manufacturer, -1, -1, null, null, -1);
                return new Response<>("Product type updated successfully");
            } else if (supplier_price != -1) {
                productController.updateProduct(branch, null, catalog_number, null, supplier_price, -1, null, null, -1);
                return new Response<>("Product type updated successfully");
            } else if (store_price != -1) {
                productController.updateProduct(branch, null, catalog_number, null, -1, store_price, null, null, -1);
                return new Response<>("Product type updated successfully");
            } else if (category != null) {
                productController.updateProduct(branch, null, catalog_number, null, -1, -1, category, null, -1);
                return new Response<>("Product type updated successfully");
            } else if (sub_category != null) {
                productController.updateProduct(branch, null, catalog_number, null, -1, -1, null, sub_category, -1);
                return new Response<>("Product type updated successfully");
            } else if (min_amount != -1) {
                productController.updateProduct(branch, null, catalog_number, null, -1, -1, null, null, min_amount);
                return new Response<>("Product type updated successfully");
            } else {
                return Response.createErrorResponse("Invalid input parameters");
            }
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating product type: " + e.getMessage());
        }
    }

    public Response updateProduct(int is_defective, int catalog_number, int serial_num, int is_sold, int supplier, int supplier_price, int sold_price, String location, String branch) {
        try {
            if (is_defective != -1) {
                productController.updateProductItem(branch, is_defective, catalog_number, serial_num, -1, -1, -1, -1, null);
                return new Response<>("Product type updated successfully");
            } else if (is_sold != -1) {
                productController.updateProductItem(branch, -1, catalog_number, serial_num, is_sold, -1, -1, -1, null);
                return new Response<>("Product type updated successfully");
            } else if (supplier != -1) {
                productController.updateProductItem(branch, -1, catalog_number, serial_num, -1, supplier, -1, -1, null);
                return new Response<>("Product type updated successfully");
            } else if (supplier_price != -1) {
                productController.updateProductItem(branch, -1, catalog_number, serial_num, -1, -1, supplier_price, -1, null);
                return new Response<>("Product type updated successfully");
            } else if (sold_price != -1) {
                productController.updateProductItem(branch, -1, catalog_number, serial_num, -1, -1, -1, sold_price, null);
                return new Response<>("Product type updated successfully");
            } else if (location != null) {
                productController.updateProductItem(branch, -1, catalog_number, serial_num, -1, -1, -1, -1, location);
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
            return Response.createErrorResponse("Error creating catagory: " + e.getMessage());
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

    public Response getProductDetails(String catalog_num, int serial_num, String branch) {
        try {
            return new Response<>(productController.getProductDetails(branch, catalog_num, serial_num));
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating discount: " + e.getMessage());
        }
    }
}
