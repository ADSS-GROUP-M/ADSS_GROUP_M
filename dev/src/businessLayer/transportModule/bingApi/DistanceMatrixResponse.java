package businessLayer.transportModule.bingApi;

public record DistanceMatrixResponse (

        String authenticationResultCode,
        String brandLogoURI,
        String copyright,
        DistanceResourceSet[] resourceSets,
        long statusCode,
        String statusDescription,
        String traceID
        
    ){}

