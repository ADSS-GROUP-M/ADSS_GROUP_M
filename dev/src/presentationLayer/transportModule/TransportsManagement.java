package presentationLayer.transportModule;

import objects.transportObjects.DeliveryRoute;
import objects.transportObjects.Driver;
import objects.transportObjects.Site;
import objects.transportObjects.Transport;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.transportModule.TransportsService;
import utils.JsonUtils;
import utils.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class TransportsManagement {
    
    private final UiData uiData;
    private final TransportsService ts;
    private final EmployeesService es;

    public TransportsManagement(UiData uiData, TransportsService ts, EmployeesService es) {
        this.uiData = uiData;
        this.ts = ts;
        this.es = es;
    }

    void manageTransports() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Transports management");
            System.out.println("Please select an option:");
            System.out.println("1. Create new transport");
            System.out.println("2. Update existing transport");
            System.out.println("3. Delete transport");
            System.out.println("4. View full transport information");
            System.out.println("5. View all transports");
            System.out.println("6. Return to previous menu");
            int option = uiData.readInt();
            switch (option) {
                case 1 -> createTransport();
                case 2 -> updateTransport();
                case 3 -> removeTransport();
                case 4 -> viewTransport();
                case 5 -> viewAllTransports();
                case 6 -> {
                    return;
                }
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private void createTransport() {

        if (verifyDataAvailability()) {
            return;
        }

        System.out.println("=========================================");
        System.out.println("Enter transport details:");

        // date/time
        LocalDate departureDate = uiData.readDate("Departure date (format: yyyy-mm-dd): ");
        if(departureDate == null){
            return;
        }
        LocalTime departureTime = uiData.readTime("Departure time (format: hh:mm): ");
        if(departureTime == null){
            return;
        }
        LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);

        // driver
        System.out.println("Driver: ");
        Driver driver = pickFromAvailableDrivers(departureDateTime);
        if(driver == null){
            return;
        }

        // truck
        System.out.println("Truck: ");
        String truckId = uiData.pickTruck(false).id();

        // route
        LinkedList<String> route = new LinkedList<>();
        HashMap<String, Integer> itemsList = new HashMap<>();
        routeMaker(route, itemsList, departureDateTime);

        //weight
        int truckWeight = uiData.readInt("Truck weight: ");

        Transport newTransport = new Transport(
                route,
                itemsList,
                driver.id(),
                truckId,
                departureDateTime,
                truckWeight
        );

        Response response = createTransportHelperMethod(newTransport);

        if(response.success() == false){
            pickAnOptionIfFail(newTransport);
        }
    }

    private Driver pickFromAvailableDrivers(LocalDateTime departureDateTime) {
        String json = es.getAvailableDrivers(JsonUtils.serialize(departureDateTime));
        Response response = Response.fromJson(json);
        if(response.success() == false){
            System.out.println("No available drivers for "+departureDateTime.toString()+", aborting...");
            return null;
        }
        Driver[] availableDrivers = Arrays.stream(
                response.<String[]>data(String[].class))
                .map(driver -> uiData.drivers().get(driver))
                .filter(Objects::nonNull)
                .toArray(Driver[]::new);
        return uiData.pickDriver(false,availableDrivers);
    }

    private void pickAnOptionIfFail(Transport newTransport) {
        while(true){
            System.out.println("current weight: "+newTransport.weight());
            System.out.println("Select one of the following options:");
            System.out.println("1. Pick new truck and driver");
            System.out.println("2. Pick new destinations and item lists");
            System.out.println("3. Cancel and return to previous menu");
            int option = uiData.readInt();
            switch (option) {
                case 1 -> {
                    System.out.println("Truck: ");
                    String truckId = uiData.pickTruck(false).id();
                    System.out.println("Driver: ");
                    Driver driver = pickFromAvailableDrivers(newTransport.departureTime());
                    newTransport = new Transport(
                            newTransport.route(),
                            newTransport.itemLists(),
                            driver.id(),
                            truckId,
                            newTransport.departureTime(),
                            newTransport.weight()
                    );
                    Response response2 = createTransportHelperMethod(newTransport);
                    if(response2.success()) {
                        return;
                    }
                }
                case 2 -> {
                    LinkedList<String> destinations = new LinkedList<>();
                    HashMap<String, Integer> itemsList = new HashMap<>();
                    routeMaker(destinations, itemsList, newTransport.departureTime());
                    System.out.println("New weight :");
                    int weight = uiData.readInt();
                    newTransport = new Transport(
                            destinations,
                            itemsList,
                            newTransport.driverId(),
                            newTransport.truckId(),
                            newTransport.departureTime(),
                            weight
                    );
                    Response response2 = createTransportHelperMethod(newTransport);
                    if(response2.success()) {
                        return;
                    }
                }
                case 3 -> {
                    System.out.println("Transport creation cancelled");
                    return;
                }
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private void updateTransport() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select transport to update:");
            Transport oldtransport = getTransport();
            if(oldtransport == null) {
                return;
            }
            printTransportDetails(oldtransport);
            System.out.println("=========================================");
            System.out.println("Please select an option:");
            System.out.println("1. Update driver");
            System.out.println("2. Update truck");
            System.out.println("3. Update route");
            System.out.println("5. Update weight");
            System.out.println("6. Set manual arrival times");
            System.out.println("7. Return to previous menu");
            int option = uiData.readInt();
            switch (option) {

                case 1 -> {
                    System.out.println("Select driver: ");
                    Driver driver = pickFromAvailableDrivers(oldtransport.departureTime());
                    if(driver == null) {
                        continue;
                    }

                    updateTransportHelperMethod(
                            oldtransport.id(),
                            oldtransport.route(),
                            oldtransport.itemLists(),
                            oldtransport.truckId(),
                            driver.id(),
                            oldtransport.departureTime(),
                            oldtransport.weight()
                    );
                }
                case 2 -> {
                    System.out.println("Select truck: ");
                    String truckId = uiData.pickTruck(false).id();
                    updateTransportHelperMethod(
                            oldtransport.id(),
                            oldtransport.route(),
                            oldtransport.itemLists(),
                            truckId,
                            oldtransport.driverId(),
                            oldtransport.departureTime(),
                            oldtransport.weight()
                    );
                }
                case 3 -> {
                    System.out.println("Select source: ");
                    Site source = uiData.pickSite(false);
                    updateTransportHelperMethod(
                            oldtransport.id(),
                            oldtransport.route(),
                            oldtransport.itemLists(),
                            oldtransport.truckId(),
                            oldtransport.driverId(),
                            oldtransport.departureTime(),
                            oldtransport.weight()
                    );
                }
                case 4 ->{
                    System.out.println("Select new destinations and item lists: ");
                    HashMap<String, Integer> itemsList = new HashMap<>();
                    LinkedList<String> destinations = new LinkedList<>();
                    routeMaker(destinations, itemsList, oldtransport.departureTime());

                    updateTransportHelperMethod(
                            oldtransport.id(),
                            destinations,
                            itemsList,
                            oldtransport.truckId(),
                            oldtransport.driverId(),
                            oldtransport.departureTime(),
                            oldtransport.weight()
                    );
                }
                case 5 -> {
                    int truckWeight = uiData.readInt("Truck weight: ");
                    updateTransportHelperMethod(
                            oldtransport.id(),
                            oldtransport.route(),
                            oldtransport.itemLists(),
                            oldtransport.truckId(),
                            oldtransport.driverId(),
                            oldtransport.departureTime(),
                            truckWeight
                    );
                }
                case 6 -> {
                    overrideArrivalTimes(oldtransport);
                    updateTransportHelperMethod(
                            oldtransport.id(),
                            oldtransport.deliveryRoute(),
                            oldtransport.truckId(),
                            oldtransport.driverId(),
                            oldtransport.departureTime(),
                            oldtransport.weight()
                    );
                }
                case 7 -> {
                    return;
                }
                default -> {
                    System.out.println("\nInvalid option!");
                    continue;
                }
            }
            break;
        }
    }

    private void overrideArrivalTimes(Transport oldtransport) {
        System.out.println("Select new arrival times (enter 'cancel!' to cancel): ");
        HashMap<String, LocalTime> arrivalTimes = new HashMap<>();
        for (String destination : oldtransport.route()) {
            System.out.println("Arrival time for " + destination + ": ");
            LocalTime arrivalTime = uiData.readTime("(format: hh:mm): ");
            if(arrivalTime == null) {
                return;
            }
            arrivalTimes.put(destination, arrivalTime);
        }
        for(var pair : arrivalTimes.entrySet()) {
            oldtransport.deliveryRoute().overrideArrivalTime(pair.getKey(), pair.getValue());
        }
    }


    private void removeTransport() {
        while(true) {
            System.out.println("=========================================");
            Transport transport = getTransport();
            if(transport == null) {
                return;
            }
            printTransportDetails(transport);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to delete this transport? (y/n)");
            String option = uiData.readLine();
            switch (option) {
                case "y" -> {
                    String json = transport.toJson();
                    String responseJson = ts.removeTransport(json);
                    Response response = JsonUtils.deserialize(responseJson, Response.class);
                    if (response.success()) {
                        uiData.transports().remove(transport.id());
                    }
                    System.out.println("\nTransport deleted successfully!");
                }
                case "n"-> {}
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private void viewTransport() {
        System.out.println("=========================================");
        Transport transport = getTransport();
        if(transport == null) {
            return;
        }
        printTransportDetails(transport);
        System.out.println("\nEnter 'done!' to return to previous menu");
        uiData.readLine();
    }

    private void viewAllTransports() {
        System.out.println("=========================================");
        System.out.println("All transports:");
        for(Transport transport : uiData.transports().values()){
            printTransportDetails(transport);
            System.out.println("-----------------------------------------");
        }
        System.out.println("\nEnter 'done!' to return to previous menu");
        uiData.readLine();
    }

    private void printTransportDetails(Transport transport) {
        System.out.println("Transport id: "+ transport.id());
        System.out.println("Date:         "+ transport.departureTime().toLocalDate());
        System.out.println("Time:         "+ transport.departureTime().toLocalTime());
        System.out.println("DriverId:     "+ transport.driverId());
        System.out.println("TruckId:      "+ transport.truckId());
        System.out.println("Weight:       "+ transport.weight());
        System.out.println("Route: ");
        for(String address : transport.route()){
            Site destination = uiData.sites().get(address);
            LocalTime arrivalTime = transport.deliveryRoute().getEstimatedTimeOfArrival(address);
            String time = String.format("[%02d:%02d] ", arrivalTime.getHour(), arrivalTime.getMinute());
            Integer itemListId = transport.itemLists().get(address);
            String itemListIdStr = itemListId == -1 ? "" : " (items list id: "+ itemListId +")";
            System.out.println(time+destination + itemListIdStr);
        }
    }

    private void updateTransportHelperMethod(int id,
                                             DeliveryRoute deliveryRoute,
                                             String truckId,
                                             String driverId,
                                             LocalDateTime departureDateTime,
                                             int weight) {
        Transport newTransport = new Transport(
                id,
                deliveryRoute,
                driverId,
                truckId,
                departureDateTime,
                weight
        );

        String json = newTransport.toJson();
        String responseJson = ts.updateTransport(json);
        Response response = JsonUtils.deserialize(responseJson, Response.class);
        if(response.success()){
            Transport _transport = Transport.fromJson(response.data());
            uiData.transports().put(id, _transport);
        }
        System.out.println("\n"+response.message());
    }

    private void updateTransportHelperMethod(int id,
                                             List<String> destinations,
                                             Map<String, Integer> itemLists,
                                             String truckId,
                                             String driverId,
                                             LocalDateTime departureDateTime,
                                             int weight) {
        Transport newTransport = new Transport(
                id,
                new DeliveryRoute(destinations, itemLists),
                driverId,
                truckId,
                departureDateTime,
                weight
        );

        String json = newTransport.toJson();
        String responseJson = ts.updateTransport(json);
        Response response = JsonUtils.deserialize(responseJson, Response.class);
        if(response.success()){
            Transport _transport = Transport.fromJson(response.data());
            uiData.transports().put(id, _transport);
        }
        System.out.println("\n"+response.message());
    }

    private Transport getTransport() {
        int transportId = uiData.readInt("Enter transport ID (enter '-1' to return to previous menu): ");
        if(transportId == -1) {
            return null;
        }
        if(uiData.transports().containsKey(transportId) == false) {
            System.out.println("Transport with ID "+transportId+" does not exist!");
            return null;
        }
        return uiData.transports().get(transportId);
    }

    private void routeMaker(LinkedList<String> destinations, HashMap<String, Integer> itemsList, LocalDateTime departureDateTime) {
        System.out.println("Pick route source, destinations and items lists:\n");
        // source
        System.out.println("Source: ");
        Site source = uiData.pickSite(false);
        destinations.add(source.name());
        itemsList.put(source.name(), -1);

        // destinations
        int destinationId = 1;
        while(true){
            System.out.println("=========================================");
            System.out.println("Destination number "+destinationId+": ");
            Site site = uiData.pickSite(true);
            if(site == null) {
                break;
            }
            //validate the site if it is a branch
             if(site.siteType() == Site.SiteType.BRANCH) {
                String stokeKeeperMassageJson =  es.checkStoreKeeperAvailability(JsonUtils.serialize(departureDateTime),site.name());
                Response response = JsonUtils.deserialize(stokeKeeperMassageJson, Response.class);
                if(response.success() == false){
                 System.out.println("\n"+response.message());
                 continue;
                }
            }

            int listId = uiData.readInt("Items list id: ");
            if (uiData.itemLists().containsKey(listId) == false) {
                System.out.println("\nItems list with ID " + listId + " does not exist!");
                System.out.println();
                continue;
            }
            itemsList.put(site.name(), listId);
            destinations.add(site.name());
            destinationId++;
        }
    }

    private Response createTransportHelperMethod(Transport newTransport) {

        // send to server
        String json = newTransport.toJson();
        String responseJson = ts.addTransport(json);
        Response response = JsonUtils.deserialize(responseJson, Response.class);
        System.out.println("\n"+response.message());

        if(response.success()){
            Transport _added = Transport.fromJson(response.data());
            uiData.transports().put(_added.id(), _added);
        }
        return response;
    }

    private boolean verifyDataAvailability() {

        boolean isMissingData = false;
        StringBuilder errorMessage = new StringBuilder("\nData not found for ");

        if(uiData.trucks().isEmpty()){
            errorMessage.append("trucks");
            isMissingData = true;
        }
        if(uiData.drivers().isEmpty()){
            if(isMissingData) {
                errorMessage.append(", ");
            }
            errorMessage.append("drivers");
            isMissingData = true;
        }
        if(uiData.sites().isEmpty()){
            if(isMissingData) {
                errorMessage.append(", ");
            }
            errorMessage.append("sites");
            isMissingData = true;
        }
        if(uiData.itemLists().isEmpty()){
            if(isMissingData) {
                errorMessage.append(", ");
            }
            errorMessage.append("item lists");
            isMissingData = true;
        }

        if(isMissingData) {
            errorMessage.append("\nAborting.....\n");
            System.out.println(errorMessage);
        }
        return isMissingData;
    }

    @Deprecated
    private Transport fetchAddedTransport(int _id) {
        String _json = Transport.getLookupObject(_id).toJson();
        String _responseJson = ts.getTransport(_json);
        Response _response = JsonUtils.deserialize(_responseJson, Response.class);
        return Transport.fromJson(_response.data());
    }
}
