package Backend.DataAccessLayer.InventoryModule;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

import java.sql.SQLException;

public class CategoryDataMapper extends AbstractDataMapper {
    public CategoryDataMapper() {
        super("category", new String[]{"category_name"});
    }


    public void insert(String category_name) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s')",
                tableName, columnsString, category_name));
    }

    public void delete(String category_name) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE category_name = %s", tableName, category_name));
    }
}

