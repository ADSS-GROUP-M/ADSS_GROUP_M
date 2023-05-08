package businessLayer.transportModule.bingApi;

public record LocationByQueryResponse(
        String authenticationResultCode,
        String brandLogoURI,
        String copyright,
        ResourceSet[] resourceSets,
        long statusCode,
        String statusDescription,
        String traceID){

    public record ResourceSet(
            long estimatedTotal,
            Resource[] resources
    ){}

    public record Resource(
            String __type,
            double[] bbox,
            String name,
            Point point,
            Address address,
            String confidence,
            String entityType,
            GeocodePoint[] geocodePoints,
            String[] matchCodes
    ) {}

    public record Address (
            String addressLine,
            String adminDistrict,
            String adminDistrict2,
            String countryRegion,
            String formattedAddress,
            String locality,
            String postalCode
    ){}

    public record GeocodePoint (
            String type,
            double[] coordinates,
            String calculationMethod,
            String[] usageTypes
    ){}
    
    public record Point (
            String type,
            double[] coordinates
    ){}
}

