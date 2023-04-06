package BusinessLayer;

import BusinessLayer.DeliveryAgreements.DeliveryAgreement;
import BusinessLayer.DeliveryAgreements.DeliveryByInvitation;
import BusinessLayer.DeliveryAgreements.DeliveryFixedDays;
import BusinessLayer.Discounts.Discount;

import java.util.List;
import java.util.Map;

/***
 * controls the system functionality
 */
public class SystemController {
    private SupplierController supplierController;
    private OrderController orderController;
    public SystemController(){
        supplierController = new SupplierController();
        orderController = new OrderController();
    }
    public void addSupplier(String name, String bnNumber, String bankAccount, String paymentMethod,
                            List<String> fields, Map<String,Pair<String, String>> contactsInfo,
                            List<Product> productList, DeliveryAgreement deliveryAgreement){
        supplierController.addSupplier(name,bnNumber,bankAccount,paymentMethod,fields, contactsInfo, productList, deliveryAgreement);
    }

    public void order(Map<Integer, Integer> order){
        orderController.order(order, supplierController.getCopyOfSuppliers());
    }

    public void setSupplierName(String bnNumber, String name){
        supplierController.getSupplier(bnNumber).setName(name);
    }

    public void setSupplierBnNumber(String bnNumber, String newBnNumber){
        Supplier supplier = supplierController.getSupplier(bnNumber);
        supplierController.removeSupplier(bnNumber);
        supplier.setBnNumber(newBnNumber);
        supplierController.addSupplier(bnNumber, supplier);
    }

    public void setSupplierBankAccount(String bnNumber, String bankAccount){
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

    public void addProduct(String bnNumber, String name, int productId, String catalogNumber, double price, int numberOfUnits){
        supplierController.getSupplier(bnNumber).addProduct(name, productId, catalogNumber, price, numberOfUnits);
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

    public void setDiscountOnAmountOfProducts(String bnNumber, int amountOfProductsForDiscount, double price, Discount discount){
        supplierController.getSupplier(bnNumber).getAgreement().getBillOfQuantities().setDiscountOnAmountOfProducts(amountOfProductsForDiscount, price, discount);
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
