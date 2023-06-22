package Fronend.PresentationLayer.SuppliersModule.CLI.Discounts.Discounts;

public class PercentageDiscount extends Discount {
    private double percentage;
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
