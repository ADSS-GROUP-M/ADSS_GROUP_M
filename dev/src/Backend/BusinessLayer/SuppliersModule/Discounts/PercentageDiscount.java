package Backend.BusinessLayer.SuppliersModule.Discounts;

public class PercentageDiscount extends Discount {
    private double percentage;
    public PercentageDiscount(double percentage){
        if(percentage > 100 | percentage < 0)
            throw new RuntimeException("percentage is invalid");
        this.percentage = percentage;
    }
    @Override
    public double applyDiscount(double price) {
        return Math.max(price - (percentage/100) * price, 0);
    }

    @Override
    public String toString() {
        return "discount in percentages, percentage of discount: " + percentage;
    }

    public double getPercentage() {
        return percentage;
    }
}
