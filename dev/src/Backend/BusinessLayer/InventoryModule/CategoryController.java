package Backend.BusinessLayer.InventoryModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;

import java.util.*;

public class CategoryController {
    public Map<String,Category> categories; // Map<name, Category>

    //create the controller as Singleton
    private static CategoryController categoryController = null;
    private CategoryController() {
        this.categories = new HashMap<Branch, Map<String,Category>>();}
    public static CategoryController CategoryController(){
        if(categoryController == null)
            categoryController = new CategoryController();
        return categoryController;
    }

    public void checkIfBranchExist(Branch branch){
        if(!categories.containsKey(branch))
            categories.put(branch,new HashMap<String,Category>());
    }
    public Boolean checkIfCategoryExist(Branch branch, String categoryName){
            return categories.get(branch).containsKey(categoryName);
    }

    public void createCategory(Branch branch, Optional<List<String>> subcategoriesName, String categoryName){
        checkIfBranchExist(branch);
        List<Category> subcategories = new ArrayList<Category>();
        if(subcategoriesName.isPresent() && !subcategoriesName.get().isEmpty()){
            for (String subcategoryName: subcategoriesName.get()){
                if(!categories.containsKey(subcategoryName))
                    createCategory(branch, Optional.empty(), subcategoryName);
                subcategories.add(getCategory(branch, subcategoryName));
            }
        }
        categories.get(branch).put(categoryName,new Category(categoryName,subcategories));
    }

    public void removeCategory(Branch branch, String categoryName){
        checkIfBranchExist(branch);
        if(checkIfCategoryExist(branch, categoryName)){
            if(!categories.get(branch).get(categoryName).isRelatedProductEmpty()){
                // verify in each category that the category does not contain the remove category
                for(Category category: categories.get(branch).values())
                    category.removeSubCategory(categoryName);
                categories.get(branch).remove(categoryName);
            }
            else
                throw new RuntimeException("Category related to other products, please update before remove");
        }
        else{
            throw new RuntimeException("Category does not exist");
        }
    }

    public void addProductToCategory(Branch branch, String catalog_number, String categoryName){
        checkIfBranchExist(branch);
        if(checkIfCategoryExist(branch, categoryName)){
            ProductController pc = ProductController.ProductController();
            categories.get(branch).get(categoryName).addProductToCategory(pc.getProduct(branch,catalog_number));
        }
        else{
            throw new RuntimeException("Category does not exist");
        }
    }

    public void removeProductFromCategory(Branch branch, String catalog_number, String categoryName){
        if(checkIfCategoryExist(branch, categoryName)){
            categories.get(branch).get(categoryName).removeProduct(catalog_number);
        }
        else{
            throw new RuntimeException("Category does not exist");
        }
    }

    //TODO
    public void addSubcategory(Branch branch,String categoryName, List<String> subcategoriesName){
        List<Category> subcategories = new ArrayList<Category>();
        if(!subcategoriesName.isEmpty() && subcategoriesName != null){
            for (String subcategoryName: subcategoriesName){
                if(!categories.containsKey(subcategoryName))
                    createCategory(branch, null, subcategoryName);
                subcategories.add(getCategory(branch, subcategoryName));
            }
        }
        categories.get(branch).get(categoryName).addSubcategories(subcategories);
    }

    public void removeSubcategory(Branch branch, List<String> subcategoriesName, String name){
        if(!checkIfCategoryExist(branch, name)){
            Category category = categories.get(branch).get(name);
            for(String subcategoryName: subcategoriesName)
                category.removeSubCategory(subcategoryName);
        }
        else{
            throw new RuntimeException("Category does not exist");
        }

    }
    public Category getCategory(Branch branch, String categoryName){
        if(checkIfCategoryExist(branch, categoryName))
            return categories.get(branch).get(categoryName);
        else
            throw new RuntimeException("Category does not exist");
    }

    //TODO
    public Map<String, Product> getCategoryProducts(Branch branch, String categoryName){
        checkIfBranchExist(branch);
        if(checkIfCategoryExist(branch,categoryName))
            return categories.get(branch).get(categoryName).getProductsRelated();
        else
            throw new RuntimeException("Category does not exist");
    }


    private List<Record> getProductRecordPerCategory(String categoryName, Branch branch, List<Record> records){
        checkIfBranchExist(branch);
        for(Product product: categories.get(branch).get(categoryName).getProductsRelated().values()){
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
            Category category = categories.get(branch).get(categoryName);
            productsCategoryRecords = getProductRecordPerCategory(category.getCategoryName(),branch,productsCategoryRecords);
            for(Category subcategory: category.getSubcategories().values()){
                if(!categoriesName.contains(subcategory.getCategoryName()))
                    productsCategoryRecords = getProductRecordPerCategory(subcategory.getCategoryName(),branch,productsCategoryRecords);
            }
        }
        return productsCategoryRecords;
    }

}
