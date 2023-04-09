package transportModule.backend.businessLayer;

import transportModule.backend.businessLayer.records.Truck;

import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * The TrucksController class is responsible for managing trucks in the transport module.
 * It provides functionality to add, remove, update, and retrieve trucks, as well as get a list of all trucks.
 */
public class TrucksController {

    private final TreeMap<String, Truck> trucks;

    public TrucksController(){
        trucks = new TreeMap<>();
    }

    /**
     * Adds a truck to the controller.
     *
     * @param truck The truck to be added.
     * @throws IOException If the truck already exists or if the base weight is greater than the max weight.
     */
    public void addTruck(Truck truck) throws IOException {
        if (trucks.containsKey(truck.id()) == true)
            throw new IOException("Truck already exists");

        if(truck.baseWeight() > truck.maxWeight())
            throw new IOException("Truck base weight is bigger than max weight");

        trucks.put(truck.id(), truck);
    }

    /**
     * Removes a truck from the controller.
     *
     * @param id The ID of the truck to be removed.
     * @throws IOException If the truck is not found.
     */
    public void removeTruck(String id) throws IOException {
        if (trucks.containsKey(id) == false)
            throw new IOException("Truck not found");

        trucks.remove(id);
    }

    /**
     * Retrieves a truck from the controller by ID.
     *
     * @param id The ID of the truck to be retrieved.
     * @return The retrieved truck.
     * @throws IOException If the truck is not found.
     */
    public Truck getTruck(String id) throws IOException {
        if (trucks.containsKey(id) == false)
            throw new IOException("Truck not found");

        return trucks.get(id);
    }

    /**
     * Updates a truck in the controller.
     *
     * @param id The ID of the truck to be updated.
     * @param newTruck The updated truck object.
     * @throws IOException If the truck is not found or if the base weight of the updated truck is greater than the max weight.
     */
    public void updateTruck(String id, Truck newTruck) throws IOException{
        if(trucks.containsKey(id) == false)
            throw new IOException("Truck not found");
        if(newTruck.baseWeight() > newTruck.maxWeight())
            throw new IOException("Truck base weight is bigger than max weight");

        trucks.put(id, newTruck);
    }

    /**
     * Retrieves a list of all trucks in the controller.
     *
     * @return A linked list of all trucks.
     */
    public LinkedList<Truck> getAllTrucks(){
        return new LinkedList<>(trucks.values());
    }

}
