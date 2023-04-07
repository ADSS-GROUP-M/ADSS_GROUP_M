package Backend.ServiceLayer;

import Backend.BusinessLayer.OrderController;
import Backend.BusinessLayer.Pair;
import Backend.BusinessLayer.Supplier;


import java.util.List;
import java.util.Map;
import com.google.gson.*;

public class OrderService {
    private OrderController orderController;
    private Gson gson;
    public OrderService(){
        orderController = new OrderController();
        gson = new Gson();
    }

    public String order(Map<Integer, Integer> order, List<Supplier> suppliers){
        try {
            Map<Supplier, Pair<Map<Integer, Integer>, Double>> fullOrder = orderController.order(order, suppliers);
            Response< Map<Supplier, Pair<Map<Integer, Integer>, Double>>> r = new Response<>(fullOrder);
            return gson.toJson(r);
        }
        catch (RuntimeException exception) {
            return gson.toJson(new Response<>(exception.getMessage()));
        }

    }
}
