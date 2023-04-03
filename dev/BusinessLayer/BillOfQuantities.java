package BusinessLayer;

import BusinessLayer.Discounts.Discount;

import java.util.*;
import java.util.function.Function;

public class BillOfQuantities {
    /***
     * maps between product's id to its set of discounts by amount ordered
     */
    private Map<Integer, Map<Integer, Discount>> productsDiscounts;
    /***
     * discount to be applied on the total order after discount on amount - T is the price before and R is the price after
     */
    private Function<Double, Double> discountOnTotalOrder;
    /***
     * discount to be applied on the total order after per-product discounts - T is the price before and R is the price after
     */
    private Function<Double, Double> discountOnAmountOfProducts;
    /**
     * indicates the order of the discounts - true if amount-discount before total-payment-discount, false otherwise
     */
    private boolean amountBeforeTotal;
    public BillOfQuantities(){
        productsDiscounts = new TreeMap<>();
    }
    public void setOrderOfDiscounts(boolean amountBeforeTotal){
        this.amountBeforeTotal = amountBeforeTotal;
    }
    public void setDiscountOnTotalOrder(double priceToActivateDiscount, Discount discount){
        discountOnTotalOrder = (Double price) -> {
            if (price >= priceToActivateDiscount)
                return discount.applyDiscount(price);
            return price;
        };
    }
    public void removeDiscountOnTotalOrder(){
        discountOnTotalOrder = null;
    }

    public void addProductDiscount(int productId, int amount, Discount discount){
        if(productsDiscounts.get(productId) == null)
            productsDiscounts.put(productId, new HashMap<>());
        productsDiscounts.get(productId).put(amount, discount);
    }

    public void removeProductDiscount(int productId, int amount){
        if(productsDiscounts.get(productId) != null)
            productsDiscounts.get(productId).remove(amount);
    }

    public void setProductsDiscounts(Map<Integer, Map<Integer, Discount>> productsDiscounts) {
        this.productsDiscounts = productsDiscounts;
    }

    public double getProductPriceAfterDiscount(int productId, int amount, double price){
        Discount discount = null;
        for(int amountCheck : productsDiscounts.get(productId).keySet())
            if(amountCheck <= amount)
                discount = productsDiscounts.get(productId).get(amountCheck);
        if(discount != null)
            return discount.applyDiscount(price);
        return price;
    }

    public void setDiscountOnAmountOfProducts(int amountOfProductsForDiscount, double price, Discount discount){
        discountOnAmountOfProducts = (Double amountOfProducts) ->{
            if(amountOfProducts >= amountOfProductsForDiscount)
                return discount.applyDiscount(price);
            return price;
        };
    }

    public void removeDiscountOnAmountOfProducts(){
        discountOnAmountOfProducts = null;
    }
}
