package BusinessLayer;
import java.util.TreeSet;
public class TrucksController {
    TreeSet <Truck> trucks;
    public TrucksController(){
        trucks = new TreeSet<>();
    }
    public Truck createTruck(int id,String model, int baseWeight,int maxWeight){
        Truck truck = new Truck(id,model,baseWeight, maxWeight);
        trucks.add(truck);
        return truck;
    }
    public Truck removeTruck(int id) throws Exception {
        Truck truck = getTruck(id);
        if (truck != null) {
            Truck removedTruck = truck;
            trucks.remove(truck);
            return removedTruck;
        }
        throw new Exception("Truck not found");
    }
    public Truck getTruck(int id){
        for (Truck truck : trucks) {
            if (truck.getId() == id) {
                return truck;
            }
        }
       return null;
    }
    public boolean updateTruck(int id, Truck newTruck){
        Truck truck = getTruck(id);
        if (truck != null) {
            trucks.remove(truck);
            trucks.add(newTruck);
            return true;
        }
        return false;
    }
}
