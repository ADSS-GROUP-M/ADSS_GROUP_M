package CMDApp.Records;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

public record Transport (int id, Site source, LinkedList<Site> destinations, HashMap<Site, ItemList> itemLists,
                         String truckId, int driverId, LocalDateTime scheduledTime, int weight){

    public Transport(int id, Site source, LinkedList<Site> destinations, HashMap<Site, ItemList> itemLists,
                     String truckId, int driverId, LocalDateTime scheduledTime){
        this(id, source, destinations, itemLists, truckId, driverId, scheduledTime, 0);
    }
}


