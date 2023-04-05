package BusinessLayer;

import BusinessLayer.DeliveryAgreements.DeliveryAgreement;

import java.util.List;
import java.util.Map;

public class Supplier {
    private String name;
    public String bnNumber;
    private String bankAccount;
    private String paymentMethod;
    private List<String> fields;
    /***
     * maps between the name of the contact person and his contact info - email, phone number, etc
     */
    private Map<String,List<String>> contactsInfo;
    /***
     * the agreement with the supplier
     */
    private Agreement agreement;
    /***
     * all the orders that have been ordered from the supplier
     */
    private List<Order> orderHistory;
    public Supplier(String name, String bnNumber, String bankAccount, String paymentMethod,
                    List<String> fields, Map<String, List<String>> contactsInfo,
                    List<Product> productList, DeliveryAgreement deliveryAgreement){
        this.name = name;
        this.bnNumber = bnNumber;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
        this.fields = fields;
        this.contactsInfo = contactsInfo;
        agreement = new Agreement(paymentMethod, productList, deliveryAgreement);
    }

    public void setName(String name){
        this.name = name;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setBnNumber(String bnNumber) {
        this.bnNumber = bnNumber;
    }

    public void removeContactInfo(String contactsName) {
        contactsInfo.remove(contactsName);
    }
    public void addContactInfo(String contactName, List<String> contactInfo){
        this.contactsInfo.put(contactName, contactInfo);
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void addProduct(Product product, int amount){
        agreement.addProduct(product, amount);
    }

    public void setPaymentMethod(String paymentMethod){
        this.paymentMethod = paymentMethod;
        agreement.setPaymentMethod(paymentMethod);
    }

    public void addField(String field){
        if(!fields.contains(field))
            fields.add(field);
    }

    public void removeField(String field){
        fields.remove(field);
    }

    public void addOrder(Order order){
        orderHistory.add(order);
    }
    public String getBnNumber(){
        return bnNumber;
    }

    public List<Order> getOrderHistory(){
        return orderHistory;
    }
}
