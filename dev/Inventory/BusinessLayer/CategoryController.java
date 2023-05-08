package dev.Inventory.BusinessLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryController {
    // Map<name, Category>
    public Map<String,Category> categories;

    //create the controller as Singleton
    private static CategoryController categoryController = null;
    private CategoryController() {
        this.categories = new HashMap<String,Category>();}
    public static CategoryController CategoryController(){
        if(categoryController == null)
            categoryController = new CategoryController();
        return categoryController;
    }
//    private Boolean checkIfBranchExist(String branch){
//        return categoriesPerBranch.containsKey(branch);
//    }
    public Boolean checkIfCategoryExist(String categoryName){
            return categories.containsKey(categoryName);
    }

    public void createCategory(String categoryName, List<String> subcategoriesName){
        List<Category> subcategories = new ArrayList<Category>();
        if(!subcategoriesName.isEmpty() || subcategoriesName != null){
            for (String subcategoryName: subcategoriesName){
                if(!categories.containsKey(subcategoryName))
                    createCategory(subcategoryName,null);
                subcategories.add(getCategory(subcategoryName));
            }
        }
        categories.put(categoryName,new Category(categoryName,subcategories));
    }


    public void removeCategory(String categoryName){
        if(checkIfCategoryExist(categoryName)){
            if(!categories.get(categoryName).isRelatedProductEmpty()){
                // verify in each category that the category does not contain the remove category
                for(Category category: categories.values())
                    category.removeSubCategory(categoryName);
                categories.remove(categoryName);
            }
            else
                throw new RuntimeException("Category related to other products, please update before remove");
        }
        else{
            throw new RuntimeException("Category does not exist");
        }

    }
    //TODO
    public void addSubcategory(String categoryName, List<String> subcategoriesName){
        List<Category> subcategories = new ArrayList<Category>();
        if(!subcategoriesName.isEmpty() || subcategoriesName != null){
            for (String subcategoryName: subcategoriesName){
                if(!categories.containsKey(subcategoryName))
                    createCategory(subcategoryName,null);
                subcategories.add(getCategory(subcategoryName));
            }
        }
        categories.get(categoryName).addSubcategories(subcategories);
    }

    public void removeSubcategory(String name,  List<String> subcategoriesName){
        if(!checkIfCategoryExist(name)){
            Category category = categories.get(name);
            for(String subcategoryName: subcategoriesName)
                category.removeSubCategory(subcategoryName);
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


    //TODO
    public Map<String, Product> getCategoryProducts(String categoryName){
        return categories.get(categoryName).getProductsRelated();
    }

    private List<Record> getProductRecordPerCategory(String categoryName,String branch, List<Record> records){
        for(Product product: categories.get(categoryName).getProductsRelated().values()){
            String catalog_number = product.getCatalogNumber();
            String name = product.getName();
            String manufacture = product.getManufacturer();
            DiscountController discountController = DiscountController.DiscountController();
            double storePrice = discountController.calcSoldPrice(branch,catalog_number,product.getOriginalStorePrice());
            int warehouseAmount = product.getWarehouseAmount();
            int storeAmount = product.getStoreAmount();
            Record record = new Record(catalog_number,name,branch,manufacture,storePrice,warehouseAmount,storeAmount);
            records.add(record);
        }
        return records;
    }

    // Report Category function - inorder to receive all category product details
    public List<Record> getProductsPerCategory(List<String> categoriesName, String branch){
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
