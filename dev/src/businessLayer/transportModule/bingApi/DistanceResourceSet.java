package businessLayer.transportModule.bingApi;

public record DistanceResourceSet(
        long estimatedTotal,
        DistanceResource[] resources
) {
}
