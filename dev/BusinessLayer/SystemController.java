package BusinessLayer;

import BusinessLayer.DeliveryAgreements.DeliveryAgreement;

import java.util.List;
import java.util.Map;

/***
 * controls the system functionality
 */
public class SystemController {
    private SupplierController supplierController;
    public SystemController(){
        supplierController = new SupplierController();
    }
    public void addSupplier(String name, String bnNumber, String bankAccount, String paymentMethod,
                            List<String> fields, Map<String, List<String>> contactsInfo,
                            List<Product> productList, DeliveryAgreement deliveryAgreement){
        supplierController.addSupplier(name,bnNumber,bankAccount,paymentMethod,fields, contactsInfo, productList, deliveryAgreement);
    }

    public void order(Map<Product, Integer> products){
        Order order = new Order(products);

    }
}
