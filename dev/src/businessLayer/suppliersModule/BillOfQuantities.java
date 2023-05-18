package businessLayer.suppliersModule;

import businessLayer.suppliersModule.Discounts.Discount;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class BillOfQuantities {
    /***
     * maps between product's id to its set of discounts by amount ordered
     */
    private Map<String, Map<Integer, Discount>> productsDiscounts;
    /***
     * discount to be applied on the total order after discount on amount - T is the price before and R is the price after
     */
    private Function<Double, Double> discountOnTotalOrder;
    /***
     * discount to be applied on the total order after per-product discounts - T is the price before and R is the price after
     */
    private Function<Integer, Function<Double, Double>> discountOnAmountOfProducts;
    /***
     * for "toString" purpose
     */
    private int amountToReachForDiscount;
    private double priceToReachForDiscount;
    private Discount discountOnAmount;
    private Discount discountOnTotal;
    /**
     * indicates the order of the discounts - true if amount-discount before total-payment-discount, false otherwise
     */
    private boolean amountBeforeTotal;
    public BillOfQuantities(){
        productsDiscounts = new TreeMap<>();
    }
    public BillOfQuantities(Map<String, Map<Integer, Discount>> productsDiscounts){
        this.productsDiscounts = productsDiscounts;
    }
    public void setOrderOfDiscounts(boolean amountBeforeTotal){
        this.amountBeforeTotal = amountBeforeTotal;
    }
    public void setDiscountOnTotalOrder(double priceToActivateDiscount, Discount discount){
        priceToReachForDiscount = priceToActivateDiscount;
        discountOnTotal = discount;
        discountOnTotalOrder = (Double price) -> {
            if (price >= priceToActivateDiscount)
                return discount.applyDiscount(price);
            return price;
        };
    }
    public void removeDiscountOnTotalOrder(){
        discountOnTotalOrder = null;
    }

    public void addProductDiscount(String catalogNumber, int amount, Discount discount){
        if(discount == null)
            throw new RuntimeException("discount can't be null");
        if(productsDiscounts.get(catalogNumber) == null)
            productsDiscounts.put(catalogNumber, new HashMap<>());
        productsDiscounts.get(catalogNumber).put(amount, discount);
    }

    public void removeProductDiscount(String catalogNumber, int amount){
        if(productsDiscounts.get(catalogNumber) != null)
            productsDiscounts.get(catalogNumber).remove(amount);
    }

    public void removeProductDiscount(String catalogNumber){
        productsDiscounts.remove(catalogNumber);
    }

    public void setProductsDiscounts(Map<String, Map<Integer, Discount>> productsDiscounts) {
        this.productsDiscounts = productsDiscounts;
    }

    public double getProductPriceAfterDiscount(String catalogNumber, int amount, double price){
        if(!productsDiscounts.containsKey(catalogNumber))
            return price;
        Discount discount = null;
        for(int amountCheck : productsDiscounts.get(catalogNumber).keySet())
            if(amountCheck <= amount)
                discount = productsDiscounts.get(catalogNumber).get(amountCheck);
        if(discount != null)
            return discount.applyDiscount(price);
        return price;
    }

    public double getPriceAfterDiscounts(int amount, double priceBefore){
        double price = priceBefore;
        if(amountBeforeTotal){
            if(discountOnAmountOfProducts != null)
                price = discountOnAmountOfProducts.apply(amount).apply(price);
            if(discountOnTotalOrder != null)
                price = discountOnTotalOrder.apply(price);
        }
        else{
            if(discountOnTotalOrder != null)
                price = discountOnTotalOrder.apply(price);
            if(discountOnAmountOfProducts != null)
                price = discountOnAmountOfProducts.apply(amount).apply(price);
        }
        return price;
    }

    public void setDiscountOnAmountOfProducts(int amountOfProductsForDiscount, Discount discount){
        amountToReachForDiscount = amountOfProductsForDiscount;
        discountOnAmount = discount;
        discountOnAmountOfProducts = (Integer amountOfProducts) ->{
            if(amountOfProducts >= amountOfProductsForDiscount)
                return (Double priceBefore) -> discount.applyDiscount(priceBefore);
            return (Double priceBefore) -> priceBefore;
        };
    }

    public void removeDiscountOnAmountOfProducts(){
        discountOnAmountOfProducts = null;
    }

    public String toString(){
        String productsDiscountString = "PRODUCTS DISCOUNT:";
        int counter = 1;
        for (Map.Entry<String, Map<Integer, Discount>> productDiscount: productsDiscounts.entrySet()){
            productsDiscountString += "\n\t\t\t\t" + counter++ + ". Product id: " + productDiscount.getKey() + "\n\t\t\t\tDiscounts: ";
            for(Map.Entry<Integer, Discount> discount : productDiscount.getValue().entrySet())
                productsDiscountString += "\n\t\t\t\t\t" + "Amount to reach: " +discount.getKey() + " Discount: " + discount.getValue().toString();
        }
        String discountTotalStr = "";
        if(discountOnTotal != null)
            discountTotalStr = "DISCOUNT ON TOTAL ORDER:\n\t\t\t\torder price to reach: " + priceToReachForDiscount + " " + discountOnTotal.toString();
        String discountAmountStr = "";
        if(discountOnAmount != null)
            discountAmountStr = "DISCOUNT ON AMOUNT OF PRODUCTS:\n\t\t\t\tNumber of products to reach: " + amountToReachForDiscount + " " + discountOnAmount.toString();

        String res = "BILL OF QUANTITIES:\n\t\t\t" + productsDiscountString + "\n\t\t\t" + discountTotalStr + "\n\t\t\t" + discountAmountStr;
        return res;
    }

    public boolean getAmountBeforeTotal() {
        return amountBeforeTotal;
    }
}
