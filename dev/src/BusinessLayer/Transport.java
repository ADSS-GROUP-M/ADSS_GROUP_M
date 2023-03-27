package BusinessLayer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

public class Transport {
    private final int id;
    private Site source;
    private LinkedList<Site> destinations;
    private HashMap<Site,ItemList> itemLists;
    private int truckId;
    private int driverId;
    private LocalDateTime scheduledTime;
    private int weight;

    public Transport(int id, Site source, LinkedList<Site> destinations, HashMap<Site,ItemList> itemLists,
                     int truckId, int driverId, LocalDateTime scheduledTime/*, int weight*/){
        this.id = id;
        this.source = source;
        this.destinations = destinations;
        this.itemLists = itemLists;
        this.truckId = truckId;
        this.driverId = driverId;
        this.scheduledTime = scheduledTime;
//        this.weight = weight;
    }

}
