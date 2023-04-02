package BusinessLayer;

import java.util.Map;

public class Order {
    /***
     * maps between product and amount ordered
     */
    private Map<Product, Integer> products;

    public Order(Map<Product, Integer> products){
        this.products = products;
    }

}
