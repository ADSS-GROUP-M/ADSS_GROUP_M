package dataAccessLayer.transportModule;

public record siteRoute(String source, String destination, double distance, double duration) {

    public static siteRoute getLookupObject(String source, String destination) {
        return new siteRoute(source,destination, 0,0);
    }
}
