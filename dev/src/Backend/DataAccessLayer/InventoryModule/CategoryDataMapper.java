package Backend.DataAccessLayer.InventoryModule;
import Backend.BusinessLayer.InventoryModule.Category;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

import java.sql.SQLException;

public class CategoryDataMapper extends AbstractDataMapper {
    private static CategoryDataMapper instance = null;

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
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE category_name = %s", tableName, category_name));
    }

//    public Category initializedCache(){
//
//    }

}

