package transportModule.backend.businessLayer;

import transportModule.records.Truck;
import utils.ErrorCollection;

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
        if (truckExists(truck.id())) {
            throw new IOException("Truck already exists");
        }

        validateTruck(truck);

        trucks.put(truck.id(), truck);
    }

    /**
     * Removes a truck from the controller.
     *
     * @param id The ID of the truck to be removed.
     * @throws IOException If the truck is not found.
     */
    public void removeTruck(String id) throws IOException {
        if (truckExists(id) == false) {
            throw new IOException("Truck not found");
        }

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
        if (truckExists(id) == false) {
            throw new IOException("Truck not found");
        }

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
        if(truckExists(id) == false) {
            throw new IOException("Truck not found");
        }

        validateTruck(newTruck);

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


    private static void validateTruck(Truck newTruck) throws IOException {

        ErrorCollection ec = new ErrorCollection();

        if(newTruck.baseWeight() <= 0) {
             ec.addError("Truck base weight must be positive","baseWeight");
        }

        if(newTruck.maxWeight() <= 0) {
            ec.addError("Truck max weight must be positive","maxWeight");
        }

        if(newTruck.baseWeight() > newTruck.maxWeight()){
            ec.addError("Truck base weight must be smaller than max weight","baseWeightMaxWeight");
        }

        if(ec.hasErrors()){
            throw new IOException(ec.message(),ec.cause());
        }
    }

    public boolean truckExists(String id) {
        return trucks.containsKey(id);
    }
}
