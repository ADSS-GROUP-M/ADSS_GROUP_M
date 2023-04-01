package TransportModule.BusinessLayer;

import TransportModule.BusinessLayer.Records.Truck;

import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

public class TrucksController {

    TreeMap<String, Truck> trucks;

    public TrucksController(){
        trucks = new TreeMap<>();
    }

    public void addTruck(Truck truck) throws IOException {
        if (trucks.containsKey(truck.id()) == true)
            throw new IOException("Truck already exists");

        if(truck.baseWeight() > truck.maxWeight())
            throw new IOException("Truck base weight is bigger than max weight");

        trucks.put(truck.id(), truck);
    }

    public Truck removeTruck(String id) throws IOException {
        if (trucks.containsKey(id) == false)
            throw new IOException("Truck not found");

        return trucks.remove(id);
    }
    public Truck getTruck(String id) throws IOException {
        if (trucks.containsKey(id) == false)
            throw new IOException("Truck not found");

        return trucks.get(id);
    }

    public void updateTruck(String id, Truck newTruck) throws IOException{
        if(trucks.containsKey(id) == false)
            throw new IOException("Truck not found");
        if(newTruck.baseWeight() > newTruck.maxWeight())
            throw new IOException("Truck base weight is bigger than max weight");

        trucks.put(id, newTruck);
    }

    public LinkedList<Truck> getAllTrucks(){
        LinkedList<Truck> trucksList = new LinkedList<>();
        for(Truck truck : trucks.values())
            trucksList.add(truck);
        return trucksList;
    }

}
