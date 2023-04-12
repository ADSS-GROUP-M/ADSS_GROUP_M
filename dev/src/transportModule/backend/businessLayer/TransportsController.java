package transportModule.backend.businessLayer;

import transportModule.records.Driver;
import transportModule.records.Transport;
import transportModule.records.Truck;
import utils.ErrorCollection;

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
    private final SitesController sc;
    private final ItemListsController ilc;
    private final TreeMap<Integer, Transport> transports;
    private int idCounter;

    public TransportsController(TrucksController tc, DriversController dc, SitesController sc, ItemListsController ic){
        this.sc = sc;
        this.ilc = ic;
        this.tc = tc;
        this.dc = dc;
        transports = new TreeMap<>();
        idCounter = 0; //TODO: currently not in use. this will have to be restored from the DB in the future
    }

    /**
     * Adds a transport object to the TransportsController.
     *
     * @param transport The transport object to add.
     * @throws IOException If the transport object is invalid or if a transport with the same ID already exists.
     */
    public Integer addTransport(Transport transport)throws IOException{

        //TODO: remove support for pre-defined IDs and move to auto-incrementing IDs.
        // currently, the ID is set to -1 if it is not pre-defined.
        // this is a temporary solution until the DB is implemented.

        if(transportExists(transport.id())) {
            throw new IOException("A transport with this id already exists");
        }

        validateTransport(transport);

        //<TEMPORARY SOLUTION>
        if(transport.id() != -1){

            //TODO: uncomment this when pre-defined IDs are no longer supported
            //throw new UnsupportedOperationException("Pre-defined IDs are not supported");

            transports.put(transport.id(), transport);
            return transport.id();
        }
        //<TEMPORARY SOLUTION/>
        else{
            Transport toAdd = new Transport(idCounter++,transport);
            transports.put(toAdd.id(), toAdd);
            return toAdd.id();
        }
    }

    /**
     * Retrieves a transport object with the given ID from the TransportsController.
     *
     * @param id The ID of the transport object to retrieve.
     * @return The transport object with the given ID.
     * @throws IOException If a transport with the given ID is not found.
     */
    public Transport getTransport(int id) throws IOException {
        if (transportExists(id) == false) {
            throw new IOException("Transport not found");
        }

        return transports.get(id);
    }

    /**
     * Removes a transport object with the given ID from the TransportsController.
     *
     * @param id The ID of the transport object to remove.
     * @throws IOException If a transport with the given ID is not found.
     */
    public void removeTransport(int id) throws IOException {
        if (transportExists(id) == false) {
            throw new IOException("Transport not found");
        }
;
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
        if(transportExists(id) == false) {
            throw new IOException("Transport not found");
        }

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

    private void validateTransport(Transport transport) throws IOException{

        ErrorCollection ec = new ErrorCollection();

        // driver validation
        Driver driver = dc.driverExists(transport.driverId()) ? dc.getDriver(transport.driverId()) : null;
        if(driver == null) {
            ec.addError("Driver not found", "driver");
        }

        // truck validation
        Truck truck = tc.truckExists(transport.truckId()) ? tc.getTruck(transport.truckId()) :  null;
        if(truck == null) {
            ec.addError("Truck not found", "truck");
        }

        // truck - driver validation
        if(driver != null && truck != null){
            String[] requiredLicense = truck.requiredLicense().split("");
            String[] driverLicense = driver.licenseType().name().split("");

            if((driverLicense[0].compareToIgnoreCase(requiredLicense[0]) < 0
             || driverLicense[1].compareToIgnoreCase(requiredLicense[1]) < 0)) {

                ec.addError("A driver with license type "
                        + driver.licenseType()
                        + " is not permitted to drive this truck", "license");
            }
        }

        // source validation
        try{
            sc.getSite(transport.source());
        }
        catch(IOException e){
            ec.addError("Site with address " + transport.source() + " does not exist", "source");
        }

        // destinations + itemLists validation
        int destIndex = 0;
        for(String address : transport.destinations()){

            //destination validation
            if(sc.siteExists(address) == false){
                ec.addError("Site with address " + address + " does not exist", "destination:"+destIndex);
            }

            //itemList validation
            Integer itemListId = transport.itemLists().get(address);
            if(ilc.listExists(itemListId) == false) {
                ec.addError("Item list with id " + itemListId + " does not exist", "itemList:"+destIndex);
            }

            destIndex++;
        }

        // weight validation
        int weight = transport.weight();
        if (truck != null && truck.maxWeight() < weight) {
            ec.addError("The truck's maximum weight has been exceeded", "weight");
        }

        if(ec.hasErrors()) {
            throw new IOException(ec.message(),ec.cause());
        }
    }

    public boolean transportExists(int id) {
        return transports.containsKey(id);
    }
}
