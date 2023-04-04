package CMDApp;

import CMDApp.Records.ItemList;
import CMDApp.Records.Site;
import CMDApp.Records.Transport;
import TransportModule.ServiceLayer.TransportsService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;

import static CMDApp.Main.*;

public class TransportsManagement {

    private static final TransportsService ts = factory.getTransportsService();

    static void manageTransports() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Transports management");
            System.out.println("Please select an option:");
            System.out.println("1. Create new transport");
            System.out.println("2. Update existing transport");
            System.out.println("3. Delete transport");
            System.out.println("4. View full transport information");
            System.out.println("5. View all transports");
            System.out.println("6. Return to main menu");
            int option = getInt();
            switch (option) {
                case 1 -> createTransport();
                case 2 -> updateTransport();
                case 3 -> deleteTransport();
                case 4 -> viewTransport();
                case 5 -> viewAllTransports();
                case 6 -> {
                    return;
                }
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private static void createTransport() {

        System.out.println("=========================================");
        System.out.println("Transport ID: "+transportIdCounter);
        System.out.println("Enter transport details:");

        // date/time
        LocalDate departureDate = LocalDate.parse(getLine("Departure date (format: yyyy-mm-dd): "));
        LocalTime departureTime = LocalTime.parse(getLine("Departure time (format: hh:mm): "));
        LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);

        // truck
        System.out.println("Truck: ");
        String truckId = pickTruck(false).id();

        // driver
        System.out.println("Driver: ");
        int driverID = pickDriver(false).id();

        // source
        System.out.println("Source: ");
        Site source = pickSite(false);

        // destinations and items lists
        LinkedList<Site> destinations = new LinkedList<>();
        HashMap<Site, ItemList> itemsList = new HashMap<>();
        destinationsMaker(destinations, itemsList);

        //weight
        int truckWeight = getInt("Truck weight: ");

        Transport newTransport = new Transport(
                transportIdCounter,
                source,
                destinations,
                itemsList,
                truckId,
                driverID,
                departureDateTime,
                truckWeight
        );

        Response<String> response = createTransportHelperMethod(newTransport);

        if(response.isSuccess() == false){
            pickAnOptionIfFail(newTransport, response);
        }
    }

    private static void pickAnOptionIfFail(Transport newTransport, Response<String> response) {
        String[] errors = response.getData().split(",");
        if(errors[0].equalsIgnoreCase("weight")){
            while(true){
                System.out.println("current weight: "+newTransport.weight());
                System.out.println("Select one of the following options:");
                System.out.println("1. Pick new truck and driver");
                System.out.println("2. Pick new destinations and item lists");
                System.out.println("3. Cancel and return to previous menu");
                int option = getInt();
                switch (option) {
                    case 1 -> {
                        System.out.println("Truck: ");
                        String truckId = pickTruck(false).id();
                        System.out.println("Driver: ");
                        int driverID = pickDriver(false).id();
                        Response<String> response2 = createTransportHelperMethod(
                                new Transport(
                                        newTransport.id(),
                                        newTransport.source(),
                                        newTransport.destinations(),
                                        newTransport.itemLists(),
                                        truckId,
                                        driverID,
                                        newTransport.scheduledTime(),
                                        newTransport.weight()
                                )
                        );
                        if(response2.isSuccess()) return;
                    }
                    case 2 -> {
                        LinkedList<Site> destinations = new LinkedList<>();
                        HashMap<Site, ItemList> itemsList = new HashMap<>();
                        destinationsMaker(destinations, itemsList);
                        System.out.println("New weight :");
                        int weight = getInt();
                        Response<String> response2 = createTransportHelperMethod(
                                new Transport(
                                        newTransport.id(),
                                        newTransport.source(),
                                        destinations,
                                        itemsList,
                                        newTransport.truckId(),
                                        newTransport.driverId(),
                                        newTransport.scheduledTime(),
                                        weight
                                )
                        );
                        if(response2.isSuccess()) return;
                    }
                    case 3 -> {
                        System.out.println("Transport creation cancelled");
                        return;
                    }
                    default -> System.out.println("\nInvalid option!");
                }
            }
        }
    }

