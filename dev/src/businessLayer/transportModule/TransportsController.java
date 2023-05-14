package businessLayer.transportModule;

import com.google.gson.JsonStreamParser;
import com.google.gson.reflect.TypeToken;
import dataAccessLayer.transportModule.DeliveryRoutesDAO;
import dataAccessLayer.transportModule.TransportsDAO;
import exceptions.DalException;
import exceptions.TransportException;
import javafx.util.Pair;
import objects.transportObjects.*;
import serviceLayer.employeeModule.Services.EmployeesService;
import utils.ErrorCollection;
import utils.JsonUtils;
import utils.Response;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * The TransportsController class is responsible for managing and controlling transport objects.
 * It provides methods to add, remove, update, and retrieve transport objects, as well as validate
 * transport objects before adding or updating them.
 */
public class TransportsController {

    public static final long AVERAGE_TIME_PER_VISIT = 15;
    private final TrucksController tc;
    private final DriversController dc;
    private final SitesController sc;
    private final ItemListsController ilc;
    private EmployeesService es;
    private final DeliveryRoutesDAO drDao;
    private final TransportsDAO dao;
    private final SitesRoutesController src;
    private int idCounter;

    public TransportsController(TrucksController tc,
                                DriversController dc,
                                SitesController sc,
                                ItemListsController ic,
                                SitesRoutesController src,
                                DeliveryRoutesDAO drDao,
                                TransportsDAO dao) throws TransportException{
        this.sc = sc;
        this.ilc = ic;
        this.tc = tc;
        this.dc = dc;
        this.src = src;
        this.drDao = drDao;
        this.dao = dao;
        try {
            idCounter = dao.selectCounter();
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    public void injectDependencies(EmployeesService es){
        this.es = es;
    }

    /**
     * Adds a transport object to the TransportsController.
     *
     * @param transport The transport object to add.
     * @throws TransportException If the transport object is invalid or if a transport with the same ID already exists.
     */
    public Transport addTransport(Transport transport)throws TransportException{

        if(transport.id() != -1){
            throw new UnsupportedOperationException("Pre-defined IDs are not supported");
        }
        validateTransport(transport);
        initializeEstimatedArrivalTimes(transport);
        DeliveryRoute routeWithNewId = new DeliveryRoute(idCounter, transport.deliveryRoute()); // create route with new id
        Transport toAdd = new Transport(idCounter,routeWithNewId,transport); // update transport with new id and route
        try {
            dao.insert(toAdd);
            drDao.insert(toAdd.deliveryRoute());
            dao.incrementCounter();
            idCounter++;
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
        return toAdd;
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

        try {
            Transport selectedTransport = dao.select(Transport.getLookupObject(id)); // get transport without route
            DeliveryRoute selectedRoute = drDao.select(DeliveryRoute.getLookupObject(selectedTransport.id())); // initialize route
            return new Transport(selectedRoute, selectedTransport); // add route to transport
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
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

        try {
            drDao.delete(DeliveryRoute.getLookupObject(id));
            dao.delete(Transport.getLookupObject(id));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Updates a transport object with the given ID in the TransportsController.
     *
     * @param id The ID of the transport object to update.
     * @param newTransport The updated transport object.
     * @throws TransportException If the newTransport object is invalid or if a transport with the given ID is not found.
     */
    public Transport updateTransport(int id, Transport newTransport) throws TransportException{
        if(transportExists(id) == false) {
            throw new TransportException("Transport not found");
        }

        validateTransport(newTransport);
        if(newTransport.deliveryRoute().manuallyOverrideEstimatedArrivalTimes() == false){
            initializeEstimatedArrivalTimes(newTransport);
        }
        Transport toUpdate = new Transport(id, newTransport);
        try {
            dao.update(toUpdate);
            drDao.update(toUpdate.deliveryRoute());
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
        return toUpdate;
    }

    /**
     * Retrieves a list of all transport objects in the TransportsController.
     *
     * @return A list of all transport objects.
     */
    public List<Transport> getAllTransports() throws TransportException{
        try {
            List<Transport> selected = dao.selectAll();
            List<Transport> output = new LinkedList<>();
            for(Transport transport : selected){
                DeliveryRoute route = drDao.select(DeliveryRoute.getLookupObject(transport.id()));
                output.add(new Transport(route, transport));
            }
            return output;
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    private boolean checkIfDriverIsAvailable(Driver driver, LocalDateTime dateTime){

        String driverJson = es.getAvailableDrivers(JsonUtils.serialize(dateTime));
        Response response = Response.fromJson(driverJson);
        Type type = new TypeToken<LinkedList<String>>(){}.getType();
        if(response.success()){
            LinkedList<String> driversIds = response.data(type);
            return driversIds.stream().anyMatch(x -> x.equals(driver.id()));
        } else {
            return false;
        }
    }

    private void validateTransport(Transport transport) throws TransportException{

        ErrorCollection ec = new ErrorCollection();

        // driver validation
        Driver driver = dc.driverExists(transport.driverId()) ? dc.getDriver(transport.driverId()) : null;
        if(driver == null) {
            ec.addError("Driver not found", "driver");
        }

        if(driver != null && checkIfDriverIsAvailable(driver, transport.departureTime()) == false){
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

        //=========================== route validation ============================|
        ListIterator<String> iter = transport.route().listIterator();

        // source validation
        String curr = iter.next();
        if(sc.siteExists(curr) == false){
            ec.addError("site with name " + curr + " does not exist", "source");
        }

        while(iter.hasNext()){
            curr = iter.next();

            //site validation
            if(sc.siteExists(curr) == false){
                ec.addError("Site with name " + curr + " does not exist", "destination:"+(iter.previousIndex()-1));
            }

            //store keeper validation
            else if(sc.getSite(curr).siteType() == Site.SiteType.BRANCH){
                String destinationJson = es.checkStoreKeeperAvailability(JsonUtils.serialize(transport.departureTime()),curr);
                Response response = Response.fromJson(destinationJson);
                if(response.success()==false){
                    ec.addError("Store keeper is not available on site with name " + curr, "storeKeeper");
                }
            }

            //itemList validation
            Integer itemListId = transport.itemLists().get(curr);
            if(ilc.listExists(itemListId) == false) {
                ec.addError("Item list with id " + itemListId + " does not exist", "itemList:"+(iter.previousIndex()-1));
            }
        }
        //=========================================================================|

        // weight validation
        int weight = transport.weight();
        if (truck != null && truck.maxWeight() < weight) {
            ec.addError("The truck's maximum weight has been exceeded", "weight");
        }

        // throw exception if there are errors
        if(ec.hasErrors()) {
            throw new TransportException(ec.message(),ec.cause());
        }
    }

    public boolean transportExists(int id) throws TransportException {
        try {
            return dao.exists(Transport.getLookupObject(id));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    public void initializeEstimatedArrivalTimes(Transport transport) throws TransportException{

        Map<String,LocalTime> estimatedArrivalTimes = new HashMap<>();

        List<Site> route = new LinkedList<>();
        for (String x : transport.route()) {
            route.add(sc.getSite(x));
        }
        Map<Pair<String,String>,Double> durations = src.buildSitesTravelTimes(route);

        ListIterator<String> destinationsIterator = transport.route().listIterator();
        LocalTime time = transport.departureTime().toLocalTime();
        String curr = destinationsIterator.next(); // source is the first destination on the list
        String next;

        //handle source
        estimatedArrivalTimes.put(curr, time);

        //handle the first destination, no extra time spent in the previous destination
        if(destinationsIterator.hasNext()){
            next = destinationsIterator.next();
            time = addTravelTime(time, curr, next, durations);
            estimatedArrivalTimes.put(next, time);
            curr = next;
        }

        //handle the rest of the destinations that include the extra time spent in the previous destination
        while(destinationsIterator.hasNext()){
            time = time.plusMinutes(AVERAGE_TIME_PER_VISIT);
            next = destinationsIterator.next();
            time = addTravelTime(time, curr, next,durations);
            estimatedArrivalTimes.put(next, time);
            curr = next;
        }
        transport.deliveryRoute().initializeArrivalTimes(estimatedArrivalTimes);
    }

    private static LocalTime addTravelTime(LocalTime time, String curr, String next, Map<Pair<String,String>,Double> durations){
        long minutesToAdd = durations.get(new Pair<>(curr,next)).longValue();
        time = time.plusMinutes(minutesToAdd);
        return time;
    }
}
