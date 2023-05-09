package dataAccessLayer.transportModule;

public record DistanceBetweenSites(String source, String destination, double distance, double duration) {
    public DistanceBetweenSites(DistanceBetweenSites old, int newDistance, int newDuration) {
        this(old.source(), old.destination(), newDistance, newDuration);
    }

    public static DistanceBetweenSites getLookupObject(String source, String destination) {
        return new DistanceBetweenSites(source,destination, 0,0);
    }
}
