package transportModule.frontend.cmdApp;

import transportModule.backend.serviceLayer.ItemListsService;
import transportModule.backend.serviceLayer.ResourceManagementService;
import transportModule.backend.serviceLayer.TransportsService;
import transportModule.records.*;
import utils.Response;

import java.time.LocalDateTime;
import java.util.*;

public class AppData {

    private Scanner scanner;
    private boolean dataGenerated;
    private final HashMap<Integer, Driver> drivers;
    private final HashMap<String, Truck> trucks;
    private final HashMap<String, Site> sites;
    private final HashMap<Integer, ItemList> itemLists;
    private final HashMap<Integer, Transport> transports;
    private final ResourceManagementService rms;
    private final ItemListsService ils;
    private final TransportsService ts;

    public AppData(ResourceManagementService rms, ItemListsService ils, TransportsService ts){
        scanner = new Scanner(System.in);
        dataGenerated = false;
        this.rms = rms;
        this.ils = ils;
        this.ts = ts;
        drivers = new HashMap<>();
        trucks = new HashMap<>();
        sites = new HashMap<>();
        itemLists = new HashMap<>();
        transports = new HashMap<>();
    }

    public HashMap<Integer, Driver> drivers() {
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

    public void generateData(){
        if(dataGenerated){
            System.out.println("Data already generated!");
            return;
        }
        generateAndAddData();
        dataGenerated = true;
        fetchDrivers();
        fetchTrucks();
        fetchSites();
        fetchItemLists();
        fetchTransports();
        System.out.println("\nData generated successfully!");
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
            sites.put(site.address(), site);
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

    private void generateAndAddData() {

        //!!!!!!!!!!!!! CHAT GPT IS KING !!!!!!!!!!!

        // generate sites with random data:
        Site site1 = new Site("zone a", "123 main st", "(555) 123-4567", "john smith", Site.SiteType.BRANCH);
        Site site2 = new Site("zone b", "456 oak ave", "(555) 234-5678", "jane doe", Site.SiteType.LOGISTICAL_CENTER);
        Site site3 = new Site("zone c", "789 elm st", "(555) 345-6789", "bob johnson", Site.SiteType.SUPPLIER);
        Site site4 = new Site("zone d", "246 maple st", "(555) 456-7890", "mary jones", Site.SiteType.BRANCH);
        Site site5 = new Site("zone e", "369 pine ave", "(555) 567-8901", "tom smith", Site.SiteType.LOGISTICAL_CENTER);

        // generate drivers with random data:
        Driver driver1 = new Driver(1234, "megan smith", Driver.LicenseType.A1);
        Driver driver2 = new Driver(5678, "john doe", Driver.LicenseType.B2);
        Driver driver3 = new Driver(9012, "emily chen", Driver.LicenseType.C2);
        Driver driver4 = new Driver(3456, "david lee", Driver.LicenseType.C3);
        Driver driver5 = new Driver(7890, "sarah kim", Driver.LicenseType.C3);

        // generate trucks with random data:
        Truck truck1 = new Truck("abc123", "ford", 1500, 10000, Truck.CoolingCapacity.NONE);
        Truck truck2 = new Truck("def456", "chevy", 2000, 15000, Truck.CoolingCapacity.COLD);
        Truck truck3 = new Truck("ghi789", "toyota", 2500, 20000, Truck.CoolingCapacity.COLD);
        Truck truck4 = new Truck("jkl012", "honda", 3000, 25000, Truck.CoolingCapacity.FROZEN);
        Truck truck5 = new Truck("mno345", "nissan", 3500, 30000, Truck.CoolingCapacity.FROZEN);

        rms.addDriver(driver1.toJson());
        rms.addDriver(driver2.toJson());
        rms.addDriver(driver3.toJson());
        rms.addDriver(driver4.toJson());
        rms.addDriver(driver5.toJson());
        rms.addTruck(truck1.toJson());
        rms.addTruck(truck2.toJson());
        rms.addTruck(truck3.toJson());
        rms.addTruck(truck4.toJson());
        rms.addTruck(truck5.toJson());
        rms.addSite(site1.toJson());
        rms.addSite(site2.toJson());
        rms.addSite(site3.toJson());
        rms.addSite(site4.toJson());
        rms.addSite(site5.toJson());

        // generate item lists with random data:
        HashMap<String, Integer> load1 = new HashMap<>();
        load1.put("shirts", 20);
        load1.put("pants", 15);
        load1.put("socks", 30);

        HashMap<String, Integer> unload1 = new HashMap<>();
        unload1.put("jackets", 10);
        unload1.put("hats", 5);
        unload1.put("gloves", 20);

        ItemList itemList1 = new ItemList(load1, unload1);

        HashMap<String, Integer> load2 = new HashMap<>();
        load2.put("pencils", 50);
        load2.put("notebooks", 20);
        load2.put("erasers", 30);

        HashMap<String, Integer> unload2 = new HashMap<>();
        unload2.put("pens", 40);
        unload2.put("markers", 15);
        unload2.put("highlighters", 25);

        ItemList itemList2 = new ItemList(load2, unload2);

        HashMap<String, Integer> load3 = new HashMap<>();
        load3.put("laptops", 5);
        load3.put("tablets", 10);
        load3.put("smartphones", 20);

        HashMap<String, Integer> unload3 = new HashMap<>();
        unload3.put("desktops", 8);
        unload3.put("monitors", 12);
        unload3.put("printers", 6);

        ItemList itemList3 = new ItemList(load3, unload3);

        HashMap<String, Integer> load4 = new HashMap<>();
        load4.put("carrots", 15);
        load4.put("broccoli", 10);
        load4.put("celery", 20);

        HashMap<String, Integer> unload4 = new HashMap<>();
        unload4.put("tomatoes", 12);
        unload4.put("peppers", 8);
        unload4.put("cucumbers", 18);

        ItemList itemList4 = new ItemList(load4, unload4);

        HashMap<String, Integer> load5 = new HashMap<>();
        load5.put("screws", 500);
        load5.put("nails", 1000);
        load5.put("bolts", 750);

        HashMap<String, Integer> unload5 = new HashMap<>();
        unload5.put("washers", 800);
        unload5.put("anchors", 600);
        unload5.put("clamps", 900);

        ItemList itemList5 = new ItemList(load5, unload5);

        ils.addItemList(itemList1.toJson());
        ils.addItemList(itemList2.toJson());
        ils.addItemList(itemList3.toJson());
        ils.addItemList(itemList4.toJson());
        ils.addItemList(itemList5.toJson());

        // Randomly generated transports
        Transport transport1 = new Transport(
                site1.address(),
                new LinkedList<>(Arrays.asList(site2.address(), site3.address())),
                new HashMap<>() {{
                    put(site2.address(), 1);
                    put(site3.address(), 2);
                }},
                truck1.id(),
                driver1.id(),
                LocalDateTime.of(2023, 4, 5, 8, 30),
                truck1.maxWeight()
        );

        Transport transport2 = new Transport(
                site2.address(),
                new LinkedList<>(Arrays.asList(site1.address(), site4.address())),
                new HashMap<>() {{
                    put(site1.address(), 3);
                    put(site4.address(), 4);
                }},
                truck2.id(),
                driver2.id(),
                LocalDateTime.of(2023, 4, 7, 12, 0),
                truck2.maxWeight()
        );

        Transport transport3 = new Transport(
                site3.address(),
                new LinkedList<>(Arrays.asList(site1.address(), site5.address())),
                new HashMap<>() {{
                    put(site1.address(), 2);
                    put(site5.address(), 5);
                }},
                truck3.id(),
                driver3.id(),
                LocalDateTime.of(2023, 4, 10, 9, 30),
                truck3.maxWeight()
        );

        Transport transport4 = new Transport(
                site4.address(),
                new LinkedList<>(Arrays.asList(site2.address(), site3.address(), site5.address())),
                new HashMap<>() {{
                    put(site2.address(), 4);
                    put(site3.address(), 1);
                    put(site5.address(), 3);
                }},
                truck4.id(),
                driver4.id(),
                LocalDateTime.of(2023, 4, 12, 14, 0),
                truck4.maxWeight()
        );

        Transport transport5 = new Transport(
                site5.address(),
                new LinkedList<>(List.of(site4.address())),
                new HashMap<>() {{
                    put(site4.address(), 2);
                }},
                truck5.id(),
                driver5.id(),
                LocalDateTime.of(2023, 4, 15, 10, 0),
                truck5.maxWeight()
        );

        ts.addTransport(transport1.toJson());
        ts.addTransport(transport2.toJson());
        ts.addTransport(transport3.toJson());
        ts.addTransport(transport4.toJson());
        ts.addTransport(transport5.toJson());
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
            System.out.print(i+".");
            System.out.println(" Transport zone: "+site.transportZone());
            System.out.println("   address:        "+site.address());
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

    Driver pickDriver(boolean allowDone) {
        int i = 1;
        Driver[] driverArray = new Driver[drivers.size()];
        for(Driver driver : drivers.values()){
            System.out.print(i+".");
            System.out.println(" name:         "+driver.name());
            System.out.println("   license type: "+driver.licenseType());
            driverArray[i-1] = driver;
            i++;
        }
        if(allowDone) {
            System.out.println(i+". Done");
        }
        int option = readInt()-1;
        if( (allowDone && (option < 0 || option > drivers.size()))
                || (!allowDone && (option < 0 || option > drivers.size()-1))){
            System.out.println("\nInvalid option!");
            return pickDriver(allowDone);
        }
        if(allowDone && option == drivers.size()) {
            return null;
        }
        return driverArray[option];
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
}
