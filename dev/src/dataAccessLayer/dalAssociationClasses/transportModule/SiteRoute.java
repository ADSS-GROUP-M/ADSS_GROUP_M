package dataAccessLayer.dalAssociationClasses.transportModule;

public record SiteRoute(String source, String destination, double distance, double duration) {

    public static SiteRoute getLookupObject(String source, String destination) {
        return new SiteRoute(source,destination, 0,0);
    }
}
