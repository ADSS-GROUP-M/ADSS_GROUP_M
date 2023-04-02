package BusinessLayer;

import BusinessLayer.Discounts.Discount;

import java.util.HashMap;
import java.util.Map;

public class BillOfQuantities {
    /***
     * maps between product's id to its set of discounts by amount ordered
     */
    private Map<Integer, Map<Integer, Discount>> productsDiscounts;
    /***
     * discount to be applied on the total order after per-product discounts
     */
    private Discount discountOnTotalOrder;

    public BillOfQuantities(){
        productsDiscounts = new HashMap<>();
    }

    public void setDiscountOnTotalOrder(Discount discount){
        discountOnTotalOrder = discount;
    }

    public void setProductDiscount(int productId, int amount, Discount discount){
        if(productsDiscounts.get(productId) == null)
            productsDiscounts.put(productId, new HashMap<>());
        productsDiscounts.get(productId).put(amount, discount);
    }

    public void removeProductDiscount(int productId, int amount){
        if(productsDiscounts.get(productId) != null)
            productsDiscounts.get(productId).remove(amount);
    }
}
