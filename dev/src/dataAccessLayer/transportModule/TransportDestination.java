package dataAccessLayer.transportModule;

public record TransportDestination(int transportId, int destination_index, String address, int itemListId) {
    public static TransportDestination getLookupObject(int transportId, int destination_index) {
        return new TransportDestination(transportId, destination_index, null, 0);
    }
}
