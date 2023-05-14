package objects.transportObjects;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeliveryRoute {

    private final int transportId;
    private final List<String> route;
    private final Set<String> destinationsSet;
    private final Map<String,Integer> destinations_itemListIds;
    private Map<String, LocalTime> estimatedArrivalTimes;
    private boolean manualOverride;
    public DeliveryRoute(int transportId,
                         String source,
                         LocalTime departureTime,
                         List<String> route,
                         Map<String, Integer> destinations_itemListIds,
                         Map<String, LocalTime> estimatedArrivalTimes){
        this.transportId = transportId;
        this.route = route;
        this.destinationsSet = new HashSet<>(route);
        this.destinations_itemListIds = destinations_itemListIds;
        this.estimatedArrivalTimes = estimatedArrivalTimes;
        route.add(0,source);
        estimatedArrivalTimes.put(source,departureTime);
    }

    public DeliveryRoute(int transportId,
                         String source,
                         LocalTime departureTime,
                         List<String> route,
                         Map<String, Integer> destinations_itemListIds){
        this(transportId, source, route,destinations_itemListIds,null);
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
}
