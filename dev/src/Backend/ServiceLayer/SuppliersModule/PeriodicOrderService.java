package Backend.ServiceLayer.SuppliersModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;
import Backend.BusinessLayer.SuppliersModule.Order;
import Backend.BusinessLayer.SuppliersModule.PeriodicOrder;
import Backend.BusinessLayer.SuppliersModule.PeriodicOrderController;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.Map;

public class PeriodicOrderService {
    private PeriodicOrderController periodicOrderController;
    private Gson gson;

    public PeriodicOrderService() {
        this.periodicOrderController =  PeriodicOrderController.getInstance();
        gson = new Gson();
    }

    public String addPeriodicOrder(String bnNumber, Map<String, Integer> products, int day, Branch branch)  {
        try {
            periodicOrderController.addPeriodicOrder(bnNumber, products, day, branch);
            return gson.toJson(new Response<String>("periodic order created!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String removePeriodicOrder(int orderId){
        try {
            periodicOrderController.removePeriodicOrder(orderId);
            return gson.toJson(new Response<String>("periodic order removed!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String getAllPeriodicOrders(){
        try {
            Map<Integer, PeriodicOrder> periodicOrders = periodicOrderController.getAllPeriodicOrders();
            return gson.toJson(new Response<Map<Integer, PeriodicOrder>>(periodicOrders));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String setDay(int orderId, int day)  {
        try {
            periodicOrderController.setDay(orderId, day);
            return gson.toJson(new Response<String>("day is set!", false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }

    public String getDetails(int orderId)  {
        try {
            return gson.toJson(new Response<String>(periodicOrderController.getDetails(orderId), false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }


    /***
     * to use after insert
     * @return the details of the order
     */
    public String getDetails()  {
        try {
            return gson.toJson(new Response<String>(periodicOrderController.getDetails(), false));
        }
        catch (Exception e){
            return gson.toJson(new Response<>(e.getMessage(), true));
        }
    }
}
