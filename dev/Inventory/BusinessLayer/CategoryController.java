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

    // Report Category function - inorder to receive all category product details
    public List<Record> getProductsPerCategory(List<Category> categories, String branch){
        List<Record> productsCategoryRecords = new ArrayList<Record>();
        for(Category category: categories){
            for(Product product: category.getProductsRelated().values()){
                for(ProductItem productItem: product.getProductItems().values()){
                    String catalog_number = product.getCatalogNumber();
                    String serial_number = productItem.getSerial_number();
                    String name = product.getName();
                    String manufacture = product.getManufacturer();
                    double supplierPrice = productItem.getSupplierPrice();
                    double supplierDiscount = productItem.getSupplierDiscount();
                    DiscountController discountController = DiscountController.DiscountController();
                    double storePrice = discountController.calcSoldPrice(branch,catalog_number,product.getOriginalStorePrice());
                    List<Category> subCategory = product.getSubCategory();
                    String location = productItem.getLocation();
                    //create Record
                    Record record = new Record(catalog_number,serial_number,name,branch,manufacture,supplierPrice,supplierDiscount,storePrice,category,subCategory,location);
                    productsCategoryRecords.add(record);
                }
            }
        }
        return productsCategoryRecords;
    }

}
