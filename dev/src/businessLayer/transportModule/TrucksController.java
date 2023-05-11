package businessLayer.transportModule;

import exceptions.DalException;
import dataAccessLayer.transportModule.TrucksDAO;
import objects.transportObjects.Truck;
import utils.ErrorCollection;
import exceptions.TransportException;

import java.util.List;

/**
 * The TrucksController class is responsible for managing trucks in the transport module.
 * It provides functionality to add, remove, update, and retrieve trucks, as well as get a list of all trucks.
 */
public class TrucksController {

    private final TrucksDAO dao;

    public TrucksController(TrucksDAO dao){
        this.dao = dao;
    }

    /**
     * Adds a truck to the controller.
     *
     * @param truck The truck to be added.
     * @throws TransportException If the truck already exists or if the base weight is greater than the max weight.
     */
    public void addTruck(Truck truck) throws TransportException {
        if (truckExists(truck.id())) {
            throw new TransportException("Truck already exists");
        }

        validateTruck(truck);

        try {
            dao.insert(truck);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Removes a truck from the controller.
     *
     * @param id The ID of the truck to be removed.
     * @throws TransportException If the truck is not found.
     */
    public void removeTruck(String id) throws TransportException {
        if (truckExists(id) == false) {
            throw new TransportException("Truck not found");
        }

        try {
            dao.delete(Truck.getLookupObject(id));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Retrieves a truck from the controller by ID.
     *
     * @param id The ID of the truck to be retrieved.
     * @return The retrieved truck.
     * @throws TransportException If the truck is not found.
     */
    public Truck getTruck(String id) throws TransportException {
        if (truckExists(id) == false) {
            throw new TransportException("Truck not found");
        }

        try {
            return dao.select(Truck.getLookupObject(id));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Updates a truck in the controller.
     *
     * @param id The ID of the truck to be updated.
     * @param newTruck The updated truck object.
     * @throws TransportException If the truck is not found or if the base weight of the updated truck is greater than the max weight.
     */
    public void updateTruck(String id, Truck newTruck) throws TransportException{
        if(truckExists(id) == false) {
            throw new TransportException("Truck not found");
        }

        validateTruck(newTruck);

        try {
            dao.update(newTruck);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Retrieves a list of all trucks in the controller.
     *
     * @return A linked list of all trucks.
     */
    public List<Truck> getAllTrucks() throws TransportException{
        try {
            return dao.selectAll();
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }


    private static void validateTruck(Truck newTruck) throws TransportException {

        ErrorCollection ec = new ErrorCollection();

        if(newTruck.baseWeight() <= 0) {
             ec.addError("Truck base weight must be positive","negativeBaseWeight");
        }

        if(newTruck.maxWeight() <= 0) {
            ec.addError("Truck max weight must be positive","negativeMaxWeight");
        }

        if(newTruck.baseWeight() > newTruck.maxWeight()){
            ec.addError("Truck base weight must be smaller than max weight","baseWeightGreaterThanMaxWeight");
        }

        if(ec.hasErrors()){
            throw new TransportException(ec.message(),ec.cause());
        }
    }

    public boolean truckExists(String id) throws TransportException{
        try {
            return dao.exists(Truck.getLookupObject(id));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }
}
