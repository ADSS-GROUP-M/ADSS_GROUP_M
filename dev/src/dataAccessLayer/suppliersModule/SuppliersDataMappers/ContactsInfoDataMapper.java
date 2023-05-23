package dataAccessLayer.suppliersModule.SuppliersDataMappers;

import businessLayer.suppliersModule.Pair;
import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.*;

public class ContactsInfoDataMapper extends AbstractDataMapper {
    public ContactsInfoDataMapper(SQLExecutor sqlExecutor) throws DalException {
        super(sqlExecutor, "contacts_info", new String[]{"bn_number", "name", "phone_number", "email"});
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

    /**
     * Used to insert data into {@link AbstractDataMapper#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    @Override
    protected void initializeCreateTableQueryBuilder() throws DalException {
        createTableQueryBuilder
                .addColumn("bn_number", ColumnType.TEXT,ColumnModifier.PRIMARY_KEY)
                .addColumn("name", ColumnType.TEXT)
                .addColumn("phone_number", ColumnType.TEXT)
                .addColumn("email", ColumnType.TEXT,ColumnModifier.PRIMARY_KEY)
                .addForeignKey("bn_number", "suppliers","bn_number", ON_UPDATE.CASCADE, ON_DELETE.CASCADE);
    }
}
