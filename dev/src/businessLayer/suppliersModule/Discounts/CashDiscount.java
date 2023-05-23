package businessLayer.suppliersModule.Discounts;

public class CashDiscount extends Discount {
    private final double amountOfDiscount;
    public CashDiscount(double amountOfDiscount){
        if(amountOfDiscount < 0)
            throw new RuntimeException("amount of cash cant be less than 0");
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
