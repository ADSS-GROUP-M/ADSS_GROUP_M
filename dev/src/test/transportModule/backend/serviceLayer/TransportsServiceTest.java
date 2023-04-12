package transportModule.backend.serviceLayer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transportModule.records.*;
import utils.Response;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransportsServiceTest {

    private Transport transport;
    private TransportsService ts;

    private ItemListsService ils;
    private ResourceManagementService rms;

    @BeforeEach
    void setUp() {
        ModuleFactory mf = new ModuleFactory();
        ts = mf.getTransportsService();
        ils = mf.getItemListsService();
        rms = mf.getResourceManagementService();

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

        ils.addItemList(itemList1.toJson());

        rms.addDriver(driver1.toJson());
        rms.addTruck(truck1.toJson());
        rms.addSite(site1.toJson());
        rms.addSite(site2.toJson());

        HashMap<String,Integer> hm = new HashMap<>();
        hm.put(site2.address(), itemList1.id());

        transport = new Transport(
                1,
                site1.address(),
                new LinkedList<>(List.of(site2.address())),
                hm,
                truck1.id(),
                driver1.id(),
                LocalDateTime.of(2020, 1, 1, 0, 0),
                100
        );
        ts.createTransport(transport.toJson());
    }

    @AfterEach
    void tearDown() {

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

        HashMap<String,Integer> hm = new HashMap<>();

        Site source = new Site("zone b", "456 oak ave", "(555) 234-5678", "jane doe", Site.SiteType.LOGISTICAL_CENTER);
        Site destination = new Site("zone a", "123 main st", "(555) 123-4567", "john smith", Site.SiteType.BRANCH);
        hm.put(destination.address(), itemList.id());
        LinkedList<String> destinations = new LinkedList<>(List.of(destination.address()));
        Transport newTransport = new Transport(
                50,
                source.address(),
                destinations,
                hm,
                "abc123",
                123,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                2000
        );
        String json = ts.createTransport(newTransport.toJson());
        Response response = Response.fromJson(json);
        assertTrue(response.success());
        String updatedJson = ts.getTransport(Transport.getLookupObject(newTransport.id()).toJson());
        Response updatedResponse = Response.fromJson(updatedJson);
        Transport updatedTransport = updatedResponse.data(Transport.class);
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
    void createTransportAlreadyExists(){
        String json = ts.createTransport(transport.toJson());
        Response response = Response.fromJson(json);
        assertFalse(response.success());
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

        HashMap<String,Integer> hm = new HashMap<>();

        Site source = new Site("zone b", "456 oak ave", "(555) 234-5678", "jane doe", Site.SiteType.LOGISTICAL_CENTER);
        Site destination = new Site("zone a", "123 main st", "(555) 123-4567", "john smith", Site.SiteType.BRANCH);
        hm.put(destination.address(), itemList.id());
        LinkedList<String> destinations = new LinkedList<>(List.of(destination.address()));
        Transport newTransport = new Transport(
                1,
                source.address(),
                destinations,
                hm,
                "abc123",
                123,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                2000
        );
        String json = ts.updateTransport(newTransport.toJson());
        Response response = Response.fromJson(json);
        assertTrue(response.success());
        String updatedJson = ts.getTransport(Transport.getLookupObject(newTransport.id()).toJson());
        Response updatedResponse = Response.fromJson(updatedJson);
        Transport updatedTransport = updatedResponse.data(Transport.class);
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
    void updateTransportDoesntExist(){
        Transport updatedTransport = Transport.getLookupObject(5);
        String json = ts.updateTransport(updatedTransport.toJson());
        Response response = Response.fromJson(json);
        assertFalse(response.success());
    }

    @Test
    void removeTransport() {
        String json = ts.removeTransport(Transport.getLookupObject(transport.id()).toJson());
        Response response = Response.fromJson(json);
        assertTrue(response.success());
        String updatedJson = ts.getTransport(Transport.getLookupObject(transport.id()).toJson());
        Response updatedResponse = Response.fromJson(updatedJson);
        assertFalse(updatedResponse.success());
    }

    @Test
    void removeTransportDoesntExist(){
        String json = ts.removeTransport(Transport.getLookupObject(5).toJson());
        Response response = Response.fromJson(json);
        assertFalse(response.success());
    }

    @Test
    void getTransport() {
        String json = ts.getTransport(Transport.getLookupObject(transport.id()).toJson());
        Response response = Response.fromJson(json);
        Transport TransportReceived = response.data(Transport.class);
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
    void getTransportDoesntExist(){
        String json = ts.getTransport(Transport.getLookupObject(5).toJson());
        Response response = Response.fromJson(json);
        assertFalse(response.success());
    }

    @Test
    void getAllTransports() {
        String json = ts.getAllTransports();
        Response response = Response.fromJson(json);
        LinkedList<Transport> updatedTransports = Transport.listFromJson(response.data());
        assertTrue(updatedTransports.contains(transport));
    }

    @Test
    void createTransportWithTooMuchWeight(){
        Truck truck1 = new Truck("abcd1234", "ford", 1500, 10000, Truck.CoolingCapacity.FROZEN);
        rms.addTruck(truck1.toJson());

        Transport newTransport = new Transport(
                50,
                "123 main st",
                new LinkedList<>(),
                new HashMap<>(),
                "abcd1234",
                123,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                30000
        );
        String json = ts.createTransport(newTransport.toJson());
        Response response = Response.fromJson(json);
        assertFalse(response.success());
        assertEquals("weight",response.data());
    }

    @Test
    void createTransportWithBadLicense(){
        Driver driver = new Driver(12345,"name", Driver.LicenseType.A1);
        Truck truck1 = new Truck("abcd1234", "ford", 1500, 15000, Truck.CoolingCapacity.FROZEN);
        rms.addDriver(driver.toJson());
        rms.addTruck(truck1.toJson());

        Transport newTransport = new Transport(
                50,
                "123 main st",
                new LinkedList<>(),
                new HashMap<>(),
                "abcd1234",
                12345,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                10000
        );
        String json = ts.createTransport(newTransport.toJson());
        Response response = Response.fromJson(json);
        assertFalse(response.success());
        assertEquals("license",response.data());
    }
    @Test
    void createTransportWithBadLicenseAndTooMuchWeight(){
        Driver driver = new Driver(12345,"name", Driver.LicenseType.A1);
        Truck truck1 = new Truck("abcd1234", "ford", 1500, 15000, Truck.CoolingCapacity.FROZEN);
        rms.addDriver(driver.toJson());
        rms.addTruck(truck1.toJson());

        Transport newTransport = new Transport(
                50,
                "123 main st",
                new LinkedList<>(),
                new HashMap<>(),
                "abcd1234",
                12345,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                30000
        );
        String json = ts.createTransport(newTransport.toJson());
        Response response = Response.fromJson(json);
        assertFalse(response.success());
        assertEquals("license,weight",response.data());
    }

    @Test
    void createTransportEverythingDoesntExist(){
        Transport newTransport = new Transport(
                50,
                "some address",
                new LinkedList<>(){{
                    add("some other address");
                    add("some other address2");
                }},
                new HashMap<>(){{
                    put("some other address", 10);
                    put("some other address2", 20);
                }},
                "some driver",
                9999999,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                10000
        );
        String json = ts.createTransport(newTransport.toJson());
        Response response = Response.fromJson(json);
        assertFalse(response.success());
        assertEquals("driver,truck,source,destination:0,itemList:0,destination:1,itemList:1",response.data());
    }

}
