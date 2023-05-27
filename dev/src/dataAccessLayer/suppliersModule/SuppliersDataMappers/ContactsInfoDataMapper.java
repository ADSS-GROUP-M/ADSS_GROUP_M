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

    public void insert(String bnNumber, String name, String email, String phoneNumber) throws DalException {
        String columnsString = String.join(", ", columns);
        try {
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', '%s', '%s', '%s')",tableName, columnsString, bnNumber,
                    name, email, phoneNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void delete(String bnNumber, String email) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s' and email = '%s'",
                    tableName, bnNumber, email));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void delete(String bnNumber) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s'", tableName, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updateEmail(String bnNumber, String email, String newEmail) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET email = '%s' WHERE bn_number = '%s' and email = '%s'", tableName, newEmail, bnNumber, email));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updateName(String bnNumber, String email, String newName) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET name = '%s' WHERE bn_number = '%s' and email = '%s'", tableName, newName, bnNumber, email));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updatePhoneNumber(String bnNumber, String email, String newPhoneNumber) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET phone_number = '%s' WHERE bn_number = '%s' and email = '%s'", tableName, newPhoneNumber, bnNumber, email));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public Map<String, Pair<String, String>> find(String bnNumber) throws DalException {
        try {
            String columnsString = String.join(", ", columns);
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'", columnsString, tableName, bnNumber));
            Map<String, Pair<String, String>> contactsInfo = new HashMap<>();
            while (resultSet.next()) {
                contactsInfo.put(resultSet.getString("email"), new Pair<>(resultSet.getString("name"), resultSet.getString("phone_number")));
            }
            return contactsInfo;
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
                .addColumn("bn_number", ColumnType.TEXT,ColumnModifier.PRIMARY_KEY)
                .addColumn("name", ColumnType.TEXT)
                .addColumn("phone_number", ColumnType.TEXT)
                .addColumn("email", ColumnType.TEXT,ColumnModifier.PRIMARY_KEY)
                .addForeignKey("bn_number", "suppliers","bn_number", ON_UPDATE.CASCADE, ON_DELETE.CASCADE);
    }
}
