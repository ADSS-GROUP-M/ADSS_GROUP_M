package Backend.BusinessLayer.InventoryModule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountController {

    //Map<branch,Map<catalog_number, List<ProductStoreDiscount>>>
    public Map<String, Map<String,List<ProductStoreDiscount>>> storeDiscounts;


    //create the controller as Singleton
    private static DiscountController discountController = null;
    private DiscountController() {
        this.storeDiscounts = new HashMap<String, Map<String,List<ProductStoreDiscount>>>();}
    public static DiscountController DiscountController(){
        if(discountController == null)
            discountController = new DiscountController();
        return discountController;
    }

    private Boolean checkIfBranchExist(String branch){
        return storeDiscounts.containsKey(branch);
    }


    private Boolean checkIfProductExist(String branch, String catalog_number){
        if(checkIfBranchExist(branch))
            return storeDiscounts.get(branch).containsKey(catalog_number);
        return false;
    }

    public void createStoreDiscount(String catalog_number, String branch,double discount, LocalDateTime startDate, LocalDateTime endDate){
        if(!checkIfBranchExist(branch))
            storeDiscounts.put(branch, new HashMap<String,List<ProductStoreDiscount>>());
        if(!checkIfProductExist(branch,catalog_number))
            storeDiscounts.get(branch).put(catalog_number,new ArrayList<ProductStoreDiscount>());
        storeDiscounts.get(branch).get(catalog_number).add(new ProductStoreDiscount(catalog_number,branch,startDate,endDate,discount));
    }

    //TODO: need to edit
    public void createCategoryDiscount(String categoryName, String branch,double discount, LocalDateTime startDate, LocalDateTime endDate){
        CategoryController categoryController = CategoryController.CategoryController();
        if(categoryController.checkIfCategoryExist(categoryName)){
            Map<String, Product> relatedProducts = categoryController.getCategoryProducts(categoryName);
            for(Product product: relatedProducts.values()){
               createStoreDiscount(product.getCatalogNumber(),branch,discount,startDate,endDate);
            }
        }
        else
            throw new RuntimeException("Category does not exist, please create category in order to continue");
    }
    //TODO : need to edit and verify
    public double getTodayBiggestStoreDiscount(String catalog_number, String branch){
        if(checkIfBranchExist(branch)){
            double maxDiscount = 0;
            for(ProductStoreDiscount PDS: storeDiscounts.get(branch).get(catalog_number)){
               double currentDiscount = PDS.getDiscount(LocalDateTime.now());
               if(currentDiscount > maxDiscount)
                   maxDiscount= currentDiscount;
            }
            return maxDiscount;
        }
        else
            return 0;
    }

    // check if there is a discount today on the current product and calculate to final price
    public double calcSoldPrice(String branch, String catalog_number, double originalStorePrice){
        if(checkIfBranchExist(branch)){
            double currentDiscount = getTodayBiggestStoreDiscount(catalog_number, branch);
            if (currentDiscount > 0)
                return (100 - currentDiscount) * originalStorePrice;
        }
        return originalStorePrice;
    }
    public List<ProductStoreDiscount> getStoreDiscountPerDate(String catalog_number, String branch, LocalDateTime startDate, LocalDateTime endDate){
        if(checkIfBranchExist(branch)){
            List<ProductStoreDiscount> discountList = new ArrayList<ProductStoreDiscount>();
            for(ProductStoreDiscount PSD: storeDiscounts.get(branch).get(catalog_number)){
                discountList = PSD.addDiscountStore(discountList,startDate,endDate);
            }
            if(!discountList.isEmpty())
                return discountList;
            else
                throw new RuntimeException(String.format("There is no discount for this product type catalog number: %s", catalog_number));
        }
        else
            throw new RuntimeException("Branch does not exist, please create discount in order to continue");
    }


    //TODO : remove
// Report Category function - inorder to receive all category product details
    public List<Record> getProductsPerCategory(List<Category> categories, String branch){
        if(checkIfBranchExist(branch)){
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
                        double storePrice = calcSoldPrice(branch,catalog_number,product.getOriginalStorePrice());
                        List<Category> subCategory = product.getSubCategory();
                        String location = productItem.getLocation();
                        //create Record
                        Record record = new Record(catalog_number,serial_number,name,branch,manufacture,supplierPrice,supplierDiscount,storePrice, location);
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
