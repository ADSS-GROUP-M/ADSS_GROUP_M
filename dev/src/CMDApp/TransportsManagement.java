package CMDApp;

import CMDApp.Records.Transport;
import jdk.swing.interop.SwingInterOpUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Scanner;

import static CMDApp.Main.*;

public class TransportsManagement {

    private static Scanner scanner = new Scanner(System.in);

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
        LocalDate departureDate = LocalDate.parse(getString("Departure date (format: yyyy-mm-dd): "));
        LocalTime departureTime = LocalTime.parse(getString("Departure time (format: hh:mm): "));

        // driver
        System.out.println("Driver: ");
        int driverID = pickDriver(false);

        // truck
        System.out.println("Truck: ");
        String truckId = pickTruck(false);

        // source
        System.out.println("Source: ");
        String source = sites[pickSite(false)];

        //========== destinations and items lists ================|
        int destinationId = 1;
        LinkedList<String> destinations = new LinkedList<>();
        LinkedList<Integer> itemsList = new LinkedList<>();
        System.out.println("Pick destinations and items lists:");
        while(true){
            System.out.println("Destination number "+destinationId+": ");
            int option = pickSite(true);
            if(option == -1) break;
            int listId = getInt("Items list id: ");
            itemsList.add(listId);
            destinations.add(sites[option]);
            destinationId++;
        }
        //========================================================|

        //weight
        int truckWeight = getInt("Truck weight: ");

        //TODO: code for adding transport
        transportIdCounter++;
        System.out.println("\nTransport added successfully!");
    }

    private static void updateTransport() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select transport to update:");
            int transportId = getInt("Transport ID: ");
            if(transportId == -1) return;
            //TODO: code for fetching transport
            Transport transport = transport1;
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
                    //TODO: code for updating date
                    break;
                case 2:
                    //TODO: code for updating time
                    break;
                case 3:
                    //TODO: code for updating driver
                    break;
                case 4:
                    //TODO: code for updating truck
                    break;
                case 5:
                    //TODO: code for updating source
                    break;
                case 6:
                    //TODO: code for updating destinations
                    break;
                case 7:
                    //TODO: code for updating weight
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
            //TODO: code for updating transport
            System.out.println("\nTransport updated successfully!");
        }
    }

    private static void printTransportDetails(Transport transport) {
        System.out.println("Transport details:");
        System.out.println("Date: "+ transport.scheduledTime().toLocalDate());
        System.out.println("Time: "+ transport.scheduledTime().toLocalTime());
        System.out.println("DriverId: "+ transport.driverId());
        System.out.println("TruckId: "+ transport.truckId());
        System.out.println("Source: "+ transport.source());
        System.out.println("Destinations: ");
        for(int i = 0; i< transport.destinations().size(); i++){
            System.out.println("   "+ transport.destinations().get(i) + " (items list id: "+ transport.itemLists().get(i)+")");
        }
        System.out.println("Weight: "+ transport.weight());
    }

    private static void deleteTransport() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select transport to delete:");
            int transportId = getInt("Transport ID: ");
            if (transportId == -1) return;
            //TODO: code for fetching transport
            Transport transport = transport1;
            printTransportDetails(transport);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to delete this transport? (y/n)");
            String option = getString();
            switch (option) {
                case "y":
                    //TODO: code for deleting transport
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
    }

    private static void viewAllTransports() {
    }
}
