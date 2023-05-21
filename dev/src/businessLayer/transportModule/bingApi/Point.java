package businessLayer.transportModule.bingApi;

public record Point(String address, double[] coordinates){
    public double latitude() {
        return coordinates[0];
    }
    public double longitude() {
        return coordinates[1];
    }
}

