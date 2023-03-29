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
     * <code>
     *{<br/>
     *   &nbsp"id": 1,<br/>
     *   &nbsp"source": {<br/>
     *     &nbsp&nbsp"TransportZone": "source",<br/>
     *     &nbsp&nbsp"Address": "source-address",<br/>
     *     &nbsp&nbsp"phoneNumber": "contact",<br/>
     *     &nbsp&nbsp"contactName": "loading"<br/>
     *   &nbsp},<br/>
     *   &nbsp"destinations": [<br/>
     *     &nbsp&nbsp{<br/>
     *       &nbsp&nbsp&nbsp"TransportZone": "source",<br/>
     *       &nbsp&nbsp&nbsp"Address": "source-address",<br/>
     *       &nbsp&nbsp&nbsp"phoneNumber": "contact",<br/>
     *       &nbsp&nbsp&nbsp"contactName": "loading"<br/>
     *     &nbsp&nbsp}<br/>
     *   &nbsp],<br/>
     *   &nbsp"itemLists": {<br/>
     *     &nbsp"TransportModule.BusinessLayer.Site@cc2cd554": {<br/>
     *       &nbsp&nbsp"id": 1,<br/>
     *       &nbsp&nbsp"items": {<br/>
     *         &nbsp&nbsp&nbsp&nbsp"item2": 2,<br/>
     *         &nbsp&nbsp&nbsp&nbsp"item1": 1,<br/>
     *         &nbsp&nbsp&nbsp&nbsp"item3": 3<br/>
     *       &nbsp&nbsp&nbsp&nbsp}<br/>
     *     &nbsp&nbsp&nbsp}<br/>
     *   &nbsp&nbsp},<br/>
     *   &nbsp"truckId": 1,<br/>
     *   &nbsp"driverId": 1,<br/>
     *   &nbsp"scheduledDate": "1977-01-01",<br/>
     *   &nbsp"leavingTime": "14:00",<br/>
     *   &nbsp"weight": 0<br/>
     * }
     * </code>
     *
     *
     * @param json
     * @return
     */
    public String createTransport(String json){
        Transport transport = JsonSerializer.deserialize(json, Transport.class);
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
        Transport transport = JsonSerializer.deserialize(json, Transport.class);
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
        Transport transport = JsonSerializer.deserialize(json, Transport.class);
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
        Transport transport = JsonSerializer.deserialize(json, Transport.class);
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
        LocalDateTime dateTime = LocalDateTime.of(1977,1,1,0,0);
        HashMap<String,Integer> items = new HashMap<>();
        items.put("item1", 1);
        items.put("item2", 2);
        items.put("item3", 3);
        ItemList itemList = new ItemList(1,items);
        HashMap<Site,ItemList> siteItems = new HashMap<>();
        siteItems.put(destination, itemList);
        Transport t = new Transport(1,source,sites,siteItems,1,1,"1977-01-01","14:00");
        String json = JsonSerializer.serialize(t);
        System.out.println(json);
    }
}
