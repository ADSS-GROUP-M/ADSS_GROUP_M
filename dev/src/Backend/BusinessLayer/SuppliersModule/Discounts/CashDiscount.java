package Backend.BusinessLayer.SuppliersModule.Discounts;

public class CashDiscount extends Discount {
    public CashDiscount(double amountOfDiscount){
        super(amountOfDiscount, true);
        if(amountOfDiscount < 0)
            throw new RuntimeException("amount of cash cant be less than 0");
    }
    @Override
    public double applyDiscount(double price) {
        return Math.max(price - value, 0);
    }

    public String toString(){
        return "discount in cash, amount: " + value;
    }

    public double getAmountOfDiscount(){
        return value;
    }
}
