package dataAccessLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.inventoryModule.Category;
import businessLayer.inventoryModule.Product;
import dataAccessLayer.dalUtils.AbstractDataMapper;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CategoryDataMapper extends AbstractDataMapper {
    private static CategoryDataMapper instance = null;
    private Map<String, Category> cachedCategories;

    private CategoryDataMapper() {
        super("category", new String[]{"category_name"});
    }

    public static CategoryDataMapper getInstance(){
        if(instance == null){
            return new CategoryDataMapper();
        } else {
            return instance;
        }
    }

    public void insert(String category_name) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s')",
                tableName, columnsString, category_name));
    }

    public void delete(String category_name) throws SQLException {
        sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE category_name = '%s'", tableName, category_name));
    }

    public Map<String, Category> initializedCache() throws SQLException {
        Map<String, Category> cachedCategory = new HashMap<String, Category>();
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s", columnsString, tableName));
        while (resultSet.next()) {
            Category category = new Category(resultSet.getString("category_name"), Collections.emptyList());
            cachedCategory.put(resultSet.getString("category_name"), category);
        }
        return cachedCategory;
    }

    public void initProductsWithCategory(Map<String, Category>cached_categories){
        try{
            Map<Branch, Map<String, Product>> cachedProducts = ProductManagerMapper.getInstance().getCachedProducts();
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
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean isExists(String category_name) throws SQLException {
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT COUNT(*) as count FROM %s WHERE category_name = '%s'", tableName, category_name));
        if (resultSet.next()) {
            Integer count = resultSet.getInt("count");
            return count > 0;
        }
        else {
            return false;
        }
    }

}

