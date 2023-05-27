package serviceLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.CategoryController;
import businessLayer.inventoryModule.DiscountController;
import businessLayer.inventoryModule.ProductController;
import businessLayer.inventoryModule.Record;
import exceptions.InventoryException;
import utils.Response;

import java.time.LocalDateTime;
import java.util.List;

public class StockService {
    private final ProductController productController;
    private final DiscountController discountController;
    private final CategoryController categoryController;
    public final String ALERT = "\n Alert! Product catalog number: %s is going to be out of stock soon!%This product has been automatically ordered.%n";

    public StockService(ProductController productController, DiscountController discountController, CategoryController categoryController) {
        this.productController = productController;
        this.discountController = discountController;
        this.categoryController = categoryController;
    }

    public String addProduct(String catalog_number, String name, String manufacturer, double store_price, String branch) {
        try {
            productController.createProduct(catalog_number, Branch.valueOf(branch), name, manufacturer, store_price);
            return new Response("Product added successfully",true).toJson();
        } catch (InventoryException e) {
            return new Response("Error adding product type: " + e.getMessage(),false).toJson();
        }
    }

    public String addProductItem(List<String> serialNumbers, String catalog_number, String supplier, double supplierPrice, double supplierDiscount,String branch, String location, LocalDateTime expirationDate, String periodicSupplier) {
        try {
            String alert = "";
            productController.createProductItem(serialNumbers, catalog_number, Branch.valueOf(branch), supplier, supplierPrice, supplierDiscount, location, expirationDate,periodicSupplier);
//            if(productController.isProductLack(Branch.valueOf(branch),catalog_number))
//                alert = String.format(ALERT,catalog_number);
            return new Response("Product added successfully" + alert,true).toJson();
        } catch (InventoryException e) {
            return new Response("Error updating product type: " + e.getMessage(),false).toJson();
        }
    }

    public String updateProduct(String name, String catalog_number, String manufacturer, double store_price, int newMinAmount, String branchString) {
        try {
            Branch branch = Branch.valueOf(branchString);
            String alert = "";
            if (name != null) {
                productController.updateProduct(branch, name, catalog_number, null, -1,   -1);
                return new Response("Product type updated successfully",true).toJson();
            } else if (manufacturer != null) {
                productController.updateProduct(branch, null, catalog_number, manufacturer, -1,  -1);
                return new Response("Product type updated successfully",true).toJson();
            } else if (store_price != -1) {
                productController.updateProduct(branch, null, catalog_number, null, store_price, -1);
                return new Response("Product type updated successfully",true).toJson();
            }else if (newMinAmount != -1) {
                productController.updateProduct(branch, null, catalog_number, null, -1, newMinAmount);
                if(productController.isProductLack(branch,catalog_number)){
                    alert = String.format(ALERT,catalog_number);
                }
                return new Response("Product type updated successfully" + alert,true).toJson();
            } else {
                return new Response("Invalid input parameters",false).toJson();
            }
        } catch (InventoryException e) {
            return new Response("Error updating product type: " + e.getMessage(),false).toJson();
        }
    }

    public String updateProduct(int is_defective, String catalog_number, String serial_num, int is_sold, String supplier, double supplier_price, double supplier_discount,int sold_price, String location, String branchString) {
        try {
            String alert = "";
            Branch branch = Branch.valueOf(branchString);
            if (is_defective != -1) {
                productController.updateProductItem(branch, is_defective, serial_num,  catalog_number,-1, null,-1,-1, -1, null);
                if(productController.isProductLack(branch,catalog_number)){
                    alert = String.format(ALERT,catalog_number);
                }
                return new Response("Product type updated successfully" + alert,true).toJson();
            } else if (is_sold != -1) {
                productController.updateProductItem(branch, -1, serial_num,  catalog_number, is_sold, null,-1,-1, -1,  null);
                if(productController.isProductLack(branch,catalog_number)) {
                    alert = String.format(ALERT, catalog_number);
                }
                return new Response("Product type updated successfully" + alert,true).toJson();
            } else if (supplier != null) {
                productController.updateProductItem(branch, -1, serial_num,  catalog_number,-1, supplier,-1,-1, -1, null);
                return new Response("Product type updated successfully",true).toJson();
            }else if (supplier_price != -1) {
                productController.updateProductItem(branch, -1, serial_num,  catalog_number,-1, null, supplier_price,-1,-1, null);
                return new Response("Product type updated successfully",true).toJson();
            }else if (supplier_discount != -1) {
                productController.updateProductItem(branch, -1, serial_num,  catalog_number,-1, null,-1,supplier_discount, -1, null);
                return new Response("Product type updated successfully",true).toJson();
            } else if (sold_price != -1) {
                productController.updateProductItem(branch, -1, serial_num, catalog_number, -1, null,-1,-1, sold_price, null);
                return new Response("Product type updated successfully",true).toJson();
            } else if (location != null) {
                productController.updateProductItem(branch, -1, catalog_number, serial_num, -1, null,-1,-1, -1,  location);
                return new Response("Product type updated successfully",true).toJson();

            } else {
                return new Response("Invalid input parameters",false).toJson();
            }
        } catch (InventoryException e) {
            return new Response("Error updating product type: " + e.getMessage(),false).toJson();
        }
    }



    public String getDefectiveProducts(Branch branch) {
        try {
            return new Response(true,productController.getDefectiveProducts(branch)).toJson();
        } catch (InventoryException e) {
            return new Response("Error creating category: " + e.getMessage(),false).toJson();
        }
    }
    public String getShortagesProducts(Branch branch) {
        try {
            return new Response(true,productController.getInventoryShortages(branch)).toJson();
        } catch (InventoryException e) {
            return new Response("Error creating category: " + e.getMessage(),false).toJson();
        }
    }


    public String updateDiscountPerCategory(String name, Branch branch, double discount, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            discountController.createCategoryDiscount(name, branch, discount, startDate, endDate);
            return new Response("discount added successfully",true).toJson();
        } catch (InventoryException e) {
            return new Response("Error updating discount: " + e.getMessage(),false).toJson();
        }
    }

    public String updateDiscountPerProduct(String catalog_number, Branch branch, double discount, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            discountController.createStoreDiscount(catalog_number, branch, discount, startDate, endDate);
            return new Response("discount added successfully",true).toJson();
        } catch (InventoryException e) {
            return new Response("Error updating discount: " + e.getMessage(),false).toJson();
        }
    }

    public String getProductDetails(String catalog_num, String serial_num, String branch) {
        try {
            return new Response(true,productController.getProductDetails(Branch.valueOf(branch), catalog_num, serial_num)).toJson();
        } catch (InventoryException e) {
            return new Response("Error updating discount: " + e.getMessage(),false).toJson();
        }
    }
}
