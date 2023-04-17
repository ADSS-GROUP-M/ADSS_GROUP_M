package transportModule.backend.businessLayer;

import transportModule.records.Driver;

import transportModule.transportUtils.TransportException;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * The DriversController class is responsible for managing the drivers in the transport system.
 * It provides methods for adding, retrieving, updating, and removing drivers from the system.
 */
public class DriversController {
    private final TreeMap <Integer, Driver> drivers;

    public DriversController(){
        drivers = new TreeMap<>();
    }

    /**
     * Adds a new driver to the system.
     *
     * @param driver The driver to be added.
     * @throws TransportException If the driver with the same ID already exists in the system.
     */
    public void addDriver(Driver driver) throws TransportException{
        if (driverExists(driver.id()) == false) {
            drivers.put(driver.id(), driver);
        } else {
            throw new TransportException("Driver already exists");
        }
    }

    /**
     * Retrieves a driver by ID.
     *
     * @param id The ID of the driver to retrieve.
     * @return The driver with the specified ID.
     * @throws TransportException If the driver with the specified ID is not found.
     */
    public  Driver getDriver(int id) throws TransportException {
        if (driverExists(id) == false) {
            throw new TransportException("Driver not found");
        }

        return drivers.get(id);
    }

    /**
     * Removes a driver from the system by ID.
     *
     * @param id The ID of the driver to remove.
     * @throws TransportException If the driver with the specified ID is not found.
     */
    public void removeDriver(int id) throws TransportException {
        if (driverExists(id) == false) {
            throw new TransportException("Driver not found");
        }

        drivers.remove(id);
    }

    /**
     * Updates the information of an existing driver in the system.
     *
     * @param id The ID of the driver to update.
     * @param newDriver The updated driver object.
     * @throws TransportException If the driver with the specified ID is not found.
     */
    public void updateDriver(int id, Driver newDriver) throws TransportException{
        if(driverExists(id) == false) {
            throw new TransportException("Driver not found");
        }

        drivers.put(id, newDriver);
    }

    /**
     * Retrieves a list of all drivers in the system.
     *
     * @return A linked list of all drivers in the system.
     */
    public LinkedList<Driver> getAllDrivers(){
        return new LinkedList<>(drivers.values());
    }

    public boolean driverExists(int id){
        return drivers.containsKey(id);
    }
}

