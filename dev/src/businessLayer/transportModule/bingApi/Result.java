package businessLayer.transportModule.bingApi;

public record Result(
        int destinationIndex,
        int originIndex,
        int totalWalkDuration,
        double travelDistance,
        double travelDuration
) {
}
