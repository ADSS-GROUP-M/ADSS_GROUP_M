package presentationLayer.suppliersModule.Discounts;

public class CashDiscount extends Discount {
    private final double amountOfDiscount;
    public CashDiscount(double amountOfDiscount){
        this.amountOfDiscount = amountOfDiscount;
    }
    @Override
    public double applyDiscount(double price) {
        return Math.max(price - amountOfDiscount, 0);
    }

    public String toString(){
        return "discount in cash:\n\tamount: " + amountOfDiscount;
    }
}
