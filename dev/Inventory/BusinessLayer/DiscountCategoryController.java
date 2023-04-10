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
            Map<Integer,ProductType> relatedProducts = categoriesPerBranch.get(branch).get(categoryName).getProductsRelated();
            for(ProductType productType: relatedProducts.values()){
               createStoreDiscount(productType.getProductTypeID(),branch,discount,startDate,endDate);
            }
        }
        else
            throw new RuntimeException("Category does not exist, please create category in order to continue");
    }
    public double getTodayBiggestStoreDiscountI(int productID, String branch){
        if(checkIfBranchExistStoreDiscount(branch)){
            double maxDiscount = -1;
            for(ProductStoreDiscount PDS: storeDiscounts.get(branch).get(productID)){
               double currentDiscount = PDS.getDiscount(LocalDateTime.now());
               if(currentDiscount > maxDiscount)
                   maxDiscount= currentDiscount;
            }
            if(maxDiscount != -1)
                return maxDiscount;
            else
                throw new RuntimeException(String.format("There is no discount for this product type ID: %s", productID));
        }
        else
            throw new RuntimeException("Branch does not exist, please create discount in order to continue");
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
        if(checkIfBranchExistStoreDiscount(branch)){
            List<ProductStoreDiscount> discountList = new ArrayList<ProductStoreDiscount>();
            for(ProductStoreDiscount PSD: storeDiscounts.get(branch).get(productID)){
                discountList = PSD.addDiscountSupplier(discountList,startDate,endDate);
            }
            if(!discountList.isEmpty())
                return discountList;
            else
                throw new RuntimeException(String.format("There is no discount for this product type ID: %s", productID));
        }
        else
            throw new RuntimeException("Branch does not exist, please create discount in order to continue");

    }

    public List<ProductDiscountSupplier> getSupplierDiscountPerDate(int productID, String branch, int supplierID, LocalDateTime startDate, LocalDateTime endDate){
        if(checkIfSupplierExist(branch,supplierID)){
            List<ProductDiscountSupplier> supplierDiscountList = supplierDiscount.get(branch).get(supplierID);
            List<ProductDiscountSupplier> supplierDiscountResults = new ArrayList<ProductDiscountSupplier>();
            for(ProductDiscountSupplier PDS: supplierDiscountList){
                PDS.addDiscountSupplier(supplierDiscountResults,startDate,endDate,productID);
            }
            if(!supplierDiscountResults.isEmpty())
                return supplierDiscountResults;
            else
                throw new RuntimeException(String.format("in range %s  %s there is not Discount form this supplierID: %s",startDate, endDate, supplierID));
        }
        else
            throw new RuntimeException("supplier or branch does not exist, please create discount in order to continue");

    }

    public Map<String ,List<Product>> getStockProduct(List<Category> categories, String branch, LocalDateTime startDate, LocalDateTime endDate){
        //TODO: need to implement
        throw new RuntimeException();
    }
}
