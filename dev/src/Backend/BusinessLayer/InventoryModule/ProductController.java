package Backend.BusinessLayer.InventoryModule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {

    Map<String, Map<String, Product>> products; // <branch, <productTYPEID, ProductType>
    DiscountController DCController;

    //create the controller as Singleton
    private static ProductController productController = null;
    private ProductController() {
        //Map<branch, Map<catalog_number, Product>
        this.products = new HashMap<String, Map<String, Product>>();
        this.DCController = DiscountController.DiscountController();
    }

    public static ProductController ProductController(){
        if(productController == null)
            productController = new ProductController();
        return productController;
    }

    private Boolean checkIfProductExist(String branch, String catalog_number){
        if(checkIfBranchExist(branch)){
            Map<String, Product> branchProductsType = products.get(branch);
            if(branchProductsType.containsKey(catalog_number)){
                return true;
            }
            else {throw new RuntimeException("ProductType does not exist, please create the ProductType first");}
        }
        return false;
    }
    private Boolean checkIfBranchExist(String branch){
        if(products.containsKey(branch)){
            return true;
        }
        else{
            throw new RuntimeException("brunch does not exist, please create ProductType in order to continue");
        }
    }
    // Add new product
    public void createProductItem(String serial_number, String catalog_number, String branch, String supplierID, String location, LocalDateTime expirationDate) {
        if(checkIfProductExist(branch,catalog_number))
            products.get(branch).get(catalog_number).addProductItem(serial_number,supplierID,location, expirationDate);
        else{
            throw new RuntimeException(String.format("Product type does not exist with the ID : %s",catalog_number));
        }
    }

    // Add new product type
    public void createProduct(String catalog_number, String branch, String name, String manufacture, double originalStorePrice){
        if(!products.containsKey(branch)){
            products.put(branch, new HashMap<String, Product>());
        }
        Product newProductType = new Product(catalog_number,name,manufacture,originalStorePrice, branch);
        products.get(branch).put(catalog_number,newProductType);
    }

    public void updateProductItem(String branch, int isDefective, String serial_number, String catalog_number, int isSold, String newSupplier, double newSoldPrice, String newLocation) {
        if(checkIfProductExist(branch,catalog_number)){
            ProductItem productItem = products.get(branch).get(catalog_number).getProduct(serial_number);
            Product product = products.get(branch).get(catalog_number);
            if(isDefective != -1){productItem.reportAsDefective();}
            if(isSold != -1){
                productItem.reportAsSold(DCController.getTodayBiggestStoreDiscount(catalog_number,branch));
            }
            if(newSupplier != null){productItem.setSupplierID(newSupplier);}
            if(newSoldPrice != -1){productItem.setSoldPrice(newSoldPrice);}
            if(newLocation != null){productItem.setLocation(newLocation);}
        }
        else
            throw new RuntimeException(String.format("Product type does not exist with the ID : %s",catalog_number));
    }
    //TODO: need to update
    public void updateProduct(String branch, String newName, String catalog_number, String newManufacturer, double newStorePrice, int newMinAmount ){
        CategoryController categoryController = CategoryController.CategoryController();
        if(checkIfProductExist(branch,catalog_number)){
            Product product = products.get(branch).get(catalog_number);
            //set name
            if(newName != null){product.setName(newName);}
            if(newManufacturer != null){product.setManufacturer(newManufacturer);}
            if(newStorePrice != -1){product.setOriginalStorePrice(newStorePrice);}
            if(newMinAmount != -1){product.setNotificationMin(newMinAmount);}
        }
        else
            throw new RuntimeException(String.format("Product type does not exist with the ID : %s",catalog_number));

    }

    // update products to defective
    public void reportProductAsDefective(String catalog_number, List<String> serialNumbers, String branch){
        if(checkIfProductExist(branch,catalog_number)){
            Product productType = products.get(branch).get(catalog_number);
            productType.reportAsDefective(serialNumbers);
        }
        else
            throw new RuntimeException(String.format("Unable to update defective products,\nproduct type does not exist with the ID : %s",catalog_number));
    }


    // update products status to sold
    public void reportProductAsSold(String catalog_number, List<Integer> serialNumbers, String branch){
        if(checkIfProductExist(branch,catalog_number)){
            Product product = products.get(branch).get(catalog_number);
            product.reportAsSold(serialNumbers, DCController.calcSoldPrice(branch,catalog_number,product.getOriginalStorePrice()));
        }
        else
            throw new RuntimeException(String.format("Unable to update sold products,\n product type does not exist with the ID : %s",catalog_number));
    }


    //in chosen branch, for each ProductType return all related products to the return Map
    public Map<String,List<ProductItem>> getStockProductsDetails(String branch){
        Map<String,List<ProductItem>> allProductsList = new HashMap<String,List<ProductItem>>();
        if(checkIfBranchExist(branch)){
            for (Product productType: products.get(branch).values()){
                productType.getAllProductItems(allProductsList);
            }
        }
        return allProductsList;
    }

    public Record getProductDetails(String branch, String catalog_number, String serial_number){
        if(checkIfProductExist(branch,catalog_number)){
            Product product = products.get(branch).get(catalog_number);
            ProductItem productItem = product.getProductItems().get(serial_number);
            String name = product.getName();
            String manufacture = product.getManufacturer();
            double supplierPrice = productItem.getSupplierPrice();
            double supplierDiscount = productItem.getSupplierDiscount();
            // TODO : verify that working
            double storePrice = DCController.calcSoldPrice(branch,catalog_number,product.getOriginalStorePrice());
            Category category = product.getCategory();
            List<Category> subCategory = product.getSubCategory();
            String location = productItem.getLocation();
            //create Record
            Record record = new Record(catalog_number,serial_number,name,branch,manufacture,supplierPrice,supplierDiscount,storePrice,category,subCategory,location);
            return record;
        }
        return null;
    }

    //Report shortages products - inorder to receive all shortages product details
    public List<Record> getInventoryShortages(String branch){
        List<Record> shortagesProductsRecord = new ArrayList<Record>();
        if(checkIfBranchExist(branch)){
            Map<String, Product> branchCatalogNumber = products.get(branch);
            for (Product product: branchCatalogNumber.values()){
                if(product.isProductLack()){
                    for(ProductItem productItem: product.getProductItems().values()){
                        String productCatalogNumber = product.getCatalogNumber();
                        String serial_number = productItem.getSerial_number();
                        String name = product.getName();
                        String manufacture = product.getManufacturer();
                        double supplierPrice = productItem.getSupplierPrice();
                        double supplierDiscount = productItem.getSupplierDiscount();
                        double storePrice = DCController.calcSoldPrice(branch,productCatalogNumber,product.getOriginalStorePrice());
                        Category category = product.getCategory();
                        List<Category> subCategory = product.getSubCategory();
                        String location = productItem.getLocation();
                        //create Record
                        Record record = new Record(productCatalogNumber,serial_number,name,branch,manufacture,supplierPrice,supplierDiscount,storePrice,category,subCategory,location);
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
            Map<String, Product> branchCatalogNumber = products.get(branch);
            for (Product product: branchCatalogNumber.values())
                for(ProductItem productItem : product.getDefectiveProductItems()){
                    String productCatalogNumber = product.getCatalogNumber();
                    String serial_number = productItem.getSerial_number();
                    String name = product.getName();
                    String manufacture = product.getManufacturer();
                    double supplierPrice = productItem.getSupplierPrice();
                    double supplierDiscount = productItem.getSupplierDiscount();
                    double storePrice = product.getOriginalStorePrice();
                    Category category = product.getCategory();
                    List<Category> subCategory = product.getSubCategory();
                    String location = productItem.getLocation();
                    //create Record
                    Record record = new Record(productCatalogNumber,serial_number,name,branch,manufacture,supplierPrice,storePrice,supplierDiscount,category,subCategory,location);
                    defectiveRecords.add(record);
                }
        }
        return defectiveRecords;
    }

}
