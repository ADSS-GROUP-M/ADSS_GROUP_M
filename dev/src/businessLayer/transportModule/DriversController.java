package businessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.transportModule.DriversDAO;
import objects.transportObjects.Driver;

import utils.transportUtils.TransportException;
import java.util.LinkedList;
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
        try {
            if (dao.exists(driver) == false) {
                dao.insert(driver);
            } else {
                throw new TransportException("Driver already exists");
            }
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
        Driver lookupObject = Driver.getLookupObject(id);

        try {
            if (dao.exists(lookupObject) == false) {
                throw new TransportException("Driver not found");
            } else {
                return dao.select(lookupObject);
            }
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
        Driver lookupObject = Driver.getLookupObject(id);

        try {
            if (dao.exists(lookupObject) == false) {
                throw new TransportException("Driver not found");
            } else {
                dao.delete(lookupObject);
            }
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

        Driver lookupObject = Driver.getLookupObject(id);

        try {
            if (dao.exists(lookupObject) == false) {
                throw new TransportException("Driver not found");
            } else {
                dao.update(newDriver);
            }
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

