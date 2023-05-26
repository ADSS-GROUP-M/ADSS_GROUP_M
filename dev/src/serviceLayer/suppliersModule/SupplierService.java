package serviceLayer.suppliersModule;


import businessLayer.suppliersModule.*;
import businessLayer.suppliersModule.DeliveryAgreements.DeliveryAgreement;
import com.google.gson.Gson;
import utils.Response;

import java.util.List;
import java.util.Map;

public class SupplierService {
    private final SupplierController supplierController;
    private final AgreementController agreementController;
    private final BillOfQuantitiesController billOfQuantitiesController;
//    private final Gson gson;

    public SupplierService(SupplierController supplierController, AgreementController agreementController, BillOfQuantitiesController billOfQuantitiesController){
        this.supplierController = supplierController;
        this.agreementController = agreementController;
        this.billOfQuantitiesController = billOfQuantitiesController;
//        gson = new Gson();
    }

    public String addSupplier(String name, String bnNumber, BankAccount bankAccount, String paymentMethod,
                              List<String> fields, Map<String, Pair<String, String>> contactsInfo,
                              List<Product> productList, DeliveryAgreement deliveryAgreement) {
        try {
            supplierController.addSupplier(name,bnNumber,bankAccount,paymentMethod,fields, contactsInfo);
            agreementController.addAgreement(bnNumber, productList, deliveryAgreement);
            return new Response("new Supplier Registered!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
        }
    }

    public String removeSupplier(String bnNumber){
        try {
            supplierController.removeSupplier(bnNumber);
            return new  Response("supplier was removed", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
        }
    }

    public String setSupplierName(String bnNumber, String name){
        try {
            supplierController.setName(bnNumber, name);
            return new  Response("supplier's name is set!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
        }
    }

    public String setSupplierBnNumber(String bnNumber, String newBnNumber){
        try {
            supplierController.setBnNumber(bnNumber, newBnNumber);
            return new  Response("supplier's bn number is set!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
        }
    }

    public String setSupplierBankAccount(String bnNumber, String bank, String branch, String accountNumber){
        try {
            supplierController.setBankAccount(bnNumber, bank, branch, accountNumber);
            return new  Response("supplier's bank account is set!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
        }
    }

    public String setSupplierPaymentMethod(String bnNumber, String paymentMethod){
        try {
            supplierController.setPaymentMethod(bnNumber, paymentMethod);
            return new  Response("supplier's payment method is set!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
        }
    }

    public String removeContactInfo(String bnNumber, String contactsEmail){
        try {
            supplierController.removeContactInfo(bnNumber, contactsEmail);
            return new  Response("contact " + contactsEmail + " removed!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
        }
    }

    public String addContactInfo(String bnNumber, String contactName, String email, String phoneNumber){
        try {
            supplierController.addContactInfo(bnNumber, contactName, email, phoneNumber);
            return new  Response("contact info is added!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
        }
    }

    public String setContactsEmail(String bnNumber, String email, String newEmail){
        try {
            supplierController.setContactsEmail(bnNumber, email, newEmail);
            return new  Response("contact email is edited!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
        }
    }

    public String setContactsPhoneNumber(String bnNumber, String email, String phoneNumber){
        try {
            supplierController.setContactsPhoneNumber(bnNumber, email, phoneNumber);
            return new  Response("contact phone number is edited!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
        }
    }



    public String addField(String bnNumber, String field){
        try {
            supplierController.addField(bnNumber, field);
            return new  Response("field was added!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
        }
    }

    public String removeField(String bnNumber, String field){
        try {
            supplierController.removeField(bnNumber, field);
            return new Response("field was removed!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
        }
    }

    public String getSupplierDetails(String bnNumber){
        try {
            return new  Response(supplierController.getSupplier(bnNumber).toString() + agreementController.toString(bnNumber) + billOfQuantitiesController.toString(bnNumber), true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
        }
    }

    public String getSuppliersName(String bnNumber){
        try {
            return new  Response(supplierController.getName(bnNumber), true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toString();
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
//            return gson.toJson(new  Response("data is loaded!", false));
//        }
//        catch (Exception e){
//            return gson.toJson(new  Response(e.getMessage(), true));
//        }
        return null;
    }
}
