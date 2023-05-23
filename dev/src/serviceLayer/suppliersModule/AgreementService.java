package serviceLayer.suppliersModule;

import businessLayer.suppliersModule.AgreementController;
import businessLayer.suppliersModule.BillOfQuantitiesController;
import com.google.gson.Gson;

import java.util.List;

public class AgreementService {
    private final AgreementController agreementController;
    private final BillOfQuantitiesController billOfQuantitiesController;
    private final Gson gson;

    public AgreementService(AgreementController agreementController, BillOfQuantitiesController billOfQuantitiesController){
        this.agreementController = agreementController;
        this.billOfQuantitiesController = billOfQuantitiesController;
        gson = new Gson();
    }

    public String setFixedDeliveryAgreement(String bnNumber, boolean haveTransport, List<Integer> days){
        try {
            agreementController.setFixedDeliveryAgreement(bnNumber, haveTransport, days);
            return gson.toJson(new Response<String>("delivery agreement is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setByInvitationDeliveryAgreement(String bnNumber, boolean haveTransport, int numOfDays){
        try {
            agreementController.setByInvitationDeliveryAgreement(bnNumber, haveTransport, numOfDays);
            return gson.toJson(new Response<String>("contact phone number is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setSuppliersCatalogNumber(String bnNumber, String catalogNumber, String newSuppliersCatalogNumber){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber))
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            agreementController.setSuppliersCatalogNumber(bnNumber, catalogNumber, newSuppliersCatalogNumber);
            return gson.toJson(new Response<String>("product catalog number is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setProductPrice(String bnNumber, String catalogNumber, double price){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber))
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            agreementController.setPrice(bnNumber, catalogNumber, price);
            return gson.toJson(new Response<String>("product price is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setProductAmount(String bnNumber, String catalogNumber, int amount){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber))
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            agreementController.setNumberOfUnits(bnNumber, catalogNumber, amount);
            return gson.toJson(new Response<String>("product number of units is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String addProduct(String bnNumber, String catalogNumber, String supplierCatalogNumber, double price, int numberOfUnits){
        try {
            agreementController.addProduct(bnNumber, catalogNumber, supplierCatalogNumber, price, numberOfUnits);
            return gson.toJson(new Response<String>("product is added!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String getSuppliersCatalogNumber(String bnNumber, String catalogNumber){
        try {
            return gson.toJson(new Response<String>(agreementController.getSuppliersCatalogNumber(bnNumber, catalogNumber)));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String getDeliveryAgreement(String bnNumber){
        try {
            return gson.toJson(new Response<String>(agreementController.getDeliveryAgreement(bnNumber).toString2()));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removeProduct(String bnNumber, String catalogNumber){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber))
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            agreementController.removeProduct(bnNumber, catalogNumber);
            billOfQuantitiesController.removeProductDiscount(bnNumber, catalogNumber);
            return gson.toJson(new Response<String>("product was removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removeAgreement(String bnNumber){
        try {
            agreementController.removeAgreement(bnNumber);
            return gson.toJson(new Response<String>("agreement was removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }
}
