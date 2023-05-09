package dev.Inventory.Tests;
import dev.Inventory.BusinessLayer.InventoryModule.ProductItem;
import org.junit.Assert;
import org.junit.Before;

import java.time.LocalDateTime;

public class ProductItemTest {
    public static String serial_number = "0444";
    public static String supplierId = "123";
    public static String location = "store";
    public static LocalDateTime expireDate = LocalDateTime.now().plusDays(7);

    private ProductItem item;

    @Before
    public void setUp() {
        //create new product item
        item = new ProductItem(serial_number,supplierId,location,expireDate);
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
