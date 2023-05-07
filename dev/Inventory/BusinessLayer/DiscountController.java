package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountController {
    //Map<branch,Map<supplierID, List<ProductDiscountSupplier>>>
    public Map<String, Map<Integer,List<ProductDiscountSupplier>>> supplierDiscount;
    //Map<branch,Map<catalog_number, List<ProductStoreDiscount>>>
    public Map<String, Map<String,List<ProductStoreDiscount>>> storeDiscounts;


    //create the controller as Singleton
    private static DiscountController discountController = null;
    private DiscountController() {
        this.storeDiscounts = new HashMap<String, Map<String,List<ProductStoreDiscount>>>();
        this.supplierDiscount = new HashMap<String, Map<Integer,List<ProductDiscountSupplier>>>();}
    public static DiscountController DiscountController(){
        if(discountController == null)
            discountController = new DiscountController();
        return discountController;
    }

    private Boolean checkIfBranchExistStoreDiscount(String branch){
        return storeDiscounts.containsKey(branch);
    }
    private Boolean checkIfBranchExistSupplierDiscount(String branch){
        return supplierDiscount.containsKey(branch);
    }

    private Boolean checkIfSupplierExist(String branch, int supplierID){
        if(checkIfBranchExistSupplierDiscount(branch))
            return supplierDiscount.get(branch).containsKey(supplierID);
        return false;
    }
    private Boolean checkIfProductExist(String branch, String catalog_number){
        if(checkIfBranchExistStoreDiscount(branch))
            return storeDiscounts.get(branch).containsKey(catalog_number);
        return false;
    }

    public void createSupplierDiscount(String catalog_number, String branch,double discount, int supplierID, LocalDateTime startDate, LocalDateTime endDate){
        if(!checkIfBranchExistSupplierDiscount(branch))
            supplierDiscount.put(branch,new HashMap<Integer,List<ProductDiscountSupplier>>());
        if(!checkIfSupplierExist(branch,supplierID))
            supplierDiscount.get(branch).put(supplierID,new ArrayList<ProductDiscountSupplier>());
        supplierDiscount.get(branch).get(supplierID).add(new ProductDiscountSupplier(catalog_number,branch,startDate,endDate,discount,supplierID));
    }
    public void createStoreDiscount(String catalog_number, String branch,double discount, LocalDateTime startDate, LocalDateTime endDate){
        if(!checkIfBranchExistStoreDiscount(branch))
            storeDiscounts.put(branch, new HashMap<String,List<ProductStoreDiscount>>());
        if(!checkIfProductExist(branch,catalog_number))
            storeDiscounts.get(branch).put(catalog_number,new ArrayList<ProductStoreDiscount>());
        storeDiscounts.get(branch).get(catalog_number).add(new ProductStoreDiscount(catalog_number,branch,startDate,endDate,discount));
    }

    public void createCategoryDiscount(String categoryName, String branch,double discount, LocalDateTime startDate, LocalDateTime endDate){
        CategoryController categoryController = CategoryController.CategoryController();
        if(categoryController.checkIfCategoryExist(branch,categoryName)){
            Map<Integer, Product> relatedProducts = categoryController.getCategoryProducts(branch,categoryName);
            for(Product product: relatedProducts.values()){
               createStoreDiscount(product.getCatalogNumber(),branch,discount,startDate,endDate);
            }
        }
        else
            throw new RuntimeException("Category does not exist, please create category in order to continue");
    }
    public double getTodayBiggestStoreDiscountI(String catalog_number, String branch){
        if(checkIfBranchExistStoreDiscount(branch)){
            double maxDiscount = -1;
            for(ProductStoreDiscount PDS: storeDiscounts.get(branch).get(catalog_number)){
               double currentDiscount = PDS.getDiscount(LocalDateTime.now());
               if(currentDiscount > maxDiscount)
                   maxDiscount= currentDiscount;
            }
            return maxDiscount;
        }
        else
            throw new RuntimeException("Branch does not exist, please create discount in order to continue");
    }
    public double getTodaySupplierDiscountI(String catalog_number, String branch, int supplierID){
        if(checkIfSupplierExist(branch,supplierID)){
            List<ProductDiscountSupplier> supplierDiscountList = supplierDiscount.get(branch).get(supplierID);
            double maxDisxount =-1;
            for(ProductDiscountSupplier PDS: supplierDiscountList){
                double currentDiscount = PDS.getDiscountPerDate(LocalDateTime.now(),catalog_number);
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

    // need to edit
    // check if there is a discount today on the current product and calculate to final price
    public double calcSoldPrice(String branch, String catalog_number, double originalStorePrice){
        double currentDiscount = getTodayBiggestStoreDiscountI(catalog_number,branch);
        if(currentDiscount != -1)
            return (100-currentDiscount)*originalStorePrice;
        return originalStorePrice;
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

    public List<ProductDiscountSupplier> getSupplierDiscountPerDate(String catalog_number, String branch, int supplierID, LocalDateTime startDate, LocalDateTime endDate){
        if(checkIfSupplierExist(branch,supplierID)){
            List<ProductDiscountSupplier> supplierDiscountList = supplierDiscount.get(branch).get(supplierID);
            List<ProductDiscountSupplier> supplierDiscountResults = new ArrayList<ProductDiscountSupplier>();
            for(ProductDiscountSupplier PDS: supplierDiscountList){
                PDS.addDiscountSupplier(supplierDiscountResults,startDate,endDate,catalog_number);
            }
            if(!supplierDiscountResults.isEmpty())
                return supplierDiscountResults;
            else
                throw new RuntimeException(String.format("in range %s  %s there is not Discount form this supplierID: %s",startDate, endDate, supplierID));
        }
        else
            throw new RuntimeException("supplier or branch does not exist, please create discount in order to continue");

    }

    //TODO : remove
// Report Category function - inorder to receive all category product details
    public List<Record> getProductsPerCategory(List<Category> categories, String branch){
        if(checkIfBranchExistStoreDiscount(branch)){
            List<Record> productsCategoryRecords = new ArrayList<Record>();
            for(Category category: categories){
                for(Product productType: category.getProductsRelated().values()){
                    for(ProductItem product: productType.getProductItems().values()){
                        String catalog_number = productType.getCatalogNumber();
                        int productID = product.getSerial_number();
                        String name = productType.getName();
                        String manufacture = productType.getManufacturer();
                        double supplierPrice = productType.getOriginalSupplierPrice();
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
