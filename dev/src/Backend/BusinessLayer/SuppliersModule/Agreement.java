package Backend.BusinessLayer.SuppliersModule;


import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryAgreement;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryByInvitation;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryFixedDays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agreement {

    /***
     * products on the agreement - maps between product's id to the product and its amount
     */
    private Map<String, Product> products;
    private BillOfQuantities billOfQuantities;
    private DeliveryAgreement deliveryAgreement;

    public Agreement(List<Product> productsList, DeliveryAgreement deliveryAgreement){
        products = new HashMap<>();
        for (Product p : productsList)
            products.put(p.getCatalogNumber(), p);
        this.deliveryAgreement = deliveryAgreement;
    }

    public Agreement(List<Product> productsList, DeliveryAgreement deliveryAgreement, BillOfQuantities billOfQuantities){
        this(productsList, deliveryAgreement);
        this.billOfQuantities = billOfQuantities;
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public Product getProduct(String catalogNumber){
        return products.get(catalogNumber);
    }
    public DeliveryAgreement getDeliveryAgreement() {
        return deliveryAgreement;
    }

    public void setFixedDeliveryAgreement(boolean haveTransport, List<Integer> days){
        this.deliveryAgreement = new DeliveryFixedDays(haveTransport, days);
    }

    public void setByInvitationDeliveryAgreement(boolean haveTransport, int numOfDays){
        deliveryAgreement = new DeliveryByInvitation(haveTransport, numOfDays);
    }

    public void addProduct(String catalogNumber, String supplierCatalogNumber, double price, int numberOfUnits){
        products.put(catalogNumber, new Product(catalogNumber, supplierCatalogNumber, price, numberOfUnits));
    }

    public void removeProduct(String catalogNumber){
        products.remove(catalogNumber);
    }

    public boolean productExist(String catalogNumber){
        return getProduct(catalogNumber) != null;
    }
    public void setSuppliersCatalogNumber(String catalogNumber, String newSuppliersCatalogNumber){
        getProduct(catalogNumber).setSuppliersCatalogNumber(newSuppliersCatalogNumber);
    }

    public void setPrice(String catalogNumber, double price){
        getProduct(catalogNumber).setPrice(price);
    }

    public void setNumberOfUnits(String catalogNumber, int numberOfUnits){
        getProduct(catalogNumber).setNumberOfUnits(numberOfUnits);
    }
    public BillOfQuantities getBillOfQuantities(){
        return billOfQuantities;
    }
    public BillOfQuantities BillOfQuantities(){
        if(billOfQuantities == null)
            billOfQuantities = new BillOfQuantities();
        return billOfQuantities;
    }

    public String getSuppliersCatalogNumber(String catalogNumber){
        return products.get(catalogNumber).getSuppliersCatalogNumber();
    }

    public int getNumberOfUnits(String catalogNumber){
        return products.get(catalogNumber).getNumberOfUnits();
    }

    public void setBillOfQuantities(BillOfQuantities billOfQuantities) {
        this.billOfQuantities = billOfQuantities;
    }

    public String toString(){
        String productsString = "PRODUCTS DETAILS:";
        for (Product product : products.values())
            productsString += "\n\t\t\t" + product.toString();

        return "AGREEMENT:\n\t\t" + productsString + "\n\t\t" + deliveryAgreement.toString() + "\n\t\t" + (billOfQuantities == null ? "" : billOfQuantities.toString());
    }

}
