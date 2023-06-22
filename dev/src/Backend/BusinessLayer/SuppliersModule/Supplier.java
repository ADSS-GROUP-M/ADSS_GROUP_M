package Backend.BusinessLayer.SuppliersModule;


import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryAgreement;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Supplier {
    private String name;
    private String bnNumber;
    private BankAccount bankAccount;
    private List<String> fields;
    private String paymentMethod;
    /***
     * maps between the email of the contact person and his contact info - name, phone number
     */
    private Map<String,Pair<String, String>> contactsInfo;

    /***
     * all the orders that have been ordered from the supplier
     */
    private List<Order> orderHistory;
    public Supplier(String name, String bnNumber, BankAccount bankAccount, String paymentMethod,
                    List<String> fields, Map<String,Pair<String, String>> contactsInfo){
        this.name = name;
        this.bnNumber = bnNumber;
        this.bankAccount = bankAccount;
        this.fields = fields;
        this.contactsInfo = contactsInfo;
        this.paymentMethod = paymentMethod;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setBankAccount(String bank, String branch, String accountNumber) {
        this.bankAccount = new BankAccount(bank, branch, accountNumber);
    }

    public void setBnNumber(String bnNumber) {
        this.bnNumber = bnNumber;
    }

    public void removeContactInfo(String contactsEmail) {
        contactsInfo.remove(contactsEmail);
    }
    public void addContactInfo(String contactName, String email, String phoneNumber){
        this.contactsInfo.put(email, new Pair<>(contactName, phoneNumber));
    }

    public void setContactsEmail(String email, String newEmail){
        Pair<String, String> temp = contactsInfo.get(email);
        contactsInfo.remove(email);
        contactsInfo.put(newEmail, temp);
    }
    public void setContactsPhoneNumber(String email, String phoneNumber){
        contactsInfo.get(email).setSecond(phoneNumber);
    }

    public void addField(String field){
        if(!fields.contains(field))
            fields.add(field);
    }



    public void removeField(String field){
        fields.remove(field);
    }

    public String getBnNumber(){
        return bnNumber;
    }

    public List<Order> getOrderHistory(){
        return orderHistory;
    }

    public String getName() {
        return name;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public String toString(){
        String contactsInfo = "CONTACTS INFORMATION:";
        for(Map.Entry<String, Pair<String, String>> contact : this.contactsInfo.entrySet())
            contactsInfo = contactsInfo + "\n\t\tName: " + contact.getValue().getFirst() + " Email: " + contact.getKey() + " Phone number: " + contact.getValue().getSecond();
        String fields = "FIELDS:";
        for (String field : this.fields)
            fields = fields + "\n\t\t" + field;
        String bankAccount = "BANK ACCOUNT:\n\t\t" + this.bankAccount.toString();

        String res = "SUPPLIER:\n\tNAME: " + name +"\n\tBN NUMBER: " + bnNumber + "\n\t" + bankAccount + "\n\t" + fields
                + "\n\t" + contactsInfo + "\n\t" + "PAYMENT METHOD: " + paymentMethod +"\n\t";
        return res;
    }

    public boolean equals(Object other){
        if(!(other instanceof  Supplier))
            return false;
        Supplier otherSupplier = (Supplier) other;
        if(bnNumber.equals(otherSupplier.bnNumber))
            return true;
        return false;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public List<String> getFields() {
        return fields;
    }

    public String getPaymentMethod() {return paymentMethod;
    }
}
