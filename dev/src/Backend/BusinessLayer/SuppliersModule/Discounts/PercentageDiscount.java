package Backend.BusinessLayer.SuppliersModule.Discounts;

public class PercentageDiscount extends Discount {
    public PercentageDiscount(double percentage){
        super(percentage, false);
        if(percentage > 100 | percentage < 0) {
            throw new RuntimeException("percentage is invalid");
        }
    }
    @Override
    public double applyDiscount(double price) {
        return Math.max(price - (value/100) * price, 0);
    }

    @Override
    public String toString() {
        return "discount in percentages, percentage of discount: " + value;
    }

    public double getPercentage() {
        return value;
    }
}
