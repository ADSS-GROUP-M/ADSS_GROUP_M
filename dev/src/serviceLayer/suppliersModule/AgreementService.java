package serviceLayer.suppliersModule;

import businessLayer.suppliersModule.AgreementController;
import businessLayer.suppliersModule.BillOfQuantitiesController;
import com.google.gson.Gson;
import exceptions.SupplierException;
import utils.Response;

import java.util.List;

public class AgreementService {
    private final AgreementController agreementController;
    private final BillOfQuantitiesController billOfQuantitiesController;

    public AgreementService(AgreementController agreementController, BillOfQuantitiesController billOfQuantitiesController){
        this.agreementController = agreementController;
        this.billOfQuantitiesController = billOfQuantitiesController;
    }

    public String setFixedDeliveryAgreement(String bnNumber, boolean haveTransport, List<Integer> days){
        try {
            agreementController.setFixedDeliveryAgreement(bnNumber, haveTransport, days);
            return new Response("delivery agreement is edited!", true).toJson();
        }
        catch (SupplierException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String setByInvitationDeliveryAgreement(String bnNumber, boolean haveTransport, int numOfDays){
        try {
            agreementController.setByInvitationDeliveryAgreement(bnNumber, haveTransport, numOfDays);
            return new Response("contact phone number is edited!", true).toJson();
        }
        catch (SupplierException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String setSuppliersCatalogNumber(String bnNumber, String catalogNumber, String newSuppliersCatalogNumber){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber)) {
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            }
            agreementController.setSuppliersCatalogNumber(bnNumber, catalogNumber, newSuppliersCatalogNumber);
            return new Response("product catalog number is edited!", true).toJson();
        }
        catch (SupplierException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String setProductPrice(String bnNumber, String catalogNumber, double price){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber)) {
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            }
            agreementController.setPrice(bnNumber, catalogNumber, price);
            return new Response("product price is edited!", true).toJson();
        }
        catch (SupplierException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String setProductAmount(String bnNumber, String catalogNumber, int amount){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber)) {
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            }
            agreementController.setNumberOfUnits(bnNumber, catalogNumber, amount);
            return new Response("product number of units is edited!", true).toJson();
        }
        catch (SupplierException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String addProduct(String bnNumber, String catalogNumber, String supplierCatalogNumber, double price, int numberOfUnits){
        try {
            agreementController.addProduct(bnNumber, catalogNumber, supplierCatalogNumber, price, numberOfUnits);
            return new Response("product is added!", true).toJson();
        }
        catch (SupplierException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String getSuppliersCatalogNumber(String bnNumber, String catalogNumber){
        try {
            return new Response(true,agreementController.getSuppliersCatalogNumber(bnNumber, catalogNumber)).toJson();
        }
        catch (SupplierException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String getDeliveryAgreement(String bnNumber){
        try {
            return new Response(true,agreementController.getDeliveryAgreement(bnNumber).toString2()).toJson();
        }
        catch (SupplierException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String removeProduct(String bnNumber, String catalogNumber){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber)) {
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            }
            agreementController.removeProduct(bnNumber, catalogNumber);
            billOfQuantitiesController.removeProductDiscount(bnNumber, catalogNumber);
            return new Response("product was removed!", true).toJson();
        }
        catch (SupplierException e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String removeAgreement(String bnNumber){
        try {
            agreementController.removeAgreement(bnNumber);
            return new Response("agreement was removed!", true).toJson();
        }
        catch (SupplierException e){
            return Response.getErrorResponse(e).toJson();
        }
    }
}
