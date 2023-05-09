package businessLayer.transportModule.bingApi;

public record LocationResourceSet(
        long estimatedTotal,
        LocationResource[] locationResources
) {
}
