package Backend.BusinessLayer;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.InventoryModule.Product;
import Backend.BusinessLayer.InventoryModule.ProductController;
import Backend.BusinessLayer.InventoryModule.ProductItem;
import Backend.BusinessLayer.InventoryModule.ProductStoreDiscount;
import Backend.BusinessLayer.SuppliersModule.Order;
import Backend.BusinessLayer.SuppliersModule.OrderController;
import Backend.BusinessLayer.SuppliersModule.PeriodicOrderController;
import Backend.ServiceLayer.SuppliersModule.SupplierService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IntegrationTest {
    private static String catalog_number = "0x123";
    private static String catalog_name = "productTest";
    private static String manufacturer = "tnuva";
    private static double storePrice = 5.0;
    private static Branch branch = Branch.branch1;

//    private static String serial_number = "0444";
    private static String supplierId = "123";
    private static double supplierPrice = 7.7;
    private static double supplierDiscount = 6.7;
    private static String location = "store";
    private static LocalDateTime expireDate = LocalDateTime.now().plusDays(7);

//    private Product product;
    private ProductController productController;
    private OrderController orderController;
    private PeriodicOrderController periodicOrderController;
    private SupplierService supplierService;


    @Before
    public void setUp() {
        productController = ProductController.ProductController();
        orderController = OrderController.getInstance();
        periodicOrderController = PeriodicOrderController.getInstance();
        supplierService = new SupplierService();
        //create new product
        productController.createProduct(catalog_number,branch,catalog_name,manufacturer,storePrice);
        List<String> serial_numbers = new ArrayList<>();
        serial_numbers.add("0");
        serial_numbers.add("1");
        serial_numbers.add("2");
        serial_numbers.add("3");
        productController.createProductItem(serial_numbers,catalog_number,branch,supplierId,supplierPrice,supplierDiscount, location,expireDate, "y");

    }

    @After
    public void after(){}

    /**
     * Test1: product contain less than the min expected amount --> should send automatic order
     */
    @org.junit.Test
    public void sendAutomaticOrder(){
        productController.updateProduct(branch,null,catalog_number,null,-1,5);
        try {
            List<Order> orders = orderController.getOrderHistory("123");
            Assert.assertFalse("pass",orders.isEmpty());
        }
        catch (Exception e){
            Assert.assertNotNull("failed",null);
        }
    }

    @org.junit.Test
    public void Test2(){
        productController.updateProduct(branch,null,catalog_number,null,-1,3);
        try {
            List<Order> orders = orderController.getOrderHistory("123");
            Assert.assertFalse("pass",orders.isEmpty());
        }
        catch (Exception e){
            Assert.assertNotNull("failed",null);
        }
    }

    /**
     * Test2: The Min notification calculate according to supplier info
     */
    @org.junit.Test
    public void calcMinAccordingToSupplierInf(){
        try {
            int daysForOrder = periodicOrderController.getDaysForOrder(catalog_number,branch);
        }
        catch (Exception e){
            Assert.assertNotNull("failed",null);
        }

        productController.updateProduct(branch,null,catalog_number,null,-1,5);
        productController.updateProductItem(branch,-1,"0",catalog_number,1,null,-1,-1,-1,null);
        // TODO: compere between the min value before and now
        // Assert.assertTrue();
    }

}
