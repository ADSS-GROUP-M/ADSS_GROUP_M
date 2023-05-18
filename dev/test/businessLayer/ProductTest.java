package businessLayer;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.Product;
import businessLayer.inventoryModule.ProductController;
import dataAccessLayer.inventoryModule.ProductManagerMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private ProductController productController = ProductController.ProductController();


    @Before
    public void setUp() {
        //create new product
//        productController.createProduct(catalog_number,branch,catalog_name,manufacturer,storePrice);
        List<String> serial_numbers = new ArrayList<>();
        serial_numbers.add(serial_number);
//        productController.createProductItem(serial_numbers,catalog_number,branch,supplierId,supplierPrice,supplierDiscount, location,expireDate, "y");
    }

    @After
    public void after(){
        productController.updateProduct(branch,null,catalog_number,null,-1,2);
    }



    // Test 1
    @org.junit.Test
    public void getProductItemFromList() {
        product = productController.getProduct(branch,catalog_number);
        Assert.assertEquals("get item from product list failed",product.getProduct(serial_number).getSerial_number(),serial_number);
    }

    // Test 2
    @org.junit.Test
    public void updateMin() {
        productController.updateProduct(branch,null,catalog_number,null,-1,6);
        try {
            Assert.assertTrue("update min notification failed",productController.isProductLack(branch,catalog_number));
        }
        catch (Exception e){
            Assert.assertNotNull(e.getMessage(),null);
        }
    }

    // Test 3
    @org.junit.Test
    public void productDefective() {
        productController.updateProductItem(branch,1,serial_number,catalog_number,-1,null,-1,-1,-1,null);
//        product.reportAsDefective(serial_number);
        Assert.assertTrue("update defective item failed",productController.getProduct(branch,catalog_number).getProduct(serial_number).isDefective());
    }

}
