package businessLayer.transportModule.bingApi;

public record LocationResource(
        String __type,
        double[] bbox,
        String name,
        Point point,
        Address address,
        String confidence,
        String entityType,
        GeocodePoint[] geocodePoints,
        String[] matchCodes
) {
}
