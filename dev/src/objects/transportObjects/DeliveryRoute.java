package objects.transportObjects;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class DeliveryRoute {

    private final int transportId;
    private final List<String> route;
    private final Set<String> destinationsSet;
    private final Map<String,Integer> destinations_itemListIds;
    private Map<String, LocalTime> estimatedArrivalTimes;
    private boolean manualOverride;
    public DeliveryRoute(int transportId,
                         List<String> route,
                         Map<String, Integer> destinations_itemListIds,
                         Map<String, LocalTime> estimatedArrivalTimes){
        this.transportId = transportId;
        this.route = route;
        this.destinationsSet = new HashSet<>(route);
        this.destinations_itemListIds = destinations_itemListIds;
        this.estimatedArrivalTimes = estimatedArrivalTimes;
    }


    /**
     * this constructor sets the id to be -1
     */
    public DeliveryRoute(
                         List<String> route,
                         Map<String, Integer> destinations_itemListIds){
        this(-1,route,destinations_itemListIds,null);
    }

    public DeliveryRoute(int newId, DeliveryRoute deliveryRoute) {
        this(newId, deliveryRoute.route, deliveryRoute.destinations_itemListIds, deliveryRoute.estimatedArrivalTimes);
    }

    public DeliveryRoute(int transportId,
                         List<String> route,
                         Map<String, Integer> destinations_itemListIds){
        this(transportId,route,destinations_itemListIds,null);
    }

    public void initializeArrivalTimes(Map<String,LocalTime> estimatedArrivalTimes){
        // check there is an arrival time for each destination
        for(String destination : route){
            if(estimatedArrivalTimes.containsKey(destination) == false){
                throw new RuntimeException("Incomplete estimated arrival times");
            }
        }
        this.estimatedArrivalTimes = estimatedArrivalTimes;
    }

    public LocalTime getEstimatedTimeOfArrival(String destination) {
        if(estimatedArrivalTimes == null){
            throw new RuntimeException("Estimated arrival times have not been initialized");
        }

        if(destinationsSet.contains(destination) == false){
            throw new RuntimeException("Destination not found");
        }

        return estimatedArrivalTimes.get(destination);
    }

    public void overrideArrivalTime(String destination, LocalTime arrivalTime){
        if(estimatedArrivalTimes == null){
            throw new RuntimeException("Estimated arrival times have not been initialized");
        }
        estimatedArrivalTimes.put(destination,arrivalTime);
        manualOverride = true;
    }

    public int transportId() {
        return transportId;
    }

    /**
     * @apiNote route().get(0) is the source
     */
    public List<String> route() {
        return route;
    }

    public Map<String, Integer> itemLists() {
        return destinations_itemListIds;
    }

    public Map<String, LocalTime> estimatedArrivalTimes() {
        return estimatedArrivalTimes;
    }

    public boolean manuallyOverrideEstimatedArrivalTimes() {
        return manualOverride;
    }

    public static DeliveryRoute getLookupObject(int transportId){
        return new DeliveryRoute(transportId,new LinkedList<>(),null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryRoute that = (DeliveryRoute) o;
        return transportId == that.transportId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transportId);
    }
}
