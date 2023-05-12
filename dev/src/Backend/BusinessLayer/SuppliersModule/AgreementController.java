package Backend.BusinessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryAgreement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgreementController {
    private static AgreementController instance;
    private Map<String, Agreement> supplierAgreement;
    private AgreementController(){
        supplierAgreement = new HashMap<>();
    }

    public static AgreementController getInstance(){
        if(instance == null)
            instance = new AgreementController();
        return instance;
    }

    public void addAgreement(String bnNumber, List<Product> productsList, DeliveryAgreement deliveryAgreement){
        supplierAgreement.put(bnNumber, new Agreement(productsList, deliveryAgreement));
    }

    private Agreement getAgreement(String bnNumber){
        return supplierAgreement.get(bnNumber);
    }

    public Map<String, Product> getProducts(String bnNumber) {
        return supplierAgreement.get(bnNumber).getProducts();
    }

    public Product getProduct(String bnNumber, String catalogNumber){
        return getAgreement(bnNumber).getProduct(catalogNumber);
    }

    public DeliveryAgreement getDeliveryAgreement(String bnNumber){
        return getAgreement(bnNumber).getDeliveryAgreement();
    }

    public void setFixedDeliveryAgreement(String bnNumber, boolean haveTransport, List<Integer> days){
        getAgreement(bnNumber).setFixedDeliveryAgreement(haveTransport, days);
    }

    public void setByInvitationDeliveryAgreement(String bnNumber, boolean haveTransport, int numOfDays){
        getAgreement(bnNumber).setByInvitationDeliveryAgreement(haveTransport, numOfDays);
    }

    public void addProduct(String bnNumber, String catalogNumber, String supplierCatalogNumber,double price, int numberOfUnits){
        getAgreement(bnNumber).addProduct(catalogNumber, supplierCatalogNumber, price, numberOfUnits);
    }

    public void removeProduct(String bnNumber, String catalogNumber){
        getAgreement(bnNumber).removeProduct(catalogNumber);
    }

    public boolean productExist(String bnNumber, String catalogNumber){
        return getAgreement(bnNumber).productExist(catalogNumber);
    }

    public void setSuppliersCatalogNumber(String bnNumber, String catalogNumber, String suppliersCatalogNumber){
        getAgreement(bnNumber).setSuppliersCatalogNumber(catalogNumber, suppliersCatalogNumber);
    }

    public void setPrice(String bnNumber, String catalogNumber, double price){
        getAgreement(bnNumber).setPrice(catalogNumber, price);
    }

    public void setNumberOfUnits(String bnNumber, String catalogNumber, int numberOfUnits){
        getAgreement(bnNumber).setNumberOfUnits(catalogNumber, numberOfUnits);
    }

    public String getSuppliersCatalogNumber(String bnNumber, String catalogNumber) {
        return getAgreement(bnNumber).getSuppliersCatalogNumber(catalogNumber);
    }

    public int getNumberOfUnits(String bnNumber, String catalogNumber){
        return getAgreement(bnNumber).getNumberOfUnits(catalogNumber);
    }

    public void removeAgreement(String bnNumber){
        supplierAgreement.remove(bnNumber);
    }

    public String toString(String bnNumber){
        return getAgreement(bnNumber).toString();
    }
}
