package Backend.DataAccessLayer.InventoryModule;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductPairBranchDataMapper extends AbstractDataMapper {
    public static ProductPairBranchDataMapper instance = null;
    private List<ProductPairBranchDAO> cachedProductsPairBranch = new ArrayList<>(); //Map<Branch, Map<catalog_number, Product>

    private ProductPairBranchDataMapper() {
        super("product_pair_branch", new String[]{"branch_name", "product_catalog_number", "original_store_price", "notification_min"});
    }

    public static ProductPairBranchDataMapper getInstance(){
        if (instance == null) {
            return new ProductPairBranchDataMapper();
        } else {
            return instance;
        }    }

    public void insert(String branch_name, String product_catalog_number, double original_store_price) throws SQLException {
        if(isExists(product_catalog_number, branch_name)){
            String columnsString = String.join(", ", columns);
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %s, '%f')",
                    tableName, columnsString, branch_name, product_catalog_number, original_store_price));
            ProductPairBranchDAO productPairBranchDAO = new ProductPairBranchDAO(branch_name, product_catalog_number, original_store_price);
            cachedProductsPairBranch.add(productPairBranchDAO);
        }
    }

    public void update(String branch_name, String product_catalog_number, double original_store_price, int notification_min){
        try {
            if (isExists(product_catalog_number, branch_name)) {
                if (original_store_price != -1)
                    sqlExecutor.executeWrite(String.format("UPDATE %s SET original_store_price = '%s' WHERE catalog_number = '%s' and branch = '%s'", tableName, original_store_price, product_catalog_number, branch_name));
                if (notification_min != -1) {
                    sqlExecutor.executeWrite(String.format("UPDATE %s SET notification_min = '%s' WHERE catalog_number = '%s' and branch = '%s'", tableName, notification_min, product_catalog_number, branch_name));
                }
            }
        }
        catch (SQLException e){
            //TODO: Handle the exception appropriately
        }

    }

    public List<ProductPairBranchDAO> getCachedProductsPairBranch() {
        return cachedProductsPairBranch;
    }

    public boolean isExists(String catalogNumber, String branchName) {
        for (ProductPairBranchDAO product : cachedProductsPairBranch) {
            if (product.catalog_number.equals(catalogNumber) && product.branch_name.equals(branchName)) {
                return true;
            }
        }
        return false;
    }

}
