package Backend.DataAccessLayer.InventoryModule;
import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.InventoryModule.Category;
import Backend.BusinessLayer.InventoryModule.Product;
import Backend.DataAccessLayer.InventoryModule.CategoryDataMapper;
import Backend.DataAccessLayer.InventoryModule.CategoryHierarchyDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryManagerMapper{
    CategoryDataMapper categoryDataMapper;
    CategoryHierarchyDataMapper categoryHierarchyDataMapper;
    public static CategoryManagerMapper instance = null;
    Map<String, Category> cached_categories;

    private CategoryManagerMapper(){
        categoryDataMapper = CategoryDataMapper.getInstance();
        categoryHierarchyDataMapper = CategoryHierarchyDataMapper.getInstance();
        try{
            cached_categories = categoryHierarchyDataMapper.initializedCache(categoryDataMapper.initializedCache());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        categoryDataMapper.initProductsWithCategory(cached_categories);
    }

    public static CategoryManagerMapper getInstance(){
        if (instance == null) {
            return new CategoryManagerMapper();
        } else {
            return instance;
        }
    }

    public void createCategory(String category_name, List<Category> sub_category) {
        try {
            if(!categoryDataMapper.isExists(category_name)){
                categoryDataMapper.insert(category_name);
            }
            if(!sub_category.isEmpty()){
                for (Category sub : sub_category) {
                    if(!categoryHierarchyDataMapper.isExists(category_name, sub.getCategoryName())){
                        categoryHierarchyDataMapper.insert(category_name, sub.getCategoryName());
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void removeCategory(String category_name, List<Category> sub_category) {
        try {
            categoryDataMapper.delete(category_name);
            if (!sub_category.isEmpty()) {
                for (Category sub : sub_category) {
                    categoryHierarchyDataMapper.insert(category_name, sub.getCategoryName());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void createSubCategory(String category_name, String subcategory_name){
        try {
            categoryHierarchyDataMapper.insert(category_name, subcategory_name);
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void removeSubCategory(String category_name, String subcategory_name){
        try {
            categoryHierarchyDataMapper.delete(category_name, subcategory_name);
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Map<String, Category> getCached_categories(){
        return this.cached_categories;
    }


}
