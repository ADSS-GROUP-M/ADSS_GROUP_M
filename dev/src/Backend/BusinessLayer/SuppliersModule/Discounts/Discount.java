package Backend.BusinessLayer.SuppliersModule.Discounts;

public class Discount {
    protected double value;
    protected boolean cashDiscount;
    public Discount(double value, boolean cashDiscount){
        this.value = value;
        this.cashDiscount = cashDiscount;
    }
    public  double applyDiscount(double price){
        return value;
    }

    public String toString(){
        if(cashDiscount)
            return "discount in cash, amount: " + value;
        return "discount in percentages, percentage of discount: " + value;
    }

}