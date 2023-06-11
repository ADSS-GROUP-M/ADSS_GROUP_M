package dataAccessLayer.dalAssociationClasses.transportModule;

import java.time.LocalDateTime;

public record TransportMetaData(
        int transportId,
        String driverId,
        String truckId,
        LocalDateTime departureTime,
        int weight
) {

    public static TransportMetaData getLookupObject(int id){
        return new TransportMetaData(id, null, null, null, 0);
    }
}


