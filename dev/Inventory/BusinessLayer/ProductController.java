package dev.Inventory.BusinessLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {

    Map<String, Map<Integer,ProductType>> productTypes;
    DiscountCategoryController DCContoller;

    //create the controller as Singleton
    private static ProductController productController = null;
    private ProductController() {
        this.productTypes = new HashMap<String, Map<Integer,ProductType>>();
        this.DCContoller = DiscountCategoryController.DiscountCategoryController();
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
            if(isSold != -1){
                currentProduct.updateIsSold(DCContoller.getTodayBiggestStoreDiscountI(productTypeID,branch));
            }
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
                if(DCContoller.checkIfCategoryExist(branch,newCategory))
                    currentProductType.setCategory(DCContoller.getCategory(branch,newCategory));
                else
                    DCContoller.createCategory(branch,newCategory,1);
            }
            if(newSubCategory != null){
                if(DCContoller.checkIfCategoryExist(branch,newCategory))
                    currentProductType.setCategory(DCContoller.getCategory(branch,newSubCategory));
                else
                    DCContoller.createCategory(branch,newSubCategory,0);
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
            productType.setToSold(productsID, DCContoller.calcSoldPrice(branch,productTypeID,productType.getOriginalStorePrice()));
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
    //Report shortages products - inorder to receive all shortages product details
    public List<Record> getInventoryShortages(String branch){
        List<Record> shortagesProductsRecord = new ArrayList<Record>();
        if(checkIfBranchExist(branch)){
            Map<Integer, ProductType> branchProductsType = productTypes.get(branch);
            for (ProductType productType: branchProductsType.values()){
                if(productType.productIsShortage()){
                    for(Product product: productType.getProducts().values()){
                        int productTypeID = productType.getProductTypeID();
                        int productID = product.getProductID();
                        String name = productType.getName();
                        String manufacture = productType.getManufacturer();
                        double supplierPrice = productType.getOriginalSupplierPrice();
                        double storePrice = DCContoller.calcSoldPrice(branch,productTypeID,productType.getOriginalStorePrice());
                        Category category = productType.getCategory();
                        List<Category> subCategory = productType.getSubCategory();
                        String location = product.getLocation();
                        //create Record
                        Record record = new Record(productTypeID,productID,name,branch,manufacture,supplierPrice,storePrice,category,subCategory,location);
                        shortagesProductsRecord.add(record);
                    }
                }

            }
        }
        return shortagesProductsRecord;
    }

    // Report defective function - inorder to receive all defective product details
    public List<Record> getDefectiveProducts(String branch){
        List<Record> defectiveRecords = new ArrayList<Record>();
        if(checkIfBranchExist(branch)) {
            Map<Integer, ProductType> branchProductsType = productTypes.get(branch);
            for (ProductType productType: branchProductsType.values())
                for(Product product : productType.getDefectiveProducts()){
                    int productTypeID = productType.getProductTypeID();
                    int productID = product.getProductID();
                    String name = productType.getName();
                    String manufacture = productType.getManufacturer();
                    double supplierPrice = productType.getOriginalSupplierPrice();
                    double storePrice = DCContoller.calcSoldPrice(branch,productTypeID,productType.getOriginalStorePrice());
                    Category category = productType.getCategory();
                    List<Category> subCategory = productType.getSubCategory();
                    String location = product.getLocation();
                    //create Record
                    Record record = new Record(productTypeID,productID,name,branch,manufacture,supplierPrice,storePrice,category,subCategory,location);
                    defectiveRecords.add(record);
                }
        }
        return defectiveRecords;
    }

}
