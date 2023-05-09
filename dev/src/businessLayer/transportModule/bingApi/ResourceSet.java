package businessLayer.transportModule.bingApi;

public record ResourceSet(
        long estimatedTotal,
        Resource[] resources
) {
}
