package businessLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;
import dataAccessLayer.inventoryModule.CategoryManagerMapper;
import dataAccessLayer.inventoryModule.ProductManagerMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CategoryController {
    private final ProductController pc;
    private final DiscountController discountController;
    public Map<String,Category> categories; // Map<name, Category>
    private final CategoryManagerMapper categoryManagerMapper;
    private final ProductManagerMapper productManagerMapper;

    public CategoryController(ProductController pc, DiscountController discountController, CategoryManagerMapper categoryManagerMapper, ProductManagerMapper productManagerMapper) {
        this.pc = pc;
        this.discountController = discountController;
        this.categoryManagerMapper = categoryManagerMapper;
        this.productManagerMapper = productManagerMapper;
        this.categories = this.categoryManagerMapper.getCached_categories();
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
