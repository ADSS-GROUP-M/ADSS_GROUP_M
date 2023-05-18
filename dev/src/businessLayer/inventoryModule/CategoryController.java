package businessLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;
import dataAccessLayer.inventoryModule.CategoryManagerMapper;
import dataAccessLayer.inventoryModule.ProductManagerMapper;

import java.util.*;

public class CategoryController {
    public Map<String,Category> categories; // Map<name, Category>

    //create the controller as Singleton
    private static CategoryController categoryController = null;
    private CategoryManagerMapper categoryManagerMapper;
    private ProductManagerMapper productManagerMapper;

    private CategoryController() {
        categoryManagerMapper = CategoryManagerMapper.getInstance();
        productManagerMapper = ProductManagerMapper.getInstance();
        this.categories = categoryManagerMapper.getCached_categories();
    }

    public static CategoryController CategoryController(){
        if(categoryController == null)
            categoryController = new CategoryController();
        return categoryController;
    }

    public Boolean checkIfCategoryExist(String categoryName){
            return categories.containsKey(categoryName);
    }

    public void createCategory(Branch branch,List<String> subcategoriesName, String categoryName){
        try {
            if (!checkIfCategoryExist(categoryName)) {
                List<Category> subcategories = new ArrayList<Category>();
                if (!subcategoriesName.isEmpty()) {
                    for (String subcategoryName : subcategoriesName) {
                        if (!categories.containsKey(subcategoryName)) {
                            List<String> emptyList = new ArrayList<>();
                            createCategory(branch, emptyList, subcategoryName);
                        }
                        subcategories.add(getCategory(subcategoryName));
                    }
                }
                categories.put(categoryName, new Category(categoryName, subcategories));
                categoryManagerMapper.createCategory(categoryName, subcategories);
//                if (!subcategoriesName.isEmpty()) {
//                    for (String subcategoryName : subcategoriesName) {
//                        categoryManagerMapper.createSubCategory(categoryName, subcategoryName);
//                    }
//                }
            }
        }
        catch (Exception e){throw new RuntimeException(e.getMessage());
        }
    }

    public void removeCategory(Branch branch, String categoryName){
        try {
            if (checkIfCategoryExist(categoryName)) {
                if (categories.get(categoryName).isRelatedProductEmpty()) {
                    // verify in each category that the category does not contain the remove category
                    Collection<Category> categoriesCollection = categories.get(categoryName).getSubcategories().values();
                    List<Category> subcategories = new ArrayList<>(categoriesCollection);
                    categoryManagerMapper.removeCategory(categoryName, subcategories);
                    for (Category category : categories.values()) {
                        categoryManagerMapper.removeSubCategory(categoryName, category.getCategoryName());
                        category.removeSubCategory(categoryName);
                    }
                    categories.remove(categoryName);
                } else
                    throw new RuntimeException("Category related to other products, please update before remove");
            } else {
                throw new RuntimeException("Category does not exist");
            }
        }
        catch (Exception e){throw new RuntimeException(e.getMessage());
        }

    }

    public void addProductToCategory(Branch branch, String catalog_number, String categoryName){
        if(checkIfCategoryExist(categoryName)){
            if(!categories.get(categoryName).isProductIDRelated(catalog_number, branch)) {
                productManagerMapper.addCategoryToProduct(catalog_number,categoryName);
                ProductController pc = ProductController.ProductController();
                categories.get(categoryName).addProductToCategory(pc.getProduct(branch, catalog_number));
            }
        }
        else{
            throw new RuntimeException("Category does not exist");
        }
    }

    public void removeProductFromCategory(Branch branch, String catalog_number, String categoryName){
        if(checkIfCategoryExist(categoryName)){
            productManagerMapper.removeCategoryFromProduct(catalog_number);
            ProductController pc = ProductController.ProductController();
            categories.get(categoryName).removeProduct(pc.getProduct(branch,catalog_number));
        }
        else{
            throw new RuntimeException("Category does not exist");
        }
    }

    public Category getCategory(String categoryName){
        if(checkIfCategoryExist(categoryName))
            return categories.get(categoryName);
        else
            throw new RuntimeException("Category does not exist");
    }

    public List <Product> getCategoryProducts(Branch branch, String categoryName){
        if(checkIfCategoryExist(categoryName))
            return categories.get(categoryName).getProductsRelated(branch);
        else
            throw new RuntimeException("Category does not exist");
    }


    private List<Record> getProductRecordPerCategory(String categoryName, Branch branch, List<Record> records){
        for(Product product: categories.get(categoryName).getProductsRelated(branch)){
            String catalog_number = product.getCatalogNumber();
            String name = product.getName();
            String manufacture = product.getManufacturer();
            DiscountController discountController = DiscountController.DiscountController();
            double storePrice = discountController.calcSoldPrice(branch,catalog_number,product.getOriginalStorePrice());
            int warehouseAmount = product.getWarehouseAmount();
            int storeAmount = product.getStoreAmount();
            Record record = new Record(catalog_number,name,branch.name(),manufacture,storePrice,warehouseAmount,storeAmount);
            records.add(record);
        }
        return records;
    }

    // Report Category function - inorder to receive all category product details
    public List<Record> getProductsPerCategory(List<String> categoriesName, Branch branch){
        List<Record> productsCategoryRecords = new ArrayList<Record>();
        for(String categoryName: categoriesName){
            Category category = categories.get(categoryName);
            productsCategoryRecords = getProductRecordPerCategory(category.getCategoryName(),branch,productsCategoryRecords);
            for(Category subcategory: category.getSubcategories().values()){
                if(!categoriesName.contains(subcategory.getCategoryName()))
                    productsCategoryRecords = getProductRecordPerCategory(subcategory.getCategoryName(),branch,productsCategoryRecords);
            }
        }
        return productsCategoryRecords;
    }

}
