package dataAccessLayer.transportModule;

import java.time.LocalTime;

public record TransportDestination(int transportId, int destination_index, String name, int itemListId, LocalTime expectedArrivalTime) {
    public static TransportDestination getLookupObject(int transportId, int destination_index) {
        return new TransportDestination(transportId, destination_index, null, 0 , null);
    }
}
