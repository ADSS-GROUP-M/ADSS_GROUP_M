package Backend.ServiceLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.AgreementController;
import Backend.BusinessLayer.SuppliersModule.BillOfQuantitiesController;
import Backend.BusinessLayer.SuppliersModule.Discounts.Discount;
import com.google.gson.Gson;

import java.util.Map;

public class BillOfQuantitiesService {
    private AgreementController agreementController;
    private BillOfQuantitiesController billOfQuantitiesController;
    private Gson gson;

    public BillOfQuantitiesService(){
        agreementController = AgreementController.getInstance();
        billOfQuantitiesController = BillOfQuantitiesController.getInstance();
        gson = new Gson();
    }

    public String setProductDiscount(String bnNumber, String catalogNumber, int amount, Discount discount){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber))
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            billOfQuantitiesController.addProductDiscount(bnNumber, catalogNumber, amount, discount);
            return gson.toJson(new Response<String>("product discount is edited!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removeProductDiscount(String bnNumber, String catalogNumber, int amount){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber))
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            if(billOfQuantitiesController.billOfQuantitiesExist(bnNumber))
                billOfQuantitiesController.removeProductDiscount(bnNumber, catalogNumber, amount);
            else
                return gson.toJson(new Response<String>("there are no products discounts!",true));
            return gson.toJson(new Response<String>("product discount removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removeProductDiscounts(String bnNumber, String catalogNumber){
        try {
            if(!agreementController.productExist(bnNumber, catalogNumber))
                throw new RuntimeException("the supplier does not supply product - " + catalogNumber);
            if(billOfQuantitiesController.billOfQuantitiesExist(bnNumber))
                billOfQuantitiesController.removeProductDiscount(bnNumber, catalogNumber);
            else
                return gson.toJson(new Response<String>("there are no products discounts!",true));
            return gson.toJson(new Response<String>("product discount removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }


    public String removeDiscountOnTotalOrder(String bnNumber){
        try {
            if(billOfQuantitiesController.billOfQuantitiesExist(bnNumber))
                billOfQuantitiesController.removeDiscountOnTotalOrder(bnNumber);
            else
                return gson.toJson(new Response<String>("there are no discounts!",true));
            return gson.toJson(new Response<String>("product discount on total order removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setDiscountOnTotalOrder(String bnNumber, double priceToActivateDiscount, Discount discount){
        try {
            billOfQuantitiesController.setDiscountOnTotalOrder(bnNumber, priceToActivateDiscount, discount);
            return gson.toJson(new Response<String>("product discount on total order is set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setOrderOfDiscounts(String bnNumber, boolean amountBeforeTotal){
        try {
            billOfQuantitiesController.setOrderOfDiscounts(bnNumber, amountBeforeTotal);
            return gson.toJson(new Response<String>("order of discount id set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setProductsDiscounts(String bnNumber, Map<String, Map<Integer, Discount>> productsDiscounts){
        try {
            billOfQuantitiesController.setProductsDiscounts(bnNumber, productsDiscounts);
            return gson.toJson(new Response<String>("product discounts are set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setDiscountOnAmountOfProducts(String bnNumber, int amountOfProductsForDiscount, Discount discount){
        try {
            billOfQuantitiesController.setDiscountOnAmountOfProducts(bnNumber, amountOfProductsForDiscount, discount);
            return gson.toJson(new Response<String>("product discount on amount of products is set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removeDiscountOnAmountOfProducts(String bnNumber){
        try {
            if(billOfQuantitiesController.billOfQuantitiesExist(bnNumber))
                billOfQuantitiesController.removeDiscountOnAmountOfProducts(bnNumber);
            else
                return gson.toJson(new Response<String>("there are no discounts!",true));
            return gson.toJson(new Response<String>("product discount on amount of products is removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removeBillOfQuantities(String bnNumber){
        try {
            billOfQuantitiesController.removeBillOfQuantities(bnNumber);
            return gson.toJson(new Response<String>("BillOfQuantities is removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }
}
