package BusinessLayer.transportModule;

import BusinessLayer.employeeModule.Controllers.EmployeesController;
import ServiceLayer.employeeModule.Services.EmployeesService;
import com.google.gson.reflect.TypeToken;
import objects.transportObjects.Driver;
import objects.transportObjects.Transport;
import objects.transportObjects.Truck;
import utils.JsonUtils;
import utils.Response;
import utils.transportUtils.ErrorCollection;

import utils.transportUtils.TransportException;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
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
    private final EmployeesService es;
    private final TreeMap<Integer, Transport> transports;
    private int idCounter;

    public TransportsController(TrucksController tc, DriversController dc, SitesController sc, ItemListsController ic, EmployeesService es){
        this.sc = sc;
        this.ilc = ic;
        this.tc = tc;
        this.dc = dc;
        this.es = es;
        transports = new TreeMap<>();
        idCounter = 1; //TODO: currently not in use. this will have to be restored from the DB in the future
    }

    /**
     * Adds a transport object to the TransportsController.
     *
     * @param transport The transport object to add.
     * @throws TransportException If the transport object is invalid or if a transport with the same ID already exists.
     */
    public Integer addTransport(Transport transport)throws TransportException{

        if(transport.id() != -1){
            throw new UnsupportedOperationException("Pre-defined IDs are not supported");
        }

        validateTransport(transport);

        Transport toAdd = new Transport(idCounter++,transport);
        transports.put(toAdd.id(), toAdd);
        return toAdd.id();
    }

    /**
     * Retrieves a transport object with the given ID from the TransportsController.
     *
     * @param id The ID of the transport object to retrieve.
     * @return The transport object with the given ID.
     * @throws TransportException If a transport with the given ID is not found.
     */
    public Transport getTransport(int id) throws TransportException {
        if (transportExists(id) == false) {
            throw new TransportException("Transport not found");
        }

        return transports.get(id);
    }

    /**
     * Removes a transport object with the given ID from the TransportsController.
     *
     * @param id The ID of the transport object to remove.
     * @throws TransportException If a transport with the given ID is not found.
     */
    public void removeTransport(int id) throws TransportException {
        if (transportExists(id) == false) {
            throw new TransportException("Transport not found");
        }
;
        transports.remove(id);
    }

    /**
     * Updates a transport object with the given ID in the TransportsController.
     *
     * @param id The ID of the transport object to update.
     * @param newTransport The updated transport object.
     * @throws TransportException If the newTransport object is invalid or if a transport with the given ID is not found.
     */
    public void updateTransport(int id, Transport newTransport) throws TransportException{
        if(transportExists(id) == false) {
            throw new TransportException("Transport not found");
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

    private boolean checkIfDriverIsAvailable(Driver driver, LocalDateTime dateTime){

        String driverJson = es.getAvailableDrivers(JsonUtils.serialize(dateTime));
        Response response = Response.fromJson(driverJson);
        Type type = new TypeToken<LinkedList<String>>(){}.getType();
        LinkedList<String> driversIds = response.data(type);
        return driversIds.stream().anyMatch(x -> x.equals(driver.id()));

    }

    private void validateTransport(Transport transport) throws TransportException{

        ErrorCollection ec = new ErrorCollection();

        // driver validation
        Driver driver = dc.driverExists(transport.driverId()) ? dc.getDriver(transport.driverId()) : null;
        if(driver == null) {
            ec.addError("Driver not found", "driver");
        }

        if(driver != null && checkIfDriverIsAvailable(driver, transport.scheduledTime()) == false){
            ec.addError("Driver is not available", "driverNotAvailable");
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
        if(sc.siteExists(transport.source()) == false){
            ec.addError("Site with address " + transport.source() + " does not exist", "source");
        }

        // destinations + itemLists validation
        int destIndex = 0;
        for(String address : transport.destinations()){

            //destination validation
            if(sc.siteExists(address) == false){
                ec.addError("Site with address " + address + " does not exist", "destination:"+destIndex);
            }
            String destinationJson = es.checkStoreKeeperAvailability(JsonUtils.serialize(transport.scheduledTime()),address);
            Response response = Response.fromJson(destinationJson);
            if(response.success()==false){
                ec.addError("Store keeper is not available on site with address " + address, "storeKeeper");
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
            throw new TransportException(ec.message(),ec.cause());
        }
    }

    public boolean transportExists(int id) {
        return transports.containsKey(id);
    }
}
