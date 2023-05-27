package dataAccessLayer.inventoryModule;

import businessLayer.inventoryModule.Category;
import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class CategoryHierarchyDataMapper extends AbstractDataMapper {


    public CategoryHierarchyDataMapper(SQLExecutor sqlExecutor) throws DalException {
        super(sqlExecutor, "category_hierarchy", new String[]{"category", "sub_category"});
    }

    public void insert(String category_name, String sub_category) throws DalException{
        String columnsString = String.join(", ", columns);
        try {
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', '%s')",
                    tableName, columnsString, category_name, sub_category));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void delete(String category_name, String sub_category) throws DalException{
        try {
            sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE category = %s and sub_category = %s", tableName, category_name, sub_category));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public Map<String, Category> initializedCache(Map<String, Category> map) throws DalException{
        try {
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
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public boolean isExists(String category_name, String sub_category_name) throws DalException{
        try {
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT COUNT(*) as count FROM %s WHERE category = '%s' AND sub_category = '%s'", tableName, category_name, sub_category_name));
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
        createTableQueryBuilder
                .addColumn("category", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn("sub_category", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addForeignKey("category", "category", "category_name")
                .addForeignKey("sub_category", "category", "category_name");
    }
}
