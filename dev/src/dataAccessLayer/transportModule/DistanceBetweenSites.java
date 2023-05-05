package dataAccessLayer.transportModule;

public record DistanceBetweenSites(String source, String destination, double distance) {
    public DistanceBetweenSites(DistanceBetweenSites old, int newDistance) {
        this(old.source(), old.destination(), newDistance);
    }

    public static DistanceBetweenSites getLookupObject(String source, String destination) {
        return new DistanceBetweenSites(source,destination, 0);
    }
}
