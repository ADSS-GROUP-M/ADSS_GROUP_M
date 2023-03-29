package TransportModule.BusinessLayer;

import java.io.IOException;
import java.util.TreeMap;

public class TrucksController {
    TreeMap<Integer,Truck> trucks;

    public TrucksController(){
        trucks = new TreeMap<>();
    }

    public Truck createTruck(int id,String model, int baseWeight,int maxWeight){
        Truck truck = new Truck(id,model,baseWeight, maxWeight);
        trucks.put(id,truck);
        return truck;
    }

    public Truck removeTruck(int id) throws IOException {
        if (trucks.containsKey(id) == false)
            throw new IOException("Truck not found");

        return trucks.remove(id);
    }
    public Truck getTruck(int id) throws IOException {
        if (trucks.containsKey(id) == false)
            throw new IOException("Truck not found");

        return trucks.get(id);
    }

    public void updateTruck(int id, Truck newTruck) throws IOException{
        if(trucks.containsKey(id) == false)
            throw new IOException("Truck not found");

        trucks.put(id, newTruck);
    }

}
