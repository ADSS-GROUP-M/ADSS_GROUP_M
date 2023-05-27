package dataAccessLayer.inventoryModule;
import businessLayer.inventoryModule.Category;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class CategoryManagerMapper{
    private final CategoryDataMapper categoryDataMapper;
    private final CategoryHierarchyDataMapper categoryHierarchyDataMapper;
    private final Map<String, Category> cached_categories;

    public CategoryManagerMapper(CategoryDataMapper categoryDataMapper, CategoryHierarchyDataMapper categoryHierarchyDataMapper) throws DalException {
        this.categoryDataMapper = categoryDataMapper;
        this.categoryHierarchyDataMapper = categoryHierarchyDataMapper;
        cached_categories = this.categoryHierarchyDataMapper.initializedCache(this.categoryDataMapper.initializedCache());
        if(cached_categories != null && !cached_categories.isEmpty()) {
            this.categoryDataMapper.initProductsWithCategory(cached_categories);
        }
    }

    public void createCategory(String category_name, List<Category> sub_category) throws DalException {
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
    }

    public void removeCategory(String category_name, List<Category> sub_category) throws DalException {
        categoryDataMapper.delete(category_name);
        if (!sub_category.isEmpty()) {
            for (Category sub : sub_category) {
                categoryHierarchyDataMapper.insert(category_name, sub.getCategoryName());
            }
        }
    }

    public void createSubCategory(String category_name, String subcategory_name) throws DalException {
        categoryHierarchyDataMapper.insert(category_name, subcategory_name);
    }

    public void removeSubCategory(String category_name, String subcategory_name) throws DalException {
        categoryHierarchyDataMapper.delete(category_name, subcategory_name);
    }

    public Map<String, Category> getCached_categories(){
        return this.cached_categories;
    }


}
