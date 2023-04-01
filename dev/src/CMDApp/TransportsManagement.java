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

    private static TransportsService ts = factory.getTransportsService();

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
            switch (option){
                case 1:
                    createTransport();
                    break;
                case 2:
                    updateTransport();
                    break;
                case 3:
                    deleteTransport();
                    break;
                case 4:
                    viewTransport();
                    break;
                case 5:
                    viewAllTransports();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
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

        // driver
        System.out.println("Driver: ");
        int driverID = pickDriver(false).id();

        // truck
        System.out.println("Truck: ");
        String truckId = pickTruck(false).id();

        // source
        System.out.println("Source: ");
        Site source = pickSite(false);

        //========== destinations and items lists ================|
        int destinationId = 1;
        LinkedList<Site> destinations = new LinkedList<>();
        HashMap<Site, ItemList> itemsList = new HashMap<>();
        System.out.println("Pick destinations and items lists:");
        while(true){
            System.out.println("Destination number "+destinationId+": ");
            Site site = pickSite(true);
            if(site == null) break;
            int listId = getInt("Items list id: ");
            itemsList.put(site, itemLists.get(listId));
            destinations.add(site);
            destinationId++;
        }
        //========================================================|

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

        String json = JSON.serialize(newTransport);
        String responseJson = ts.createTransport(json);
        Response<String> response = JSON.deserialize(responseJson, Response.class);
        if(response.isSuccess()){
            transports.put(transportIdCounter, newTransport);
            transportIdCounter++;
        }
        System.out.println("\n"+response.getMessage());
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
                case 1:
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
                    break;
                case 2:
                    LocalTime departureTime = LocalTime.parse(getLine("Departure time (format: hh:mm): "));
                    departureDateTime = LocalDateTime.of(transport.scheduledTime().toLocalDate(), departureTime);
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
                    break;
                case 3:
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
                    break;
                case 4:
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
                    break;
                case 5:
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
                    break;
                case 6:
                    //TODO: support updating destinations
                    System.out.println("currently not supported");
                    break;
                case 7:
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
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
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
                case "y":
                    String json = JSON.serialize(transport);
                    String responseJson = ts.removeTransport(json);
                    Response<String> response = JSON.deserialize(responseJson, Response.class);
                    if(response.isSuccess()) transports.remove(transport.id());
                    System.out.println("\nTransport deleted successfully!");
                    break;
                case "n":
                    break;
                default:
                    System.out.println("Invalid option!");
                    continue;
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
        System.out.println("Date: "+ transport.scheduledTime().toLocalDate());
        System.out.println("Time: "+ transport.scheduledTime().toLocalTime());
        System.out.println("DriverId: "+ transport.driverId());
        System.out.println("TruckId: "+ transport.truckId());
        System.out.println("Source: "+ transport.source());
        System.out.println("Destinations: ");
        for(Site destination : transport.destinations()){
            System.out.println("   "+ destination + " (items list id: "+ transport.itemLists().get(destination).id()+")");
        }
        System.out.println("Weight: "+ transport.weight());
    }

    private static void updateTransportHelperMethod(int id, Site source, LinkedList<Site> destinations, HashMap<Site, ItemList> itemLists, String truckId, int driverId, LocalDateTime departureDateTime, int weight) {
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
}
