package CMDApp;

import CMDApp.Records.*;
import TransportModule.ServiceLayer.ItemListsService;
import TransportModule.ServiceLayer.ModuleFactory;
import TransportModule.ServiceLayer.ResourceManagementService;
import TransportModule.ServiceLayer.TransportsService;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

import static CMDApp.DriversManagement.manageDrivers;
import static CMDApp.ItemListsManagement.manageItemLists;
import static CMDApp.SitesManagement.manageSites;
import static CMDApp.SitesManagement.printSiteDetails;
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
            switch (option) {
                case 1 -> manageTransports();
                case 2 -> manageItemLists();
                case 3 -> manageResources();
                case 4 -> System.exit(0);
                default -> System.out.println("\nInvalid option!");
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
            switch (option) {
                case 1 -> manageSites();
                case 2 -> manageDrivers();
                case 3 -> manageTrucks();
                case 4 -> {
                    return;
                }
                default -> System.out.println("\nInvalid option!");
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
            return 0;
        }
        scanner.nextLine();
        return option;
    }

    static String getLine(){
        return getLine(">> ");
    }

    static String getLine(String prefix) {
        if(prefix != "") System.out.print(prefix);
        return scanner.nextLine().toLowerCase();
    }

    static String getWord(){
        return getWord(">> ");
    }

    static String getWord(String prefix) {
        if(prefix != "") System.out.print(prefix);
        String str = scanner.next().toLowerCase();
        scanner.nextLine();
        return str;
    }

    static Site pickSite(boolean allowDone) {
        int i = 1;
        Site[] siteArray = new Site[sites.size()];
        for(Site site : sites.values()){
            System.out.print(i+".");
            System.out.println(" Transport zone: "+site.transportZone());
            System.out.println("   address:        "+site.address());
            siteArray[i-1] = site;
            i++;
        }
        if(allowDone) System.out.println(i+". Done");
        int option = getInt()-1;
        if( (allowDone && (option < 0 || option > sites.size()))
                || (!allowDone && (option < 0 || option > sites.size()-1))){
            System.out.println("\nInvalid option!");
            return pickSite(allowDone);
        }
        if(allowDone && option == sites.size()) return null;
        return siteArray[option];
    }

    static Driver pickDriver(boolean allowDone) {
        int i = 1;
        Driver[] driverArray = new Driver[drivers.size()];
        for(Driver driver : drivers.values()){
            System.out.print(i+".");
            System.out.println(" name:         "+driver.name());
            System.out.println("   license type: "+driver.licenseType());
            driverArray[i-1] = driver;
            i++;
        }
        if(allowDone) System.out.println(i+". Done");
        int option = getInt()-1;
        if( (allowDone && (option < 0 || option > drivers.size()))
                || (!allowDone && (option < 0 || option > drivers.size()-1))){
            System.out.println("\nInvalid option!");
            return pickDriver(allowDone);
        }
        if(allowDone && option == drivers.size()) return null;
        return driverArray[option];
    }

    static Truck pickTruck(boolean allowDone) {
        int i = 1;
        Truck[] truckArray = new Truck[trucks.size()];
        for(Truck truck : trucks.values()){
//            System.out.println((i)+". "+"License plate: "+truck.id()+", model: "+truck.model()+", max weight: "+truck.maxWeight());
            System.out.print(i+".");
            System.out.println(" license plate: "+truck.id());
            System.out.println("   model:         "+truck.model());
            truckArray[i-1] = truck;
            i++;
        }
        if(allowDone) System.out.println(i+". Done");
        int option = getInt()-1;
        if( (allowDone && (option < 0 || option > trucks.size()))
                || (!allowDone && (option < 0 || option > trucks.size()-1))){
            System.out.println("\nInvalid option!");
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

    public static void generateData() {

        //!!!!!!!!!!!!! CHAT GPT IS KING !!!!!!!!!!!

        // generate sites with random data:
        Site site1 = new Site("zone a", "123 main st", "(555) 123-4567", "john smith", Site.SiteType.BRANCH);
        Site site2 = new Site("zone b", "456 oak ave", "(555) 234-5678", "jane doe", Site.SiteType.LOGISTICAL_CENTER);
        Site site3 = new Site("zone c", "789 elm st", "(555) 345-6789", "bob johnson", Site.SiteType.SUPPLIER);
        Site site4 = new Site("zone d", "246 maple st", "(555) 456-7890", "mary jones", Site.SiteType.BRANCH);
        Site site5 = new Site("zone e", "369 pine ave", "(555) 567-8901", "tom smith", Site.SiteType.LOGISTICAL_CENTER);

        // generate drivers with random data:
        Driver driver1 = new Driver(1234, "megan smith", "class b");
        Driver driver2 = new Driver(5678, "john doe", "class c");
        Driver driver3 = new Driver(9012, "emily chen", "class a");
        Driver driver4 = new Driver(3456, "david lee", "class b");
        Driver driver5 = new Driver(7890, "sarah kim", "class c");

        // generate trucks with random data:
        Truck truck1 = new Truck("abc123", "ford", 1500, 10000);
        Truck truck2 = new Truck("def456", "chevy", 2000, 15000);
        Truck truck3 = new Truck("ghi789", "toyota", 2500, 20000);
        Truck truck4 = new Truck("jkl012", "honda", 3000, 25000);
        Truck truck5 = new Truck("mno345", "nissan", 3500, 30000);


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
        load1.put("shirts", 20);
        load1.put("pants", 15);
        load1.put("socks", 30);

        HashMap<String, Integer> unload1 = new HashMap<>();
        unload1.put("jackets", 10);
        unload1.put("hats", 5);
        unload1.put("gloves", 20);

        ItemList itemList1 = new ItemList(1001, load1, unload1);

        HashMap<String, Integer> load2 = new HashMap<>();
        load2.put("pencils", 50);
        load2.put("notebooks", 20);
        load2.put("erasers", 30);

        HashMap<String, Integer> unload2 = new HashMap<>();
        unload2.put("pens", 40);
        unload2.put("markers", 15);
        unload2.put("highlighters", 25);

        ItemList itemList2 = new ItemList(1002, load2, unload2);

        HashMap<String, Integer> load3 = new HashMap<>();
        load3.put("laptops", 5);
        load3.put("tablets", 10);
        load3.put("smartphones", 20);

        HashMap<String, Integer> unload3 = new HashMap<>();
        unload3.put("desktops", 8);
        unload3.put("monitors", 12);
        unload3.put("printers", 6);

        ItemList itemList3 = new ItemList(1003, load3, unload3);

        HashMap<String, Integer> load4 = new HashMap<>();
        load4.put("carrots", 15);
        load4.put("broccoli", 10);
        load4.put("celery", 20);

        HashMap<String, Integer> unload4 = new HashMap<>();
        unload4.put("tomatoes", 12);
        unload4.put("peppers", 8);
        unload4.put("cucumbers", 18);

        ItemList itemList4 = new ItemList(1004, load4, unload4);

        HashMap<String, Integer> load5 = new HashMap<>();
        load5.put("screws", 500);
        load5.put("nails", 1000);
        load5.put("bolts", 750);

        HashMap<String, Integer> unload5 = new HashMap<>();
        unload5.put("washers", 800);
        unload5.put("anchors", 600);
        unload5.put("clamps", 900);

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

