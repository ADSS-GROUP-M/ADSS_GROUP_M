package dataAccessLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.ProductStoreDiscount;
import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class StoreProductDiscountDataMapper extends AbstractDataMapper {

    private static StoreProductDiscountDataMapper instance = null;
    public StoreProductDiscountDataMapper(SQLExecutor sqlExecutor) throws DalException {
        super(sqlExecutor, "store_product_discount", new String[]{"catalog_number", "start_date", "end_date", "discount","branch"});
    }

    public static StoreProductDiscountDataMapper getInstance() throws DalException {
        if(instance == null){
            return new StoreProductDiscountDataMapper();
        } else {
            return instance;
        }
    }
    public void insert(String catalog_number, String start_date, String end_date, double discount, String branch) throws SQLException {
        try {
            if(!isExists(catalog_number, start_date, end_date, discount, branch)){
                String columnsString = String.join(", ", columns);
                sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', '%s', '%s', '%s', '%s')",
                        tableName, columnsString, catalog_number, start_date, end_date, discount, branch));
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Map<Branch, Map<String, List<ProductStoreDiscount>>> initializeCache() throws SQLException {
        Map<Branch, Map<String,List<ProductStoreDiscount>>> cachedDiscount = new HashMap<Branch, Map<String,List<ProductStoreDiscount>>>();
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s", columnsString, tableName));
        while (resultSet.next()) {
            ProductStoreDiscount productStoreDiscount = new ProductStoreDiscount(resultSet.getString("catalog_number"),
                    Branch.valueOf(resultSet.getString("branch")),
                    LocalDateTime.parse(resultSet.getString("start_date")),
                    LocalDateTime.parse(resultSet.getString("end_date")),
                    resultSet.getDouble("discount"));
            Branch branch = productStoreDiscount.getBranch();
            String catalog_number = productStoreDiscount.getCatalog_number();
            if(!cachedDiscount.containsKey(branch)) {
                Map<String, List<ProductStoreDiscount>> mapProductDiscount = new HashMap<String, List<ProductStoreDiscount>>();
                cachedDiscount.put(branch,mapProductDiscount);
            }
            if(!cachedDiscount.get(branch).containsKey(catalog_number)){
                List<ProductStoreDiscount> listProductStoreDiscount = new ArrayList<ProductStoreDiscount>();
                cachedDiscount.get(branch).put(catalog_number,listProductStoreDiscount);
            }
            cachedDiscount.get(branch).get(catalog_number).add(productStoreDiscount);
        }
        return cachedDiscount;
    }

    public boolean isExists(String catalog_number, String start_date, String end_date, double discount, String branch) throws SQLException {
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT COUNT(*) as count FROM %s WHERE catalog_number = '%s' AND start_date = '%s' AND end_date = '%s' AND discount = %f AND branch = '%s'", tableName, catalog_number, start_date, end_date,
        discount, branch));
        if (resultSet.next())
            return resultSet.getInt("count") > 0;
        return false;
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
                .addColumn("catalog_number", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn("start_date", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn("end_date", ColumnType.INTEGER, ColumnModifier.PRIMARY_KEY)
                .addColumn("discount", ColumnType.REAL, ColumnModifier.PRIMARY_KEY)
                .addColumn("branch", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addForeignKey("catalog_number", "products", "catalog_number");
    }
}
