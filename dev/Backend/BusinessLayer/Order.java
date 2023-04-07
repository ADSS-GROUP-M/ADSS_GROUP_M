package Backend.BusinessLayer;

import java.util.HashMap;
import java.util.Map;

public class Order {
    /***
     * maps between productId and amount ordered
     */
    private Map<Integer, Integer> products;

    public Order(Map<Integer, Integer> products){
        this.products = products;
    }

    public Order(){
        products = new HashMap<>();
    }

    public void addProduct(Integer productId, int amount){
        products.put(productId, amount);
    }

    public Map<Integer, Integer> getProducts() {
        return products;
    }
}
