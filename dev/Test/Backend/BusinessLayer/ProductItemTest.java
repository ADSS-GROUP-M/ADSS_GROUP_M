package Backend.BusinessLayer;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.InventoryModule.ProductController;
import Backend.BusinessLayer.InventoryModule.ProductItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


    @Before
    public void setUp() {
        //create new product item
        productController.createProduct(catalog_number,branch,catalog_name,manufacturer,storePrice);
        List<String> serial_numbers = new ArrayList<>();
        serial_numbers.add(serial_number);
        productController.createProductItem(serial_numbers,catalog_number,branch,supplierId,supplierPrice,supplierDiscount, location,expireDate, "y");

    }

    // Test 1
    @org.junit.Test
    public void testDefectiveItem() {
        item.reportAsDefective();
        Assert.assertNotNull("update defective Item failed",item.isDefective());
    }

    // Test 2
    @org.junit.Test
    public void getSoldDate() {
        Assert.assertNull("sold price should init as null",item.getSoldDate());
    }

    // Test 3
    @org.junit.Test
    public void getSoldDateUpdated() {
        item.setSoldPrice(50);
        Assert.assertNotNull("sold date shouldn't be null",item.getSoldDate());
    }
}
