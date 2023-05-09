package businessLayer.transportModule.bingApi;

public record LocationByQueryResponse(
        String authenticationResultCode,
        String brandLogoURI,
        String copyright,
        DistanceResourceSet[] resourceSets,
        long statusCode,
        String statusDescription,
        String traceID){
}

