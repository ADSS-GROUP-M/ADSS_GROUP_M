package Backend.DataAccessLayer.InventoryModule;
import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.InventoryModule.Category;
import Backend.DataAccessLayer.InventoryModule.CategoryDataMapper;
import Backend.DataAccessLayer.InventoryModule.CategoryHierarchyDataMapper;

import java.sql.SQLException;
import java.util.Map;


public class CategoryManagerMapper{
    CategoryDataMapper categoryDataMapper;
    CategoryHierarchyDataMapper categoryHierarchyDataMapper;
    public static CategoryManagerMapper instance = null;
    Map<Branch, Map<String, Category>> categories;

    private CategoryManagerMapper(){
        categoryDataMapper = CategoryDataMapper.getInstance();
        categoryHierarchyDataMapper = CategoryHierarchyDataMapper.getInstance();

    }

    public static CategoryManagerMapper getInstance(){
        if (instance == null) {
            return new CategoryManagerMapper();
        } else {
            return instance;
        }
    }

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
