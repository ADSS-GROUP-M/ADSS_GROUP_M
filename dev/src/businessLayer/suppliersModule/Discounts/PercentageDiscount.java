package businessLayer.suppliersModule.Discounts;

import exceptions.SupplierException;

public class PercentageDiscount extends Discount {
    private final double percentage;
    public PercentageDiscount(double percentage) {
        if(percentage > 100 | percentage < 0) {
            throw new IllegalArgumentException("percentage is invalid");
        }
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
