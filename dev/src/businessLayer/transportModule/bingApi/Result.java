package businessLayer.transportModule.bingApi;

public record Result(
        long destinationIndex,
        long originIndex,
        long totalWalkDuration,
        double travelDistance,
        double travelDuration
) {
}
