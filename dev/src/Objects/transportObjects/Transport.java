package Objects.transportObjects;

import com.google.gson.reflect.TypeToken;
import utils.JsonUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public record Transport (int id, String source, LinkedList<String> destinations, HashMap<String, Integer> itemLists,
                         String truckId, int driverId, LocalDateTime scheduledTime, int weight){


    /**
     * @param id new id of the transport
     * @param other transport to copy from
     */
    public Transport(int id, Transport other){
        this(
                id,
                other.source,
                other.destinations,
                other.itemLists,
                other.truckId,
                other.driverId,
                other.scheduledTime,
                other.weight
        );
    }


    /**
     * this constructor sets the id to be -1
     */
    public Transport(String source, LinkedList<String> destinations, HashMap<String, Integer> itemLists,
                     String truckId, int driverId, LocalDateTime scheduledTime, int weight){
        this(
                -1,
                source,
                destinations,
                itemLists,
                truckId,
                driverId,
                scheduledTime,
                weight
        );
    }

    public static Transport getLookupObject(int id){
        return new Transport(id, null, null, null, null, 0, null, 0);
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

    public Transport newId(int id) {
        return new Transport(id, this);
    }
}


