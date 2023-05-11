package businessLayer.transportModule.bingApi;

public record GeocodePoint(
        String type,
        double[] coordinates,
        String calculationMethod,
        String[] usageTypes
) {
}
