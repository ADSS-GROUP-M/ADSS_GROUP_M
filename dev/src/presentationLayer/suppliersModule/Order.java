package presentationLayer.suppliersModule;

import java.util.HashMap;
import java.util.Map;

public class Order {
    /***
     * maps between productId and amount ordered
     */
    private final Map<Integer, Integer> products;

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

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("ORDER:");
        for(Map.Entry<Integer, Integer> product : products.entrySet()) {
            res.append("\n\t\tproduct: ").append(product.getKey()).append(" amount: ").append(product.getValue());
        }
        return res.toString();
    }
}
