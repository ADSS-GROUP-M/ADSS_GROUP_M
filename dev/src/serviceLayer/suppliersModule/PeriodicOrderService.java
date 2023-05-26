package serviceLayer.suppliersModule;

import businessLayer.businessLayerUsage.Branch;
import businessLayer.suppliersModule.PeriodicOrderController;
import com.google.gson.Gson;
import utils.Response;

import java.util.Map;

import static utils.Response.getErrorResponse;

public class PeriodicOrderService {
    private final PeriodicOrderController periodicOrderController;

    public PeriodicOrderService(PeriodicOrderController periodicOrderController) {
        this.periodicOrderController = periodicOrderController;
    }

    public String addPeriodicOrder(String bnNumber, Map<String, Integer> products, int day, Branch branch)  {
        try {
            periodicOrderController.addPeriodicOrder(bnNumber, products, day, branch);
            return new Response("periodic order created!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String setDay(int orderId, int day)  {
        try {
            periodicOrderController.setDay(orderId, day);
            return new Response("day is set!", true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String getDetails(int orderId)  {
        try {
            return new Response(periodicOrderController.getDetails(orderId), true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }


    /***
     * to use after insert
     * @return the details of the order
     */
    public String getDetails()  {
        try {
            return new Response(periodicOrderController.getDetails(), true).toJson();
        }
        catch (Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }
}
