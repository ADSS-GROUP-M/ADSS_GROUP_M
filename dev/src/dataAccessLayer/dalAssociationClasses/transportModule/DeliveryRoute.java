package dataAccessLayer.dalAssociationClasses.transportModule;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record DeliveryRoute (
    int transportId,
    List<String> route,
    Map<String,Integer> itemLists,
    Map<String, LocalTime> estimatedArrivalTimes){

    public static DeliveryRoute getLookupObject(int transportId){
        return new DeliveryRoute(transportId,null,null,null);
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


    public boolean deepEquals(DeliveryRoute other) {
        return this.equals(other) &&
                this.route.equals(other.route) &&
                this.itemLists.equals(other.itemLists) &&
                this.estimatedArrivalTimes.equals(other.estimatedArrivalTimes);
    }
}
