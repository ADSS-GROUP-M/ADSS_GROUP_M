package Backend.BusinessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.Discounts.Discount;

import java.util.HashMap;
import java.util.Map;

public class BillOfQuantitiesController {
    private static BillOfQuantitiesController instance;
    private Map<String, BillOfQuantities> supplierBillOfQuantities;

    public static BillOfQuantitiesController getInstance(){
        if(instance == null)
            instance = new BillOfQuantitiesController();
        return instance;
    }

    private BillOfQuantitiesController(){
        supplierBillOfQuantities = new HashMap<>();
    }

    public boolean billOfQuantitiesExist(String bnNumber){
        return getBillOfQuantities(bnNumber) != null;
    }

    public void addBillOfQuantities(String bnNumber){
        supplierBillOfQuantities.put(bnNumber, new BillOfQuantities());
    }

    private BillOfQuantities getBillOfQuantities(String bnNumber){
        return supplierBillOfQuantities.get(bnNumber);
    }

    public void setOrderOfDiscounts(String bnNumber, boolean amountBeforeTotal){
        if(!billOfQuantitiesExist(bnNumber))
            addBillOfQuantities(bnNumber);
        getBillOfQuantities(bnNumber).setOrderOfDiscounts(amountBeforeTotal);
    }

    public void setDiscountOnTotalOrder(String bnNumber, double priceToActivateDiscount, Discount discount){
        if(!billOfQuantitiesExist(bnNumber))
            addBillOfQuantities(bnNumber);
        getBillOfQuantities(bnNumber).setDiscountOnTotalOrder(priceToActivateDiscount, discount);
    }

    public void removeDiscountOnTotalOrder(String bnNumber){
        getBillOfQuantities(bnNumber).removeDiscountOnTotalOrder();
    }

    public void addProductDiscount(String bnNumber, String catalogNumber, int amount, Discount discount){
        if(!billOfQuantitiesExist(bnNumber))
            addBillOfQuantities(bnNumber);
        getBillOfQuantities(bnNumber).addProductDiscount(catalogNumber, amount, discount);
    }

    public void removeProductDiscount(String bnNumber, String catalogNumber, int amount){
        getBillOfQuantities(bnNumber).removeProductDiscount(catalogNumber, amount);
    }

    public void removeProductDiscount(String bnNumber, String catalogNumber){
        getBillOfQuantities(bnNumber).removeProductDiscount(catalogNumber);
    }

    public void setProductsDiscounts(String bnNumber, Map<String, Map<Integer, Discount>> productsDiscounts){
        if(!billOfQuantitiesExist(bnNumber))
            addBillOfQuantities(bnNumber);
        getBillOfQuantities(bnNumber).setProductsDiscounts(productsDiscounts);
    }

    public double getProductPriceAfterDiscount(String bnNumber, String catalogNumber, int amount, double price){
        return getBillOfQuantities(bnNumber).getProductPriceAfterDiscount(catalogNumber, amount, price);
    }

    public double getPriceAfterDiscounts(String bnNumber, int amount, double priceBefore){
        return getBillOfQuantities(bnNumber).getPriceAfterDiscounts(amount, priceBefore);
    }

    public void setDiscountOnAmountOfProducts(String bnNumber, int amountOfProductsForDiscount, Discount discount){
        if(!billOfQuantitiesExist(bnNumber))
            addBillOfQuantities(bnNumber);
        getBillOfQuantities(bnNumber).setDiscountOnAmountOfProducts(amountOfProductsForDiscount, discount);
    }

    public void removeDiscountOnAmountOfProducts(String bnNumber){
        getBillOfQuantities(bnNumber).removeDiscountOnAmountOfProducts();
    }

    public boolean getAmountBeforeTotal(String bnNumber){
        return getBillOfQuantities(bnNumber).getAmountBeforeTotal();
    }

    public void removeBillOfQuantities(String bnNumber){
        supplierBillOfQuantities.remove(bnNumber);
    }

    public String toString(String bnNumber){
        return getBillOfQuantities(bnNumber) != null ? getBillOfQuantities(bnNumber).toString() : "";
    }
}
