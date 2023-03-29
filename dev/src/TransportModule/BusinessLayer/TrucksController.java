package TransportModule.BusinessLayer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

public class TrucksController {

    TreeMap<Integer,Truck> trucks;

    public TrucksController(){
        trucks = new TreeMap<>();
    }

    public void addTruck(Truck truck) throws IOException {
        if (trucks.containsKey(truck.getId()) == false)
            trucks.put(truck.getId(), truck);
        else throw new IOException("Truck already exists");
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

    public LinkedList<Truck> getAllTrucks(){
        LinkedList<Truck> trucksList = new LinkedList<>();
        for(Truck truck : trucks.values())
            trucksList.add(truck);
        return trucksList;
    }

}
