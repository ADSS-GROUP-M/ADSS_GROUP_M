package Backend.BusinessLayer;

import Backend.BusinessLayer.InventoryModule.ProductStoreDiscount;
import org.junit.Assert;
import org.junit.Before;

import java.time.LocalDateTime;

public class ProductStoreDiscountTest {
    private static String catalog_number = "0444";
    private static String branch = "beer sheva";
    private static LocalDateTime startDate = LocalDateTime.now();
    private static LocalDateTime endDate = LocalDateTime.now().plusDays(3);
    private static double discount = 15;

    private ProductStoreDiscount productStoreDiscount;

    @Before
    public void setUp() {
        //create new product store discount
        productStoreDiscount = new ProductStoreDiscount(catalog_number,branch,startDate,endDate,discount);
    }

    // Test 1
    @org.junit.Test
    public void getIfDiscountInDate() {
        double expectedDiscount = 15.0;
        double actualDiscount = productStoreDiscount.getDiscount(LocalDateTime.now());
        Assert.assertEquals("failed to get discount in date",actualDiscount,expectedDiscount,0.001);
    }

    // Test 2
    @org.junit.Test
    public void testDatesInRange() {
        Assert.assertFalse("should failed range date",productStoreDiscount.isDateInRange(LocalDateTime.now(),LocalDateTime.now().plusDays(5)));
    }

    // Test 3
    @org.junit.Test
    public void testDateInRange() {
        Assert.assertFalse("should failed cause date is not in between start and end dates",productStoreDiscount.isDateInRange(LocalDateTime.now().plusDays(5)));
    }
}
