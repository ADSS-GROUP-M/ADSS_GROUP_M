package dataAccessLayer.suppliersModule.SuppliersDataMappers;

import businessLayer.suppliersModule.BankAccount;
import businessLayer.suppliersModule.Pair;
import businessLayer.suppliersModule.Supplier;
import dataAccessLayer.dalUtils.AbstractDataMapper;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.*;

public class SupplierDataMapper extends AbstractDataMapper {
    private Map<String, Supplier> suppliers;
    private ContactsInfoDataMapper contactsInfoDataMapper;
    private FieldsDataMapper fieldsDataMapper;

    public SupplierDataMapper() {
        super("suppliers", new String[]{"bn_number", "name", "bank", "branch", "account_number", "payment_method"});
        suppliers = new HashMap<>();
        contactsInfoDataMapper = new ContactsInfoDataMapper();
        fieldsDataMapper = new FieldsDataMapper();
    }

    public Supplier getSupplier(String bnNumber) throws SQLException, DalException {
        if(suppliers.containsKey(bnNumber))
            return suppliers.get(bnNumber);
        Supplier supplier = find(bnNumber);
        if(supplier == null)
            throw new DalException("no such supplier - " + bnNumber);
        suppliers.put(bnNumber, supplier);
        return supplier;
    }

    public List<Supplier> getCopyOfSuppliers() throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s ", columnsString, tableName));
        while (resultSet.next()){
            String bnNumber = resultSet.getString("bn_number");
            if(!suppliers.containsKey(bnNumber))
                suppliers.put(bnNumber, new Supplier(resultSet.getString("name"), resultSet.getString("bn_number"),
                    new BankAccount(resultSet.getString("bank"), resultSet.getString("branch"), resultSet.getString("account_number")),
                    resultSet.getString("payment_method"), fieldsDataMapper.find(bnNumber), contactsInfoDataMapper.find(bnNumber)));
        }
        return new LinkedList<>(suppliers.values());
    }


    public void insert(String bnNumber, String name, String bank, String branch, String accountNumber, String paymentMethod,
                       List<String> fields, Map<String, Pair<String, String>> contactsInfo) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', '%s', '%s', '%s', '%s', '%s')",tableName, columnsString, bnNumber,
                name, bank, branch, accountNumber, paymentMethod));
        for(String field : fields)
            fieldsDataMapper.insert(bnNumber, field);
        for(Map.Entry<String, Pair<String, String>> contact : contactsInfo.entrySet())
            contactsInfoDataMapper.insert(bnNumber, contact.getValue().getFirst(), contact.getKey(), contact.getValue().getSecond());
    }

    public void delete(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s'", tableName, bnNumber));
    }

    public Supplier find(String bnNumber) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'", columnsString, tableName, bnNumber));
        if(resultSet.next())
            return new Supplier(resultSet.getString("name"), resultSet.getString("bn_number"),
                    new BankAccount(resultSet.getString("bank"), resultSet.getString("branch"), resultSet.getString("account_number")),
                    resultSet.getString("payment_method"), fieldsDataMapper.find(bnNumber), contactsInfoDataMapper.find(bnNumber));
        return null;
    }

    public void updateBankAccount(String bnNumber,String bank, String branch, String accountNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET bank = '%s', branch = '%s', account_number = '%s' WHERE bn_number = '%s'", tableName,
                bank, branch, accountNumber, bnNumber));
    }

    public void updateName(String bnNumber, String name) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET name = '%s' WHERE bn_number = '%s'", tableName, name, bnNumber));
    }

    public void updatePaymentMethod(String bnNumber, String paymentMethod) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET payment_method = '%s' WHERE bn_number = '%s'", tableName, paymentMethod, bnNumber));
    }


    public void updateBnNumber(String bnNumber, String newBnNumber) throws SQLException, DalException {
        Supplier supplier = getSupplier(bnNumber);
        sqlExecutor.executeWrite(String.format("UPDATE %s SET bn_number = '%s' WHERE bn_number = '%s'", tableName, newBnNumber, bnNumber));
        suppliers.remove(bnNumber);
        supplier.setBnNumber(newBnNumber);
        suppliers.put(newBnNumber, supplier);
    }

    public void removeContactInfo(String bnNumber, String contactsEmail) throws SQLException {
        contactsInfoDataMapper.delete(bnNumber, contactsEmail);
    }
    public void addContactInfo(String bnNumber, String contactName, String email, String phoneNumber) throws SQLException {
        contactsInfoDataMapper.insert(bnNumber, contactName, email, phoneNumber);
    }

    public void setContactsEmail(String bnNumber, String email, String newEmail) throws SQLException {
        contactsInfoDataMapper.updateEmail(bnNumber, email, newEmail);
    }
    public void setContactsPhoneNumber(String bnNumber, String email, String phoneNumber) throws SQLException {
        contactsInfoDataMapper.updatePhoneNumber(bnNumber, email, phoneNumber);
    }

    public void addField(String bnNumber, String field) throws SQLException {
        fieldsDataMapper.insert(bnNumber, field);
    }

    public void removeField(String bnNumber, String field) throws SQLException {
        fieldsDataMapper.delete(bnNumber, field);
    }

    public void removeSupplier(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s'", tableName, bnNumber));
        fieldsDataMapper.delete(bnNumber);
        contactsInfoDataMapper.delete(bnNumber);
        suppliers.remove(bnNumber);
    }
}
