package businessLayer.suppliersModule;

import businessLayer.businessLayerUsage.Branch;

public class PeriodicOrder {
    private final String bnNumber;
    private final Order order;
    /**
     * day of the week, of the order
     */
    private final int day;
    private final Branch branch;

    public PeriodicOrder(String bnNumber, Order order, int day, Branch branch){
        this.bnNumber = bnNumber;
        this.order = order;
        this.day = day;
        this.branch = branch;
    }

    public String getBnNumber() {
        return bnNumber;
    }

    public Order getOrder() {
        return order;
    }

    public int getDay() {
        return day;
    }

    public Branch getBranch() {
        return branch;
    }

    public void addProduct(String catalogNumber, int quantity){
        order.addProduct(catalogNumber, quantity);
    }
    public String toString(int orderId){
        return "ORDER ID:\n\t" +  orderId + "\nSUPPLIER:\n\t" + bnNumber + "\nDAY:\n\t" + day + "\nORDER:\n" + order.toString();
    }

    public boolean equals(Object other){
        if(!(other instanceof  PeriodicOrder)) {
            return false;
        }
        PeriodicOrder otherPeriodicOrder = (PeriodicOrder) other;
        if(bnNumber.equals(otherPeriodicOrder.getBnNumber()) && order.equals(otherPeriodicOrder.getOrder()) && day == otherPeriodicOrder.getDay() && branch.equals(otherPeriodicOrder.getBranch())) {
            return true;
        }
        return false;
    }
}
