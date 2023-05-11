package Backend.ServiceLayer.SuppliersModule;


import Backend.BusinessLayer.SuppliersModule.*;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryAgreement;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryByInvitation;
import Backend.BusinessLayer.SuppliersModule.Discounts.CashDiscount;
import Backend.BusinessLayer.SuppliersModule.Discounts.Discount;
import Backend.BusinessLayer.SuppliersModule.Discounts.PercentageDiscount;
import com.google.gson.Gson;

import java.util.*;

public class SupplierService {
    private SupplierController supplierController;
    private Gson gson;

    public SupplierService(){
        supplierController = SupplierController.getInstance();
        gson = new Gson();
    }

    public String addSupplier(String name, String bnNumber, BankAccount bankAccount, String paymentMethod,
                              List<String> fields, Map<String, Pair<String, String>> contactsInfo,
                              List<Product> productList, DeliveryAgreement deliveryAgreement){
        supplierController.addSupplier(name,bnNumber,bankAccount,paymentMethod,fields, contactsInfo, productList, deliveryAgreement);
        return gson.toJson(new Response<>("new Supplier Registered!", false));
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
            supplierController.getSupplier(bnNumber).setName(name);
            return gson.toJson(new Response<String>("supplier's name is set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setSupplierBnNumber(String bnNumber, String newBnNumber){
        try {
            Supplier supplier = supplierController.getSupplier(bnNumber);
            supplierController.removeSupplier(bnNumber);
            supplier.setBnNumber(newBnNumber);
            supplierController.addSupplier(bnNumber, supplier);
            return gson.toJson(new Response<String>("supplier's bn number is set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setSupplierBankAccount(String bnNumber, String bank, String branch, String accountNumber){
        try {
            supplierController.getSupplier(bnNumber).setBankAccount(bank, branch, accountNumber);
            return gson.toJson(new Response<String>("supplier's bank account is set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setSupplierPaymentMethod(String bnNumber, String paymentMethod){
        try {
            supplierController.getSupplier(bnNumber).setPaymentMethod(paymentMethod);
            return gson.toJson(new Response<String>("supplier's payment method is set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removeContactInfo(String bnNumber, String contactsName){
        try {
            supplierController.getSupplier(bnNumber).removeContactInfo(contactsName);
            return gson.toJson(new Response<String>("contact " + contactsName + " removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String addContactInfo(String bnNumber, String contactName, String email, String phoneNumber){
        try {
            supplierController.getSupplier(bnNumber).addContactInfo(contactName, email, phoneNumber);
            return gson.toJson(new Response<String>("contact info is added!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setContactsEmail(String bnNumber, String email, String newEmail){
        try {
            supplierController.getSupplier(bnNumber).setContactsEmail(email, newEmail);
            return gson.toJson(new Response<String>("contact email is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setContactsPhoneNumber(String bnNumber, String email, String phoneNumber){
        try {
            supplierController.getSupplier(bnNumber).setContactsPhoneNumber(email, phoneNumber);
            return gson.toJson(new Response<String>("contact phone number is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setFixedDeliveryAgreement(String bnNumber, boolean haveTransport, List<Integer> days){
        try {
            supplierController.getSupplier(bnNumber).getAgreement().setFixedDeliveryAgreement(haveTransport, days);
            return gson.toJson(new Response<String>("delivery agreement is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setByInvitationDeliveryAgreement(String bnNumber, boolean haveTransport, int numOfDays){
        try {
            supplierController.getSupplier(bnNumber).getAgreement().setByInvitationDeliveryAgreement(haveTransport, numOfDays);
            return gson.toJson(new Response<String>("contact phone number is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setProductCatalogNumber(String bnNumber, int productId, String newCatalogNumber){
        try {
            if(!supplierController.getSupplier(bnNumber).productExist(productId))
                throw new RuntimeException("the supplier does not supply product - " + productId);
            supplierController.getSupplier(bnNumber).getAgreement().getProduct(productId).setSuppliersCatalogNumber(newCatalogNumber);
            return gson.toJson(new Response<String>("product catalog number is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }
    public String setProductPrice(String bnNumber, int productId, double price){
        try {
            if(!supplierController.getSupplier(bnNumber).productExist(productId))
                throw new RuntimeException("the supplier does not supply product - " + productId);
            supplierController.getSupplier(bnNumber).getAgreement().getProduct(productId).setPrice(price);
            return gson.toJson(new Response<String>("product price is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }
    public String setProductAmount(String bnNumber, int productId, int amount){
        try {
            if(!supplierController.getSupplier(bnNumber).productExist(productId))
                throw new RuntimeException("the supplier does not supply product - " + productId);
            supplierController.getSupplier(bnNumber).getAgreement().getProduct(productId).setNumberOfUnits(amount);
            return gson.toJson(new Response<String>("product number of units is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String addProduct(String bnNumber, String name, String catalogNumber, double price, int numberOfUnits){
        try {
            supplierController.getSupplier(bnNumber).addProduct(name, catalogNumber, price, numberOfUnits);
            return gson.toJson(new Response<String>("product is added!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setProductDiscount(String bnNumber, int productId, int amount, Discount discount){
        try {
            if(!supplierController.getSupplier(bnNumber).productExist(productId))
                throw new RuntimeException("the supplier does not supply product - " + productId);
            supplierController.getSupplier(bnNumber).getAgreement().BillOfQuantities().addProductDiscount(productId, amount, discount);
            return gson.toJson(new Response<String>("product discount is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }
    public String removeProductDiscount(String bnNumber, int productId, int amount){
        try {
            if(!supplierController.getSupplier(bnNumber).productExist(productId))
                throw new RuntimeException("the supplier does not supply product - " + productId);
            if(supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities() != null)
                supplierController.getSupplier(bnNumber).getAgreement().BillOfQuantities().removeProductDiscount(productId, amount);
            else
                return gson.toJson(new Response<String>("there are no products discounts!",true));
            return gson.toJson(new Response<String>("product discount removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }
    public String removeProductDiscounts(String bnNumber, int productId){
        try {
            if(!supplierController.getSupplier(bnNumber).productExist(productId))
                throw new RuntimeException("the supplier does not supply product - " + productId);
            if(supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities() != null)
                supplierController.getSupplier(bnNumber).getAgreement().BillOfQuantities().removeProductDiscount(productId);
            else
                return gson.toJson(new Response<String>("there are no products discounts!",true));
            return gson.toJson(new Response<String>("product discount removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removeDiscountOnTotalOrder(String bnNumber){
        try {
            if(supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities() != null)
                supplierController.getSupplier(bnNumber).getAgreement().BillOfQuantities().removeDiscountOnTotalOrder();
            else
                return gson.toJson(new Response<String>("there are no discounts!",true));
            return gson.toJson(new Response<String>("product discount on total order removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setDiscountOnTotalOrder(String bnNumber, double priceToActivateDiscount, Discount discount){
        try {
            supplierController.getSupplier(bnNumber).getAgreement().BillOfQuantities().setDiscountOnTotalOrder(priceToActivateDiscount, discount);
            return gson.toJson(new Response<String>("product discount on total order is set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setOrderOfDiscounts(String bnNumber, boolean amountBeforeTotal){
        try {
            supplierController.getSupplier(bnNumber).getAgreement().BillOfQuantities().setOrderOfDiscounts(amountBeforeTotal);
            return gson.toJson(new Response<String>("order of discount id set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setProductsDiscounts(String bnNumber, Map<Integer, Map<Integer, Discount>> productsDiscounts){
        try {
            supplierController.getSupplier(bnNumber).getAgreement().BillOfQuantities().setProductsDiscounts(productsDiscounts);
            return gson.toJson(new Response<String>("product discounts are set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setDiscountOnAmountOfProducts(String bnNumber, int amountOfProductsForDiscount, Discount discount){
        try {
            supplierController.getSupplier(bnNumber).getAgreement().BillOfQuantities().setDiscountOnAmountOfProducts(amountOfProductsForDiscount, discount);
            return gson.toJson(new Response<String>("product discount on amount of products is set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removeDiscountOnAmountOfProducts(String bnNumber){
        try {
            if(supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities() != null)
                supplierController.getSupplier(bnNumber).getAgreement().BillOfQuantities().removeDiscountOnAmountOfProducts();
            else
                return gson.toJson(new Response<String>("there are no discounts!",true));
            return gson.toJson(new Response<String>("product discount on amount of products is removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String getOrderHistory(String bnNumber){
        try {
            return gson.toJson(new Response<List<Order>>(supplierController.getSupplier(bnNumber).getOrderHistory()));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String addField(String bnNumber, String field){
        try {
            supplierController.getSupplier(bnNumber).addField(field);
            return gson.toJson(new Response<String>("field was added!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removeField(String bnNumber, String field){
        try {
            supplierController.getSupplier(bnNumber).removeField(field);
            return gson.toJson(new Response<String>("field was removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String getCatalogNumber(String bnNumber, int productId){
        try {
            return gson.toJson(new Response<String>(supplierController.getSupplier(bnNumber).getAgreement().getProduct(productId).getSuppliersCatalogNumber()));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String getDeliveryAgreement(String bnNumber){
        try {
            return gson.toJson(new Response<String>(supplierController.getSupplier(bnNumber).getAgreement().getDeliveryAgreement().toString2()));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }
    public String removeProduct(String bnNumber, Integer productId){
        try {
            if(!supplierController.getSupplier(bnNumber).productExist(productId))
                throw new RuntimeException("the supplier does not supply product - " + productId);
            supplierController.getSupplier(bnNumber).getAgreement().removeProduct(productId);
            return gson.toJson(new Response<String>("product was removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String getSupplierDetails(String bnNumber){
        try {
            return gson.toJson(new Response<String>(supplierController.getSupplier(bnNumber).toString(), false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }



    public String getSuppliersName(String bnNumber){
        try {
            return gson.toJson(new Response<>(supplierController.getSupplier(bnNumber).getName(), false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String loadData(){
        try {
            supplierController.addSupplier("yossi", "1234", new BankAccount("leumi", "789", "12344"),
                    "net", new ArrayList<>(){{add("food");}}, new HashMap<String, Pair<String, String>>(){{put("elay", new Pair<>("elay@gmail.com", "054487956"));}}
            , new ArrayList<>(){{add(new Product("milk", "1", 12.5, 1200));add(new Product("coffee", "2", 14, 1500));}}
                    ,new DeliveryByInvitation(true, 3));
            supplierController.addSupplier("Elay", "12345", new BankAccount("leumi", "132", "12344"),
                    "net", new ArrayList<>(){{add("food");}}, new HashMap<String, Pair<String, String>>(){{put("yossi", new Pair<>("yossi@gmail.com", "054488956"));}}
            , new ArrayList<>(){{add(new Product("coffee", "2", 10, 1500));
                            add(new Product("pizza", "3", 22, 1300));}}
                    ,new DeliveryByInvitation(true, 3));
            supplierController.addSupplier("Ilay", "123", new BankAccount("leumi", "125", "12344"),
                    "net", new ArrayList<>(){{add("food");}}, new HashMap<String, Pair<String, String>>(){{put("Noam", new Pair<>("Noam@gmail.com", "053488956"));}}
            , new ArrayList<>(){{add(new Product("pizza", "1", 20, 1200));add(new Product("coffee", "2", 10, 1500));}}
                    ,new DeliveryByInvitation(true, 3));
            setProductsDiscounts("1234", new HashMap<>(){{put(0, new HashMap<>(){{put(5, new PercentageDiscount(15));
            put(10, new CashDiscount(15));}});}});
            setDiscountOnTotalOrder("12345", 1500, new PercentageDiscount(20));
            return gson.toJson(new Response<>("data is loaded!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }
}
