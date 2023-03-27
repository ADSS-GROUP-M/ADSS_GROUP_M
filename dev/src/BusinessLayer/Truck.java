package BusinessLayer;

public class Truck {
    private final int id;
    private final String model;
    private final int baseWeight;
    private final int maxWeight;

    public Truck(int id, String model, int baseWeight, int maxWeight){
        this.id = id;
        this.model = model;
        this.baseWeight = baseWeight;
        this.maxWeight = maxWeight;
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public int getBaseWeight() {
        return baseWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }
}
