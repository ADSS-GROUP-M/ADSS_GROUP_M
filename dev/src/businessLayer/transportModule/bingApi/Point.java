package businessLayer.transportModule.bingApi;

public record Point(
        String type,
        double[] coordinates){

    public String address() {
        return type;
    }

    public double latitude() {
        return coordinates[0];
    }

    public double longitude() {
        return coordinates[1];
    }
}

