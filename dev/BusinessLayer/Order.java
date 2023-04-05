package BusinessLayer;

import java.util.Map;

public class Order {
    /***
     * maps between productId and amount ordered
     */
    private Map<Integer, Integer> products;

    public Order(Map<Integer, Integer> products){
        this.products = products;
    }

    public Map<Integer, Integer> getProducts() {
        return products;
    }
}
