package TransportModule.ServiceLayer;

import TransportModule.BusinessLayer.*;

import java.io.IOException;
import java.util.LinkedList;

public class TransportsService {

    private TransportsController tc;

    public TransportsService(){
        tc = new TransportsController();
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
     * @param json
     * @return
     */
    public String createTransport(String json){
        Transport transport = JSON.deserialize(json, Transport.class);
        try
        {
            tc.addTransport(transport);
        }
        catch(IOException e){
            return new Response<String>(e.getMessage(), false, "").getJson();
        }
        return new Response<String>("Transport created successfully",true, "").getJson();
    }

    public String updateTransport(String json){
        Transport transport = JSON.deserialize(json, Transport.class);
        try
        {
            tc.updateTransport(transport.getId(), transport);
        }
        catch(IOException e){
            return new Response<String>(e.getMessage(), false, "").getJson();
        }
        return new Response<String>("Transport updated successfully",true, "").getJson();
    }

    public String removeTransport(String json){
        Transport transport = JSON.deserialize(json, Transport.class);
        try
        {
            tc.removeTransport(transport.getId());
        }
        catch(IOException e){
            return new Response<String>(e.getMessage(), false, "").getJson();
        }
        return new Response<String>("Transport removed successfully",true, "").getJson();
    }

    public String getTransport(String json){
        Transport transport = JSON.deserialize(json, Transport.class);
        try
        {
            transport = tc.getTransport(transport.getId());
        }
        catch(IOException e){
            return new Response<String>(e.getMessage(), false, "").getJson();
        }
        return new Response<Transport>("Transport found successfully",true, transport).getJson();
    }

    public String getAllTransports(){
        return new Response<LinkedList<Transport>>("All Transports found successfully",true, tc.getAllTransports()).getJson();
    }
}
