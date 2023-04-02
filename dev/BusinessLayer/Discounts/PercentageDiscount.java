package BusinessLayer.Discounts;

import BusinessLayer.Discounts.Discount;

public class PercentageDiscount extends Discount {
    private double percentage;
    public PercentageDiscount(double percentage){
        this.percentage = percentage;
    }
    @Override
    public double applyDiscount(double price) {
        return price - (percentage/100) * price;
    }
}
