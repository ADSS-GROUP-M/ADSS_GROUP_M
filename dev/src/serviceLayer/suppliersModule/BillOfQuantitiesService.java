package serviceLayer.suppliersModule;

import businessLayer.suppliersModule.AgreementController;
import businessLayer.suppliersModule.BillOfQuantitiesController;
import businessLayer.suppliersModule.Discounts.Discount;
import com.google.gson.Gson;
import utils.Response;

import java.util.Map;

public class BillOfQuantitiesService {
    private final AgreementController agreementController;
    private final BillOfQuantitiesController billOfQuantitiesController;

    public BillOfQuantitiesService(AgreementController agreementController, BillOfQuantitiesController billOfQuantitiesController){
        this.agreementController = agreementController;
        this.billOfQuantitiesController = billOfQuantitiesController;
    }

    public String setProductDiscount(String bnNumber, String catalogNumber, int amount, Discount discount){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber)) {
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            }
            billOfQuantitiesController.addProductDiscount(bnNumber, catalogNumber, amount, discount);
            return new Response("product discount is edited!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String removeProductDiscount(String bnNumber, String catalogNumber, int amount){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber)) {
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            }
            if(billOfQuantitiesController.billOfQuantitiesExist(bnNumber)) {
                billOfQuantitiesController.removeProductDiscount(bnNumber, catalogNumber, amount);
            } else {
                return new Response("there are no products discounts!",false).toJson();
            }
            return new Response("product discount removed!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String removeProductDiscounts(String bnNumber, String catalogNumber){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber)) {
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            }
            if(billOfQuantitiesController.billOfQuantitiesExist(bnNumber)) {
                billOfQuantitiesController.removeProductDiscount(bnNumber, catalogNumber);
            } else {
                return new Response("there are no products discounts!",false).toJson();
            }
            return new Response("product discount removed!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }


    public String removeDiscountOnTotalOrder(String bnNumber){
        try {
            if(billOfQuantitiesController.billOfQuantitiesExist(bnNumber)) {
                billOfQuantitiesController.removeDiscountOnTotalOrder(bnNumber);
            } else {
                return new Response("there are no discounts!",false).toJson();
            }
            return new Response("product discount on total order removed!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String setDiscountOnTotalOrder(String bnNumber, double priceToActivateDiscount, Discount discount){
        try {
            billOfQuantitiesController.setDiscountOnTotalOrder(bnNumber, priceToActivateDiscount, discount);
            return new Response("product discount on total order is set!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String setOrderOfDiscounts(String bnNumber, boolean amountBeforeTotal){
        try {
            billOfQuantitiesController.setOrderOfDiscounts(bnNumber, amountBeforeTotal);
            return new Response("order of discount id set!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String setProductsDiscounts(String bnNumber, Map<String, Map<Integer, Discount>> productsDiscounts){
        try {
            billOfQuantitiesController.setProductsDiscounts(bnNumber, productsDiscounts);
            return new Response("product discounts are set!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String setDiscountOnAmountOfProducts(String bnNumber, int amountOfProductsForDiscount, Discount discount){
        try {
            billOfQuantitiesController.setDiscountOnAmountOfProducts(bnNumber, amountOfProductsForDiscount, discount);
            return new Response("product discount on amount of products is set!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String removeDiscountOnAmountOfProducts(String bnNumber){
        try {
            if(billOfQuantitiesController.billOfQuantitiesExist(bnNumber)) {
                billOfQuantitiesController.removeDiscountOnAmountOfProducts(bnNumber);
            } else {
                return new Response("there are no discounts!",false).toJson();
            }
            return new Response("product discount on amount of products is removed!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String removeBillOfQuantities(String bnNumber){
        try {
            billOfQuantitiesController.removeBillOfQuantities(bnNumber);
            return new Response("BillOfQuantities is removed!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }
}
