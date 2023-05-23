package suppliersInventory;

import businessLayer.BusinessFactory;
import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.Product;
import businessLayer.inventoryModule.ProductController;
import businessLayer.suppliersModule.Order;
import businessLayer.suppliersModule.OrderController;
import businessLayer.suppliersModule.PeriodicOrderController;
import dataAccessLayer.DalFactory;
import exceptions.TransportException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serviceLayer.suppliersModule.SupplierService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
    private static String catalog_number1 = "0x123";
    private static String catalog_number2 = "23";
    private static String catalog_number3 = "555";
    private static String catalog_number4 = "24";
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

    String test1Result = "order failed - product - 0x123 cant be fully ordered";
    String test4Result = "Product does not exist with the ID : 0";

//    private Product product;
    private ProductController productController;
    private OrderController orderController;
    private PeriodicOrderController periodicOrderController;
    private SupplierService supplierService;


    @BeforeEach
    public void setUp() {
        try {
            BusinessFactory factory = new BusinessFactory(DalFactory.TESTING_DB_NAME);
            productController = factory.productController();
            orderController = factory.orderController();
            periodicOrderController = factory.periodicOrderController();

//
//            productController = ProductController.ProductController();
//            orderController = OrderController.getInstance();
//            periodicOrderController = PeriodicOrderController.getInstance();
            List<String> serial_number = new ArrayList<>();
            serial_number.add("000");
            productController.createProductItem(serial_number,catalog_number2,branch,"2",5.30,0.1, "warehouse",expireDate,"y" );
        } catch (TransportException e) {
            fail(e);
        }

    }
    @AfterEach
    public void after(){
        productController.updateProduct(branch,null,catalog_number1,null,-1,6);
    }

    /**
     * Test1: product contain less than the min expected amount --> should send automatic order
     * supplier don't have the need product --> expect to exception
     */
    @Test
    public void sendAutomaticOrder(){
        productController.updateProduct(branch,null,catalog_number1,null,-1,10);
        try {
            productController.isProductLack(branch,catalog_number1);
            List<Order> orders = orderController.getOrderHistory("123");
            fail("failed");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            assertEquals(e.getMessage(),test1Result,"pass");
        }
    }

    /**
     * Test2: product contain less than the min expected amount --> should send automatic order
     */
    @Test
    public void Test2(){
        productController.updateProduct(branch,null,catalog_number3,null,-1,5);
        try {
            productController.isProductLack(branch,catalog_number3);
            List<Order> orders = orderController.getOrderHistory("2");
            assertFalse(orders.isEmpty(), "Order created successfully");
        }
        catch (Exception e){
            fail("Failed to create order");
        }
    }



    /**
     * Test3: The Min notification calculate according to supplier info
     */
    @Test
    public void calcMinAccordingToSupplierInf(){
        int daysForOrder = 0;
        try {
            daysForOrder = periodicOrderController.getDaysForOrder(catalog_number2,branch);
            productController.updateProduct(branch,null,catalog_number2,null,-1,3);
            productController.updateProductItem(branch,-1,"000",catalog_number2,1,null,-1,-1,-1,null);

            Product product =productController.getProduct(branch,catalog_number2);
            assertEquals(product.getNotificationMin(),daysForOrder*product.productDemandAmount()+1,"Min notification did update as needed");
        }
        catch (Exception e){
            assertEquals(test4Result,e.getMessage(),"Test 4 pass");
        }

    }


    /**
     * Test4: The Min notification calculate according to supplier info
     * but there is no periodic supplier
     */
    @Test
    public void calcMinAccordingToSupplierInf2(){
        int daysForOrder = 0;
        try {
            daysForOrder = periodicOrderController.getDaysForOrder(catalog_number3,branch);
            productController.updateProduct(branch,null,catalog_number1,null,-1,5);
            productController.updateProductItem(branch,-1,"0",catalog_number3,1,null,-1,-1,-1,null);
            fail("failed");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            assertEquals(test4Result,e.getMessage(),"Test 4 pass");
        }

    }

}
