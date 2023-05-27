package serviceLayer.suppliersModule;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.suppliersModule.Order;
import businessLayer.suppliersModule.OrderController;
import businessLayer.suppliersModule.Pair;
import com.google.gson.Gson;
import exceptions.SupplierException;
import utils.Response;

import java.util.List;
import java.util.Map;

public class OrderService {
    private final OrderController orderController;
    public OrderService(OrderController orderController){
        this.orderController = orderController;
    }

    public String order(Map<String, Integer> order, Branch branch){
        try {
            Map<String, Pair<Map<String, Integer>, Double>> fullOrder = orderController.order(order, branch);
            return new Response(true,fullOrder).toJson();
        }
        catch (SupplierException e) {
            return Response.getErrorResponse(e).toJson();
        }

    }
    public String orderDueToShortage(Map<String, Integer> order, Branch branch){
        try {
            orderController.orderDueToShortage(order, branch);
            return new Response("order ordered!", true).toJson();
        }
        catch (SupplierException e) {
            return Response.getErrorResponse(e).toJson();
        }

    }

    public String getOrderHistory(String bnNumber){
        try {
            return new Response(true,orderController.getOrderHistory(bnNumber)).toJson();
        }
        catch (SupplierException e){
            return Response.getErrorResponse(e).toJson();
        }
    }
}
