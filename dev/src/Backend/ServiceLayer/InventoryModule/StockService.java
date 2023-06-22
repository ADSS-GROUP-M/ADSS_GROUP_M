package Backend.ServiceLayer.InventoryModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.InventoryModule.ProductController;
import Backend.BusinessLayer.InventoryModule.DiscountController;
import Backend.BusinessLayer.InventoryModule.CategoryController;

import java.time.LocalDateTime;
import java.util.List;

public class StockService {
    ProductController productController;
    DiscountController discountController;
    CategoryController categoryController;
    public final String ALERT = "\n Alert! Product catalog number: %s is going to be out of stock soon! This product has been automatically ordered.%n";

    public StockService() {
        productController = ProductController.ProductController();
        categoryController = CategoryController.CategoryController();
        discountController = DiscountController.DiscountController();
    }

    public Response addProduct(String catalog_number, String name, String manufacturer, double store_price, String branch) {
        try {
            productController.createProduct(catalog_number, Branch.valueOf(branch), name, manufacturer, store_price);
            return new Response<>("Product added successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error adding product type: " + e.getMessage());
        }
    }

    public Response addProductItem(List<String> serialNumbers, String catalog_number, String supplier, double supplierPrice, double supplierDiscount,String branch, String location, LocalDateTime expirationDate, String periodicSupplier) {
        try {
            String alert = "";
            productController.createProductItem(serialNumbers, catalog_number, Branch.valueOf(branch), supplier, supplierPrice, supplierDiscount, location, expirationDate,periodicSupplier);
//            if(productController.isProductLack(Branch.valueOf(branch),catalog_number))
//                alert = String.format(ALERT,catalog_number);
            return new Response<>("Product added successfully" + alert);
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating product type: " + e.getMessage());
        }
    }

    public Response updateProduct(String name, String catalog_number, String manufacturer, double store_price, int newMinAmount, String branchString) {
        try {
            Branch branch = Branch.valueOf(branchString);
            String alert = "";
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
                if(productController.isProductLack(branch,catalog_number)){
                    alert = String.format(ALERT,catalog_number);
                }
                return new Response<>("Product type updated successfully" + alert);
            } else {
                return Response.createErrorResponse("Invalid input parameters");
            }
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating product type: " + e.getMessage());
        }
    }

    public Response updateProduct(int is_defective, String catalog_number, String serial_num, int is_sold, String supplier, double supplier_price, double supplier_discount,int sold_price, String location, String branchString) {
        try {
            String alert = "";
            Branch branch = Branch.valueOf(branchString);
            if (is_defective != -1) {
                productController.updateProductItem(branch, is_defective, serial_num,  catalog_number,-1, null,-1,-1, -1, null);
                if(productController.isProductLack(branch,catalog_number)){
                    alert = String.format(ALERT,catalog_number);
                }
                return new Response<>("Product type updated successfully" + alert);
            } else if (is_sold != -1) {
                productController.updateProductItem(branch, -1, serial_num,  catalog_number, is_sold, null,-1,-1, -1,  null);
                if(productController.isProductLack(branch,catalog_number)) {
                    alert = String.format(ALERT, catalog_number);
                }
                return new Response<>("Product type updated successfully" + alert);
            } else if (supplier != null) {
                productController.updateProductItem(branch, -1, serial_num,  catalog_number,-1, supplier,-1,-1, -1, null);
                return new Response<>("Product type updated successfully");
            }else if (supplier_price != -1) {
                productController.updateProductItem(branch, -1, serial_num,  catalog_number,-1, null, supplier_price,-1,-1, null);
                return new Response<>("Product type updated successfully");
            }else if (supplier_discount != -1) {
                productController.updateProductItem(branch, -1, serial_num,  catalog_number,-1, null,-1,supplier_discount, -1, null);
                return new Response<>("Product type updated successfully");
            } else if (sold_price != -1) {
                productController.updateProductItem(branch, -1, serial_num, catalog_number, -1, null,-1,-1, sold_price, null);
                return new Response<>("Product type updated successfully");
            } else if (location != null) {
                productController.updateProductItem(branch, -1, catalog_number, serial_num, -1, null,-1,-1, -1,  location);
                return new Response<>("Product type updated successfully");

            } else {
                return Response.createErrorResponse("Invalid input parameters");
            }
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating product type: " + e.getMessage());
        }
    }



    public Response getDefectiveProducts(Branch branch) {
        try {
            return new Response<>(productController.getDefectiveProducts(branch));
        } catch (Exception e) {
            return Response.createErrorResponse("Error creating category: " + e.getMessage());
        }
    }
    public Response getShortagesProducts(Branch branch) {
        try {
            return new Response<>(productController.getInventoryShortages(branch));
        } catch (Exception e) {
            return Response.createErrorResponse("Error creating category: " + e.getMessage());
        }
    }


    public Response updateDiscountPerCategory(String name, Branch branch, double discount, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            discountController.createCategoryDiscount(name, branch, discount, startDate, endDate);
            return new Response<>("discount added successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating discount: " + e.getMessage());
        }
    }

    public Response updateDiscountPerProduct(String catalog_number, Branch branch, double discount, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            discountController.createStoreDiscount(catalog_number, branch, discount, startDate, endDate);
            return new Response<>("discount added successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating discount: " + e.getMessage());
        }
    }

    public Response getProductDetails(String catalog_num, String serial_num, String branch) {
        try {
            return new Response<>(productController.getProductDetails(Branch.valueOf(branch), catalog_num, serial_num));
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating discount: " + e.getMessage());
        }
    }
}
