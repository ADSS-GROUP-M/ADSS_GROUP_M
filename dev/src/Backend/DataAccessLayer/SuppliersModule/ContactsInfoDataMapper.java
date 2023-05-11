package Backend.DataAccessLayer.SuppliersModule;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

import java.sql.SQLException;

public class ContactsInfoDataMapper extends AbstractDataMapper {
    public ContactsInfoDataMapper() {
        super("Contacts_info", new String[]{"bn_number", "name", "phoneNumber"});
    }

    public void insert(String bnNumber, String name, String email, String phoneNumber) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES(%s, %s, %s, %s)",tableName, columnsString, bnNumber,
                name, email, phoneNumber));
    }

    public void delete(String bnNumber, String email) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s and email = %s",
                tableName, bnNumber, email));
    }

    public void delete(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s", tableName, bnNumber));
    }

    public void updateEmail(String bnNumber, String email, String phoneNumber, String newEmail){

    }
}
