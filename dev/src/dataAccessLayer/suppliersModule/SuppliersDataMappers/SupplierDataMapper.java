package dataAccessLayer.suppliersModule.SuppliersDataMappers;

import businessLayer.suppliersModule.BankAccount;
import businessLayer.suppliersModule.Pair;
import businessLayer.suppliersModule.Supplier;
import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class SupplierDataMapper extends AbstractDataMapper {
    private final Map<String, Supplier> suppliers;
    private final ContactsInfoDataMapper contactsInfoDataMapper;
    private final FieldsDataMapper fieldsDataMapper;

    public SupplierDataMapper(SQLExecutor sqlExecutor, ContactsInfoDataMapper contactsInfoDataMapper, FieldsDataMapper fieldsDataMapper) throws DalException {
        super(sqlExecutor, "suppliers", new String[]{"bn_number", "name", "bank", "branch", "account_number", "payment_method"});
        this.contactsInfoDataMapper = contactsInfoDataMapper;
        this.fieldsDataMapper = fieldsDataMapper;
        suppliers = new HashMap<>();
    }

    public Supplier getSupplier(String bnNumber) throws DalException {
        if(suppliers.containsKey(bnNumber)) {
            return suppliers.get(bnNumber);
        }
        Supplier supplier = null;
        supplier = find(bnNumber);
        if(supplier == null) {
            throw new DalException("no such supplier - " + bnNumber);
        }
        suppliers.put(bnNumber, supplier);
        return supplier;
    }

    public List<Supplier> getCopyOfSuppliers() throws DalException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = null;
        try {
            resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s ", columnsString, tableName));
            while (resultSet.next()){
                String bnNumber = resultSet.getString("bn_number");
                if(!suppliers.containsKey(bnNumber)) {
                    suppliers.put(bnNumber, new Supplier(resultSet.getString("name"), resultSet.getString("bn_number"),
                        new BankAccount(resultSet.getString("bank"), resultSet.getString("branch"), resultSet.getString("account_number")),
                        resultSet.getString("payment_method"), fieldsDataMapper.find(bnNumber), contactsInfoDataMapper.find(bnNumber)));
                }
            }
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
        return new LinkedList<>(suppliers.values());
    }


    public void insert(String bnNumber, String name, String bank, String branch, String accountNumber, String paymentMethod,
                       List<String> fields, Map<String, Pair<String, String>> contactsInfo) throws DalException {
        String columnsString = String.join(", ", columns);
        try {
            sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', '%s', '%s', '%s', '%s', '%s')",tableName, columnsString, bnNumber,
                    name, bank, branch, accountNumber, paymentMethod));
            for(String field : fields) {
                fieldsDataMapper.insert(bnNumber, field);
            }
            for(Map.Entry<String, Pair<String, String>> contact : contactsInfo.entrySet()) {
                contactsInfoDataMapper.insert(bnNumber, contact.getValue().getFirst(), contact.getKey(), contact.getValue().getSecond());
            }
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

    public Supplier find(String bnNumber) throws DalException {
        String columnsString = String.join(", ", columns);
        try{
            OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'", columnsString, tableName, bnNumber));
            if(resultSet.next()) {
                return new Supplier(resultSet.getString("name"), resultSet.getString("bn_number"),
                        new BankAccount(resultSet.getString("bank"), resultSet.getString("branch"), resultSet.getString("account_number")),
                        resultSet.getString("payment_method"), fieldsDataMapper.find(bnNumber), contactsInfoDataMapper.find(bnNumber));
            }
            return null;
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updateBankAccount(String bnNumber,String bank, String branch, String accountNumber) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET bank = '%s', branch = '%s', account_number = '%s' WHERE bn_number = '%s'", tableName,
                    bank, branch, accountNumber, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updateName(String bnNumber, String name) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET name = '%s' WHERE bn_number = '%s'", tableName, name, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void updatePaymentMethod(String bnNumber, String paymentMethod) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("UPDATE %s SET payment_method = '%s' WHERE bn_number = '%s'", tableName, paymentMethod, bnNumber));
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }


    public void updateBnNumber(String bnNumber, String newBnNumber) throws DalException {
        try {
            Supplier supplier = getSupplier(bnNumber);
            sqlExecutor.executeWrite(String.format("UPDATE %s SET bn_number = '%s' WHERE bn_number = '%s'", tableName, newBnNumber, bnNumber));
            suppliers.remove(bnNumber);
            supplier.setBnNumber(newBnNumber);
            suppliers.put(newBnNumber, supplier);
        } catch (SQLException e) {
            throw new DalException(e.getMessage(), e);
        }
    }

    public void removeContactInfo(String bnNumber, String contactsEmail) throws DalException {
        contactsInfoDataMapper.delete(bnNumber, contactsEmail);
    }

    public void addContactInfo(String bnNumber, String contactName, String email, String phoneNumber) throws DalException {
        contactsInfoDataMapper.insert(bnNumber, contactName, email, phoneNumber);
    }
    public void setContactsEmail(String bnNumber, String email, String newEmail) throws DalException {
        contactsInfoDataMapper.updateEmail(bnNumber, email, newEmail);
    }

    public void setContactsPhoneNumber(String bnNumber, String email, String phoneNumber) throws DalException {
        contactsInfoDataMapper.updatePhoneNumber(bnNumber, email, phoneNumber);
    }
    public void addField(String bnNumber, String field) throws DalException {
        fieldsDataMapper.insert(bnNumber, field);
    }

    public void removeField(String bnNumber, String field) throws DalException {
        fieldsDataMapper.delete(bnNumber, field);
    }

    public void removeSupplier(String bnNumber) throws DalException {
        try {
            sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s'", tableName, bnNumber));
            fieldsDataMapper.delete(bnNumber);
            contactsInfoDataMapper.delete(bnNumber);
            suppliers.remove(bnNumber);
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
                .addColumn("name", ColumnType.TEXT)
                .addColumn("bn_number", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn("bank", ColumnType.TEXT)
                .addColumn("branch", ColumnType.TEXT)
                .addColumn("account_number", ColumnType.TEXT)
                .addColumn("payment_method", ColumnType.TEXT);
    }

    @Override
    public void clearTable(){
        suppliers.clear();
        contactsInfoDataMapper.clearTable();
        fieldsDataMapper.clearTable();
        super.clearTable();
    }
}
