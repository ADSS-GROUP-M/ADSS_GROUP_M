package CMDApp;

import CMDApp.Records.*;
import TransportModule.ServiceLayer.ItemListsService;
import TransportModule.ServiceLayer.ModuleFactory;
import TransportModule.ServiceLayer.ResourceManagementService;
import TransportModule.ServiceLayer.TransportsService;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

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
        generateData();

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
        Type type = new TypeToken<Response<LinkedList<Truck>>>(){}.getType();
        Response<LinkedList<Truck>> response = JSON.deserialize(json, type);
        HashMap<String, Truck> truckMap = new HashMap<>();
        for(Truck truck : response.getData()){
            truckMap.put(truck.id(), truck);
        }
        trucks = truckMap;
    }

    static void fetchDrivers() {
        String json = factory.getResourceManagementService().getAllDrivers();
        Type type = new TypeToken<Response<LinkedList<Driver>>>(){}.getType();
        Response<LinkedList<Driver>> response = JSON.deserialize(json, type);
        HashMap<Integer, Driver> driverMap = new HashMap<>();
        for(var driver : response.getData()){
            driverMap.put(driver.id(), driver);
        }
        drivers = driverMap;
    }

    static void fetchSites() {
        String json = factory.getResourceManagementService().getAllSites();
        Type type = new TypeToken<Response<LinkedList<Site>>>(){}.getType();
        Response<LinkedList<Site>> response = JSON.deserialize(json, type);
        HashMap<String, Site> siteMap = new HashMap<>();
        for(Site site : response.getData()){
            siteMap.put(site.address(), site);
        }
        sites = siteMap;
    }

    static void fetchItemLists() {
        String json = factory.getItemListsService().getAllItemLists();
        Type type = new TypeToken<Response<LinkedList<ItemList>>>(){}.getType();
        Response<LinkedList<ItemList>> response = JSON.deserialize(json, type);
        HashMap<Integer, ItemList> listMap = new HashMap<>();
        for(ItemList list : response.getData()){
            listMap.put(list.id(), list);
        }
        itemLists = listMap;
    }

    static void fetchTransports() {
        String json = factory.getTransportsService().getAllTransports();
        Type type = new TypeToken<Response<LinkedList<Transport>>>(){}.getType();
        Response<LinkedList<Transport>> response = JSON.deserialize(json, type);
        HashMap<Integer, Transport> transportMap = new HashMap<>();
        for(Transport transport : response.getData()){
            transportMap.put(transport.id(), transport);
        }
        transports = transportMap;
    }

    private static void generateData() {

        //!!!!!!!!!!!!! CHAT GPT IS KING !!!!!!!!!!!

        // generate sites with random data:
        Site site1 = new Site("Zone A", "123 Main St", "(555) 123-4567", "John Smith", Site.SiteType.BRANCH);
        Site site2 = new Site("Zone B", "456 Oak Ave", "(555) 234-5678", "Jane Doe", Site.SiteType.LOGISTICAL_CENTER);
        Site site3 = new Site("Zone C", "789 Elm St", "(555) 345-6789", "Bob Johnson", Site.SiteType.SUPPLIER);
        Site site4 = new Site("Zone D", "246 Maple St", "(555) 456-7890", "Mary Jones", Site.SiteType.BRANCH);
        Site site5 = new Site("Zone E", "369 Pine Ave", "(555) 567-8901", "Tom Smith", Site.SiteType.LOGISTICAL_CENTER);

        // generate drivers with random data:
        Driver driver1 = new Driver(1234, "Megan Smith", "Class B");
        Driver driver2 = new Driver(5678, "John Doe", "Class C");
        Driver driver3 = new Driver(9012, "Emily Chen", "Class A");
        Driver driver4 = new Driver(3456, "David Lee", "Class B");
        Driver driver5 = new Driver(7890, "Sarah Kim", "Class C");

        // generate trucks with random data:
        Truck truck1 = new Truck("ABC123", "Ford", 1500, 10000);
        Truck truck2 = new Truck("DEF456", "Chevy", 2000, 15000);
        Truck truck3 = new Truck("GHI789", "Toyota", 2500, 20000);
        Truck truck4 = new Truck("JKL012", "Honda", 3000, 25000);
        Truck truck5 = new Truck("MNO345", "Nissan", 3500, 30000);

        ResourceManagementService rms = factory.getResourceManagementService();
        rms.addDriver(JSON.serialize(driver1));
        rms.addDriver(JSON.serialize(driver2));
        rms.addDriver(JSON.serialize(driver3));
        rms.addDriver(JSON.serialize(driver4));
        rms.addDriver(JSON.serialize(driver5));
        rms.addTruck(JSON.serialize(truck1));
        rms.addTruck(JSON.serialize(truck2));
        rms.addTruck(JSON.serialize(truck3));
        rms.addTruck(JSON.serialize(truck4));
        rms.addTruck(JSON.serialize(truck5));
        rms.addSite(JSON.serialize(site1));
        rms.addSite(JSON.serialize(site2));
        rms.addSite(JSON.serialize(site3));
        rms.addSite(JSON.serialize(site4));
        rms.addSite(JSON.serialize(site5));


        // generate item lists with random data:
        HashMap<String, Integer> load1 = new HashMap<>();
        load1.put("Shirts", 20);
        load1.put("Pants", 15);
        load1.put("Socks", 30);

        HashMap<String, Integer> unload1 = new HashMap<>();
        unload1.put("Jackets", 10);
        unload1.put("Hats", 5);
        unload1.put("Gloves", 20);

        ItemList itemList1 = new ItemList(1001, load1, unload1);

        HashMap<String, Integer> load2 = new HashMap<>();
        load2.put("Pencils", 50);
        load2.put("Notebooks", 20);
        load2.put("Erasers", 30);

        HashMap<String, Integer> unload2 = new HashMap<>();
        unload2.put("Pens", 40);
        unload2.put("Markers", 15);
        unload2.put("Highlighters", 25);

        ItemList itemList2 = new ItemList(1002, load2, unload2);

        HashMap<String, Integer> load3 = new HashMap<>();
        load3.put("Laptops", 5);
        load3.put("Tablets", 10);
        load3.put("Smartphones", 20);

        HashMap<String, Integer> unload3 = new HashMap<>();
        unload3.put("Desktops", 8);
        unload3.put("Monitors", 12);
        unload3.put("Printers", 6);

        ItemList itemList3 = new ItemList(1003, load3, unload3);

        HashMap<String, Integer> load4 = new HashMap<>();
        load4.put("Carrots", 15);
        load4.put("Broccoli", 10);
        load4.put("Celery", 20);

        HashMap<String, Integer> unload4 = new HashMap<>();
        unload4.put("Tomatoes", 12);
        unload4.put("Peppers", 8);
        unload4.put("Cucumbers", 18);

        ItemList itemList4 = new ItemList(1004, load4, unload4);

        HashMap<String, Integer> load5 = new HashMap<>();
        load5.put("Screws", 500);
        load5.put("Nails", 1000);
        load5.put("Bolts", 750);

        HashMap<String, Integer> unload5 = new HashMap<>();
        unload5.put("Washers", 800);
        unload5.put("Anchors", 600);
        unload5.put("Clamps", 900);

        ItemList itemList5 = new ItemList(1005, load5, unload5);

        ItemListsService ils = factory.getItemListsService();
        ils.addItemList(JSON.serialize(itemList1));
        ils.addItemList(JSON.serialize(itemList2));
        ils.addItemList(JSON.serialize(itemList3));
        ils.addItemList(JSON.serialize(itemList4));
        ils.addItemList(JSON.serialize(itemList5));

        // Randomly generated transports
        Transport transport1 = new Transport(
                2001,
                site1,
                new LinkedList<>(Arrays.asList(site2, site3)),
                new HashMap<>() {{
                    put(site2, itemList1);
                    put(site3, itemList2);
                }},
                truck1.id(),
                driver1.id(),
                LocalDateTime.of(2023, 4, 5, 8, 30),
                truck1.maxWeight()
        );

        Transport transport2 = new Transport(
                2002,
                site2,
                new LinkedList<>(Arrays.asList(site1, site4)),
                new HashMap<>() {{
                    put(site1, itemList3);
                    put(site4, itemList4);
                }},
                truck2.id(),
                driver2.id(),
                LocalDateTime.of(2023, 4, 7, 12, 0),
                truck2.maxWeight()
        );

        Transport transport3 = new Transport(
                2003,
                site3,
                new LinkedList<>(Arrays.asList(site1, site5)),
                new HashMap<>() {{
                    put(site1, itemList2);
                    put(site5, itemList5);
                }},
                truck3.id(),
                driver3.id(),
                LocalDateTime.of(2023, 4, 10, 9, 30),
                truck3.maxWeight()
        );

        Transport transport4 = new Transport(
                2004,
                site4,
                new LinkedList<>(Arrays.asList(site2, site3, site5)),
                new HashMap<>() {{
                    put(site2, itemList4);
                    put(site3, itemList1);
                    put(site5, itemList3);
                }},
                truck4.id(),
                driver4.id(),
                LocalDateTime.of(2023, 4, 12, 14, 0),
                truck4.maxWeight()
        );

        Transport transport5 = new Transport(
                2005,
                site5,
                new LinkedList<>(Arrays.asList(site4)),
                new HashMap<>() {{
                    put(site4, itemList2);
                }},
                truck5.id(),
                driver5.id(),
                LocalDateTime.of(2023, 4, 15, 10, 0),
                truck5.maxWeight()
        );

        TransportsService ts = factory.getTransportsService();
        ts.createTransport(JSON.serialize(transport1));
        ts.createTransport(JSON.serialize(transport2));
        ts.createTransport(JSON.serialize(transport3));
        ts.createTransport(JSON.serialize(transport4));
        ts.createTransport(JSON.serialize(transport5));

    }
}

