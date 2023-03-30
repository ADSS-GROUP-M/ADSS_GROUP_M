package CMDApp;

import TransportModule.ServiceLayer.*;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class Main {

    private static ModuleFactory factory = ModuleFactory.getInstance();
    private static Scanner scanner = new Scanner(System.in);
    private static int transportIdCounter = 1;
    private static String[] shippingZones = {"North", "South", "East", "West"};
    private static String[] sites = {"Site1", "Site2", "Site3", "Site4", "Site5"};
    private static String[] drivers = {"Driver1", "Driver2", "Driver3", "Driver4"};
    private static String[] trucks = {"Truck1", "Truck2", "Truck3", "Truck4"};

    public static void main(String[] args) {

        while(true){
            System.out.println("=========================================");
            System.out.println("Welcome to the Transport Module!");
            System.out.println("Please select an option:");
            System.out.println("1. Manage transports");
            System.out.println("2. Manage transport module resources");
            System.out.println("3. Exit");
            int option = getInt();
            switch (option){
                case 1:
                    manageTransports();
                    break;
                case 2:
                    manageResources();
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }

        }
    }

    private static void manageResources() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Transport module resources management");
            System.out.println("Please select an option:");
            System.out.println("1. Manage sites");
            System.out.println("2. Manage drivers");
            System.out.println("3. Manage trucks");
            System.out.println("4. Return to main menu");
            int option = getInt();
            switch (option){
                case 1:
                    manageSites();
                    break;
                case 2:
                    manageDrivers();
                    break;
                case 3:
                    manageTrucks();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void manageSites() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Sites management");
            System.out.println("Please select an option:");
            System.out.println("1. Create new site");
            System.out.println("2. Update site");
            System.out.println("3. Remove site");
            System.out.println("4. View full site information");
            System.out.println("5. View all sites");
            System.out.println("6. Return to previous menu");
            int option = getInt();
            switch (option){
                case 1:
                    createSite();
                    break;
                case 2:
                    updateSite();
                    break;
                case 3:
                    removeSite();
                    break;
                case 4:
                    getSite();
                    break;
                case 5:
                    getAllSites();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void createSite() {
        System.out.println("=========================================");
        System.out.println("Enter site details:");
        String shippingZone = getString("Shipping zone: ");
        String address = getString("Address: ");
        String contactPhone = getString("Contact phone: ");
        String contactName = getString("Contact name: ");
        //TODO: code for adding site

        System.out.println("\nSite added successfully!");
    }

    private static void updateSite() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select site to update:");
            int siteId = pickSite(true);
            if(siteId == -1) return;
            while(true) {
                System.out.println("=========================================");
                System.out.println("Site details:");
                System.out.println("Shipping zone: " + shippingZones[siteId]);
                System.out.println("Address: " + sites[siteId]);
                System.out.println("Contact name: " + sites[siteId]);
                System.out.println("Contact phone: " + sites[siteId]);
                System.out.println("=========================================");
                System.out.println("Please select an option:");
                System.out.println("1. Update shipping zone");
                System.out.println("2. Update address");
                System.out.println("3. Update contact name");
                System.out.println("4. Update contact phone");
                System.out.println("5. Return to previous menu");
                int option = getInt();
                switch (option) {
                    case 1:
                        String shippingZone = getString("Shipping zone: ");
                        break;
                    case 2:
                        String address = getString("Address: ");
                        break;
                    case 3:
                        String contactName = getString("Contact name: ");
                        break;
                    case 4:
                        String contactPhone = getString("Contact phone: ");
                        break;
                    case 5:
                        break;
                }

                //TODO: code for updating site
            }
        }
    }

    private static void removeSite() {
    }

    private static void getSite() {
    }

    private static void getAllSites() {
    }

    private static void manageTrucks() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Trucks management");
            System.out.println("Please select an option:");
            System.out.println("1. Create new truck");
            System.out.println("2. Update truck");
            System.out.println("3. Remove truck");
            System.out.println("4. View full truck information");
            System.out.println("5. View all trucks");
            System.out.println("6. Return to previous menu");
            int option = getInt();
            switch (option){
                case 1:
                    createTruck();
                    break;
                case 2:
                    updateTruck();
                    break;
                case 3:
                    removeTruck();
                    break;
                case 4:
                    getTruck();
                    break;
                case 5:
                    getAllTrucks();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void createTruck() {
        System.out.println("=========================================");
        System.out.println("Enter truck details:");
        String licensePlate = getString("License plate: ");
        String model = getString("Model: ");
        int baseWeight = getInt("Base weight: ");
        int maxWeight = getInt("Max weight: ");
        //TODO: code for adding truck

        System.out.println("\nTruck added successfully!");
    }

    private static void updateTruck() {

    }

    private static void removeTruck() {

    }

    private static void getTruck() {

    }

    private static void getAllTrucks() {

    }

    private static void manageDrivers() {
        while (true) {
            System.out.println("=========================================");
            System.out.println("Drivers management");
            System.out.println("Please select an option:");
            System.out.println("1. Create new driver");
            System.out.println("2. Update driver");
            System.out.println("3. Remove driver");
            System.out.println("4. View full driver information");
            System.out.println("5. View all drivers");
            System.out.println("6. Return to previous menu");
            int option = getInt();
            switch (option) {
                case 1:
                    createDriver();
                    break;
                case 2:
                    updateDriver();
                    break;
                case 3:
                    removeDriver();
                    break;
                case 4:
                    getDriver();
                    break;
                case 5:
                    getAllDrivers();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void createDriver() {
        System.out.println("=========================================");
        System.out.println("Enter driver details:");
        int id = getInt("Employee ID: ");
        String fullName = getString("Name: ");
        String licenseType = getString("License type: ");
        //TODO: code for adding driver

        System.out.println("\nDriver added successfully!");
    }

    private static void updateDriver() {

    }

    private static void removeDriver() {

    }

    private static void getDriver() {

    }

    private static void getAllDrivers() {

    }

    private static void manageTransports() {
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

    private static int pickSite(boolean allowDone) {
        int i = 1;
        for(String site : sites){
            System.out.println((i++)+". "+site);
        }
        if(allowDone) System.out.println(i+". Done");
        int option = getInt()-1;
        if( (allowDone && (option < 0 || option > sites.length))
                || (!allowDone && (option < 0 || option > sites.length-1))){
            System.out.println("Invalid option!");
            return pickSite(allowDone);
        }
        if(allowDone && option == sites.length) return -1;
        return option;
    }

    private static void itemListTemp() {
        HashMap<String,Integer> items = new HashMap<>();
        System.out.println("Enter items details:");
        System.out.println("To finish adding items, enter \"done\" in the item name");
        while(true){
            String item = getString("Item name: ");
            if(item.equalsIgnoreCase("done")){
                break;
            }
            int quantity = getInt("Quantity: ");
            items.put(item,quantity);
        }
    }

    private static void updateTransport() {
    }

    private static void deleteTransport() {
    }

    private static void viewTransport() {
    }

    private static void viewAllTransports() {
    }


    private static int getInt(){
        return getInt(">> ");
    }
    private static int getInt(String prefix){
        System.out.print(prefix);
        int option = 0;
        try{
            option = scanner.nextInt();
        }catch(InputMismatchException e){
            scanner = new Scanner(System.in);
        }
        return option;
    }
    private static String getString(){
        return getString(">> ");
    }
    private static String getString(String prefix) {
        System.out.print(prefix);
        return scanner.next();
    }
    private static void simulateInput(String s){
        new Thread(()->{
            try {
                Robot rob = new Robot();
//                Thread.sleep(10);
                for (char c : s.toCharArray()) {
                    rob.keyPress(c);
                    rob.keyRelease(c);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
