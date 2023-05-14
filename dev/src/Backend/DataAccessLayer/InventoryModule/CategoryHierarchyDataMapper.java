package Backend.DataAccessLayer.InventoryModule;
import Backend.BusinessLayer.InventoryModule.Category;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.*;

public class CategoryHierarchyDataMapper extends AbstractDataMapper {
    private static CategoryHierarchyDataMapper instance = null;

    private CategoryHierarchyDataMapper() {
        super("category_hierarchy", new String[]{"category", "sub_category"});
    }

    public static CategoryHierarchyDataMapper getInstance(){
        if (instance == null) {
            return new CategoryHierarchyDataMapper();
        } else {
            return instance;
        }
    }

    public void insert(String category_name, String sub_category) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', '%s')",
                tableName, columnsString, category_name, sub_category));
    }

    public void delete(String category_name, String sub_category) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE category = %s and sub_category = %s", tableName, category_name, sub_category));
    }

    public Map<String, Category> initializedCache(Map<String, Category> map) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s", columnsString, tableName));
        while (resultSet.next()) {
            String categoryName = resultSet.getString("category");
            String subCategoryName = resultSet.getString("sub_category");
            // create prime category if needed
            Category category = map.computeIfAbsent(categoryName, name -> new Category(name, Collections.emptyList()));

            // get prime categories' sub hash map and add subCategory instance if needed
            category.getSubcategories().computeIfAbsent(subCategoryName, name -> new Category(name, Collections.emptyList()));
        }
        return map;
    }
}
