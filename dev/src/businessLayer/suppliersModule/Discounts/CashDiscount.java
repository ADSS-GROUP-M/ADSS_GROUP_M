package businessLayer.suppliersModule.Discounts;

import exceptions.SupplierException;

public class CashDiscount extends Discount {
    private final double amountOfDiscount;
    public CashDiscount(double amountOfDiscount){
        if(amountOfDiscount < 0) {
            throw new IllegalArgumentException("amount of cash cant be less than 0");
        }
        this.amountOfDiscount = amountOfDiscount;
    }
    @Override
    public double applyDiscount(double price) {
        return Math.max(price - amountOfDiscount, 0);
    }

    public String toString(){
        return "discount in cash, amount: " + amountOfDiscount;
    }

    public double getAmountOfDiscount(){
        return amountOfDiscount;
    }
}
