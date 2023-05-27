package suppliersInventory;

import businessLayer.BusinessFactory;
import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.Product;
import businessLayer.inventoryModule.ProductController;
import dataAccessLayer.DalFactory;
import exceptions.InventoryException;
import exceptions.TransportException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    private static String catalog_number = "23";
    private static String catalog_name = "productTest";
    private static String manufacturer = "tnuva";
    private static double storePrice = 5.0;
    private static Branch branch = Branch.branch1;

    private static List<String> serialNumbers = new ArrayList<>();

    private static String serial_number = "0444";
    private static String supplierId = "123";
    private static double supplierPrice = 7.0;
    private static double supplierDiscount = 6.3;
    private static String location = "store";
    private static LocalDateTime expireDate = LocalDateTime.now().plusDays(7);

    private Product product;

    private ProductController productController;


    @BeforeEach
    public void setUp() {
        try {
            BusinessFactory factory = new BusinessFactory(DalFactory.TESTING_DB_NAME);
            productController = factory.productController();
            //create new product
//        productController.createProduct(catalog_number,branch,catalog_name,manufacturer,storePrice);
            List<String> serial_numbers = new ArrayList<>();
            serial_numbers.add(serial_number);
//        productController.createProductItem(serial_numbers,catalog_number,branch,supplierId,supplierPrice,supplierDiscount, location,expireDate, "y");
        } catch (TransportException e) {
            fail(e);
        }
    }

    @AfterEach
    public void after(){
        try {
            productController.updateProduct(branch,null,catalog_number,null,-1,2);
        } catch (InventoryException e) {
            fail(e);
        }
    }



    // Test 1
    @Test
    public void getProductItemFromList() {
        try {
            product = productController.getProduct(branch,catalog_number);
            assertEquals("get item from product list failed",product.getProduct(serial_number).getSerial_number(),serial_number);
        } catch (InventoryException e) {
            fail(e);
        }
    }

    // Test 2
    @Test
    public void updateMin() {
        try {
            productController.updateProduct(branch,null,catalog_number,null,-1,6);
        } catch (InventoryException e) {
            fail(e);
        }
        try {
            assertTrue(productController.isProductLack(branch,catalog_number), "update min notification failed");
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    // Test 3
    @Test
    public void productDefective() {
        try {
            productController.updateProductItem(branch,1,serial_number,catalog_number,-1,null,-1,-1,-1,null);
//        product.reportAsDefective(serial_number);
            assertTrue(productController.getProduct(branch,catalog_number).getProduct(serial_number).isDefective(), "update defective item failed");
        } catch (InventoryException e) {
            fail(e);
        }
    }

}
