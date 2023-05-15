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

    public void addProduct(Product product){
        products.put(product.getCatalogNumber(), product);
    }

    public void removeProduct(String catalogNumber){
        products.remove(catalogNumber);
    }

    public BillOfQuantities getBillOfQuantities(){
        return billOfQuantities;
    }
    public BillOfQuantities BillOfQuantities(){
        if(billOfQuantities == null)
            billOfQuantities = new BillOfQuantities();
        return billOfQuantities;
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
