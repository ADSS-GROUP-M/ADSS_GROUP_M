package Backend.ServiceLayer;

import Backend.BankAccount;
import Backend.BusinessLayer.DeliveryAgreements.DeliveryAgreement;
import Backend.BusinessLayer.Discounts.Discount;
import Backend.BusinessLayer.Pair;
import Backend.BusinessLayer.Product;
import Backend.BusinessLayer.Supplier;
import Backend.BusinessLayer.SupplierController;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class SupplierService {
    private SupplierController supplierController;
    private Gson gson;

    public SupplierService(){
        supplierController = new SupplierController();
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

    public void setSupplierBankAccount(String bnNumber, BankAccount bankAccount){
        supplierController.getSupplier(bnNumber).setBankAccount(bankAccount);
    }

    public void setSupplierPaymentMethod(String bnNumber, String paymentMethod){
        supplierController.getSupplier(bnNumber).setPaymentMethod(paymentMethod);
    }

    public void removeContactInfo(String bnNumber, String contactsName){
        supplierController.getSupplier(bnNumber).removeContactInfo(contactsName);
    }

    public void addContactInfo(String bnNumber, String contactName, String email, String phoneNumber){
        supplierController.getSupplier(bnNumber).addContactInfo(contactName, email, phoneNumber);
    }

    public void setContactsEmail(String bnNumber, String contactName, String email){
        supplierController.getSupplier(bnNumber).setContactsEmail(contactName, email);
    }

    public void setContactsPhoneNumber(String bnNumber, String contactName, String phoneNumber){
        supplierController.getSupplier(bnNumber).setContactsPhoneNumber(contactName, phoneNumber);
    }

    public void setFixedDeliveryAgreement(String bnNumber, boolean haveTransport, List<Integer> days){
        supplierController.getSupplier(bnNumber).getAgreement().setFixedDeliveryAgreement(haveTransport, days);
    }

    public void setByInvitationDeliveryAgreement(String bnNumber, boolean haveTransport, int numOfDays){
        supplierController.getSupplier(bnNumber).getAgreement().setByInvitationDeliveryAgreement(haveTransport, numOfDays);
    }

    public void setProductCatalogNumber(String bnNumber, int productId, String newCatalogNumber){
        supplierController.getSupplier(bnNumber).getAgreement().getProduct(productId).setCatalogNumber(newCatalogNumber);
    }
    public void setProductPrice(String bnNumber, int productId, double price){
        supplierController.getSupplier(bnNumber).getAgreement().getProduct(productId).setPrice(price);
    }
    public void setProductAmount(String bnNumber, int productId, int amount){
        supplierController.getSupplier(bnNumber).getAgreement().getProduct(productId).setNumberOfUnits(amount);
    }

    public void addProduct(String bnNumber, String name, String catalogNumber, double price, int numberOfUnits){
        supplierController.getSupplier(bnNumber).addProduct(name, catalogNumber, price, numberOfUnits);
    }

    public void setProductDiscount(String bnNumber, int productId, int amount, Discount discount){
        supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities().addProductDiscount(productId, amount, discount);
    }
    public void removeProductDiscount(String bnNumber, int productId, int amount){
        supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities().removeProductDiscount(productId, amount);
    }
    public void removeProductDiscount(String bnNumber, int productId){
        supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities().removeProductDiscount(productId);
    }

    public void removeDiscountOnTotalOrder(String bnNumber){
        supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities().removeDiscountOnTotalOrder();
    }

    public void setDiscountOnTotalOrder(String bnNumber, double priceToActivateDiscount, Discount discount){
        supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities().setDiscountOnTotalOrder(priceToActivateDiscount, discount);
    }

    public void setOrderOfDiscounts(String bnNumber, boolean amountBeforeTotal){
        supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities().setOrderOfDiscounts(amountBeforeTotal);
    }

    public void setProductsDiscounts(String bnNumber, Map<Integer, Map<Integer, Discount>> productsDiscounts){
        supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities().setProductsDiscounts(productsDiscounts);
    }

    public void setDiscountOnAmountOfProducts(String bnNumber, int amountOfProductsForDiscount, Discount discount){
        supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities().setDiscountOnAmountOfProducts(amountOfProductsForDiscount, discount);
    }

    public void removeDiscountOnAmountOfProducts(String bnNumber){
        supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities().removeDiscountOnAmountOfProducts();
    }

    public void getOrderHistory(String bnNumber){
        supplierController.getSupplier(bnNumber).getOrderHistory();
    }

    public void addField(String bnNumber, String field){
        supplierController.getSupplier(bnNumber).addField(field);
    }

    public void removeField(String bnNumber, String field){
        supplierController.getSupplier(bnNumber).removeField(field);
    }

    public void getCatalogNumber(String bnNumber, int productId){
        supplierController.getSupplier(bnNumber).getAgreement().getProduct(productId).getCatalogNumber();
    }

    public void getDeliveryAgreement(String bnNumber){
        supplierController.getSupplier(bnNumber).getAgreement().getDeliveryAgreement();
    }
    public void removeProduct(String bnNumber, Integer productId){
        supplierController.getSupplier(bnNumber).getAgreement().removeProduct(productId);
    }
}
