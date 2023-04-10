package dev.Inventory.BusinessLayer;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountCategoryController {
    //Map<branch,Map<supplierID, List<ProductDiscountSupplier>>>
    public Map<String, Map<Integer,List<ProductDiscountSupplier>>> supplierDiscount;
    //Map<branch,Map<productTypeID, List<ProductStoreDiscount>>>
    public Map<String, Map<Integer,List<ProductStoreDiscount>>> storeDiscounts;

//    public Map<String, List<CategoryDiscount>> categoryDiscount;
    public Map<String,Map<String,Category>> categoriesPerBranch;

    //should be singleton
    public DiscountCategoryController() {
        this.storeDiscounts = new HashMap<String, Map<Integer,List<ProductStoreDiscount>>>();
        this.supplierDiscount = new HashMap<String, Map<Integer,List<ProductDiscountSupplier>>>();
//        this.categoryDiscount = new HashMap<String, List<CategoryDiscount>>();
        this.categoriesPerBranch = new HashMap<String, Map<String,Category>>();
    }

    private Boolean checkIfBranchExistStoreDiscount(String branch){
        return storeDiscounts.containsKey(branch);
    }
    private Boolean checkIfBranchExistSupplierDiscount(String branch){
        return supplierDiscount.containsKey(branch);
    }
    private Boolean checkIfBranchExistCategory(String branch){
        return categoriesPerBranch.containsKey(branch);
    }
    private Boolean checkIfCategoryExist(String branch, String categoryName){
        if(checkIfBranchExistCategory(branch))
            return categoriesPerBranch.get(branch).containsKey(categoryName);
        else
            throw new RuntimeException("Brunch does not exist, please create discount in order to continue");
    }
    private Boolean checkIfSupplierExist(String branch, int supplierID){
        if(checkIfBranchExistSupplierDiscount(branch))
            return supplierDiscount.get(branch).containsKey(supplierID);
        return false;
    }
    private Boolean checkIfProductIDExist(String branch, int productTypeID){
        if(checkIfBranchExistStoreDiscount(branch))
            return storeDiscounts.get(branch).containsKey(productTypeID);
        return false;
    }
    public void createCategory(String branch,String categoryName, int categoryType){
        if(!checkIfBranchExistCategory(branch))
            categoriesPerBranch.put(branch,new HashMap<String,Category>());
        categoriesPerBranch.get(branch).put(categoryName,new Category(categoryName,categoryType));
    }
    public void createSupplierDiscount(int productID, String branch,double discount, int supplierID, LocalDateTime startDate, LocalDateTime endDate){
        if(!checkIfBranchExistSupplierDiscount(branch))
            supplierDiscount.put(branch,new HashMap<Integer,List<ProductDiscountSupplier>>());
        if(!checkIfSupplierExist(branch,supplierID))
            supplierDiscount.get(branch).put(supplierID,new ArrayList<ProductDiscountSupplier>());
        supplierDiscount.get(branch).get(supplierID).add(new ProductDiscountSupplier(productID,branch,startDate,endDate,discount,supplierID));
    }
    public void createStoreDiscount(int productTypeID, String branch,double discount, LocalDateTime startDate, LocalDateTime endDate){
        if(!checkIfBranchExistStoreDiscount(branch))
            storeDiscounts.put(branch, new HashMap<Integer,List<ProductStoreDiscount>>());
        if(!checkIfProductIDExist(branch,productTypeID))
            storeDiscounts.get(branch).put(productTypeID,new ArrayList<ProductStoreDiscount>());
        storeDiscounts.get(branch).get(productTypeID).add(new ProductStoreDiscount(productTypeID,branch,startDate,endDate,discount));
    }
    public void createCategoryDiscount(String categoryName, String branch,double discount, LocalDateTime startDate, LocalDateTime endDate){
        if(checkIfCategoryExist(branch,categoryName)){
            categoriesPerBranch.get(branch).get(categoryName).addDiscountCategory(categoryName,branch,discount,startDate,endDate);
        }
        else
            throw new RuntimeException("Category does not exist, please create category in order to continue");
    }
    public double getTodayBiggestStoreDiscountI(int productID, String branch){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public double getTodaySupplierDiscountI(int productID, String branch, int supplierID){
        if(checkIfSupplierExist(branch,supplierID)){
            List<ProductDiscountSupplier> supplierDiscountList = supplierDiscount.get(branch).get(supplierID);
            double maxDisxount =-1;
            for(ProductDiscountSupplier PDS: supplierDiscountList){
                double currentDiscount = PDS.getDiscountPerDate(LocalDateTime.now(),productID);
                if(currentDiscount > maxDisxount)
                    maxDisxount = currentDiscount;
            }
            if(maxDisxount != -1)
                return maxDisxount;
            else
                throw new RuntimeException(String.format("Today there is not Discount form this supplierID: %s",supplierID));
        }
        else
            throw new RuntimeException("Supplier or branch does not exist, please create discount in order to continue");
    }
    public List<ProductStoreDiscount> getStoreDiscountPerDate(int productID, String branch, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public double getStoreMinDiscount(int productID, String branch){
        //TODO: need to implement
        throw new RuntimeException();
    }

    public List<ProductDiscountSupplier> getSupplierDiscountPerDate(int productID, String branch, int supplierID, LocalDateTime checkedDate){
        if(checkIfSupplierExist(branch,supplierID)){
            List<ProductDiscountSupplier> supplierDiscountList = supplierDiscount.get(branch).get(supplierID);
            List<ProductDiscountSupplier> supplierDiscountResults = new ArrayList<ProductDiscountSupplier>();
            for(ProductDiscountSupplier PDS: supplierDiscountList){
                PDS.addDiscountSupplier(supplierDiscountResults,checkedDate,productID);
            }
            if(!supplierDiscountResults.isEmpty())
                return supplierDiscountResults;
            else
                throw new RuntimeException(String.format("in %s there is not Discount form this supplierID: %s",checkedDate, supplierID));
        }
        else
            throw new RuntimeException("supplier or branch does not exist, please create discount in order to continue");

    }
    public double getSupplierMaxDiscount(int productID, String branch, int supplierID){
        if(checkIfSupplierExist(branch,supplierID)){
            List<ProductDiscountSupplier> supplierDiscountList = supplierDiscount.get(branch).get(supplierID);
            double maxDiscount = -1;
            for(ProductDiscountSupplier PDS: supplierDiscountList){
                PDS.getDiscount(productID);
            }
            if(maxDiscount != -1)
                return maxDiscount;
            else
                throw new RuntimeException(String.format("for %s there is not Discount form this supplierID: %s",productID, supplierID));
        }
        else
            throw new RuntimeException("supplier or branch does not exist, please create discount in order to continue");

    }
    public void updateDiscountPerCategory(String name, String branch, int discount, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }

    public Map<String ,List<CategoryDiscount>> getStockProduct(List<Category> categories, String branch, LocalDateTime startDate, LocalDateTime endDate){
        Map<String ,List<CategoryDiscount>> allProductList = new HashMap<String ,List<CategoryDiscount>>();
        if(checkIfBranchExistCategory(branch)){
            for(Category category : categories){
                List<CategoryDiscount> CDlist = new ArrayList<CategoryDiscount>();
                if(categoriesPerBranch.get(branch).containsKey(category.getNameCategory())){
                    CDlist = category.getCategoriesDiscountInRange(CDlist,startDate,endDate);
                    if(!CDlist.isEmpty())
                        allProductList.put(category.getNameCategory(), CDlist);
                }
                else{
                    throw new RuntimeException(String.format("Category %s does not exist",category.getNameCategory() ));
                }
                CDlist.clear();
            }
        }
        else
            throw new RuntimeException("Branch does not exist, please create category in order to continue");
        if(!allProductList.isEmpty())
            return allProductList;
        else
            throw new RuntimeException("There is no discount form the provided list category");

    }
}
