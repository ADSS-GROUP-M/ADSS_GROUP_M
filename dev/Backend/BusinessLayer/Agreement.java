package Backend.BusinessLayer;

import Backend.BusinessLayer.DeliveryAgreements.DeliveryAgreement;
import Backend.BusinessLayer.DeliveryAgreements.DeliveryByInvitation;
import Backend.BusinessLayer.DeliveryAgreements.DeliveryFixedDays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agreement {
    private String paymentMethod;
    /***
     * products on the agreement - maps between product's id to the product and its amount
     */
    private Map<Integer, Product> products;
    private BillOfQuantities billOfQuantities;
    private DeliveryAgreement deliveryAgreement;

    public Agreement(String paymentMethod, List<Product> productsList, DeliveryAgreement deliveryAgreement){
        this.paymentMethod = paymentMethod;
        products = new HashMap<>();
        for (Product p : productsList)
            products.put(p.getId(), p);
        this.deliveryAgreement = deliveryAgreement;
    }

    public Agreement(String paymentMethod, List<Product> productsList, DeliveryAgreement deliveryAgreement, BillOfQuantities billOfQuantities){
        this(paymentMethod, productsList, deliveryAgreement);
        this.billOfQuantities = billOfQuantities;
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public Product getProduct(int productId){
        return products.get(productId);
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
        products.put(product.getId(), product);
    }

    public void removeProduct(Integer productId){
        products.remove(productId);
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

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String toString(){
        String productsString = "PRODUCTS DETAILS:";
        for (Product product : products.values())
            productsString += "\n\t\t\t" + product.toString();

        return "AGREEMENT:\n\t\t" + productsString + "\n\t\t" + deliveryAgreement.toString() + "\n\t\t" + (billOfQuantities == null ? "" : billOfQuantities.toString());
    }

}
