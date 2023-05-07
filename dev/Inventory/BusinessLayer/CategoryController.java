package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryController {
    //Map<branch , Map<name, Category>
    public Map<String, Map<String,Category>> categoriesPerBranch;

    //create the controller as Singleton
    private static CategoryController categoryController = null;
    private CategoryController() {
        this.categoriesPerBranch = new HashMap<String, Map<String,Category>>();}
    public static CategoryController CategoryController(){
        if(categoryController == null)
            categoryController = new CategoryController();
        return categoryController;
    }
    private Boolean checkIfBranchExist(String branch){
        return categoriesPerBranch.containsKey(branch);
    }
    public Boolean checkIfCategoryExist(String branch, String categoryName){
        if(checkIfBranchExist(branch))
            return categoriesPerBranch.get(branch).containsKey(categoryName);
        else
            throw new RuntimeException("Brunch does not exist, please create discount in order to continue");
    }

    public void createCategory(String branch,String categoryName, int categoryType){
        if(!checkIfBranchExist(branch))
            categoriesPerBranch.put(branch,new HashMap<String,Category>());
        categoriesPerBranch.get(branch).put(categoryName,new Category(categoryName,categoryType));
    }

    public void removeCategory(String branch, String categoryName){
        if(checkIfCategoryExist(branch,categoryName)){
            if(!categoriesPerBranch.get(branch).get(categoryName).isRelatedProductEmpty())
                categoriesPerBranch.get(branch).remove(categoryName);
            else
                throw new RuntimeException("Category related to other products, please update before remove");
        }
        else{
            throw new RuntimeException("Category does not exist");
        }

    }

    public Category getCategory(String branch, String categoryName){return categoriesPerBranch.get(branch).get(categoryName);}

    public Map<Integer, Product> getCategoryProducts(String branch, String categoryName){
        return categoriesPerBranch.get(branch).get(categoryName).getProductsRelated();
    }

    // Report Category function - inorder to receive all category product details
    public List<Record> getProductsPerCategory(List<Category> categories, String branch){
        if(checkIfBranchExist(branch)){
            List<Record> productsCategoryRecords = new ArrayList<Record>();
            for(Category category: categories){
                for(Product productType: category.getProductsRelated().values()){
                    for(ProductItem product: productType.getProductItems().values()){
                        String catalog_number = productType.getCatalogNumber();
                        int productID = product.getSerial_number();
                        String name = productType.getName();
                        String manufacture = productType.getManufacturer();
                        double supplierPrice = productType.getOriginalSupplierPrice();
                        //TODO: need to edit
                        double storePrice = calcSoldPrice(branch,catalog_number,productType.getOriginalStorePrice());
                        List<Category> subCategory = productType.getSubCategory();
                        String location = product.getLocation();
                        //create Record
                        Record record = new Record(catalog_number,productID,name,branch,manufacture,supplierPrice,storePrice,category,subCategory,location);
                        productsCategoryRecords.add(record);
                    }
                }
            }
            return productsCategoryRecords;
        }
        else
            throw new RuntimeException("Branch does not exist, please create discount in order to continue");
    }

}
