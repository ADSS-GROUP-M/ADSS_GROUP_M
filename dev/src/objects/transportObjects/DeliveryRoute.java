package objects.transportObjects;

import javafx.util.Pair;

import java.time.LocalTime;
import java.util.*;

public class DeliveryRoute {

    public static final int AVERAGE_SPEED = 80;
    public static final long AVERAGE_TIME_PER_VISIT = 30;

    private final String source;
    private final LocalTime departureHour;
    private final List<String> destinations;
    private final Set<String> destinationsSet;
    private final Map<String,Integer> destinations_itemListIds;
    private Map<String, LocalTime> estimatedArrivalTimes;

    private Map<Pair<String,String>,Integer> distancesBetweenDestinations;

    public DeliveryRoute(String source,
                         LocalTime departureHour,
                         List<String> destinations,
                         Map<String, Integer> destinations_itemListIds){
        this.source = source;
        this.departureHour = departureHour;
        this.destinations = destinations;
        this.destinationsSet = new HashSet<>(destinations);
        this.destinations_itemListIds = destinations_itemListIds;
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
        return estimatedArrivalTimes.get(destination);
    }

    public void overrideArrivalTime(String destination, LocalTime arrivalTime){
        if(estimatedArrivalTimes == null){
            throw new RuntimeException("Estimated arrival times have not been initialized");
        }
        estimatedArrivalTimes.put(destination,arrivalTime);
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
}
