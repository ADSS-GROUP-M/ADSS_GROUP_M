package serviceLayer.suppliersModule;


import businessLayer.suppliersModule.*;
import businessLayer.suppliersModule.DeliveryAgreements.DeliveryAgreement;
import com.google.gson.Gson;

import java.util.*;

public class SupplierService {
    private SupplierController supplierController;
    private AgreementController agreementController;
    private Gson gson;

    public SupplierService(){
        supplierController = SupplierController.getInstance();
        agreementController = AgreementController.getInstance();
        gson = new Gson();
    }

    public String addSupplier(String name, String bnNumber, BankAccount bankAccount, String paymentMethod,
                              List<String> fields, Map<String, Pair<String, String>> contactsInfo,
                              List<Product> productList, DeliveryAgreement deliveryAgreement) {
        try {
            supplierController.addSupplier(name,bnNumber,bankAccount,paymentMethod,fields, contactsInfo);
            agreementController.addAgreement(bnNumber, productList, deliveryAgreement);
            return gson.toJson(new Response<>("new Supplier Registered!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removeSupplier(String bnNumber){
        try {
            supplierController.removeSupplier(bnNumber);
            return gson.toJson(new Response<>("supplier was removed", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setSupplierName(String bnNumber, String name){
        try {
            supplierController.setName(bnNumber, name);
            return gson.toJson(new Response<String>("supplier's name is set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setSupplierBnNumber(String bnNumber, String newBnNumber){
        try {
            supplierController.setBnNumber(bnNumber, newBnNumber);
            return gson.toJson(new Response<String>("supplier's bn number is set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setSupplierBankAccount(String bnNumber, String bank, String branch, String accountNumber){
        try {
            supplierController.setBankAccount(bnNumber, bank, branch, accountNumber);
            return gson.toJson(new Response<String>("supplier's bank account is set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setSupplierPaymentMethod(String bnNumber, String paymentMethod){
        try {
            supplierController.setPaymentMethod(bnNumber, paymentMethod);
            return gson.toJson(new Response<String>("supplier's payment method is set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removeContactInfo(String bnNumber, String contactsEmail){
        try {
            supplierController.removeContactInfo(bnNumber, contactsEmail);
            return gson.toJson(new Response<String>("contact " + contactsEmail + " removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String addContactInfo(String bnNumber, String contactName, String email, String phoneNumber){
        try {
            supplierController.addContactInfo(bnNumber, contactName, email, phoneNumber);
            return gson.toJson(new Response<String>("contact info is added!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setContactsEmail(String bnNumber, String email, String newEmail){
        try {
            supplierController.setContactsEmail(bnNumber, email, newEmail);
            return gson.toJson(new Response<String>("contact email is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setContactsPhoneNumber(String bnNumber, String email, String phoneNumber){
        try {
            supplierController.setContactsPhoneNumber(bnNumber, email, phoneNumber);
            return gson.toJson(new Response<String>("contact phone number is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }



    public String addField(String bnNumber, String field){
        try {
            supplierController.addField(bnNumber, field);
            return gson.toJson(new Response<String>("field was added!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removeField(String bnNumber, String field){
        try {
            supplierController.removeField(bnNumber, field);
            return gson.toJson(new Response<String>("field was removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String getSupplierDetails(String bnNumber){
        try {
            return gson.toJson(new Response<String>(supplierController.getSupplier(bnNumber).toString() + agreementController.toString(bnNumber) + BillOfQuantitiesController.getInstance().toString(bnNumber), false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String getSuppliersName(String bnNumber){
        try {
            return gson.toJson(new Response<>(supplierController.getName(bnNumber), false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String loadData(){
//        try {
//            supplierController.addSupplier("yossi", "1234", new BankAccount("leumi", "789", "12344"),
//                    "net", new ArrayList<>(){{add("food");}}, new HashMap<String, Pair<String, String>>(){{put("elay", new Pair<>("elay@gmail.com", "054487956"));}}
//            , new ArrayList<>(){{add(new Product("milk", "1", 12.5, 1200));add(new Product("coffee", "2", 14, 1500));}}
//                    ,new DeliveryByInvitation(true, 3));
//            supplierController.addSupplier("Elay", "12345", new BankAccount("leumi", "132", "12344"),
//                    "net", new ArrayList<>(){{add("food");}}, new HashMap<String, Pair<String, String>>(){{put("yossi", new Pair<>("yossi@gmail.com", "054488956"));}}
//            , new ArrayList<>(){{add(new Product("coffee", "2", 10, 1500));
//                            add(new Product("pizza", "3", 22, 1300));}}
//                    ,new DeliveryByInvitation(true, 3));
//            supplierController.addSupplier("Ilay", "123", new BankAccount("leumi", "125", "12344"),
//                    "net", new ArrayList<>(){{add("food");}}, new HashMap<String, Pair<String, String>>(){{put("Noam", new Pair<>("Noam@gmail.com", "053488956"));}}
//            , new ArrayList<>(){{add(new Product("pizza", "1", 20, 1200));add(new Product("coffee", "2", 10, 1500));}}
//                    ,new DeliveryByInvitation(true, 3));
//            setProductsDiscounts("1234", new HashMap<>(){{put("0", new HashMap<>(){{put(5, new PercentageDiscount(15));
//            put(10, new CashDiscount(15));}});}});
//            setDiscountOnTotalOrder("12345", 1500, new PercentageDiscount(20));
//            return gson.toJson(new Response<>("data is loaded!", false));
//        }
//        catch (Exception e){
//            return gson.toJson(new Response<>(e.getMessage(), true));
//        }
        return null;
    }
}
