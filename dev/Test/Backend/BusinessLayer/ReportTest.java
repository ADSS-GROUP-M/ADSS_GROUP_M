package Backend.BusinessLayer;
import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.InventoryModule.CategoryController;
import Backend.BusinessLayer.InventoryModule.ProductController;
import Backend.BusinessLayer.InventoryModule.Record;
import org.junit.Assert;
import org.junit.Before;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private String test1Result = "Record{catalog_number=0x123, name='productTest', branch='beer sheva', manufacturer=tnuva, store_price=5.0, warehouse_amount='0, store_amount='4, total_amount='4'}";
    private String test3Result = "Record{catalog_number=0x123, serial_number=3, name='productTest', branch='beer sheva', manufacturer=tnuva, supplier_price=7.7, supplier_discount=5.0, store_price=6.7, location='store'}";
    @Before
    public void setUp() {
        productController = ProductController.ProductController();
        categoryController = CategoryController.CategoryController();
        //create new product
        productController.createProduct(catalog_number,branch,catalog_name,manufacturer,storePrice);
        List<String> serial_numbers = new ArrayList<>();
        serial_numbers.add("0");
        serial_numbers.add("1");
        serial_numbers.add("2");
        serial_numbers.add("3");
        productController.createProductItem(serial_numbers,catalog_number,branch,supplierId,supplierPrice,supplierDiscount, location,expireDate, "y");

        //create new category
        List<String> subCategoriesList  = new ArrayList<>();
        subCategoriesList.add("cheese");
        subCategoriesList.add("yogurt");
        subCategoriesList.add("milk");
        Optional<List<String>> subCategories = Optional.of(subCategoriesList);
        categoryController.createCategory("Dairy products",subCategories);
    }


    //Test 1 - get shortage report
    @org.junit.Test
    public void getShortageReport() {
        productController.updateProduct(branch,null,catalog_number,null,-1,6);
        List<Record> records = productController.getInventoryShortages(branch);
        for (Record record: records){
            System.out.println("Shortage repost: " + record.toStringProduct());
            Assert.assertEquals("Shortage repost not as expected",record.toStringProduct(),test1Result);
        }
    }

    //Test 2 - get discount per Category report
    @org.junit.Test
    public void getCategoryReport() {

        productController.updateProduct(branch,null,catalog_number,null,-1,6);
        List<Record> records = productController.getInventoryShortages(branch);
        for (Record record: records){
            System.out.println("Shortage repost: " + record.toStringProduct());
            Assert.assertEquals("Shortage repost not as expected",record.toStringProduct(),test1Result);
        }
    }


    //Test 3 - get defective product report
    @org.junit.Test
    public void getDefectiveProductReport() {
        productController.updateProductItem(branch,1,"3",catalog_number,-1,null,-1,null);
        List<Record> records = productController.getDefectiveProducts(branch);
        for (Record record: records){
            System.out.println("Defective repost: " + record.toString());
            Assert.assertEquals("Defective repost not as expected",record.toString(),test3Result);
        }
    }

    //Test 4 - get defective product report with category
    @org.junit.Test
    public void getDefectiveProductReportWithCategory() {
        productController.updateProductItem(branch,1,"3",catalog_number,-1,null,-1,null);

        List<Record> records = productController.getDefectiveProducts(branch);
        for (Record record: records){
            System.out.println("Defective repost: " + record.toString());
            Assert.assertEquals("Defective repost not as expected",record.toString(),test3Result);
        }
    }



}
