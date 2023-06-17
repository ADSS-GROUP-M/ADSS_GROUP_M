package domainObjects.transportModule;

import com.google.gson.reflect.TypeToken;
import dataAccessLayer.dalAssociationClasses.transportModule.DeliveryRoute;
import dataAccessLayer.dalAssociationClasses.transportModule.TransportMetaData;
import utils.JsonUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Transport {
    private final int id;
    private final List<String> route;
    private final Map<String,Integer> destinations_itemListIds;
    private final String driverId;
    private final String truckId;
    private final LocalDateTime departureTime;
    private final int weight;
    private final Set<String> destinationsSet;
    private Map<String, LocalTime> estimatedArrivalTimes;
    private boolean manualOverride;

    /**
     * This constructor allows setting all fields.
     * Generally, this constructor should not be used outside of special cases
     * that require preserving some state (e.g. when updating a transport).
     */
    public Transport(int id,
                     List<String> route,
                     Map<String, Integer> destinations_itemListIds,
                     String driverId,
                     String truckId,
                     LocalDateTime departureTime,
                     int weight,
                     Map<String, LocalTime> estimatedArrivalTimes,
                     boolean manualOverride) {
        this.id = id;
        this.route = route;
        this.destinations_itemListIds = destinations_itemListIds;
        this.driverId = driverId;
        this.truckId = truckId;
        this.departureTime = departureTime;
        this.weight = weight;
        this.estimatedArrivalTimes = estimatedArrivalTimes;
        this.manualOverride = manualOverride;

        if(route != null) {
            this.destinationsSet = new HashSet<>(route);
        } else {
            this.destinationsSet = null;
        }
    }

    public Transport(int id,
                     List<String> route,
                     Map<String, Integer> destinations_itemListIds,
                     String driverId,
                     String truckId,
                     LocalDateTime departureTime,
                     int weight,
                     Map<String, LocalTime> estimatedArrivalTimes) {
        this(id, route, destinations_itemListIds, driverId, truckId, departureTime, weight, estimatedArrivalTimes, false);
    }

    /**
     * this constructor sets the id to be -1
     */
    public Transport(List<String> route,
                     Map<String, Integer> destinations_itemListIds,
                     String driverId,
                     String truckId,
                     LocalDateTime departureTime,
                     int weight,
                     Map<String, LocalTime> estimatedArrivalTimes) {
        this(-1, route, destinations_itemListIds, driverId, truckId, departureTime, weight, estimatedArrivalTimes);
    }

    public Transport(TransportMetaData metaData, DeliveryRoute route) {
        this(
                metaData.transportId(),
                route.route(),
                route.itemLists(),
                metaData.driverId(),
                metaData.truckId(),
                metaData.departureTime(),
                metaData.weight(),
                route.estimatedArrivalTimes()
        );
    }

    public Transport(int newId, Transport old) {
        this(
                newId,
                old.route(),
                old.itemLists(),
                old.driverId,
                old.truckId,
                old.departureTime,
                old.weight,
                old.estimatedArrivalTimes()
        );
    }

    public Transport(int id,
                     List<String> route,
                     Map<String, Integer> itemLists,
                     String driverId,
                     String truckId,
                     LocalDateTime departureTime,
                     int weight) {
        this(id, route, itemLists, driverId, truckId, departureTime, weight, null);
    }

    /**
     * this constructor sets the id to be -1
     */
    public Transport(List<String> route,
                     Map<String, Integer> destinations_itemListIds,
                     String driverId,
                     String truckId,
                     LocalDateTime departureTime,
                     int weight) {
        this(-1, route, destinations_itemListIds, driverId, truckId, departureTime, weight, null);
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

    /**
     * source is the first destination
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

    public boolean arrivalTimesManualOverride() {
        return manualOverride;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transport transport = (Transport) o;
        return id == transport.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int id() {
        return id;
    }

    public String driverId() {
        return driverId;
    }

    public String truckId() {
        return truckId;
    }

    public LocalDateTime departureTime() {
        return departureTime;
    }

    public int weight() {
        return weight;
    }

    public static Transport getLookupObject(int id) {
        return new Transport(id, null, null, null, null, null, -1, null, false);
    }

    public String toJson() {
        return JsonUtils.serialize(this);
    }

    public static Transport fromJson(String json) {
        return JsonUtils.deserialize(json, Transport.class);
    }

    public static LinkedList<Transport> listFromJson(String json) {
        Type type = new TypeToken<LinkedList<Transport>>(){}.getType();
        return JsonUtils.deserialize(json, type);
    }
}


