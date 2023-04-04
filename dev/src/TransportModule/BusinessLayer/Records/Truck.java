package TransportModule.BusinessLayer.Records;

import java.util.Objects;

public record Truck (String id, String model, int baseWeight, int maxWeight, CoolingCapacity coolingCapacity){

    public static final int MAX_WEIGHT_A_TIER = 10000;
    public static final int MAX_WEIGHT_B_TIER = 20000;

    public enum CoolingCapacity{
        NONE,
        COLD,
        FROZEN
    }

    public String requiredLicense() {
        return switch(coolingCapacity) {
            case NONE -> {
                if(maxWeight <= MAX_WEIGHT_A_TIER) yield "A1";
                else if (maxWeight <= MAX_WEIGHT_B_TIER) yield "B1";
                else yield "C1";
            }
            case COLD -> {
                if(maxWeight <= MAX_WEIGHT_A_TIER) yield "A2";
                else if (maxWeight <= MAX_WEIGHT_B_TIER) yield "B2";
                else yield "C2";
            }
            case FROZEN -> {
                if(maxWeight <= MAX_WEIGHT_A_TIER) yield "A3";
                else if (maxWeight <= MAX_WEIGHT_B_TIER) yield "B3";
                else yield "C3";
            }
        };
    }
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
}
