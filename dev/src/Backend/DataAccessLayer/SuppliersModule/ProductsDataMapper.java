package Backend.DataAccessLayer.SuppliersModule;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;

public class ProductsDataMapper  extends AbstractDataMapper {
    public ProductsDataMapper() {
        super("products", new String[]{"catalog_number", "name", "manufacture", "category"});
    }

    public void insert(String catalog_number, String name, String manufacture) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %s, '%s')",
                tableName, columnsString, name, catalog_number, manufacture));
    }

    public void delete(String catalog_number) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE catalog_number = %s", tableName, catalog_number));
    }

    public boolean isExists(String catalog_number) throws SQLException {
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT COUNT(*) as count FROM %s WHERE catalog_number = '%s'", tableName, catalog_number));
        return resultSet.getInt("count") > 0;
    }

}
