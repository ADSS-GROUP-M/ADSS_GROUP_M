package Backend.ServiceLayer.SuppliersModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.SuppliersModule.OrderController;
import Backend.BusinessLayer.SuppliersModule.Pair;
import Backend.BusinessLayer.SuppliersModule.Supplier;


import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import Backend.BusinessLayer.SuppliersModule.SupplierController;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class OrderService {
    private OrderController orderController;
    private Gson gson;
    public OrderService(){
        orderController = new OrderController();
        gson = new Gson();
    }

    public String order(Map<String, Integer> order, Branch branch){
        try {
            Map<String, Pair<Map<String, Integer>, Double>> fullOrder = orderController.order(order, branch);
            Response<Map<String, Pair<Map<String, Integer>, Double>>> r = new Response<>(fullOrder);
            return gson.toJson(r);
        }
        catch (RuntimeException exception) {
            return gson.toJson(new Response<Map<String, Pair<Map<Integer, Integer>, Double>>>(exception.getMessage(), true));
        }

    }
}
