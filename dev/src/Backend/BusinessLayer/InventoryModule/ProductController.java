package Backend.BusinessLayer.InventoryModule;


import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.SuppliersModule.OrderController;
import Backend.DataAccessLayer.InventoryModule.ProductDAO;
import Backend.DataAccessLayer.InventoryModule.ProductManagerMapper;
import Backend.DataAccessLayer.SuppliersModule.ProductsDataMapper;
import Backend.DataAccessLayer.SuppliersModule.ProductsDiscountsDataMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {

    Map<Branch, Map<String, Product>> products; // <branch, <catalog_number, Product>
    Map<Branch, List<String>> orders; // <branch, <catalog_number>
    DiscountController DCController;
    ProductManagerMapper productManagerMapper;

    //create the controller as Singleton
    private static ProductController productController = null;
    private ProductController() {
        productManagerMapper = ProductManagerMapper.getInstance();
        products = productManagerMapper.getCachedProducts();
        this.orders = new HashMap<Branch, List<String>>();
        this.DCController = DiscountController.DiscountController();
    }

    public static ProductController ProductController(){
        if(productController == null)
            productController = new ProductController();
        return productController;
    }

    private Boolean checkIfProductExist(Branch branch, String catalog_number){
        if(checkIfBranchExist(branch)){
            Map<String, Product> branchProductsType = products.get(branch);
            if(branchProductsType.containsKey(catalog_number)){
                return true;
            }
            else {throw new RuntimeException("Product does not exist, please create the product first");}
        }
        return false;
    }

    public Product getProduct(Branch branch, String catalog_number){
        if(checkIfProductExist(branch,catalog_number))
            return products.get(branch).get(catalog_number);
        else
            {throw new RuntimeException("Product does not exist, please create the product first");}
    }

    private Boolean checkIfBranchExist(Branch branch){
        if(products.containsKey(branch)){
            return true;
        }
        else{
            throw new RuntimeException("brunch does not exist, please create product in order to continue");
        }
    }

    //check if the amount of product in more than the minimum
    public boolean isProductLack(Branch branch,String catalog_number){
        boolean isProductLack = products.get(branch).get(catalog_number).isProductLack();
        if(isProductLack) {
            // if there is no waiting order, create new supplier order.
            if(!orders.get(branch).contains(catalog_number)) {
                int inventory_amount = productController.getMinNotification(branch,catalog_number);
                OrderController orderController = new OrderController();
                HashMap<String,Integer> order = new HashMap<>();
                order.put(catalog_number,inventory_amount);
                orderController.order(order, branch);
                orders.get(branch).add(catalog_number);
            }
        }
        return isProductLack;
    }


    // Add new product item
    public void createProductItem(List<String> serialNumbers, String catalog_number, Branch branch, String supplierID, double supplierPrice, double supplierDiscount,String location, LocalDateTime expirationDate, String periodicSupplier) {
        Product product = null;
        if(checkIfProductExist(branch,catalog_number)) {
            for (String serial_number : serialNumbers) {
                product = products.get(branch).get(catalog_number);
                productManagerMapper.createProductItem(serial_number, 0, "", supplierID, supplierPrice, supplierDiscount, 0, expirationDate, location, catalog_number, branch.name(), product.getName(), product.getManufacturer(), product.getOriginalStorePrice());
//                products.get(branch).get(catalog_number).addProductItem(serial_number, supplierID, supplierPrice, supplierDiscount, location, expirationDate);
            }
            //remove supplier order document from critical orders
            if(periodicSupplier == "n" && orders.get(branch).contains(catalog_number))
                orders.get(branch).remove(catalog_number);
        }
        else{
            throw new RuntimeException(String.format("Product does not exist with the catalog number : %s",catalog_number));
        }
    }

    public int getMinNotification(Branch branch, String catalog_number){return products.get(branch).get(catalog_number).getNotificationMin();}

    public void updateMinAmount(Branch branch, String catalog_number, int supplierDays){
        if(checkIfProductExist(branch,catalog_number))
            products.get(branch).get(catalog_number).updateMin(supplierDays);
        else{
            throw new RuntimeException(String.format("Product does not exist with the catalog number : %s",catalog_number));
        }

    }

    // Add new product
    public void createProduct(String catalog_number, Branch branch, String name, String manufacture, double originalStorePrice){
        if(!products.containsKey(branch)){
            products.put(branch, new HashMap<String, Product>());
            orders.put(branch, new ArrayList<String>());
        }
        productManagerMapper.createProduct(catalog_number,branch.name(),name,manufacture,originalStorePrice);
//        Product newProductType = new Product(catalog_number,name,manufacture,originalStorePrice, branch);
//        products.get(branch).put(catalog_number,newProductType);
    }

    public void updateProductItem(Branch branch, int isDefective, String serial_number, String catalog_number, int isSold, String newSupplier, double newSupplierPrice, double newSupplierDiscount,double newSoldPrice, String newLocation) {
        if(checkIfProductExist(branch,catalog_number)){
            ProductItem productItem = products.get(branch).get(catalog_number).getProduct(serial_number);
            Product product = products.get(branch).get(catalog_number);
            if(isDefective != -1){
                productManagerMapper.updateProductItem(catalog_number,branch.name(),serial_number,isDefective,LocalDateTime.now(),null,-1,-1,-1,null,null);
                productItem.reportAsDefective();
            }
            if(isSold != -1){
                double sold_price = DCController.calcSoldPrice(branch,catalog_number,product.getOriginalStorePrice());
                productManagerMapper.updateProductItem(catalog_number,branch.name(),serial_number,-1,null,null,-1,-1,sold_price,null,null);
                productItem.reportAsSold(sold_price);
                OrderController orderController = new OrderController();
                updateMinAmount(branch,catalog_number,orderController.getDaysForOrder(catalog_number,branch));
                productManagerMapper.updateProduct(catalog_number,branch.name(),null,null,-1,product.getNotificationMin());
            }
            if(newSupplier != null){
                productManagerMapper.updateProductItem(catalog_number,branch.name(),serial_number,-1,null,newSupplier,-1,-1,-1,null,null);
                productItem.setSupplierID(newSupplier);
            }
            if(newSupplierPrice != -1){
                productManagerMapper.updateProductItem(catalog_number,branch.name(),serial_number,-1,null,null,newSupplierPrice,-1,-1,null,null);
                productItem.setSupplierPrice(newSupplierPrice);}
            if(newSupplierDiscount != -1){
                productManagerMapper.updateProductItem(catalog_number,branch.name(),serial_number,-1,null,null,-1,newSupplierDiscount,-1,null,null);
                productItem.setSupplierDiscount(newSupplierDiscount);}
            if(newSoldPrice != -1){
                productManagerMapper.updateProductItem(catalog_number,branch.name(),serial_number,-1,null,null,-1,-1,newSoldPrice,null,null);
                productItem.setSoldPrice(newSoldPrice);}
            if(newLocation != null){
                productManagerMapper.updateProductItem(catalog_number,branch.name(),serial_number,-1,null,null,-1,-1,-1,null,newLocation);
                productItem.setLocation(newLocation);}
        }
        else
            throw new RuntimeException(String.format("Product does not exist with the catalog_number : %s",catalog_number));
    }

    public void updateProduct(Branch branch, String newName, String catalog_number, String newManufacturer, double newStorePrice, int newMinAmount ){
        CategoryController categoryController = CategoryController.CategoryController();
        if(checkIfProductExist(branch,catalog_number)){
            Product product = products.get(branch).get(catalog_number);
            if(newName != null){
                productManagerMapper.updateProduct(catalog_number,branch.name(),newName,product.getManufacturer(),product.getOriginalStorePrice(),-1);
                product.setName(newName);
            }
            if(newManufacturer != null){product.setManufacturer(newManufacturer);}
            if(newStorePrice != -1){product.setOriginalStorePrice(newStorePrice);}
            if(newMinAmount != -1){product.setNotificationMin(newMinAmount);}
        }
        else
            throw new RuntimeException(String.format("Product does not exist with the catalog_number : %s",catalog_number));
    }

    //in chosen branch, for each ProductType return all related products to the return Map
    public Map<String,List<ProductItem>> getStockProductsDetails(Branch branch){
        Map<String,List<ProductItem>> allProductsList = new HashMap<String,List<ProductItem>>();
        if(checkIfBranchExist(branch)){
            for (Product productType: products.get(branch).values()){
                productType.getAllProductItems(allProductsList);
            }
        }
        return allProductsList;
    }

    public Record getProductDetails(Branch branch, String catalog_number, String serial_number){
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
            Record record = new Record(catalog_number,serial_number,name,branch.name(),manufacture,supplierPrice,supplierDiscount,storePrice, location);
            return record;
        }
        return null;
    }

    //Report shortages products - inorder to receive all shortages product details
    public List<Record> getInventoryShortages(Branch branch){
        List<Record> shortagesProductsRecord = new ArrayList<Record>();
        if(checkIfBranchExist(branch)){
            Map<String, Product> branchCatalogNumber = products.get(branch);
            for (Product product: branchCatalogNumber.values()){
                if(product.isProductLack()){
                    String catalog_number = product.getCatalogNumber();
                    String name = product.getName();
                    String manufacture = product.getManufacturer();
                    double storePrice = DCController.calcSoldPrice(branch,catalog_number,product.getOriginalStorePrice());
                    int warehouse_amount = product.getWarehouseAmount();
                    int store_amount = product.getStoreAmount();
                    Record record = new Record(catalog_number,name,branch.name(),manufacture,storePrice,warehouse_amount,store_amount);
                    shortagesProductsRecord.add(record);

                }

            }
        }
        return shortagesProductsRecord;
    }

    // Report defective function - inorder to receive all defective product details
    public List<Record> getDefectiveProducts(Branch branch){
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
                    String location = productItem.getLocation();
                    //create Record
                    Record record = new Record(productCatalogNumber,serial_number,name,branch.name(),manufacture,supplierPrice,storePrice,supplierDiscount, location);
                    defectiveRecords.add(record);
                }
        }
        return defectiveRecords;
    }

}
