package TransportModule.BusinessLayer;

import java.util.Objects;

public class Truck {
    private final int id;
    private final String model;
    private final int baseWeight;
    private final int maxWeight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Truck truck = (Truck) o;
        return id == truck.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

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
