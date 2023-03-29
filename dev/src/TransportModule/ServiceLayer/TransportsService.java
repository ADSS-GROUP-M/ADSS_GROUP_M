package TransportModule.ServiceLayer;

import TransportModule.BusinessLayer.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
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
     *     "TransportZone": "source",
     *     "Address": "source-address",
     *     "phoneNumber": "contact",
     *     "contactName": "loading"
     *   },
     *   "destinations": [
     *     {
     *       "TransportZone": "source",
     *       "Address": "source-address",
     *       "phoneNumber": "contact",
     *       "contactName": "loading"
     *     }
     *   ],
     *   "itemLists": [
     *     [
     *       {
     *         "TransportZone": "destination",
     *         "Address": "destination-address",
     *         "phoneNumber": "contact",
     *         "contactName": "unloading"
     *       },
     *       {
     *         "id": 1,
     *         "items": {
     *           "item2": 2,
     *           "item1": 1,
     *           "item3": 3
     *         }
     *       }
     *     ]
     *   ],
     *   "truckId": 1,
     *   "driverId": 1,
     *   "scheduledDate": "1977-01-01",
     *   "leavingTime": "14:00",
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

    public static void main (String[] args){
        Site source = new Site("source", "source-address", "contact", "loading");
        Site destination = new Site("destination", "destination-address", "contact", "unloading");
        LinkedList<Site> sites = new LinkedList<>();
        sites.add(source);
        LocalDateTime dateTime = LocalDateTime.of(1977,1,1,12,24);
        HashMap<String,Integer> items = new HashMap<>();
        items.put("item", 5);
        ItemList itemList = new ItemList(1,items);
        HashMap<Site,ItemList> siteItems = new HashMap<>();
        siteItems.put(destination, itemList);
        Transport t = new Transport(1,source,sites,siteItems,1,1,dateTime);
        String json = JSON.serialize(t);
        System.out.println(json);
        Transport t2 = JSON.deserialize(json, Transport.class);
    }
}
