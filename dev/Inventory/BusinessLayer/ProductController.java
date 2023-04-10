package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {

    Map<String, Map<Integer,ProductType>> productTypes;
    DiscountCategoryController DC_contoller;

    //create the controller as Singleton
    private static ProductController productController = null;
    private ProductController() {
        this.productTypes = new HashMap<String, Map<Integer,ProductType>>();
        this.DC_contoller = DiscountCategoryController.DiscountCategoryController();
    }

    public static ProductController ProductController(){
        if(productController == null)
            productController = new ProductController();
        return productController;
    }

    private Boolean checkIfProductTypeExist(String branch, int productTypeID){
        if(checkIfBranchExist(branch)){
            Map<Integer,ProductType> branchProductsType = productTypes.get(branch);
            if(branchProductsType.containsKey(productTypeID)){
                return true;
            }
            else {throw new RuntimeException("ProductType does not exist, please create the ProductType first");}
        }
        return false;
    }
    private Boolean checkIfBranchExist(String branch){
        if(productTypes.containsKey(branch)){
            return true;
        }
        else{
            throw new RuntimeException("brunch does not exist, please create ProductType in order to continue");
        }
    }
    // Add new product
    public void createProduct(int productID, int productTypeID, String branch, int supplierID, double supplierPrice, String location) {
        if(checkIfProductTypeExist(branch,productTypeID))
            productTypes.get(branch).get(productTypeID).addProduct(productID,supplierID,supplierPrice,location);
        else{
            throw new RuntimeException(String.format("Product type does not exist with the ID : %s",productTypeID));
        }
    }
    // Add new product type
    public void createProductType(int productTypeID, String branch, String name, String manufacture, int storeAmount, int warehouseAmount, double originalSupplierPrice, double originalStorePrice){
        if(!productTypes.containsKey(branch)){
            productTypes.put(branch, new HashMap<Integer,ProductType>());
        }
        ProductType newProductType = new ProductType(productTypeID,name,manufacture,storeAmount,warehouseAmount,originalSupplierPrice,originalStorePrice, branch);
        productTypes.get(branch).put(productTypeID,newProductType);
    }
    public void updateProduct(String branch, int isDefective,int productID, int productTypeID, int isSold,int newSupplier, double newSupplierPrice, double newSoldPrice, String newLocation) {
        if(checkIfProductTypeExist(branch,productID)){
            Product currentProduct = productTypes.get(branch).get(productTypeID).getProduct(productID);
            if(isDefective != -1){currentProduct.setIsDefective();}
            // TODO : send price
            if(isSold != -1){currentProduct.updateIsSold();}
            if(newSupplier != -1){currentProduct.setSupplierID(newSupplier);}
            if(newSupplierPrice != -1){currentProduct.setSupplierPrice(newSupplierPrice);}
            if(newSoldPrice != -1){currentProduct.setSoldPrice(newSoldPrice);}
            if(newLocation != null){currentProduct.setLocation(newLocation);}
        }
        else
            throw new RuntimeException(String.format("Product type does not exist with the ID : %s",productTypeID));
    }
    public void updateProductType(String branch, String newName, int productTypeID, String newManufacturer, double newSupplierPrice,double newStorePrice, String newCategory, String newSubCategory, int newMinAmount ){
        if(checkIfProductTypeExist(branch,productTypeID)){
            ProductType currentProductType = productTypes.get(branch).get(productTypeID);
            //set name
            if(newName != null){currentProductType.setName(newName);}
            if (newManufacturer != null){currentProductType.setManufacturer(newManufacturer);}
            if(newSupplierPrice != -1){currentProductType.setOriginalSupplierPrice(newSupplierPrice);}
            if(newStorePrice != -1){currentProductType.setOriginalStorePrice(newStorePrice);}
            if(newMinAmount != -1){currentProductType.setNotificationMin(newMinAmount);}
            if(newCategory != null){
                if(DC_contoller.checkIfCategoryExist(branch,newCategory))
                    currentProductType.setCategory(DC_contoller.getCategory(branch,newCategory));
                else
                    DC_contoller.createCategory(branch,newCategory,1);
            }
            if(newSubCategory != null){
                if(DC_contoller.checkIfCategoryExist(branch,newCategory))
                    currentProductType.setCategory(DC_contoller.getCategory(branch,newSubCategory));
                else
                    DC_contoller.createCategory(branch,newSubCategory,0);
            }
        }
        else
            throw new RuntimeException(String.format("Product type does not exist with the ID : %s",productTypeID));

    }


    // update products to defective
    public void updateDefectiveProduct(int productTypeID, List<Integer> productsID, String branch){
        if(checkIfProductTypeExist(branch,productTypeID)){
            ProductType productType = productTypes.get(branch).get(productTypeID);
            productType.setDefective(productsID);
        }
        else
            throw new RuntimeException(String.format("Unable to update defective products,\nproduct type does not exist with the ID : %s",productTypeID));
    }
    // update products status to sold
    public void updateSoldProduct(int productTypeID, List<Integer> productsID, String branch){
        if(checkIfProductTypeExist(branch,productTypeID)){
            ProductType productType = productTypes.get(branch).get(productTypeID);
            productType.setToSold(productsID);
        }
        else
            throw new RuntimeException(String.format("Unable to update sold products,\n product type does not exist with the ID : %s",productTypeID));
    }

    public void updateProductNotificationMin(int productTypeID, int newVal, String branch){
        if(checkIfProductTypeExist(branch,productTypeID)){
            ProductType productType = productTypes.get(branch).get(productTypeID);
            productType.setNotificationMin(newVal);
        }
        else
            throw new RuntimeException(String.format("Unable to update product Min notification,\n product type does not exist with the ID : %s",productTypeID));

    }
    public double getProductPrice(int productTypeID, String branch){
        //TODO: need to implement
        throw new RuntimeException();
    }
    public List<Product> getDefectiveProducts(LocalDateTime startDate, LocalDateTime endDate, String branch){
        List<Product> defectiveList = new ArrayList<Product>();
        if(checkIfBranchExist(branch)) {
            Map<Integer, ProductType> branchProductsType = productTypes.get(branch);
            for (ProductType productType: branchProductsType.values())
                productType.getDefectiveProducts(defectiveList);
        }
        return defectiveList;
    }

    //in chosen branch, check shortage in each productType if true add the ProductType ID and obj to the return Map.
    public Map<Integer,ProductType> getInventoryShortages(LocalDateTime startDate, LocalDateTime endDate, String branch){
        Map<Integer,ProductType> shortagesProducts = new HashMap<Integer,ProductType>();
        if(checkIfBranchExist(branch)){
            Map<Integer, ProductType> branchProductsType = productTypes.get(branch);
            for (ProductType productType: branchProductsType.values()){
                if(productType.productIsShortage())
                    shortagesProducts.put(productType.getProductTypeID(),productType);
            }
        }
        return shortagesProducts;
    }
    //in chosen branch, for each ProductType return all related products to the return Map
    public Map<Integer,List<Product>> getStockProductsDetails(String branch){
        Map<Integer,List<Product>> allProductsList = new HashMap<Integer,List<Product>>();
        if(checkIfBranchExist(branch)){
            Map<Integer, ProductType> branchProductsType = productTypes.get(branch);
            for (ProductType productType: branchProductsType.values()){
                productType.getAllProducts(allProductsList);
            }
        }
        return allProductsList;
    }


    //should remove to discount controller
    public void updateDiscount(int productTypeID, int discount, LocalDateTime startDate,LocalDateTime endDate, String branch){
        //TODO: need to implement
    }

}
