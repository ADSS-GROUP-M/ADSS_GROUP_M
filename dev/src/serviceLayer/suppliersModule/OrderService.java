package serviceLayer.suppliersModule;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.suppliersModule.Order;
import businessLayer.suppliersModule.OrderController;
import businessLayer.suppliersModule.Pair;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class OrderService {
    private OrderController orderController;
    private Gson gson;
    public OrderService(){
        orderController = OrderController.getInstance();
        gson = new Gson();
    }

    public String order(Map<String, Integer> order, Branch branch){
        try {
            Map<String, Pair<Map<String, Integer>, Double>> fullOrder = orderController.order(order, branch);
            Response<Map<String, Pair<Map<String, Integer>, Double>>> r = new Response<>(fullOrder);
            return gson.toJson(r);
        }
        catch (Exception exception) {
            return gson.toJson(new Response<Map<String, Pair<Map<Integer, Integer>, Double>>>(exception.getMessage(), true));
        }

    }
    public String orderDueToShortage(Map<String, Integer> order, Branch branch){
        try {
            orderController.orderDueToShortage(order, branch);
            return gson.toJson(new Response<>("order ordered!", false));
        }
        catch (Exception exception) {
            return gson.toJson(new Response<Map<String, Pair<Map<Integer, Integer>, Double>>>(exception.getMessage(), true));
        }

    }

    public String getOrderHistory(String bnNumber){
        try {
            return gson.toJson(new Response<List<Order>>(orderController.getOrderHistory(bnNumber)));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }
}
