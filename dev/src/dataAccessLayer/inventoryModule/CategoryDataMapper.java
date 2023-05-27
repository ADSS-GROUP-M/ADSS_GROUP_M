package dataAccessLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.Category;
import businessLayer.inventoryModule.Product;
import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class CategoryDataMapper extends AbstractDataMapper {

    private Map<String, Category> cachedCategories;
    private final ProductManagerMapper productManagerMapper;

    public CategoryDataMapper(SQLExecutor sqlExecutor, ProductManagerMapper productManagerMapper) throws DalException {
        super(sqlExecutor, "category", new String[]{"category_name"});
        this.productManagerMapper = productManagerMapper;
    }

    public void insert(String category_name) throws DalException{
        String columnsString = String.join(", ", columns);
        try {
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s')",
                    tableName, columnsString, category_name));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void delete(String category_name) throws DalException{
        try {
            sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE category_name = '%s'", tableName, category_name));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public Map<String, Category> initializedCache() throws DalException{
        try {
            Map<String, Category> cachedCategory = new HashMap<String, Category>();
            String columnsString = String.join(", ", columns);
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s", columnsString, tableName));
            while (resultSet.next()) {
                Category category = new Category(resultSet.getString("category_name"), Collections.emptyList());
                cachedCategory.put(resultSet.getString("category_name"), category);
            }
            return cachedCategory;
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void initProductsWithCategory(Map<String, Category>cached_categories) throws DalException {
        try{
            Map<Branch, Map<String, Product>> cachedProducts = productManagerMapper.getCachedProducts();
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s , %s FROM %s", "catalog_number","category", "products"));
            while (resultSet.next()) {
                for(Map<String, Product> productMap : cachedProducts.values()){
                    for (Product product : productMap.values()) {
                        if (product.getCatalogNumber().equals(resultSet.getString("catalog_number"))){
                            if (cached_categories.get(resultSet.getString("category")) != null){
                                cached_categories.get(resultSet.getString("category")).addProductToCategory(product);
                            }
                        }
                    }
                }
            }
        }
        catch (SQLException e){
            throw new DalException(e.getMessage(), e);
        }
    }

    public boolean isExists(String category_name) throws DalException{
        try {
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT COUNT(*) as count FROM %s WHERE category_name = '%s'", tableName, category_name));
            if (resultSet.next()) {
                Integer count = resultSet.getInt("count");
                return count > 0;
            }
            else {
                return false;
            }
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    /**
     * Used to insert data into {@link AbstractDataMapper#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    @Override
    protected void initializeCreateTableQueryBuilder() {
        createTableQueryBuilder.addColumn("category_name", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY);
    }
}

