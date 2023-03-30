package CMDApp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
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
        System.out.println("Source: ");
        String source = sites[pickSite(false)];
        int destinationId = 1;
        LinkedList<String> destinations = new LinkedList<>();
        LinkedList<HashMap<String,Integer>> itemsList = new LinkedList<>();
        System.out.println("=========================================");
        while(true){
            System.out.println("Destination number "+destinationId+": ");
            int option = pickSite(true);
            if(option == -1) break;
            int listId = getInt("Items list id: ");
            //TODO: code for adding items list
            destinations.add(sites[option]);
            destinationId++;
        }
        LocalDate departureDate = LocalDate.parse(getString("Departure date (format: yyyy-mm-dd): "));
        LocalTime departureTime = LocalTime.parse(getString("Departure time (format: hh:mm): "));
        int truckWeight = getInt("Truck weight: ");

        //TODO: code for adding transport
        transportIdCounter++;
        System.out.println("\nTransport added successfully!");
    }

    private static void updateTransport() {
    }

    private static void deleteTransport() {
    }

    private static void viewTransport() {
    }

    private static void viewAllTransports() {
    }
}
