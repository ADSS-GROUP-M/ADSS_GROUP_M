package businessLayer.transportModule.bingRestApi;

public record LocationByQueryResponse(
    String authenticationResultCode,
    String brandLogoURI,
    String copyright,
    ResourceSet[] resourceSets,
    long statusCode,
    String statusDescription,
    String traceID){

        // ResourceSet.java
        public record ResourceSet(
                long estimatedTotal,
                Resource[] resources
        ){}

        // Resource.java
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

        // Address.java
        public record Address (
                String addressLine,
                String adminDistrict,
                String adminDistrict2,
                String countryRegion,
                String formattedAddress,
                String locality,
                String postalCode
        ){}

        // GeocodePoint.java
        public record GeocodePoint (
                String type,
                double[] coordinates,
                String calculationMethod,
                String[] usageTypes
        ){}

        // Point.java
        public record Point (
                String type,
                double[] coordinates
        ){}
}

