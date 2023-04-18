package Objects.transportObjects;

import com.google.gson.reflect.TypeToken;
import utils.JsonUtils;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Objects;

public record Truck (String id, String model, int baseWeight, int maxWeight, CoolingCapacity coolingCapacity){

    private static final int MAX_WEIGHT_A_TIER = 10000;
    private static final int MAX_WEIGHT_B_TIER = 20000;

    public enum CoolingCapacity{
        NONE,
        COLD,
        FROZEN
    }

    public String requiredLicense() {
        String license = "";
        if (maxWeight <= MAX_WEIGHT_A_TIER) {
            license += "A";
        } else if (maxWeight <= MAX_WEIGHT_B_TIER) {
            license += "B";
        } else {
            license += "C";
        }
        license += switch (coolingCapacity) {
            case NONE -> "1";
            case COLD -> "2";
            case FROZEN ->"3";
        };
        return license;
    }

    public static Truck getLookupObject(String id){
        return new Truck(id, null, 0, 0, null);
    }

    public String toJson(){
        return JsonUtils.serialize(this);
    }

    public static Truck fromJson(String json){
        return JsonUtils.deserialize(json, Truck.class);
    }

    public static LinkedList<Truck> listFromJson(String json){
        Type type = new TypeToken<LinkedList<Truck>>(){}.getType();
        return JsonUtils.deserialize(json, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Truck truck = (Truck) o;
        return id.equalsIgnoreCase(truck.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
