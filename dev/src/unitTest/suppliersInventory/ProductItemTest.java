package suppliersInventory;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.ProductController;
import businessLayer.inventoryModule.ProductItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    private ProductController productController = ProductController.ProductController();


    @BeforeEach
    public void setUp() {
        //create new product item
        productController.createProduct(catalog_number,branch,catalog_name,manufacturer,storePrice);
        List<String> serial_numbers = new ArrayList<>();
        serial_numbers.add(serial_number);
        productController.createProductItem(serial_numbers,catalog_number,branch,supplierId,supplierPrice,supplierDiscount, location,expireDate, "y");

    }

    // Test 1
    @Test
    public void testDefectiveItem() {
        productController.updateProductItem(branch,1,serial_number,catalog_number,-1,null,-1,-1,-1,null);
        assertNotNull(productController.getProduct(branch,catalog_number).getProduct(serial_number).isDefective(),"update defective Item failed");
    }

    // Test 2
    @Test
    public void isSold() {
//        productController.updateProductItem(branch,-1,serial_number,catalog_number,);
        assertFalse(productController.getProduct(branch,catalog_number).getProduct(serial_number).isSold(), "sold price should init as null");
    }

    // Test 3
    @Test
    public void getSoldDateUpdated() {
        productController.updateProductItem(branch,-1,serial_number,catalog_number,1,null,-1,-1,50,null);
        assertNotNull(productController.getProduct(branch,catalog_number).getProduct(serial_number).getSoldDate(),"sold date shouldn't be null");
    }
}
