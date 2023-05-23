package suppliersInventory;

import businessLayer.BusinessFactory;
import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.CategoryController;
import businessLayer.inventoryModule.ProductController;
import businessLayer.inventoryModule.Record;
import dataAccessLayer.DalFactory;
import exceptions.TransportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ReportTest {
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

    private ProductController productController;
    private CategoryController categoryController;

    private final Map<Integer, String> testResults1 = new HashMap<>() {{
        put(1, testResult1);
        put(2, testResult11);
        put(3, testResult111);
    }};
    private final String testResult1 = "Record{catalog_number=0x123, name='productTest', branch='branch1', category=, manufacturer=tnuva, store_price=5.0, warehouse_amount='0, store_amount='5, total_amount='5'}";
    private final String testResult11 = "Record{catalog_number=24, name='milk2', branch='branch1', category=, manufacturer=tnuva, store_price=10.0, warehouse_amount='0, store_amount='0, total_amount='0'}";
    private final String testResult111 = "Record{catalog_number=555, name='fish', branch='branch1', category=, manufacturer=ocean, store_price=2.0, warehouse_amount='0, store_amount='0, total_amount='0'}";


    private final Map<Integer, String> testResults2 = new HashMap<>() {{
        put(1, testResult2);
        put(2, testResult22);
    }};
    private final String testResult2 = "Record{catalog_number=23, name='milk', branch='branch1', category=, manufacturer=tnuva, store_price=7.0, warehouse_amount='2, store_amount='2, total_amount='4'}";
    private final String testResult22 = "Record{catalog_number=24, name='milk2', branch='branch1', category=, manufacturer=tnuva, store_price=10.0, warehouse_amount='0, store_amount='0, total_amount='0'}";
    private final String testResult222 = "Record{catalog_number=555, name='fish', branch='branch1', category=, manufacturer=ocean, store_price=2.0, warehouse_amount='0, store_amount='0, total_amount='0'}";

    private final Map<Integer, String> testResults3 = new HashMap<>() {{
        put(1, testResult3);
        put(2, testResult33);
    }};

    private final String testResult3 = "Record{catalog_number=23, serial_number=0444, name='milk', branch='branch1', manufacturer=tnuva, supplier_price=7.0, supplier_discount=7.0, store_price=6.3, location='store'}";
    private final String testResult33 = "Record{catalog_number=0x123, serial_number=0444, name='productTest', branch='branch1', manufacturer=tnuva, supplier_price=7.7, supplier_discount=5.0, store_price=6.7, location='store'}";

    @BeforeEach
    public void setUp() {
        BusinessFactory factory = null;
        try {
            factory = new BusinessFactory(DalFactory.TESTING_DB_NAME);
            productController = factory.productController();
            categoryController = factory.categoryController();
//        productController = ProductController.ProductController();
//        categoryController = CategoryController.CategoryController();
            productController.updateProduct(branch,null,catalog_number,null,-1,6);
            //create new product
//        productController.createProduct(catalog_number,branch,catalog_name,manufacturer,storePrice);
//        List<String> serial_numbers = new ArrayList<>();
//        serial_numbers.add("0");
//        serial_numbers.add("1");
//        serial_numbers.add("2");
//        serial_numbers.add("3");
//        productController.createProductItem(serial_numbers,catalog_number,branch,supplierId,supplierPrice,supplierDiscount, location,expireDate, "y");
//
//        //create new category
//        List<String> subCategoriesList  = new ArrayList<>();
//        subCategoriesList.add("cheese");
//        subCategoriesList.add("yogurt");
//        subCategoriesList.add("milk");
//        categoryController.createCategory(branch, subCategoriesList, "Dairy products");
        } catch (TransportException e) {
            fail(e);
        }
    }


    //Test 1 - get shortage report
    @Test
    public void getShortageReport() {
//        productController.updateProduct(branch,null,catalog_number,null,-1,6);
        List<Record> records = productController.getInventoryShortages(branch);
        int index = 1;
        for (Record record: records){
            System.out.println("Shortage repost: " + record.toStringProduct());
            assertEquals("Shortage repost not as expected",record.toStringProduct(), testResults1.get(index));
            index++;
        }
    }

    //Test 2 - get discount per Category report
    @Test
    public void getCategoryReport() {
//        categoryController.addProductToCategory(branch,catalog_number,"yogurt");
        List<String> categoryList = new ArrayList<String>();
        int index = 1;
        categoryList.add("dairy");
        List<Record> records = categoryController.getProductsPerCategory( categoryList,branch );
        for (Record record: records){
            System.out.println("Category report: " + record.toStringProduct());
            assertEquals("Category report not as expected",record.toStringProduct(), testResults2.get(index));
            index++;
        }
    }


    //Test 3 - get defective product report
    @Test
    public void getDefectiveProductReport() {
//        productController.updateProductItem(branch,1,"3",catalog_number,-1,null,-1,-1,-1,null);
        int index = 1;
        List<Record> records = productController.getDefectiveProducts(branch);
        for (Record record: records){
            System.out.println("Defective repost: " + record.toString());
            assertEquals("Defective repost not as expected",record.toString(), testResults3.get(index));
            index++;
        }
    }

//    //Test 4 - get defective product report with category
//    @Test
//    public void getDefectiveProductReportWithCategory() {
////        productController.updateProductItem(branch,1,"3",catalog_number,-1,null,-1,-1,-1,null);
//
//        List<Record> records = productController.getDefectiveProducts(branch);
//        for (Record record: records){
//            System.out.println("Defective repost: " + record.toString());
//            assertEquals("Defective repost not as expected",record.toString(), testResults3);
//        }
//    }

}
