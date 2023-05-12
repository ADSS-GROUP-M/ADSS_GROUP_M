package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.BankAccount;
import Backend.BusinessLayer.SuppliersModule.Supplier;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;
import Backend.DataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SupplierDataMapper extends AbstractDataMapper {
    private List<Supplier> cachedSuppliers;
    private ContactsInfoDataMapper contactsInfoDataMapper;
    private FieldsDataMapper fieldsDataMapper;

    public SupplierDataMapper() {
        super("suppliers", new String[]{"bn_number", "name", "bank", "branch", "account_number", "payment_method"});
        cachedSuppliers = new LinkedList<>();
        contactsInfoDataMapper = new ContactsInfoDataMapper();
        fieldsDataMapper = new FieldsDataMapper();
    }

    public void insert(String bnNumber, String name, String bank, String branch, String accountNumber, String paymentMethod) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES(%s, %s, %s, %s, %s, %s)",tableName, columnsString, bnNumber,
                name, bank, branch, accountNumber, paymentMethod));
    }

    public void delete(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DROP FROM %s WHERE bn_number = %s", tableName, bnNumber));
    }

    public Supplier find(String bnNumber) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = %s", columnsString, tableName, bnNumber));
        if(resultSet.next())
            return new Supplier(resultSet.getString("name"), resultSet.getString("bn_number"),
                    new BankAccount(resultSet.getString("bank"), resultSet.getString("branch"), resultSet.getString("account_number")),
                    resultSet.getString("payment_method"), fieldsDataMapper.find(bnNumber), contactsInfoDataMapper.find(bnNumber));
        return null;
    }

    public void updateBankAccount(String bnNumber,String bank, String branch, String accountNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET bank = %s, branch = %s, account_number = %s WHERE bn_number = %s", tableName,
                bank, branch, accountNumber, bnNumber));
    }

    public void updateName(String bnNumber, String name) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET name = %s WHERE bn_number = %s", tableName, name, bnNumber));
    }

    public void updatePaymentMethod(String bnNumber, String paymentMethod) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET payment_method = %s WHERE bn_number = %s", tableName, paymentMethod, bnNumber));
    }
}
