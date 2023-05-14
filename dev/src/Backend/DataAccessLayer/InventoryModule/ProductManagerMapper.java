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
            List<ProductPairBranchDAO> cachedProductsPairBranch = productPairBranchDataMapper.getCachedProductsPairBranch();
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
                productsDataMapper.insert(catalog_number, name, manufacture);
                productPairBranchDataMapper.insert(branch, catalog_number, originalStorePrice);
                Product product = new Product(catalog_number, name, manufacture, originalStorePrice, Branch.valueOf(branch));
                cachedProducts.computeIfAbsent(product.getBranch(), k -> new HashMap<>())
                        .put(product.getCatalogNumber(), product);
            }
        } catch (SQLException e) {
            //TODO: Handle the exception appropriately
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
                productPairBranchDataMapper.insert(branch, catalog_number, originalStorePrice);
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
            //TODO: Handle the exception appropriately
        }

    }

    //TODO
    public void updateProduct(String catalog_number, String branch, String name, String manufacture, double originalStorePrice){
        try {
            if(isProductExists(catalog_number)){

                productsDataMapper.update(catalog_number, name, manufacture);
                productPairBranchDataMapper.insert(branch, catalog_number, originalStorePrice);
                Product product = new Product(catalog_number, name, manufacture, originalStorePrice, Branch.valueOf(branch));
                cachedProducts.computeIfAbsent(product.getBranch(), k -> new HashMap<>())
                        .put(product.getCatalogNumber(), product);
            }
        } catch (SQLException e) {
            //TODO: Handle the exception appropriately
        }
    }

    public void addCategoryToProduct() {

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
