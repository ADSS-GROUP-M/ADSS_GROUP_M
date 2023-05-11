package objects.transportObjects;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeliveryRoute {

    private final String source;
    private final List<String> destinations;
    private final Set<String> destinationsSet;
    private final Map<String,Integer> destinations_itemListIds;
    private Map<String, LocalTime> estimatedArrivalTimes;
    private boolean manualOverride;

    public DeliveryRoute(String source,
                         List<String> destinations,
                         Map<String, Integer> destinations_itemListIds,
                         Map<String, LocalTime> estimatedArrivalTimes){
        this.source = source;
        this.destinations = destinations;
        this.destinationsSet = new HashSet<>(destinations);
        this.destinations_itemListIds = destinations_itemListIds;
        this.estimatedArrivalTimes = estimatedArrivalTimes;
    }

    public DeliveryRoute(String source,
                         List<String> destinations,
                         Map<String, Integer> destinations_itemListIds){
        this(source,destinations,destinations_itemListIds,null);
    }

    public void initializeArrivalTimes(Map<String,LocalTime> estimatedArrivalTimes){
        // check there is an arrival time for each destination
        for(String destination : destinations){
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

    public String source() {
        return source;
    }

    public List<String> destinations() {
        return destinations;
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
