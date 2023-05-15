package Backend.BusinessLayer.InventoryModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
    private  String catalog_number;
    private  String name;
    private String manufacturer;
    private double originalStorePrice;
    private Category category;
    private List<Category> subCategory;
    //default value -1
    private int notificationMin;
    private Map<String, ProductItem> productItems;
    private Branch branch;

    public Product(String catalog_number, String name, String manufacturer, double originalStorePrice, Branch branch){
        this.catalog_number = catalog_number;
        this.name = name;
        this.manufacturer = manufacturer;
        this.originalStorePrice = originalStorePrice;
        this.category = null;
        this.subCategory = new ArrayList<Category>();
        this.notificationMin = 5;
        // Map<productID, product>
        this.productItems = new HashMap<String, ProductItem>();
        this.branch = branch;
    }

    public ProductItem getProduct(String serial_number){
        if(productItems.containsKey(serial_number))
            return productItems.get(serial_number);
        else
            throw new RuntimeException(String.format("Product does not exist with the ID : %s",serial_number));
    }
    public void reportAsDefective(List<String> ItemsSerialNumber){
        for(String defectiveSerialNumber: ItemsSerialNumber){
            productItems.get(defectiveSerialNumber).reportAsDefective();
        }
    }
    public void reportAsDefective(String serialNumber){
        productItems.get(serialNumber).reportAsDefective();
    }

    public void setNotificationMin(int newVal){
        this.notificationMin = newVal;
    }

//    public void addProductItem(String serialNumber, String supplierID, double supplierPrice, double supplierDiscount,String location, LocalDateTime expirationDate){
//        productItems.put(serialNumber,new ProductItem(serialNumber,supplierID, supplierPrice, supplierDiscount, location, expirationDate, catalog_number, branch));
//    }

    public void addProductItem(ProductItem item){productItems.put(item.getSerial_number(), item);}

    public List<ProductItem> getDefectiveProductItems(){
        List<ProductItem> defectiveList = new ArrayList<ProductItem>();
        for(ProductItem product: productItems.values()){
            if(product.isDefective())
                defectiveList.add(product);
        }
        return defectiveList;
    }
    //TODO : verify we want to return all products (maybe not defective and sold)
    public Map<String,List<ProductItem>> getAllProductItems(Map<String,List<ProductItem>> allProducts){
        List<ProductItem> currentProducts = new ArrayList<ProductItem>();
        for(ProductItem product: productItems.values()){
            currentProducts.add(product);
        }
        allProducts.put(catalog_number, currentProducts);
        return allProducts;
    }

    public Map<String, ProductItem> getProductItems(){return this.productItems;}

    public Boolean isProductLack(){
        if(getStoreAmount()+getWarehouseAmount() < notificationMin)
            return true;
        return false;
    }

    //TODO: get supplier info from orderController and calc minNotification val
    private int productDemandAmount(){
        int calc = 0;
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime lastWeek = current.minusDays(1);
        for(ProductItem productItem: productItems.values()){
            if(productItem.isSold()){
                if(productItem.getSoldDate().isAfter(lastWeek))
                    calc++;
            }
        }
        return calc;
    }
    public void updateMin(int supplierDays){
        setNotificationMin((productDemandAmount()*supplierDays)+1);
    }

    public String getCatalogNumber(){return catalog_number;}

    public void setName(String newName){this.name = newName;}
    public void setManufacturer(String newManufacturer){this.manufacturer =newManufacturer;}
    public int getStoreAmount(){
        int amount =0;
        for(ProductItem productItem: productItems.values()){
            if(productItem.getLocation() == "store")
                amount++;
        }
        return amount;
    }
    public int getWarehouseAmount(){
        int amount =0;
        for(ProductItem productItem: productItems.values()){
            if(productItem.getLocation() == "warehouse")
                amount++;
        }
        return amount;
    }
    public void setOriginalStorePrice(double newPrice ){this.originalStorePrice = newPrice;}
    public double getOriginalStorePrice(){return this.originalStorePrice;}
    public String getName(){return this.name;}
    public String getManufacturer(){return this.manufacturer;}
    public Category getCategory(){return this.category;}
    public String getCatalogNum() {return this.catalog_number;}
    public List<Category> getSubCategory(){return this.subCategory;}
    public Branch getBranch() {return this.branch;}
    public int getNotificationMin(){return this.notificationMin;}
}
