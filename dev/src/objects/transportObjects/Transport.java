package objects.transportObjects;

import com.google.gson.reflect.TypeToken;
import utils.JsonUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record Transport (
        int id,
        DeliveryRoute deliveryRoute,
        String driverId,
        String truckId,
        LocalDateTime departureTime,
        int weight) {

    public Transport(int id, String source, List<String> destinations,Map<String,Integer> itemLists , String driverId, String truckId, LocalDateTime departureTime, int weight){
        this(
                id,
                new DeliveryRoute(id, source, departureTime.toLocalTime(), destinations, itemLists),
                driverId,
                truckId,
                departureTime,
                weight
        );
    }

    public Transport(int id, Transport transport) {
        this(
                id,
                transport.deliveryRoute(),
                transport.driverId(),
                transport.truckId(),
                transport.departureTime(),
                transport.weight()
        );
    }

    public Transport(DeliveryRoute route, Transport transport) {
        this(
                transport.id(),
                route,
                transport.driverId(),
                transport.truckId(),
                transport.departureTime(),
                transport.weight()
        );
    }

    /**
     * this constructor sets the id to be -1
     */
    public Transport(String source, List<String> destinations,Map<String,Integer> itemLists , String driverId, String truckId, LocalDateTime departureTime, int weight){
        this(1,
            new DeliveryRoute(-1, source, departureTime.toLocalTime(), destinations, itemLists),
            driverId,
            truckId,
            departureTime,
            weight
        );
    }

    /**
     * source is the first destination
     */
    public List<String> route(){
        return deliveryRoute.route();
    }

    public Map<String,Integer> itemLists(){
        return deliveryRoute.itemLists();
    }

    public static Transport getLookupObject(int id){
        return new Transport(id, null, null, null, null, 0);
    }

    public String toJson(){
        return JsonUtils.serialize(this);
    }

    public static Transport fromJson(String json){
        return JsonUtils.deserialize(json, Transport.class);
    }

    public static LinkedList<Transport> listFromJson(String json){
        Type type = new TypeToken<LinkedList<Transport>>(){}.getType();
        return JsonUtils.deserialize(json, type);
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
}


