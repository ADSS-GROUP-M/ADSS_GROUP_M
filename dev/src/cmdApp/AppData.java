package cmdApp;

import cmdApp.records.*;
import transportModule.serviceLayer.ItemListsService;
import transportModule.serviceLayer.ModuleFactory;
import transportModule.serviceLayer.ResourceManagementService;
import transportModule.serviceLayer.TransportsService;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static cmdApp.records.Driver.LicenseType.*;
import static cmdApp.records.Truck.CoolingCapacity.*;

public class AppData {

    static boolean dataGenerated = false;
    private final HashMap<Integer, Driver> drivers;
    private final HashMap<String, Truck> trucks;
    private final HashMap<String, Site> sites;
    private final HashMap<Integer, ItemList> itemLists;
    private final HashMap<Integer, Transport> transports;

    public AppData(){
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


    void fetchTrucks() {
        String json = ModuleFactory.getInstance().getResourceManagementService().getAllTrucks();
        Type type = new TypeToken<Response<LinkedList<Truck>>>(){}.getType();
        Response<LinkedList<Truck>> response = JSON.deserialize(json, type);
        for(Truck truck : response.getData()){
            trucks.put(truck.id(), truck);
        }
    }

    void fetchDrivers() {
        String json = ModuleFactory.getInstance().getResourceManagementService().getAllDrivers();
        Type type = new TypeToken<Response<LinkedList<Driver>>>(){}.getType();
        Response<LinkedList<Driver>> response = JSON.deserialize(json, type);
        for(var driver : response.getData()){
            drivers.put(driver.id(), driver);
        }
    }

    void fetchSites() {
        String json = ModuleFactory.getInstance().getResourceManagementService().getAllSites();
        Type type = new TypeToken<Response<LinkedList<Site>>>(){}.getType();
        Response<LinkedList<Site>> response = JSON.deserialize(json, type);
        for(Site site : response.getData()){
            sites.put(site.address(), site);
        }
    }

    void fetchItemLists() {
        String json = ModuleFactory.getInstance().getItemListsService().getAllItemLists();
        Type type = new TypeToken<Response<LinkedList<ItemList>>>(){}.getType();
        Response<LinkedList<ItemList>> response = JSON.deserialize(json, type);
        for(ItemList list : response.getData()){
            itemLists.put(list.id(), list);
        }
    }

    void fetchTransports() {
        String json = ModuleFactory.getInstance().getTransportsService().getAllTransports();
        Type type = new TypeToken<Response<LinkedList<Transport>>>(){}.getType();
        Response<LinkedList<Transport>> response = JSON.deserialize(json, type);
        for(Transport transport : response.getData()){
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
        Driver driver1 = new Driver(1234, "megan smith", A1);
        Driver driver2 = new Driver(5678, "john doe", B2);
        Driver driver3 = new Driver(9012, "emily chen",C2);
        Driver driver4 = new Driver(3456, "david lee", C3);
        Driver driver5 = new Driver(7890, "sarah kim", C3);

        // generate trucks with random data:
        Truck truck1 = new Truck("abc123", "ford", 1500, 10000,NONE);
        Truck truck2 = new Truck("def456", "chevy", 2000, 15000,COLD);
        Truck truck3 = new Truck("ghi789", "toyota", 2500, 20000,COLD);
        Truck truck4 = new Truck("jkl012", "honda", 3000, 25000,FROZEN);
        Truck truck5 = new Truck("mno345", "nissan", 3500, 30000,FROZEN);


        ResourceManagementService rms = ModuleFactory.getInstance().getResourceManagementService();
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

        ItemListsService ils = ModuleFactory.getInstance().getItemListsService();
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
                new LinkedList<>(List.of(site4)),
                new HashMap<>() {{
                    put(site4, itemList2);
                }},
                truck5.id(),
                driver5.id(),
                LocalDateTime.of(2023, 4, 15, 10, 0),
                truck5.maxWeight()
        );

        TransportsService ts = ModuleFactory.getInstance().getTransportsService();
        ts.createTransport(JSON.serialize(transport1));
        ts.createTransport(JSON.serialize(transport2));
        ts.createTransport(JSON.serialize(transport3));
        ts.createTransport(JSON.serialize(transport4));
        ts.createTransport(JSON.serialize(transport5));

    }
}
