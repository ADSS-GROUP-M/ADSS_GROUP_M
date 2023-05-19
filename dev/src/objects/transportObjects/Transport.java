package objects.transportObjects;

import com.google.gson.reflect.TypeToken;
import dataAccessLayer.dalAssociationClasses.transportModule.TransportMetaData;
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

    public Transport(int id, List<String> route ,Map<String,Integer> itemLists , String driverId, String truckId, LocalDateTime departureTime, int weight){
        this(
                id,
                new DeliveryRoute(id, route, itemLists),
                driverId,
                truckId,
                departureTime,
                weight
        );
    }

    /**
     * this constructor sets the id to be -1
     */
    public Transport(List<String> destinations,Map<String,Integer> itemLists , String driverId, String truckId, LocalDateTime departureTime, int weight){
        this(
                -1,
                new DeliveryRoute(destinations, itemLists),
                driverId,
                truckId,
                departureTime,
                weight
        );
    }

    public Transport(int id, DeliveryRoute route, Transport transport) {
        this(
                id,
                route,
                transport.driverId(),
                transport.truckId(),
                transport.departureTime(),
                transport.weight()
        );
    }

    public Transport(int id, Transport newTransport) {
        this(
                id,
                newTransport.deliveryRoute,
                newTransport.driverId,
                newTransport.truckId,
                newTransport.departureTime,
                newTransport.weight
        );
    }

    public Transport(DeliveryRoute selectedRoute, Transport selectedTransport) {
        this(
                selectedTransport.id,
                selectedRoute,
                selectedTransport.driverId,
                selectedTransport.truckId,
                selectedTransport.departureTime,
                selectedTransport.weight
        );
    }

    public Transport(TransportMetaData metaData, DeliveryRoute route) {
        this(
                metaData.transportId(),
                route,
                metaData.driverId(),
                metaData.truckId(),
                metaData.departureTime(),
                metaData.weight()
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


