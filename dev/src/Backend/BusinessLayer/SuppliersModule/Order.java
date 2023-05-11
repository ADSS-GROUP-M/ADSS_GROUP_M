package Backend.BusinessLayer.SuppliersModule;

import java.util.HashMap;
import java.util.Map;

public class Order {
    /***
     * maps between productId and amount ordered
     */
    private Map<String, Integer> products;

    public Order(Map<String, Integer> products){
        this.products = products;
    }

    public Order(){
        products = new HashMap<>();
    }

    public void addProduct(String catalogNumber, int amount){
        products.put(catalogNumber, amount);
    }

    public Map<String, Integer> getProducts() {
        return products;
    }
}
