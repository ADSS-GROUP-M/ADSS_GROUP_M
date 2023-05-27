package businessLayer.suppliersModule;

import java.util.HashMap;
import java.util.Map;

public class Order {
    /***
     * maps between productId and amount ordered
     */
    private final Map<String, Integer> products;

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

    public boolean equals(Object other){
        if(!(other instanceof Order)) {
            return false;
        }
        Order otherOrder = (Order) other;
        return products.equals(otherOrder.getProducts());
    }

    public String toString(){
        StringBuilder res = new StringBuilder();
        for(Map.Entry<String, Integer> order : products.entrySet()) {
            res.append("catalog number: ").append(order.getKey()).append(" , quantity: ").append(order.getValue()).append("\n");
        }
        return res.toString();
    }
}
