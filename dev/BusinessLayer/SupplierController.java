package BusinessLayer;

import BusinessLayer.DeliveryAgreements.DeliveryAgreement;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SupplierController {
    /***
     * maps between supplier's bn number to the supplier
     */
    private Map<String, Supplier> suppliers;

    public void addSupplier(String name, String bnNumber, String bankAccount, String paymentMethod,
                            List<String> fields, Map<String,Pair<String, String>> contactsInfo,
                            List<Product> productList, DeliveryAgreement deliveryAgreement){
        suppliers.put(bnNumber ,new Supplier(name,bnNumber,bankAccount,paymentMethod,fields, contactsInfo, productList, deliveryAgreement));
    }

    public void addSupplier(String bnNumber, Supplier supplier){
        suppliers.put(bnNumber, supplier);
    }

    public Supplier getSupplier(String bnNumber){
        return suppliers.get(bnNumber);
    }

    public List<Supplier> getCopyOfSuppliers(){
        return new LinkedList<>(suppliers.values());
    }

    public void removeSupplier(String bnNumber){
        suppliers.remove(bnNumber);
    }


}
