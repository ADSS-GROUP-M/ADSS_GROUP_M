package CMDApp;

import CMDApp.Records.*;
import TransportModule.ServiceLayer.ModuleFactory;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

import static CMDApp.DriversManagement.manageDrivers;
import static CMDApp.ItemListsManagement.manageItemLists;
import static CMDApp.SitesManagement.manageSites;
import static CMDApp.TransportsManagement.manageTransports;
import static CMDApp.TrucksManagement.manageTrucks;

public class Main {

    static ModuleFactory factory = ModuleFactory.getInstance();
    static Scanner scanner = new Scanner(System.in);

    static int transportIdCounter = 1;
    static int itemListIdCounter = 1;
    static HashMap<Integer, Driver> drivers = null;
    static HashMap<String, Truck> trucks = null;
    static HashMap<String,Site> sites = null;
    static HashMap<Integer,ItemList> itemLists = null;
    static HashMap<Integer, Transport> transports = null;

    public static void main(String[] args) {
        fetchDrivers();
        fetchTrucks();
        fetchSites();
        fetchItemLists();
        fetchTransports();
        mainMenu();
    }

    private static void mainMenu() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Welcome to the Transport Module!");
            System.out.println("Please select an option:");
            System.out.println("1. Manage transports");
            System.out.println("2. Manage item lists");
            System.out.println("3. Manage transport module resources");
            System.out.println("4. Exit");
            int option = getInt();
            switch (option){
                case 1:
                    manageTransports();
                    break;
                case 2:
                    manageItemLists();
                    break;
                case 3:
                    manageResources();
                    break;
                case 4:
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


    //==========================================================================|
    //============================== HELPER METHODS ============================|
    //==========================================================================|
    static int getInt(){
        return getInt(">> ");
    }

    static int getInt(String prefix){
        if(prefix != "") System.out.print(prefix);
        int option = 0;
        try{
            option = scanner.nextInt();
        }catch(InputMismatchException e){
            scanner = new Scanner(System.in);
        }
        return option;
    }

    static String getString(){
        return getString(">> ");
    }

    static String getString(String prefix) {
        if(prefix != "") System.out.print(prefix);
        return scanner.next();
    }

    static Site pickSite(boolean allowDone) {
        int i = 1;
        Site[] siteArray = new Site[sites.size()];
        for(Site site : sites.values()){
            System.out.println((i)+". Transport zone: "+site.transportZone()+", address: "+site.address());
            siteArray[i-1] = site;
            i++;
        }
        if(allowDone) System.out.println(i+". Done");
        int option = getInt()-1;
        if( (allowDone && (option < 0 || option > sites.size()))
                || (!allowDone && (option < 0 || option > sites.size()-1))){
            System.out.println("Invalid option!");
            return pickSite(allowDone);
        }
        if(allowDone && option == sites.size()) return null;
        return siteArray[option];
    }

    static Driver pickDriver(boolean allowDone) {
        int i = 1;
        Driver[] driverArray = new Driver[drivers.size()];
        for(Driver driver : drivers.values()){
            System.out.println((i)+". "+driver.name()+", license type: "+driver.licenseType());
            driverArray[i-1] = driver;
            i++;
        }
        if(allowDone) System.out.println(i+". Done");
        int option = getInt()-1;
        if( (allowDone && (option < 0 || option > drivers.size()))
                || (!allowDone && (option < 0 || option > drivers.size()-1))){
            System.out.println("Invalid option!");
            return pickDriver(allowDone);
        }
        if(allowDone && option == drivers.size()) return null;
        return driverArray[option];
    }

    static Truck pickTruck(boolean allowDone) {
        int i = 1;
        Truck[] truckArray = new Truck[trucks.size()];
        for(Truck truck : trucks.values()){
            System.out.println((i)+". "+"License plate: "+truck.id()+"model: "+truck.model()+", max weight: "+truck.maxWeight());
            truckArray[i-1] = truck;
            i++;
        }
        if(allowDone) System.out.println(i+". Done");
        int option = getInt()-1;
        if( (allowDone && (option < 0 || option > trucks.size()))
                || (!allowDone && (option < 0 || option > trucks.size()-1))){
            System.out.println("Invalid option!");
            return pickTruck(allowDone);
        }
        if(allowDone && option == trucks.size()) return null;
        return truckArray[option];
    }

    static void fetchTrucks() {
        String json = factory.getResourceManagementService().getAllTrucks();
        Response<LinkedList<Truck>> response = JSON.deserialize(json, Response.class);
        HashMap<String, Truck> truckMap = new HashMap<>();
        for(Truck truck : response.getData()){
            truckMap.put(truck.id(), truck);
        }
        trucks = truckMap;
    }

    static void fetchDrivers() {
        String json = factory.getResourceManagementService().getAllDrivers();
        Response<LinkedList<Driver>> response = JSON.deserialize(json, Response.class);
        HashMap<Integer, Driver> driverMap = new HashMap<>();
        for(Driver driver : response.getData()){
            driverMap.put(driver.id(), driver);
        }
        drivers = driverMap;
    }

    static void fetchSites() {
        String json = factory.getResourceManagementService().getAllSites();
        Response<LinkedList<Site>> response = JSON.deserialize(json, Response.class);
        HashMap<String, Site> siteMap = new HashMap<>();
        for(Site site : response.getData()){
            siteMap.put(site.address(), site);
        }
        sites = siteMap;
    }

    static void fetchItemLists() {
        String json = factory.getItemListsService().getAllItemLists();
        Response<LinkedList<ItemList>> response = JSON.deserialize(json, Response.class);
        HashMap<Integer, ItemList> listMap = new HashMap<>();
        for(ItemList list : response.getData()){
            listMap.put(list.id(), list);
        }
        itemLists = listMap;
    }

    static void fetchTransports() {

    }
}

