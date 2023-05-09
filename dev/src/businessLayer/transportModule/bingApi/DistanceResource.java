package businessLayer.transportModule.bingApi;

public record DistanceResource(
        String type,
        Destination[] destinations,
        Destination[] origins,
        Result[] results
) {
}
