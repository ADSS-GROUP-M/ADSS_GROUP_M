package businessLayer.suppliersModule;

import dataAccessLayer.suppliersModule.SuppliersDataMappers.SupplierDataMapper;
import dataAccessLayer.dalUtils.DalException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SupplierController {
    /***
     * maps between supplier's bn number to the supplier
     */
    private SupplierDataMapper supplierDataMapper;

    private static SupplierController supplierController;
    private SupplierController(){
        supplierDataMapper = new SupplierDataMapper();
    }
    public static SupplierController getInstance(){
        if(supplierController == null)
            supplierController = new SupplierController();
        return supplierController;
    }

    public void addSupplier(String name, String bnNumber, BankAccount bankAccount, String paymentMethod,
                            List<String> fields, Map<String,Pair<String, String>> contactsInfo) throws SQLException {
        supplierDataMapper.insert(bnNumber, name, bankAccount.getBank(), bankAccount.getBranch(),
                bankAccount.getAccountNumber(), paymentMethod, fields, contactsInfo);
    }

    public Supplier getSupplier(String bnNumber) throws SQLException, DalException {
        return supplierDataMapper.getSupplier(bnNumber);
    }

    public List<Supplier> getCopyOfSuppliers() throws SQLException {
        return supplierDataMapper.getCopyOfSuppliers();
    }

    public void removeSupplier(String bnNumber) throws SQLException {
        supplierDataMapper.removeSupplier(bnNumber);
    }

    public boolean supplierExists(String bnNumber) throws SQLException, DalException {
        return getSupplier(bnNumber) != null;
    }

    public void setName(String bnNumber, String name) throws SQLException, DalException {
        Supplier supplier = getSupplier(bnNumber);
        supplierDataMapper.updateName(bnNumber, name);
        supplier.setName(name);
    }

    public void setBankAccount(String bnNumber, String bank, String branch, String accountNumber) throws SQLException, DalException {
        Supplier supplier = getSupplier(bnNumber);
        supplierDataMapper.updateBankAccount(bnNumber, bank, branch, accountNumber);
        supplier.setBankAccount(bank, branch, accountNumber);
    }

    public void setBnNumber(String bnNumber, String newBnNumber) throws SQLException, DalException {
        supplierDataMapper.updateBnNumber(bnNumber, newBnNumber);
    }

    public void removeContactInfo(String bnNumber, String contactsEmail) throws SQLException, DalException {
        Supplier supplier = getSupplier(bnNumber);
        supplierDataMapper.removeContactInfo(bnNumber, contactsEmail);
        supplier.removeContactInfo(contactsEmail);
    }
    public void addContactInfo(String bnNumber, String contactName, String email, String phoneNumber) throws SQLException, DalException {
        Supplier supplier = getSupplier(bnNumber);
        supplierDataMapper.addContactInfo(bnNumber, contactName, email, phoneNumber);
        supplier.addContactInfo(contactName, email, phoneNumber);
    }

    public void setContactsEmail(String bnNumber, String email, String newEmail) throws SQLException, DalException {
        Supplier supplier = getSupplier(bnNumber);
        supplierDataMapper.setContactsEmail(bnNumber, email, newEmail);
        supplier.setContactsEmail(email, newEmail);
    }
    public void setContactsPhoneNumber(String bnNumber, String email, String phoneNumber) throws SQLException, DalException {
        Supplier supplier = getSupplier(bnNumber);
        supplierDataMapper.setContactsPhoneNumber(bnNumber, email, phoneNumber);
        supplier.setContactsPhoneNumber(email, phoneNumber);
    }

    public void addField(String bnNumber, String field) throws SQLException, DalException {
        Supplier supplier = getSupplier(bnNumber);
        supplierDataMapper.addField(bnNumber, field);
        supplier.addField(field);
    }



    public void removeField(String bnNumber, String field) throws SQLException, DalException {
        Supplier supplier = getSupplier(bnNumber);
        supplierDataMapper.removeField(bnNumber, field);
        supplier.removeField(field);
    }

    public void setPaymentMethod(String bnNumber, String paymentMethod) throws SQLException, DalException {
        if(!paymentMethod.equals("net") || !paymentMethod.equals("net 30 EOM ") || !paymentMethod.equals("new 60 EOM"))
            throw new RuntimeException("invalid payment method");
        Supplier supplier = getSupplier(bnNumber);
        supplierDataMapper.updatePaymentMethod(bnNumber, paymentMethod);
        supplier.setPaymentMethod(paymentMethod);
    }
    public String getName(String bnNumber) throws SQLException, DalException {
        return getSupplier(bnNumber).getName();
    }



}
