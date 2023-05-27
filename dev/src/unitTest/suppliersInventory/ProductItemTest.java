package suppliersInventory;

import businessLayer.BusinessFactory;
import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.ProductController;
import businessLayer.inventoryModule.ProductItem;
import dataAccessLayer.DalFactory;
import exceptions.InventoryException;
import exceptions.TransportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductItemTest {
    private static String catalog_number = "0x123";
    private static String catalog_name = "productTest";
    private static String manufacturer = "tnuva";
    private static double storePrice = 5.0;
    private static Branch branch = Branch.branch1;
    private static String serial_number = "0444";
    private static String supplierId = "123";
    private static double supplierPrice = 7.7;
    private static double supplierDiscount = 6.7;
    private static String location = "store";
    private static LocalDateTime expireDate = LocalDateTime.now().plusDays(7);

    private ProductItem item;

    private ProductController productController;


    @BeforeEach
    public void setUp() {
        try {
            BusinessFactory factory = new BusinessFactory(DalFactory.TESTING_DB_NAME);
            productController = factory.productController();
            //create new product item
            productController.createProduct(catalog_number,branch,catalog_name,manufacturer,storePrice);
            List<String> serial_numbers = new ArrayList<>();
            serial_numbers.add(serial_number);
            productController.createProductItem(serial_numbers,catalog_number,branch,supplierId,supplierPrice,supplierDiscount, location,expireDate, "y");
        } catch (TransportException | InventoryException e) {
            fail(e);
        }
    }

    // Test 1
    @Test
    public void testDefectiveItem() {
        try {
            productController.updateProductItem(branch,1,serial_number,catalog_number,-1,null,-1,-1,-1,null);
        } catch (InventoryException e) {
            fail(e);
        }
        try {
            assertNotNull(productController.getProduct(branch,catalog_number).getProduct(serial_number).isDefective(),"update defective Item failed");
        } catch (InventoryException e) {
            fail(e);
        }
    }

    // Test 2
    @Test
    public void isSold() {
//        productController.updateProductItem(branch,-1,serial_number,catalog_number,);
        try {
            assertFalse(productController.getProduct(branch,catalog_number).getProduct(serial_number).isSold(), "sold price should init as null");
        } catch (InventoryException e) {
            fail(e);
        }
    }

    // Test 3
    @Test
    public void getSoldDateUpdated() {
        try {
            productController.updateProductItem(branch,-1,serial_number,catalog_number,1,null,-1,-1,50,null);
            assertNotNull(productController.getProduct(branch,catalog_number).getProduct(serial_number).getSoldDate(),"sold date shouldn't be null");
        } catch (InventoryException e) {
            fail(e);
        }
    }
}
