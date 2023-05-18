package suppliersInventory;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.Category;
import businessLayer.inventoryModule.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoryTest {
    private static String categoryName = "test_category";
    private static List<Category> sub = new ArrayList<>();

    private static String catalog_number = "0x123";
    private static String catalog_name = "productTest";
    private static String manufacturer = "tnuva";
    private static double storePrice = 5.0;
    private static Branch branch = Branch.branch1;
    private Category category;
    private Product product;

    private Category c;
    @BeforeEach
    public void setUp() {
        //create new category
        category = new Category(categoryName, sub);
        //create new product
        product = new Product(catalog_number, catalog_name, manufacturer, storePrice,branch );
    }


    //Test 1
    // Verify that the product was added correctly
    @Test
    public void addProductToCategory() {
        category.addProductToCategory(product);
        assertTrue(category.isProductIDRelated(product.getCatalogNumber(),branch), "failed to add product to category");
    }

    //Test 2
    // Verify that the product removed from category
    @Test
    public void removeProductFromCategory() {
        category.addProductToCategory(product);
        assertTrue(category.isProductIDRelated(product.getCatalogNumber(),branch), "failed to add product to category");
        category.removeProduct(product);
        assertFalse(category.isProductIDRelated(product.getCatalogNumber(),branch), "failed to remove product from category");
    }

    //Test 3
    // Verify that subcategory added as expected
    @Test
    public void addSubcategoryToCategory() {
        Category subcategory = new Category("milk",new ArrayList<>());
        List<Category> subTest = new ArrayList<>();
        subTest.add(subcategory);
        category.addSubcategories(subTest);
        assertTrue(category.isSubcategory(subcategory.getCategoryName()), "failed to add the subcategory");
    }

}

