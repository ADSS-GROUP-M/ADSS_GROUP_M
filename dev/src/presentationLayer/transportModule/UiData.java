package presentationLayer.transportModule;

import domainObjects.transportModule.*;
import serviceLayer.transportModule.ItemListsService;
import serviceLayer.transportModule.ResourceManagementService;
import serviceLayer.transportModule.TransportsService;
import utils.Response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UiData {

    private Scanner scanner;
    private final HashMap<String, Driver> drivers;
    private final HashMap<String, Truck> trucks;
    private final HashMap<String, Site> sites;
    private final HashMap<Integer, ItemList> itemLists;
    private final HashMap<Integer, Transport> transports;
    private final ResourceManagementService rms;
    private final ItemListsService ils;
    private final TransportsService ts;

    public UiData(ResourceManagementService rms, ItemListsService ils, TransportsService ts){
        scanner = new Scanner(System.in);
        this.rms = rms;
        this.ils = ils;
        this.ts = ts;
        drivers = new HashMap<>();
        trucks = new HashMap<>();
        sites = new HashMap<>();
        itemLists = new HashMap<>();
        transports = new HashMap<>();
    }

    public HashMap<String, Driver> drivers() {
        return drivers;
    }

    public HashMap<String, Truck> trucks() {
        return trucks;
    }

    public HashMap<String, Site> sites() {
        return sites;
    }

    public HashMap<Integer, ItemList> itemLists() {
        return itemLists;
    }

    public HashMap<Integer, Transport> transports() {
        return transports;
    }

    public void loadData(){
        fetchDrivers();
        fetchTrucks();
        fetchSites();
        fetchItemLists();
        fetchTransports();
    }

    private void fetchTrucks() {
        String json = rms.getAllTrucks();
        Response response = Response.fromJson(json);

        for(Truck truck : Truck.listFromJson(response.data())){
            trucks.put(truck.id(), truck);
        }
    }

    private void fetchDrivers() {
        String json = rms.getAllDrivers();
        Response response = Response.fromJson(json);
        for(Driver driver : Driver.listFromJson(response.data())){
            drivers.put(driver.id(), driver);
        }
    }

    private void fetchSites() {
        String json = rms.getAllSites();
        Response response = Response.fromJson(json);

        for(Site site : Site.listFromJson(response.data())){
            sites.put(site.name(), site);
        }
    }

    private void fetchItemLists() {
        String json = ils.getAllItemLists();
        Response response = Response.fromJson(json);

        for(ItemList list : ItemList.listFromJson(response.data())){
            itemLists.put(list.id(), list);
        }
    }

    private void fetchTransports() {
        String json = ts.getAllTransports();
        Response response = Response.fromJson(json);

        for(Transport transport : Transport.listFromJson(response.data())){
            transports.put(transport.id(), transport);
        }
    }
    
    //==========================================================================|
    //============================== HELPER METHODS ============================|
    //==========================================================================|

    int readInt(){
        return readInt(">> ");
    }
    int readInt(String prefix){
        if(prefix.equals("") == false) {
            System.out.print(prefix);
        }
        int option;
        try{
            option = scanner.nextInt();
        }catch(InputMismatchException e){
            scanner = new Scanner(System.in);
            return 0;
        }
        scanner.nextLine();
        return option;
    }

    String readLine(){
        return readLine(">> ");
    }

    String readLine(String prefix) {
        if(prefix.equals("") == false) {
            System.out.print(prefix);
        }
        return scanner.nextLine().toLowerCase();
    }

    String readWord(){
        return readWord(">> ");
    }

    String readWord(String prefix) {
        if(prefix.equals("") == false) {
            System.out.print(prefix);
        }
        String str = scanner.next().toLowerCase();
        scanner.nextLine();
        return str;
    }

    Site pickSite(boolean allowDone) {
        int i = 1;
        Site[] siteArray = new Site[sites.size()];
        for(Site site : sites.values()){
            System.out.println();
            System.out.print(i+".");
            System.out.println("  name:           "+site.name());
            System.out.println("    Transport zone: "+site.transportZone());
            System.out.println("    site type:      "+site.siteType());
            siteArray[i-1] = site;
            i++;
        }
        if(allowDone) {
            System.out.println(i+". Done");
        }
        int option = readInt()-1;
        if( (allowDone && (option < 0 || option > sites.size()))
                || (!allowDone && (option < 0 || option > sites.size()-1))){
            System.out.println("\nInvalid option!");
            return pickSite(allowDone);
        }
        if(allowDone && option == sites.size()) {
            return null;
        }
        return siteArray[option];
    }

    Driver pickDriver(boolean allowDone){
        return pickDriver(allowDone,drivers.values().toArray(new Driver[0]));
    }

    Driver pickDriver(boolean allowDone, Driver[] availableDrivers) {

        int i = 1;
        for (; i <= availableDrivers.length; i++) {
            Driver driver = availableDrivers[i-1];
            System.out.print(i + ".");
            System.out.println(" name:         " + driver.name());
            System.out.println("   license type: " + driver.licenseType());
        }
        if(allowDone) {
            System.out.println(i+". Done");
        }
        int option = readInt()-1;
        if( (allowDone && (option < 0 || option > availableDrivers.length))
                || (!allowDone && (option < 0 || option > availableDrivers.length-1))){
            System.out.println("\nInvalid option!");
            return pickDriver(allowDone);
        }
        if(allowDone && option == availableDrivers.length) {
            return null;
        }
        return availableDrivers[option];
    }

    Truck pickTruck(boolean allowDone) {
        int i = 1;
        Truck[] truckArray = new Truck[trucks.size()];
        for(Truck truck : trucks.values()){
            System.out.print(i+".");
            System.out.println(" license plate:    "+truck.id());
            System.out.println("   model:            "+truck.model());
            System.out.println("   max weight:       "+truck.maxWeight());
            System.out.println("   cooling capacity: "+truck.coolingCapacity());
            truckArray[i-1] = truck;
            i++;
        }
        if(allowDone) {
            System.out.println(i+". Done");
        }
        int option = readInt()-1;
        if( (allowDone && (option < 0 || option > trucks.size()))
                || (!allowDone && (option < 0 || option > trucks.size()-1))){
            System.out.println("\nInvalid option!");
            return pickTruck(allowDone);
        }
        if(allowDone && option == trucks.size()) {
            return null;
        }
        return truckArray[option];
    }

    public LocalTime readTime(String prefix) {
        LocalTime arrivalTime;
        while(true) {
            try{
                String line = readLine(prefix);
                if(line.equals("cancel!")) {
                    return null;
                }
                arrivalTime = LocalTime.parse(line);
                break;
            }catch(DateTimeParseException e) {
                System.out.println("Invalid time format, please try again");
            }
        }
        return arrivalTime;
    }

    public LocalDate readDate(String prefix) {
        LocalDate arrivalTime;
        while(true) {
            try{
                String line = readLine(prefix);
                if(line.equals("cancel!")) {
                    return null;
                }
                arrivalTime = LocalDate.parse(line);
                break;
            }catch(DateTimeParseException e) {
                System.out.println("Invalid time format, please try again");
            }
        }
        return arrivalTime;
    }
}
