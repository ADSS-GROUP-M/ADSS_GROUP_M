package businessLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;
import dataAccessLayer.inventoryModule.DiscountManagerMapper;
import exceptions.DalException;
import exceptions.InventoryException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiscountController {

    private CategoryController categoryController;
    //Map<branch,Map<catalog_number, List<ProductStoreDiscount>>>
    public Map<Branch, Map<String,List<ProductStoreDiscount>>> storeDiscounts;
    private final DiscountManagerMapper discountManagerMapper;

    public DiscountController(DiscountManagerMapper discountManagerMapper) {
        this.discountManagerMapper = discountManagerMapper;
        this.storeDiscounts = this.discountManagerMapper.getCached_discounts();
    }

    public void injectDependencies(CategoryController categoryController){
        this.categoryController = categoryController;
    }

    private Boolean checkIfBranchExist(Branch branch){
        return storeDiscounts.containsKey(branch.name());
    }

    private Boolean checkIfProductExist(Branch branch, String catalog_number){
        if(checkIfBranchExist(branch)) {
            return storeDiscounts.get(branch.name()).containsKey(catalog_number);
        }
        return false;
    }

    public void createStoreDiscount(String catalog_number, Branch branch, double discount, LocalDateTime startDate, LocalDateTime endDate) throws InventoryException {
        try {
            discountManagerMapper.createDiscount(catalog_number, branch.name(), startDate, endDate, discount);
        } catch (DalException e) {
            throw new InventoryException(e.getMessage());
        }
    }

    public void createCategoryDiscount(String categoryName, Branch branch, double discount, LocalDateTime startDate, LocalDateTime endDate) throws InventoryException {
        try{
            if (categoryController.checkIfCategoryExist(categoryName)) {
                List<Product> relatedProducts = categoryController.getCategoryProducts(branch, categoryName);
                for (Product product : relatedProducts) {
                    discountManagerMapper.createDiscount(product.getCatalogNumber(), branch.name(), startDate, endDate, discount);
                    createStoreDiscount(product.getCatalogNumber(), branch, discount, startDate, endDate);
                }
            } else {
                throw new InventoryException("Category does not exist, please create category in order to continue");
            }
        } catch (DalException e){
            throw new InventoryException(e.getMessage());
        }
    }

    public double getTodayBiggestStoreDiscount(String catalog_number, Branch branch){
        if(checkIfBranchExist(branch)){
            double maxDiscount = 0;
            for(ProductStoreDiscount PDS: storeDiscounts.get(branch).get(catalog_number)){
               double currentDiscount = PDS.getDiscount(LocalDateTime.now());
               if(currentDiscount > maxDiscount) {
                   maxDiscount= currentDiscount;
               }
            }
            return maxDiscount;
        }
        else {
            return 0;
        }
    }

    // check if there is a discount today on the current product and calculate to final price
    public double calcSoldPrice(Branch branch, String catalog_number, double originalStorePrice){
        if(checkIfBranchExist(branch)){
            double currentDiscount = getTodayBiggestStoreDiscount(catalog_number, branch);
            if (currentDiscount > 0) {
                return (100 - currentDiscount) * originalStorePrice;
            }
        }
        return originalStorePrice;
    }
    public List<ProductStoreDiscount> getStoreDiscountPerDate(String catalog_number, Branch branch, LocalDateTime startDate, LocalDateTime endDate) throws InventoryException {
        if(checkIfBranchExist(branch)){
            List<ProductStoreDiscount> discountList = new ArrayList<ProductStoreDiscount>();
            for(ProductStoreDiscount PSD: storeDiscounts.get(branch).get(catalog_number)){
                discountList = PSD.addDiscountStore(discountList,startDate,endDate);
            }
            if(!discountList.isEmpty()) {
                return discountList;
            } else {
                throw new InventoryException(String.format("There is no discount for this product type catalog number: %s", catalog_number));
            }
        }
        else {
            throw new InventoryException("Branch does not exist, please create discount in order to continue");
        }
    }

}
