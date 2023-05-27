package businessLayer.suppliersModule;

import dataAccessLayer.suppliersModule.SuppliersDataMappers.SupplierDataMapper;
import exceptions.DalException;
import exceptions.SupplierException;

import java.util.List;
import java.util.Map;

public class SupplierController {
    /***
     * maps between supplier's bn number to the supplier
     */
    private final SupplierDataMapper supplierDataMapper;

    public SupplierController(SupplierDataMapper supplierDataMapper){
        this.supplierDataMapper = supplierDataMapper;
    }

    public void addSupplier(String name, String bnNumber, BankAccount bankAccount, String paymentMethod,
                            List<String> fields, Map<String,Pair<String, String>> contactsInfo) throws SupplierException {
        try {
            supplierDataMapper.insert(bnNumber, name, bankAccount.getBank(), bankAccount.getBranch(),
                    bankAccount.getAccountNumber(), paymentMethod, fields, contactsInfo);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public Supplier getSupplier(String bnNumber) throws SupplierException {
        try {
            return supplierDataMapper.getSupplier(bnNumber);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public List<Supplier> getCopyOfSuppliers() throws SupplierException {
        try {
            return supplierDataMapper.getCopyOfSuppliers();
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void removeSupplier(String bnNumber) throws SupplierException {
        try {
            supplierDataMapper.removeSupplier(bnNumber);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public boolean supplierExists(String bnNumber) throws SupplierException {
        return getSupplier(bnNumber) != null;
    }

    public void setName(String bnNumber, String name) throws SupplierException {
        try {
            Supplier supplier = getSupplier(bnNumber);
            supplierDataMapper.updateName(bnNumber, name);
            supplier.setName(name);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void setBankAccount(String bnNumber, String bank, String branch, String accountNumber) throws SupplierException {
        try {
            Supplier supplier = getSupplier(bnNumber);
            supplierDataMapper.updateBankAccount(bnNumber, bank, branch, accountNumber);
            supplier.setBankAccount(bank, branch, accountNumber);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void setBnNumber(String bnNumber, String newBnNumber) throws SupplierException {
        try {
            supplierDataMapper.updateBnNumber(bnNumber, newBnNumber);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void removeContactInfo(String bnNumber, String contactsEmail) throws SupplierException {
        try {
            Supplier supplier = getSupplier(bnNumber);
            supplierDataMapper.removeContactInfo(bnNumber, contactsEmail);
            supplier.removeContactInfo(contactsEmail);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }
    public void addContactInfo(String bnNumber, String contactName, String email, String phoneNumber) throws SupplierException {
        try {
            Supplier supplier = getSupplier(bnNumber);
            supplierDataMapper.addContactInfo(bnNumber, contactName, email, phoneNumber);
            supplier.addContactInfo(contactName, email, phoneNumber);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void setContactsEmail(String bnNumber, String email, String newEmail) throws SupplierException {
        try {
            Supplier supplier = getSupplier(bnNumber);
            supplierDataMapper.setContactsEmail(bnNumber, email, newEmail);
            supplier.setContactsEmail(email, newEmail);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }
    public void setContactsPhoneNumber(String bnNumber, String email, String phoneNumber) throws SupplierException {
        try {
            Supplier supplier = getSupplier(bnNumber);
            supplierDataMapper.setContactsPhoneNumber(bnNumber, email, phoneNumber);
            supplier.setContactsPhoneNumber(email, phoneNumber);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void addField(String bnNumber, String field) throws SupplierException {
        try {
            Supplier supplier = getSupplier(bnNumber);
            supplierDataMapper.addField(bnNumber, field);
            supplier.addField(field);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }



    public void removeField(String bnNumber, String field) throws SupplierException {
        try {
            Supplier supplier = getSupplier(bnNumber);
            supplierDataMapper.removeField(bnNumber, field);
            supplier.removeField(field);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void setPaymentMethod(String bnNumber, String paymentMethod) throws SupplierException {
        if(!paymentMethod.equals("net") || !paymentMethod.equals("net 30 EOM ") || !paymentMethod.equals("new 60 EOM")) {
            throw new SupplierException("invalid payment method");
        }
        try {
            Supplier supplier = getSupplier(bnNumber);
            supplierDataMapper.updatePaymentMethod(bnNumber, paymentMethod);
            supplier.setPaymentMethod(paymentMethod);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }
    public String getName(String bnNumber) throws SupplierException {
        return getSupplier(bnNumber).getName();
    }



}
