package Backend.BusinessLayer;

import Backend.BusinessLayer.InventoryModule.Product;
import org.junit.Assert;
import org.junit.Before;

import java.time.LocalDateTime;

public class ProductTest {
    public static String catalog_number = "0x123";
    public static String catalog_name = "productTest";
    public static String manufacturer = "tnuva";
    public static double storePrice = 5.0;
    public static String branch = "beer sheva";

    public static String serial_number = "0444";
    public static String supplierId = "123";
    public static String location = "store";
    public static LocalDateTime expireDate = LocalDateTime.now().plusDays(7);

    private Product product;
//    private ProductItem item;

    @Before
    public void setUp() {
        //create new product
        product = new Product(catalog_number, catalog_name, manufacturer, storePrice,branch );
    }

    // Test 1
    @org.junit.Test
    public void getProductItemFromList() {
        product.addProductItem(serial_number,supplierId,location,expireDate);
        Assert.assertEquals("get item from product list failed",product.getProduct(serial_number).getSerial_number(),serial_number);
    }

    // Test 2
    @org.junit.Test
    public void updateMin() {
        product.setNotificationMin(2);
        Assert.assertTrue("update min notification failed",product.isProductLack());
    }

    // Test 3
    @org.junit.Test
    public void productDefective() {
        product.addProductItem(serial_number,supplierId,location,expireDate);
        product.reportAsDefective(serial_number);
        Assert.assertFalse("get defective item failed",product.getDefectiveProductItems().isEmpty());
    }

}
