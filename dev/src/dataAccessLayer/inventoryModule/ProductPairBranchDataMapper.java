package dataAccessLayer.inventoryModule;

import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class ProductPairBranchDataMapper extends AbstractDataMapper {
    public static ProductPairBranchDataMapper instance = null;
    private List<ProductPairBranchDAO> cachedProductsPairBranch = new ArrayList<>(); //Map<Branch, Map<catalog_number, Product>

    private ProductPairBranchDataMapper(SQLExecutor sqlExecutor) throws DalException {
        super(sqlExecutor, "product_pair_branch", new String[]{"branch_name", "product_catalog_num", "original_store_price", "notification_min"});
    }

    public static ProductPairBranchDataMapper getInstance() throws DalException {
        if (instance == null) {
            return new ProductPairBranchDataMapper();
        } else {
            return instance;
        }    }

    public void insert(String branch_name, String product_catalog_number, double original_store_price, int notification_min) throws SQLException {
        if(!isExists(product_catalog_number, branch_name)){
            try {
                String columnsString = String.join(", ", columns);
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', '%s', %f, %s)",
                    tableName, columnsString, branch_name, product_catalog_number, original_store_price, notification_min));
            ProductPairBranchDAO productPairBranchDAO = new ProductPairBranchDAO(branch_name, product_catalog_number, original_store_price, notification_min);
            cachedProductsPairBranch.add(productPairBranchDAO);
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
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
            throw new RuntimeException(e.getMessage());
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

    public List<ProductPairBranchDAO> initializeCache() throws SQLException {
        List<ProductPairBranchDAO> cachedProducts = new ArrayList<>();
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s",
                columnsString, tableName));
        while (resultSet.next()) {
            ProductPairBranchDAO item = new ProductPairBranchDAO(resultSet.getString("branch_name"),
                    resultSet.getString("product_catalog_num"), resultSet.getDouble("original_store_price"), resultSet.getInt("notification_min"));
            cachedProducts.add(item);
        }
        return cachedProducts;
    }


    /**
     * Used to insert data into {@link AbstractDataMapper#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    @Override
    protected void initializeCreateTableQueryBuilder() throws DalException {
        createTableQueryBuilder
                .addColumn("branch_name", ColumnType.TEXT,ColumnModifier.PRIMARY_KEY)
                .addColumn("product_catalog_num", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn("original_store_price", ColumnType.REAL)
                .addColumn("notification_min", ColumnType.NUMERIC)
                .addForeignKey("product_catalog_num", "products", "catalog_number");
    }
}
