package dataAccessLayer.transportModule;

public record DistanceBetweenSites(String source, String destination, int distance) {
    public static DistanceBetweenSites getLookupObject(String source, String destination) {
        return new DistanceBetweenSites(source,destination, 0);
    }
}
