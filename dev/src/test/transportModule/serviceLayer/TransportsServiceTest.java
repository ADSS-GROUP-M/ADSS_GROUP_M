package transportModule.serviceLayer;

import transportModule.businessLayer.records.*;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransportsServiceTest {

    private Transport transport;
    private TransportsService ts;

    @BeforeEach
    void setUp() {
        Site site1 = new Site("zone a", "123 main st", "(555) 123-4567", "john smith", Site.SiteType.BRANCH);
        Site site2 = new Site("zone b", "456 oak ave", "(555) 234-5678", "jane doe", Site.SiteType.LOGISTICAL_CENTER);
        Driver driver1 = new Driver(123, "megan smith", Driver.LicenseType.C3);
        Truck truck1 = new Truck("abc123", "ford", 1500, 10000, Truck.CoolingCapacity.FROZEN);
        HashMap<String, Integer> load1 = new HashMap<>();
        load1.put("shirts", 20);
        load1.put("pants", 15);
        load1.put("socks", 30);
        HashMap<String, Integer> unload1 = new HashMap<>();
        unload1.put("jackets", 10);
        unload1.put("hats", 5);
        unload1.put("gloves", 20);
        ItemList itemList1 = new ItemList(1001, load1, unload1);

        ItemListsService ils = ModuleFactory.getInstance().getItemListsService();
        ils.addItemList(JSON.serialize(itemList1));

        ResourceManagementService rms = ModuleFactory.getInstance().getResourceManagementService();
        rms.addDriver(JSON.serialize(driver1));
        rms.addTruck(JSON.serialize(truck1));
        rms.addSite(JSON.serialize(site1));
        rms.addSite(JSON.serialize(site2));

        HashMap<Site,ItemList> hm = new HashMap<>();
        hm.put(site2, itemList1);

        ts = ModuleFactory.getInstance().getTransportsService();
        transport = new Transport(
                1,
                site1,
                new LinkedList<>(List.of(site2)),
                hm,
                truck1.id(),
                driver1.id(),
                LocalDateTime.of(2020, 1, 1, 0, 0),
                100
        );
        ts.createTransport(JSON.serialize(transport));
    }

    @AfterEach
    void tearDown() {
        ModuleFactory.tearDownForTests();
    }

    @Test
    void createTransport() {
        HashMap<String, Integer> load1 = new HashMap<>();
        load1.put("shirts", 20);
        load1.put("pants", 15);
        load1.put("socks", 30);
        HashMap<String, Integer> unload1 = new HashMap<>();
        unload1.put("jackets", 10);
        unload1.put("hats", 5);
        unload1.put("gloves", 20);

        ItemList itemList = new ItemList(1001, load1, unload1);

        HashMap<Site,ItemList> hm = new HashMap<>();

        Site source = new Site("zone b", "456 oak ave", "(555) 234-5678", "jane doe", Site.SiteType.LOGISTICAL_CENTER);
        Site destination = new Site("zone a", "123 main st", "(555) 123-4567", "john smith", Site.SiteType.BRANCH);
        hm.put(destination, itemList);
        LinkedList<Site> destinations = new LinkedList<>(List.of(destination));
        TransportsService ts = ModuleFactory.getInstance().getTransportsService();
        Transport newTransport = new Transport(
                50,
                source,
                destinations,
                hm,
                "abc123",
                1234,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                2000
        );
        String json = ts.createTransport(JSON.serialize(newTransport));
        Response<String> response = JSON.deserialize(json, Response.class);
        assertTrue(response.isSuccess());
        String updatedJson = ModuleFactory.getInstance()
                .getTransportsService()
                .getTransport(
                        JSON.serialize(Transport.getLookupObject(newTransport.id()))
                );
        Type type = new TypeToken<Response<Transport>>(){}.getType();
        Response<Transport> updatedResponse = JSON.deserialize(updatedJson, type);
        Transport updatedTransport = updatedResponse.getData();
        assertEquals(newTransport.id(), updatedTransport.id());
        assertEquals(newTransport.source(), updatedTransport.source());
        assertEquals(newTransport.destinations(), updatedTransport.destinations());
        assertEquals(newTransport.itemLists(), updatedTransport.itemLists());
        assertEquals(newTransport.truckId(), updatedTransport.truckId());
        assertEquals(newTransport.driverId(), updatedTransport.driverId());
        assertEquals(newTransport.scheduledTime(), updatedTransport.scheduledTime());
        assertEquals(newTransport.weight(), updatedTransport.weight());
    }

    @Test
    void updateTransport() {

        HashMap<String, Integer> load1 = new HashMap<>();
        load1.put("shirts", 20);
        load1.put("pants", 15);
        load1.put("socks", 30);
        HashMap<String, Integer> unload1 = new HashMap<>();
        unload1.put("jackets", 10);
        unload1.put("hats", 5);
        unload1.put("gloves", 20);

        ItemList itemList = new ItemList(1001, load1, unload1);

        HashMap<Site,ItemList> hm = new HashMap<>();

        Site source = new Site("zone b", "456 oak ave", "(555) 234-5678", "jane doe", Site.SiteType.LOGISTICAL_CENTER);
        Site destination = new Site("zone a", "123 main st", "(555) 123-4567", "john smith", Site.SiteType.BRANCH);
        hm.put(destination, itemList);
        LinkedList<Site> destinations = new LinkedList<>(List.of(destination));
        TransportsService ts = ModuleFactory.getInstance().getTransportsService();
        Transport newTransport = new Transport(
                1,
                source,
                destinations,
                hm,
                "abc123",
                1234,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                2000
        );
        String json = ts.updateTransport(JSON.serialize(newTransport));
        Response<String> response = JSON.deserialize(json, Response.class);
        assertTrue(response.isSuccess());
        String updatedJson = ModuleFactory.getInstance()
                .getTransportsService()
                .getTransport(
                        JSON.serialize(Transport.getLookupObject(newTransport.id()))
                );
        Type type = new TypeToken<Response<Transport>>(){}.getType();
        Response<Transport> updatedResponse = JSON.deserialize(updatedJson, type);
        Transport updatedTransport = updatedResponse.getData();
        assertEquals(newTransport.id(), updatedTransport.id());
        assertEquals(newTransport.source(), updatedTransport.source());
        assertEquals(newTransport.destinations(), updatedTransport.destinations());
        assertEquals(newTransport.itemLists(), updatedTransport.itemLists());
        assertEquals(newTransport.truckId(), updatedTransport.truckId());
        assertEquals(newTransport.driverId(), updatedTransport.driverId());
        assertEquals(newTransport.scheduledTime(), updatedTransport.scheduledTime());
        assertEquals(newTransport.weight(), updatedTransport.weight());
    }

    @Test
    void removeTransport() {
        String json = ModuleFactory.getInstance()
                .getTransportsService()
                .removeTransport(
                        JSON.serialize(Transport.getLookupObject(transport.id()))
                );
        Response<String> response = JSON.deserialize(json, Response.class);
        assertTrue(response.isSuccess());
        String updatedJson = ModuleFactory.getInstance()
                .getTransportsService()
                .getTransport(
                        JSON.serialize(Transport.getLookupObject(transport.id()))
                );
        Type type = new TypeToken<Response<Transport>>(){}.getType();
        Response<Transport> updatedResponse = JSON.deserialize(updatedJson, type);
        assertFalse(updatedResponse.isSuccess());
    }

    @Test
    void getTransport() {
        String json = ts
                .getTransport(
                        JSON.serialize(Transport.getLookupObject(transport.id()))
                );
        Type type = new TypeToken<Response<Transport>>(){}.getType();
        Response<Transport> response = JSON.deserialize(json, type);
        Transport TransportReceived = response.getData();
        assertEquals(transport.id(), TransportReceived.id());
        assertEquals(transport.source(), TransportReceived.source());
        assertEquals(transport.destinations(), TransportReceived.destinations());
        assertEquals(transport.itemLists(), TransportReceived.itemLists());
        assertEquals(transport.truckId(), TransportReceived.truckId());
        assertEquals(transport.driverId(), TransportReceived.driverId());
        assertEquals(transport.scheduledTime(), TransportReceived.scheduledTime());
        assertEquals(transport.weight(), TransportReceived.weight());
    }

    @Test
    void getAllTransports() {
        String json = ModuleFactory.getInstance()
                .getTransportsService()
                .getAllTransports();
        Type type = new TypeToken<Response<List<Transport>>>(){}.getType();
        Response<List<Transport>> response = JSON.deserialize(json, type);
        List<Transport> updatedTransports = response.getData();
        assertTrue(updatedTransports.contains(transport));
    }

    @Test
    void createTransportWithTooMuchWeight(){
        Truck truck1 = new Truck("abcd1234", "ford", 1500, 10000, Truck.CoolingCapacity.FROZEN);
        ResourceManagementService rms = ModuleFactory.getInstance().getResourceManagementService();
        rms.addTruck(JSON.serialize(truck1));

        Transport newTransport = new Transport(
                50,
                Site.getLookupObject("address"),
                new LinkedList<>(),
                new HashMap<>(),
                "abcd1234",
                123,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                30000
        );
        String json = ts.createTransport(JSON.serialize(newTransport));
        Response<String> response = JSON.deserialize(json, Response.class);
        assertFalse(response.isSuccess());
        assertEquals(response.getData(),"weight");
    }

    @Test
    void createTransportWithBadLicense(){
        Driver driver = new Driver(12345,"name", Driver.LicenseType.A1);
        Truck truck1 = new Truck("abcd1234", "ford", 1500, 15000, Truck.CoolingCapacity.FROZEN);
        ResourceManagementService rms = ModuleFactory.getInstance().getResourceManagementService();
        rms.addDriver(JSON.serialize(driver));
        rms.addTruck(JSON.serialize(truck1));

        Transport newTransport = new Transport(
                50,
                Site.getLookupObject("address"),
                new LinkedList<>(),
                new HashMap<>(),
                "abcd1234",
                12345,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                10000
        );
        String json = ts.createTransport(JSON.serialize(newTransport));
        Response<String> response = JSON.deserialize(json, Response.class);
        assertFalse(response.isSuccess());
        assertEquals(response.getData(),"license");
    }
    @Test
    void createTransportWithBadLicenseAndTooMuchWeight(){
        Driver driver = new Driver(12345,"name", Driver.LicenseType.A1);
        Truck truck1 = new Truck("abcd1234", "ford", 1500, 15000, Truck.CoolingCapacity.FROZEN);
        ResourceManagementService rms = ModuleFactory.getInstance().getResourceManagementService();
        rms.addDriver(JSON.serialize(driver));
        rms.addTruck(JSON.serialize(truck1));

        Transport newTransport = new Transport(
                50,
                Site.getLookupObject("address"),
                new LinkedList<>(),
                new HashMap<>(),
                "abcd1234",
                12345,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                30000
        );
        String json = ts.createTransport(JSON.serialize(newTransport));
        Response<String> response = JSON.deserialize(json, Response.class);
        assertFalse(response.isSuccess());
        assertEquals(response.getData(),"weight,license");
    }
}
