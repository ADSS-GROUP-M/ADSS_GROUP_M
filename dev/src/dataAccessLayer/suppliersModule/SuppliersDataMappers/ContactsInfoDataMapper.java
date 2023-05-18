package dataAccessLayer.suppliersModule.SuppliersDataMappers;

import businessLayer.suppliersModule.Pair;
import dataAccessLayer.dalUtils.AbstractDataMapper;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ContactsInfoDataMapper extends AbstractDataMapper {
    public ContactsInfoDataMapper() {
        super("contacts_info", new String[]{"bn_number", "name", "phone_number", "email"});
    }

    public void insert(String bnNumber, String name, String email, String phoneNumber) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', '%s', '%s', '%s')",tableName, columnsString, bnNumber,
                name, email, phoneNumber));
    }

    public void delete(String bnNumber, String email) throws SQLException {
        sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s' and email = '%s'",
                tableName, bnNumber, email));
    }

    public void delete(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s'", tableName, bnNumber));
    }

    public void updateEmail(String bnNumber, String email, String newEmail) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET email = '%s' WHERE bn_number = '%s' and email = '%s'", tableName, newEmail, bnNumber, email));
    }

    public void updateName(String bnNumber, String email, String newName) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET name = '%s' WHERE bn_number = '%s' and email = '%s'", tableName, newName, bnNumber, email));
    }

    public void updatePhoneNumber(String bnNumber, String email, String newPhoneNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET phone_number = '%s' WHERE bn_number = '%s' and email = '%s'", tableName, newPhoneNumber, bnNumber, email));
    }

    public Map<String, Pair<String, String>> find(String bnNumber) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'", columnsString, tableName, bnNumber));
        Map<String, Pair<String, String>> contactsInfo = new HashMap<>();
        while (resultSet.next())
            contactsInfo.put(resultSet.getString("email"), new Pair<>(resultSet.getString("name"), resultSet.getString("phone_number")));
        return contactsInfo;

    }
}
