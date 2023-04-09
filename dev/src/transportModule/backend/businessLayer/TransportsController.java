package transportModule.backend.businessLayer;

import transportModule.backend.businessLayer.records.Driver;
import transportModule.backend.businessLayer.records.Transport;
import transportModule.backend.businessLayer.records.Truck;

import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * The TransportsController class is responsible for managing and controlling transport objects.
 * It provides methods to add, remove, update, and retrieve transport objects, as well as validate
 * transport objects before adding or updating them.
 */
public class TransportsController {

    private final TrucksController tc;
    private final DriversController dc;
    private final TreeMap<Integer, Transport> transports;
    private int idCounter;

    public TransportsController(TrucksController tc, DriversController dc){
        transports = new TreeMap<>();
        idCounter = 0; // currently not in use. this will have to be restored from the DB in the future
        this.tc = tc;
        this.dc = dc;
    }

    /**
     * Adds a transport object to the TransportsController.
     *
     * @param transport The transport object to add.
     * @throws IOException If the transport object is invalid or if a transport with the same ID already exists.
     */
    public void addTransport(Transport transport)throws IOException{
        validateTransport(transport);

        transports.put(transport.id(), transport);
    }

    /**
     * Retrieves a transport object with the given ID from the TransportsController.
     *
     * @param id The ID of the transport object to retrieve.
     * @return The transport object with the given ID.
     * @throws IOException If a transport with the given ID is not found.
     */
    public Transport getTransport(int id) throws IOException {
        if (transports.containsKey(id) == false)
            throw new IOException("Transport not found");

        return transports.get(id);
    }

    /**
     * Removes a transport object with the given ID from the TransportsController.
     *
     * @param id The ID of the transport object to remove.
     * @throws IOException If a transport with the given ID is not found.
     */
    public void removeTransport(int id) throws IOException {
        if (transports.containsKey(id) == false)
            throw new IOException("Transport not found");

        transports.remove(id);
    }

    /**
     * Updates a transport object with the given ID in the TransportsController.
     *
     * @param id The ID of the transport object to update.
     * @param newTransport The updated transport object.
     * @throws IOException If the newTransport object is invalid or if a transport with the given ID is not found.
     */
    public void updateTransport(int id, Transport newTransport) throws IOException{
        if(transports.containsKey(id) == false)
            throw new IOException("Transport not found");

        validateTransport(newTransport);

        transports.put(id, newTransport);
    }

    /**
     * Retrieves a list of all transport objects in the TransportsController.
     *
     * @return A list of all transport objects.
     */
    public LinkedList<Transport> getAllTransports(){
        return new LinkedList<>(transports.values());
    }

    /**
     * Validates a transport object before adding or updating it in the TransportsController.
     *
     * @param transport The transport object to validate.
     * @throws IOException If the transport object is invalid.
     */
    private void validateTransport(Transport transport) throws IOException{

        Truck truck = tc.getTruck(transport.truckId());
        Driver driver = dc.getDriver(transport.driverId());

        // used to return information about all the errors
        String message = "";
        String cause = "";
        boolean throwException = false;
        //==================================================

        // weight validation
        int weight = transport.weight();
        if (truck.maxWeight() < weight) {
            message += "The truck's maximum weight has been exceeded";
            cause = "weight";
            throwException = true;
        }

        // truck - driver validation
        String[] requiredLicense = truck.requiredLicense().split("");
        String[] driverLicense = driver.licenseType().name().split("");

        if(driverLicense[0].compareToIgnoreCase(requiredLicense[0]) < 0 ||  driverLicense[1].compareToIgnoreCase(requiredLicense[1]) < 0) {
            if(throwException){
                message += "\n";
                cause += ",";
            }
            cause += "license";
            message += "A driver with license type "+driver.licenseType()+
                            " is not permitted to drive this truck";
            throwException = true;

        }
        if(throwException) throw new IOException(message,new Throwable(cause));
    }
}
