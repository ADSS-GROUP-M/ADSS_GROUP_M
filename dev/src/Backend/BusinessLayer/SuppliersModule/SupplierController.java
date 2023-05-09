package Backend.BusinessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryAgreement;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SupplierController {
    /***
     * maps between supplier's bn number to the supplier
     */
    private Map<String, Supplier> suppliers = new HashMap<>();

    private static SupplierController supplierController;
    private SupplierController(){}
    public static SupplierController getInstance(){
        if(supplierController == null)
            supplierController = new SupplierController();
        return supplierController;
    }

    public void addSupplier(String name, String bnNumber, BankAccount bankAccount, String paymentMethod,
                            List<String> fields, Map<String,Pair<String, String>> contactsInfo,
                            List<Product> productList, DeliveryAgreement deliveryAgreement){
        suppliers.put(bnNumber ,new Supplier(name,bnNumber,bankAccount,paymentMethod,fields, contactsInfo, productList, deliveryAgreement));
    }

    public void addSupplier(String bnNumber, Supplier supplier){
        suppliers.put(bnNumber, supplier);
    }

    public Supplier getSupplier(String bnNumber){
        if(!suppliers.containsKey(bnNumber))
            throw new RuntimeException("there is no supplier with bn number: " + bnNumber);
        return suppliers.get(bnNumber);
    }

    public List<Supplier> getCopyOfSuppliers(){
        return new LinkedList<>(suppliers.values());
    }

    public void removeSupplier(String bnNumber){
        if(suppliers.remove(bnNumber) == null)
            throw new RuntimeException("there is no supplier with bn number: " + bnNumber);
    }


}
