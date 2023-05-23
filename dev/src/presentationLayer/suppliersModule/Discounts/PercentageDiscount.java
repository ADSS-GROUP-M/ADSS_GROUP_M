package presentationLayer.suppliersModule.Discounts;

public class PercentageDiscount extends Discount {
    private final double percentage;
    public PercentageDiscount(double percentage){
        this.percentage = percentage;
    }
    @Override
    public double applyDiscount(double price) {
        return price - (percentage/100) * price;
    }

    @Override
    public String toString() {
        return "discount in percentages, percentage of discount: " + percentage;
    }
}