    private static void updateTransport() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select transport to update:");
            Transport transport = getTransport();
            if(transport == null) return;
            printTransportDetails(transport);
            System.out.println("=========================================");
            System.out.println("Please select an option:");
            System.out.println("1. Update date");
            System.out.println("2. Update time");
            System.out.println("3. Update driver");
            System.out.println("4. Update truck");
            System.out.println("5. Update source");
            System.out.println("6. Update destinations");
            System.out.println("7. Update weight");
            System.out.println("8. Return to previous menu");
            int option = getInt();
            switch (option) {
                case 1 -> {
                    LocalDate departureDate = LocalDate.parse(getLine("Departure date (format: yyyy-mm-dd): "));
                    LocalDateTime departureDateTime = LocalDateTime.of(departureDate, transport.scheduledTime().toLocalTime());
                    updateTransportHelperMethod(
                            transport.id(),
                            transport.source(),
                            transport.destinations(),
                            transport.itemLists(),
                            transport.truckId(),
                            transport.driverId(),
                            departureDateTime,
                            transport.weight()
                    );
                }
                case 2 -> {
                    LocalTime departureTime = LocalTime.parse(getLine("Departure time (format: hh:mm): "));
                    LocalDateTime departureDateTime = LocalDateTime.of(transport.scheduledTime().toLocalDate(), departureTime);
                    updateTransportHelperMethod(
                            transport.id(),
                            transport.source(),
                            transport.destinations(),
                            transport.itemLists(),
                            transport.truckId(),
                            transport.driverId(),
                            departureDateTime,
                            transport.weight()
                    );
                }
                case 3 -> {
                    System.out.println("Select driver: ");
                    int driverID = pickDriver(false).id();
                    updateTransportHelperMethod(
                            transport.id(),
                            transport.source(),
                            transport.destinations(),
                            transport.itemLists(),
                            transport.truckId(),
                            driverID,
                            transport.scheduledTime(),
                            transport.weight()
                    );
                }
                case 4 -> {
                    System.out.println("Select truck: ");
                    String truckId = pickTruck(false).id();
                    updateTransportHelperMethod(
                            transport.id(),
                            transport.source(),
                            transport.destinations(),
                            transport.itemLists(),
                            truckId,
                            transport.driverId(),
                            transport.scheduledTime(),
                            transport.weight()
                    );
                }
                case 5 -> {
                    System.out.println("Select source: ");
                    Site source = pickSite(false);
                    updateTransportHelperMethod(
                            transport.id(),
                            source,
                            transport.destinations(),
                            transport.itemLists(),
                            transport.truckId(),
                            transport.driverId(),
                            transport.scheduledTime(),
                            transport.weight()
                    );
                }
                case 6 ->{
                    System.out.println("Select new destinations and item lists: ");
                    HashMap<Site, ItemList> itemsList = new HashMap<>();
                    LinkedList<Site> destinations = new LinkedList<>();
                    destinationsMaker(destinations, itemsList);
                    updateTransportHelperMethod(
                            transport.id(),
                            transport.source(),
                            destinations,
                            itemsList,
                            transport.truckId(),
                            transport.driverId(),
                            transport.scheduledTime(),
                            transport.weight()
                    );
                }
                case 7 -> {
                    int truckWeight = getInt("Truck weight: ");
                    updateTransportHelperMethod(
                            transport.id(),
                            transport.source(),
                            transport.destinations(),
                            transport.itemLists(),
                            transport.truckId(),
                            transport.driverId(),
                            transport.scheduledTime(),
                            truckWeight
                    );
                }
                case 8 -> {
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

    private static void deleteTransport() {
        while(true) {
            System.out.println("=========================================");
            Transport transport = getTransport();
            if(transport == null) return;
            printTransportDetails(transport);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to delete this transport? (y/n)");
            String option = getLine();
            switch (option) {
                case "y" -> {
                    String json = JSON.serialize(transport);
                    String responseJson = ts.removeTransport(json);
                    Response<String> response = JSON.deserialize(responseJson, Response.class);
                    if (response.isSuccess()) transports.remove(transport.id());
                    System.out.println("\nTransport deleted successfully!");
                }
                case "n"-> {}
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private static void viewTransport() {
        System.out.println("=========================================");
        Transport transport = getTransport();
        if(transport == null) return;
        printTransportDetails(transport);
        System.out.println("\nEnter 'done!' to return to previous menu");
    }

    private static void viewAllTransports() {
        System.out.println("=========================================");
        System.out.println("All transports:");
        for(Transport transport : transports.values()){
            printTransportDetails(transport);
            System.out.println("-----------------------------------------");
        }
        System.out.println("\nEnter 'done!' to return to previous menu");
        getLine();
    }

    private static void printTransportDetails(Transport transport) {
        System.out.println("Transport id: "+ transport.id());
        System.out.println("Date:         "+ transport.scheduledTime().toLocalDate());
        System.out.println("Time:         "+ transport.scheduledTime().toLocalTime());
        System.out.println("DriverId:     "+ transport.driverId());
        System.out.println("TruckId:      "+ transport.truckId());
        System.out.println("Weight:       "+ transport.weight());
        System.out.println("Source:       "+ transport.source());
        System.out.println("Destinations: ");
        for(Site destination : transport.destinations()){
            System.out.println("   "+ destination + " (items list id: "+ transport.itemLists().get(destination).id()+")");
        }
    }

    private static Response<String> updateTransportHelperMethod(int id, Site source, LinkedList<Site> destinations, HashMap<Site, ItemList> itemLists, String truckId, int driverId, LocalDateTime departureDateTime, int weight) {
        Transport newTransport = new Transport(
                id,
                source,
                destinations,
                itemLists,
                truckId,
                driverId,
                departureDateTime,
                weight
        );

        String json = JSON.serialize(newTransport);
        String responseJson = ts.updateTransport(json);
        Response<String> response = JSON.deserialize(responseJson, Response.class);
        if(response.isSuccess()){
            transports.put(id, newTransport);
        }
        System.out.println("\n"+response.getMessage());
        return response;
    }

    private static Transport getTransport() {
        int transportId = getInt("Enter transport ID (enter '-1' to return to previous menu): ");
        if(transportId == -1) return null;
        if(transports.containsKey(transportId) == false) {
            System.out.println("Transport with ID "+transportId+" does not exist!");
            return null;
        }
        return transports.get(transportId);
    }

    private static void destinationsMaker(LinkedList<Site> destinations, HashMap<Site, ItemList> itemsList) {
        int destinationId = 1;
        System.out.println("Pick destinations and items lists:");
        while(true){
            System.out.println("Destination number "+destinationId+": ");
            Site site = pickSite(true);
            if(site == null) break;
            int listId = getInt("Items list id: ");
            if (itemLists.containsKey(listId) == false) {
                System.out.println("\nItems list with ID " + listId + " does not exist!");
                System.out.println();
                continue;
            }
            itemsList.put(site, itemLists.get(listId));
            destinations.add(site);
            destinationId++;
        }
    }

    private static Response<String> createTransportHelperMethod(Transport newTransport) {

        // send to server
        String json = JSON.serialize(newTransport);
        String responseJson = ts.createTransport(json);
        Response<String> response = JSON.deserialize(responseJson, Response.class);
        System.out.println("\n"+response.getMessage());

        if(response.isSuccess()){
            transports.put(transportIdCounter, newTransport);
            transportIdCounter++;
        }
        return response;
    }
}
