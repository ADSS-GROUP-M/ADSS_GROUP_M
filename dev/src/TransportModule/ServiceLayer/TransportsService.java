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

//    public static void main (String[] args){
//        Site source = new Site("Zone1", "source-address", "054-6666666", "moshe", Site.SiteType.LOGISTICAL_CENTER);
//        Site destination = new Site("Zone1", "destination-address", "054-7777777", "yossi", Site.SiteType.BRANCH);
//        LinkedList<Site> sites = new LinkedList<>();
//        sites.add(source);
//        LocalDateTime dateTime = LocalDateTime.of(1977,1,1,12,24);
//        HashMap<String,Integer> items1 = new HashMap<>();
//        items1.put("itemToLoad", 5);
//        HashMap<String,Integer> items2 = new HashMap<>();
//        items2.put("itemToUnload", 7);
//        ItemList itemList = new ItemList(1,items1,items2);
//        HashMap<Site,ItemList> siteItems = new HashMap<>();
//        siteItems.put(destination, itemList);
//        Transport t = new Transport(1,source,sites,siteItems,1,1,dateTime);
//        String json = JSON.serialize(t);
//        System.out.println(json);
//        Transport t2 = JSON.deserialize(json, Transport.class);
//    }
}
