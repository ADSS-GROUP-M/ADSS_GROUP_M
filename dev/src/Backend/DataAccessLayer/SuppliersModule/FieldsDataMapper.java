package Backend.DataAccessLayer.SuppliersModule;


import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class FieldsDataMapper extends AbstractDataMapper {
        public FieldsDataMapper() {
                super("fields", new String[] {"bn_number", "field"});
        }

        public void insert(String bnNumber, String field) throws SQLException {
                String columnsString = String.join(", ", columns);
                sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES(%s, %s, %s, %s)",tableName, columnsString, bnNumber,
                        field));
        }

        public void delete(String bnNumber) throws SQLException {
                sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s", tableName, bnNumber));
        }

        public void delete(String bnNumber, String field) throws SQLException {
                sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s and field = %s", tableName, bnNumber, field));
        }

        public List<String> find(String bnNumber) throws SQLException {
                String columnsString = String.join(", ", columns);
                OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = %s", columnsString, tableName, bnNumber));
                List<String> fields = new LinkedList<>();
                while (resultSet.next())
                        fields.add(resultSet.getString("field"));
                return fields;
        }
}
