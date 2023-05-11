package businessLayer.transportModule;

import dataAccessLayer.transportModule.DriversDAO;
import exceptions.DalException;
import exceptions.TransportException;
import objects.transportObjects.Driver;

import java.util.List;

/**
 * The DriversController class is responsible for managing the drivers in the transport system.
 * It provides methods for adding, retrieving, updating, and removing drivers from the system.
 */
public class DriversController {
    private final DriversDAO dao;
    public DriversController(DriversDAO dao){
        this.dao = dao;
    }

    /**
     * Adds a new driver to the system.
     *
     * @param driver The driver to be added.
     * @throws TransportException If the driver with the same ID already exists in the system.
     */
    public void addDriver(Driver driver) throws TransportException{
        if (driverExists(driver.id())) {
            throw new TransportException("Driver already exists");
        }

        try {
            dao.insert(driver);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Retrieves a driver by ID.
     *
     * @param id The ID of the driver to retrieve.
     * @return The driver with the specified ID.
     * @throws TransportException If the driver with the specified ID is not found.
     */
    public  Driver getDriver(String id) throws TransportException {
        if (driverExists(id) == false) {
            throw new TransportException("Driver not found");
        }

        try {
            return dao.select(Driver.getLookupObject(id));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Removes a driver from the system by ID.
     *
     * @param id The ID of the driver to remove.
     * @throws TransportException If the driver with the specified ID is not found.
     */
    public void removeDriver(String id) throws TransportException {
        if (driverExists(id) == false) {
            throw new TransportException("Driver not found");
        }

        try {
            dao.delete(Driver.getLookupObject(id));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Updates the information of an existing driver in the system.
     *
     * @param id The ID of the driver to update.
     * @param newDriver The updated driver object.
     * @throws TransportException If the driver with the specified ID is not found.
     */
    public void updateDriver(String id, Driver newDriver) throws TransportException{
        if (driverExists(id) == false) {
            throw new TransportException("Driver not found");
        }

        try {
            dao.update(newDriver);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Retrieves a list of all drivers in the system.
     *
     * @return A linked list of all drivers in the system.
     */
    public List<Driver> getAllDrivers() throws TransportException{
        try {
            return dao.selectAll();
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    public boolean driverExists(String id) throws TransportException{
        try {
            return dao.exists(Driver.getLookupObject(id));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }
}

