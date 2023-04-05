package BusinessLayer;

import BusinessLayer.DeliveryAgreements.DeliveryAgreement;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SupplierController {
    private List<Supplier> suppliers;

    public void addSupplier(String name, String bnNumber, String bankAccount, String paymentMethod,
                            List<String> fields, Map<String, List<String>> contactsInfo,
                            List<Product> productList, DeliveryAgreement deliveryAgreement){
        suppliers.add(new Supplier(name,bnNumber,bankAccount,paymentMethod,fields, contactsInfo, productList, deliveryAgreement));
    }

    public Supplier getSupplier(String bnNumber){
        return suppliers.stream().filter(supplier -> supplier.getBnNumber().equals(bnNumber)).collect(Collectors.toList()).get(0);
    }

    public List<Supplier> getCopyOfSuppliers(){
        return new LinkedList<>(suppliers);
    }



}
