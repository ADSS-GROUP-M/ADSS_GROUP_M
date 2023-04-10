package transportModule.backend.serviceLayer;

import transportModule.backend.businessLayer.ItemListsController;
import transportModule.backend.businessLayer.TransportsController;
import transportModule.records.Transport;
import utils.JSON;
import utils.Response;

import java.io.IOException;

public class TransportsService {

    private final TransportsController tc;

    public TransportsService(TransportsController tc, ItemListsController itemListsController){
        this.tc = tc;
    }

    /**
     * The json string should be in the following format:<br/>
     * <pre>
     *{
     *   "id": 1,
     *   "source": {
     *     "TransportZone": "Zone1",
     *     "Address": "source-address",
     *     "phoneNumber": "054-6666666",
     *     "contactName": "moshe",
     *     "siteType": "LOGISTICAL_CENTER"
     *   },
     *   "destinations": [
     *     {
     *       "TransportZone": "Zone1",
     *       "Address": "source-address",
     *       "phoneNumber": "054-6666666",
     *       "contactName": "moshe",
     *       "siteType": "LOGISTICAL_CENTER"
     *     }
     *   ],
     *   "itemLists": [
     *     [
     *       {
     *         "TransportZone": "Zone1",
     *         "Address": "destination-address",
     *         "phoneNumber": "054-7777777",
     *         "contactName": "yossi",
     *         "siteType": "BRANCH"
     *       },
     *       {
     *         "id": 1,
     *         "loadingItems": {
     *           "itemToLoad": 5
     *         },
     *         "unloadingItems": {
     *           "itemToUnload": 7
     *         }
     *       }
     *     ]
     *   ],
     *   "truckId": 1,
     *   "driverId": 1,
     *   "scheduledTime": "1977-01-01T12:24",
     *   "weight": 0
     * }
     * </pre>
     * @param json - serialized Transport instance
     * @return response json
     */
    public String createTransport(String json){
        Transport transport = JSON.deserialize(json, Transport.class);
        try
        {
            tc.addTransport(transport);
        }
        catch(IOException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Transport created successfully",true, "").toJson();
    }

    public String updateTransport(String json){
        Transport transport = JSON.deserialize(json, Transport.class);
        try
        {
            tc.updateTransport(transport.id(), transport);
        }
        catch(IOException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Transport updated successfully",true, "").toJson();
    }

    public String removeTransport(String json){
        Transport transport = JSON.deserialize(json, Transport.class);
        try
        {
            tc.removeTransport(transport.id());
        }
        catch(IOException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Transport removed successfully",true, "").toJson();
    }

    public String getTransport(String json){
        Transport transport = JSON.deserialize(json, Transport.class);
        try
        {
            transport = tc.getTransport(transport.id());
        }
        catch(IOException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Transport found successfully",true, transport).toJson();
    }

    public String getAllTransports(){
        return new Response("All Transports found successfully",true, tc.getAllTransports()).toJson();
    }
}
