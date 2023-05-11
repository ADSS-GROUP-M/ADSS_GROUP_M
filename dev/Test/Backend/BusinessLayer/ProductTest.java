package Backend.BusinessLayer;

import Backend.BusinessLayer.InventoryModule.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

public class ProductTest {
    private static String catalog_number = "0x123";
    private static String catalog_name = "productTest";
    private static String manufacturer = "tnuva";
    private static double storePrice = 5.0;
    private static String branch = "beer sheva";

    private static String serial_number = "0444";
    private static String supplierId = "123";
    private static double supplierPrice = 7.0;
    private static double supplierDiscount = 6.3;
    private static String location = "store";
    private static LocalDateTime expireDate = LocalDateTime.now().plusDays(7);

    private Product product;


    @BeforeEach
    public void setUp() {
        //create new product
        product = new Product(catalog_number, catalog_name, manufacturer, storePrice,branch );
    }

    // Test 1
    @org.junit.Test
    public void getProductItemFromList() {
        product.addProductItem(serial_number,supplierId,supplierPrice,supplierDiscount,location,expireDate);
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
        product.addProductItem(serial_number,supplierId,supplierPrice,supplierDiscount,location,expireDate);
        product.reportAsDefective(serial_number);
        Assert.assertFalse("get defective item failed",product.getDefectiveProductItems().isEmpty());
    }

}
