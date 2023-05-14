package Backend.BusinessLayer;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.InventoryModule.Product;
import Backend.BusinessLayer.InventoryModule.ProductController;
import Backend.DataAccessLayer.InventoryModule.ProductManagerMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductTest {
    private static String catalog_number = "0x123";
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
        productController.createProduct(catalog_number,branch,catalog_name,manufacturer,storePrice);
//        serialNumbers.add(serial_number);
    }

    @After
    public void after(){
//        productController.
    }

    // Test 1
    @org.junit.Test
    public void getProductItemFromList() {
        List<String> serialNumbers = new ArrayList<>();
        serialNumbers.add(serial_number);
        // TODO verify @amit
        productController.createProductItem(serialNumbers,catalog_number,branch,supplierId,supplierPrice,supplierDiscount,location,expireDate,"n");
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
        productController.createProductItem(serialNumbers,catalog_number,branch,supplierId,supplierPrice,supplierDiscount,location,expireDate,"n");
        productController.updateProductItem(branch,1,serial_number,catalog_number,-1,null,-1,-1,-1,null);
//        product.reportAsDefective(serial_number);
        Assert.assertFalse("get defective item failed",product.getDefectiveProductItems().isEmpty());
    }

}
