package Backend.DataAccessLayer.InventoryModule;
import Backend.DataAccessLayer.InventoryModule.CategoryDataMapper;
import Backend.DataAccessLayer.InventoryModule.CategoryHierarchyDataMapper;

import java.sql.SQLException;


public class CategoryManagerMapper{
    CategoryDataMapper categoryDataMapper = new CategoryDataMapper();
    CategoryHierarchyDataMapper categoryHierarchyDataMapper = new CategoryHierarchyDataMapper();

    public void createCategoryWithSub(String category_name, String sub_category) {
        try {
            categoryDataMapper.insert(category_name);
            categoryHierarchyDataMapper.insert(category_name, sub_category);
        } catch (SQLException e) {
            //TODO: Handle the exception appropriately
        }
    }

    public void createCategoryWithoutSub(String category_name) {
        try {
            categoryDataMapper.insert(category_name);
        } catch (SQLException e) {
            // TODO: Handle the exception appropriately
        }
    }
}
