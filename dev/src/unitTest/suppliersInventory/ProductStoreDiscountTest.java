package suppliersInventory;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.ProductStoreDiscount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
@Disabled
public class ProductStoreDiscountTest {
    private static String catalog_number = "0444";
    private static Branch branch = Branch.branch1;
    private static LocalDateTime startDate = LocalDateTime.now();
    private static LocalDateTime endDate = LocalDateTime.now().plusDays(3);
    private static double discount = 15;

    private ProductStoreDiscount productStoreDiscount;

    @BeforeEach
    public void setUp() {
        //create new product store discount
        productStoreDiscount = new ProductStoreDiscount(catalog_number,branch,startDate,endDate,discount);
    }

    // Test 1
    @Test
    public void getIfDiscountInDate() {
        double expectedDiscount = 15.0;
        double actualDiscount = productStoreDiscount.getDiscount(LocalDateTime.now());
        assertEquals(actualDiscount,expectedDiscount,0.001, "failed to get discount in date");
    }

    // Test 2
    @Test
    public void testDatesInRange() {
        assertFalse(productStoreDiscount.isDateInRange(LocalDateTime.now(),LocalDateTime.now().plusDays(5)), "should failed range date");
    }

    // Test 3
    @Test
    public void testDateInRange() {
        assertFalse(productStoreDiscount.isDateInRange(LocalDateTime.now().plusDays(5)), "should failed cause date is not in between start and end dates");
    }
}
