package Backend.BusinessLayer;

import Backend.BusinessLayer.InventoryModule.ProductItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductItemTest {
    private static String serial_number = "0444";
    private static String supplierId = "123";
    private static double supplierPrice = 7.7;
    private static double supplierDiscount = 6.7;
    private static String location = "store";
    private static LocalDateTime expireDate = LocalDateTime.now().plusDays(7);

    private ProductItem item;

    @BeforeEach
    public void setUp() {
        //create new product item
        item = new ProductItem(serial_number,supplierId,supplierPrice,supplierDiscount,location,expireDate);
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
