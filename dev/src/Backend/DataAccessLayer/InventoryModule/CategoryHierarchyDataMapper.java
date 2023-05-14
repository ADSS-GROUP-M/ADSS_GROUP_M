package Backend.DataAccessLayer.InventoryModule;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

import java.sql.SQLException;

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

}
