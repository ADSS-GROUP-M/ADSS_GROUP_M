package Backend.ServiceLayer.SuppliersModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.SuppliersModule.*;


import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

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

    public String getOrderHistory(String bnNumber){
        try {
            return gson.toJson(new Response<List<Order>>(orderController.getOrderHistory(bnNumber)));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }
}
