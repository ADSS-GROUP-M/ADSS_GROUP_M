package transportModule.records;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public record Transport (int id, Site source, LinkedList<Site> destinations, HashMap<Site,ItemList> itemLists,
        String truckId, int driverId, LocalDateTime scheduledTime, int weight){

    public static Transport getLookupObject(int id){
        return new Transport(id, null, null, null, null, 0, null, 0);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transport transport = (Transport) o;
        return id == transport.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


