package Backend.DataAccessLayer.InventoryModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.InventoryModule.Product;
import Backend.BusinessLayer.InventoryModule.ProductItem;
import Backend.DataAccessLayer.SuppliersModule.ProductsDataMapper;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductManagerMapper {
    private Map<Branch, Map<String, Product>> cachedProducts; //Map<Branch, Map<catalog_number, Product>
    private Map<String, List<ProductItem>> cachedItems;
    private final ProductsDataMapper productsDataMapper;
    private final ProductItemDataMapper productItemDataMapper;
    private final ProductPairBranchDataMapper productPairBranchDataMapper;
    private static ProductManagerMapper instance = null;

    private ProductManagerMapper(){
        productsDataMapper = ProductsDataMapper.getInstance();
        productItemDataMapper = ProductItemDataMapper.getInstance();
        productPairBranchDataMapper = ProductPairBranchDataMapper.getInstance();
        try {
            cachedItems = productItemDataMapper.initializeCache();
            cachedProducts = new HashMap<>();
            List<ProductPairBranchDAO> cachedProductsPairBranch = productPairBranchDataMapper.initializeCache();
            for (ProductDAO dao : productsDataMapper.initializeCache()) {
                Product product = null;
                for (ProductPairBranchDAO pair : cachedProductsPairBranch) {
                    if (dao.getCatalog_number().equals(pair.catalog_number)) {
                        product = new Product(dao.getCatalog_number(), dao.getName(), dao.getManufacture(),
                                pair.original_store_price, Branch.valueOf(pair.branch_name));
                        break;
                    }
                }
                if (product == null) {
                    product = new Product(dao.getCatalog_number(), dao.getName(), dao.getManufacture(), -1, null);
                }
                cachedProducts.computeIfAbsent(product.getBranch(), k -> new HashMap<>())
                        .put(product.getCatalogNumber(), product);
            }
            for (List<ProductItem> itemList : cachedItems.values()) {
                // Iterate over each ProductItem in the current list
                for (ProductItem item : itemList) {
                    String catalogNumber = item.getCatalog_number();
                    Branch branch = item.getBranch();

                    // Check if the branch exists in cachedProducts
                    if (cachedProducts.containsKey(branch)) {
                        if (cachedProducts.get(branch).containsKey(catalogNumber)) {
                            // Put the ProductItem in cachedProducts with the same catalog number and branch
                            cachedProducts.get(branch).get(catalogNumber).addProductItem(item);
                        }

                    }
                }
            }
        } catch (SQLException e) {
            //TODO: Handle the exception appropriately
        }
    }

    public static ProductManagerMapper getInstance(){
        if(instance == null){
            return new ProductManagerMapper();
        } else {
            return instance;
        }
    }

    public void createProduct(String catalog_number, String branch, String name, String manufacture, double originalStorePrice){
        try {
            if(!isProductExists(catalog_number)){
                Product product = new Product(catalog_number, name, manufacture, originalStorePrice, Branch.valueOf(branch));
                productsDataMapper.insert(catalog_number, name, manufacture);
                productPairBranchDataMapper.insert(branch, catalog_number, originalStorePrice, product.getNotificationMin());
                cachedProducts.computeIfAbsent(product.getBranch(), k -> new HashMap<>())
                        .put(product.getCatalogNumber(), product);
            } else {
                Product product = new Product(catalog_number, name, manufacture, originalStorePrice, Branch.valueOf(branch));
                productPairBranchDataMapper.insert(branch, catalog_number, originalStorePrice, product.getNotificationMin());
                cachedProducts.computeIfAbsent(product.getBranch(), k -> new HashMap<>())
                        .put(product.getCatalogNumber(), product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void createProductItem(String serial_number, int is_defective, String defection_date, String supplier_id, double supplier_price, double supplier_discount, double sold_price, LocalDateTime expiration_date, String location, String catalog_number, String branch, String name, String manufacture, double originalStorePrice) {
        try {
            Product product = null;
            if (!isProductExists(catalog_number)) {
                createProduct(catalog_number,branch, name, manufacture, originalStorePrice);
                product = new Product(catalog_number, name, manufacture, originalStorePrice, Branch.valueOf(branch));
                cachedProducts.computeIfAbsent(product.getBranch(), k -> new HashMap<>())
                        .put(product.getCatalogNumber(), product);
                productPairBranchDataMapper.insert(branch, catalog_number, originalStorePrice, product.getNotificationMin());
            }
            if (!isItemExists(serial_number, catalog_number, branch)) {
                productItemDataMapper.insert(serial_number, is_defective, defection_date, supplier_id, supplier_price,
                        supplier_discount, sold_price, expiration_date, location, catalog_number, branch);
                ProductItem productItem = new ProductItem(serial_number, supplier_id, supplier_price, supplier_discount,
                        location, expiration_date, catalog_number, Branch.valueOf(branch));
                cachedItems.computeIfAbsent(catalog_number, k -> new ArrayList<>()).add(productItem);
                if(product == null)
                    product = cachedProducts.get(Branch.valueOf(branch)).get(catalog_number);
                product.addProductItem(productItem);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public void updateProduct(String catalog_number, String branch, String name, String manufacture, double originalStorePrice, int newMinAmount){
        try {
            if(isProductExists(catalog_number)){
                productsDataMapper.update(catalog_number, name, manufacture,null);
                productPairBranchDataMapper.update(branch, catalog_number, originalStorePrice,newMinAmount);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateProductItem(String catalog_number, String branch, String serial_number,int is_defective, LocalDateTime defective_date, String supplier_id, double supplier_price, double supplier_discount, double sold_price, LocalDateTime expiration_date, String location){
        try {
            if (isItemExists(serial_number, catalog_number, branch)) {
                productItemDataMapper.update(serial_number,is_defective,defective_date,supplier_id,supplier_price,supplier_discount,sold_price,expiration_date,location,catalog_number,branch);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void addCategoryToProduct(String catalog_number, String category_name) {
        try {
            if (isProductExists( catalog_number)) {
                productsDataMapper.update(catalog_number,null,null,category_name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void removeCategoryFromProduct(String catalog_number) {
        try {
            if (isProductExists( catalog_number)) {
                productsDataMapper.update(catalog_number,null,null,"");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean isProductExists(String catalogNumber) {
        for (Map<String, Product> branchProducts : cachedProducts.values()) {
            if (branchProducts.containsKey(catalogNumber)) {
                return true;
            }
        }
        return false;
    }

    public boolean isItemExists(String serial_number, String catalog_number, String branch){
        for (List<ProductItem> productItemList : cachedItems.values()) {
            for (ProductItem productItem : productItemList) {
                if (productItem.getSerial_number().equals(serial_number) &&
                        productItem.getCatalog_number().equals(catalog_number) &&
                        productItem.getBranch().equals(Branch.valueOf(branch))) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<Branch, Map<String, Product>> getCachedProducts(){
        return this.cachedProducts;
    }

    public Map<String, List<ProductItem>> getCachedItems() {
        return this.cachedItems;
    }

}
