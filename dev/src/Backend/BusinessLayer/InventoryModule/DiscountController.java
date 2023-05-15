package Backend.BusinessLayer.InventoryModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.DataAccessLayer.InventoryModule.StoreProductDiscountDataMapper;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountController {

    //Map<branch,Map<catalog_number, List<ProductStoreDiscount>>>
    public Map<Branch, Map<String,List<ProductStoreDiscount>>> storeDiscounts;

    //create the controller as Singleton
    private static DiscountController discountController = null;
    private StoreProductDiscountDataMapper storeProductDiscountDataMapper;
    private DiscountController() {
        storeProductDiscountDataMapper = StoreProductDiscountDataMapper.getInstance();
        this.storeDiscounts = new HashMap<Branch, Map<String,List<ProductStoreDiscount>>>();}

    public static DiscountController DiscountController(){
        if(discountController == null)
            discountController = new DiscountController();
        return discountController;
    }

    private Boolean checkIfBranchExist(Branch branch){
        return storeDiscounts.containsKey(branch.name());
    }

    private Boolean checkIfProductExist(Branch branch, String catalog_number){
        if(checkIfBranchExist(branch))
            return storeDiscounts.get(branch.name()).containsKey(catalog_number);
        return false;
    }

    public void createStoreDiscount(String catalog_number, Branch branch, double discount, LocalDateTime startDate, LocalDateTime endDate){
        try {
            if (!checkIfBranchExist(branch))
                storeDiscounts.put(branch, new HashMap<String, List<ProductStoreDiscount>>());
            if (!checkIfProductExist(branch, catalog_number))
                storeDiscounts.get(branch).put(catalog_number, new ArrayList<ProductStoreDiscount>());
            storeProductDiscountDataMapper.insert(catalog_number, startDate.toString(), endDate.toString(), discount, branch.name());
            storeDiscounts.get(branch).get(catalog_number).add(new ProductStoreDiscount(catalog_number, branch, startDate, endDate, discount));
        }
        catch (SQLException e){
            //TODO: Handle the exception appropriately
        }
    }

    //TODO: need to edit
    public void createCategoryDiscount(String categoryName, Branch branch, double discount, LocalDateTime startDate, LocalDateTime endDate){
        try {
            CategoryController categoryController = CategoryController.CategoryController();
            if (categoryController.checkIfCategoryExist(categoryName)) {
                List<Product> relatedProducts = categoryController.getCategoryProducts(branch, categoryName);
                for (Product product : relatedProducts) {
                    storeProductDiscountDataMapper.insert(product.getCatalogNumber(), startDate.toString(), endDate.toString(), discount, branch.name());
                    createStoreDiscount(product.getCatalogNumber(), branch, discount, startDate, endDate);
                }
            } else
                throw new RuntimeException("Category does not exist, please create category in order to continue");
        }
        catch (SQLException e){
            //TODO: Handle the exception appropriately
        }
    }

    public double getTodayBiggestStoreDiscount(String catalog_number, Branch branch){
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
    public double calcSoldPrice(Branch branch, String catalog_number, double originalStorePrice){
        if(checkIfBranchExist(branch)){
            double currentDiscount = getTodayBiggestStoreDiscount(catalog_number, branch);
            if (currentDiscount > 0)
                return (100 - currentDiscount) * originalStorePrice;
        }
        return originalStorePrice;
    }
    public List<ProductStoreDiscount> getStoreDiscountPerDate(String catalog_number, Branch branch, LocalDateTime startDate, LocalDateTime endDate){
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

}
