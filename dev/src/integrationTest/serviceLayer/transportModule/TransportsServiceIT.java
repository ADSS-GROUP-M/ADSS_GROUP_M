package serviceLayer.transportModule;

import dataAccessLayer.DalFactory;
import objects.transportObjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Objects.SShiftType;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;
import utils.JsonUtils;
import utils.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;
class TransportsServiceIT {

    private Transport transport;
    private TransportsService ts;
    private EmployeesService es;
    private UserService us;
    private ItemListsService ils;
    private ResourceManagementService rms;
    private Site site1;
    private Site site2;

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @BeforeEach
    void setUp() {
        DalFactory.clearTestDB();
        ServiceFactory factory = new ServiceFactory(TESTING_DB_NAME);
        ts = factory.transportsService();
        ils = factory.itemListsService();
        rms = factory.resourceManagementService();
        es = factory.employeesService();
        us = factory.userService();

        site1 = new Site("TODO: INSERT NAME HERE", "123 main st", "zone a", "(555) 123-4567", "john smith", Site.SiteType.BRANCH);
        site2 = new Site("TODO: INSERT NAME HERE", "456 oak ave", "zone b", "(555) 234-5678", "jane doe", Site.SiteType.LOGISTICAL_CENTER);
        Driver driver1 = new Driver("123", "megan smith", Driver.LicenseType.C3);
        Truck truck1 = new Truck("abc123", "ford", 1500, 10000, Truck.CoolingCapacity.FROZEN);
        HashMap<String, Integer> load1 = new HashMap<>();
        load1.put("shirts", 20);
        load1.put("pants", 15);
        load1.put("socks", 30);
        HashMap<String, Integer> unload1 = new HashMap<>();
        unload1.put("jackets", 10);
        unload1.put("hats", 5);
        unload1.put("gloves", 20);
        ItemList itemList1 = new ItemList(load1, unload1);


        String json = ils.addItemList(itemList1.toJson());
        int id = Response.fromJson(json).dataToInt();
        itemList1 = itemList1.newId(id);

        initEmployeesModuleTestData();
        rms.addDriver(driver1.toJson()); // Should probably be removed if the services are fully integrated, it should create the driver automatically when certifying a driver employee.
        rms.addTruck(truck1.toJson());
        rms.addSite(site1.toJson());
        initBranchSite1();
        rms.addSite(site2.toJson());

        HashMap<String,Integer> hm = new HashMap<>();
        hm.put(site2.address(), itemList1.id());

        Transport transportToAdd = new Transport(
                site1.address(),
                new LinkedList<>(List.of(site2.address())),
                hm,
                driver1.id(),
                truck1.id(),
                LocalDateTime.of(2020, 1, 1, 0, 0),
                100
        );
        String json2 = ts.addTransport(transportToAdd.toJson());
        Response response = Response.fromJson(json2);
        if(response.success() == false){
            fail("Setup failed:\n" + response.message() + "\ncause: " + response.data());
        } else {
            int _id = response.dataToInt();
            transport = fetchAddedTransport(_id);
        }
    }

    @Test
    void addTransport() {
        HashMap<String, Integer> load1 = new HashMap<>();
        load1.put("shirts", 20);
        load1.put("pants", 15);
        load1.put("socks", 30);
        HashMap<String, Integer> unload1 = new HashMap<>();
        unload1.put("jackets", 10);
        unload1.put("hats", 5);
        unload1.put("gloves", 20);

        ItemList itemList = new ItemList(load1, unload1);

        String json = ils.addItemList(itemList.toJson());
        int id = Response.fromJson(json).dataToInt();
        itemList = itemList.newId(id);

        HashMap<String,Integer> hm = new HashMap<>();

        hm.put(site2.address(), itemList.id());
        LinkedList<String> destinations = new LinkedList<>(List.of(site2.address()));
        Transport newTransport = new Transport(
                site1.address(),
                destinations,
                hm,
                "123",
                "abc123",
                LocalDateTime.of(2020, 1, 1, 0, 0),
                2000
        );

        String json2 = ts.addTransport(newTransport.toJson());
        Response response = Response.fromJson(json2);
        assertTrue(response.success(),response.message());
        newTransport = fetchAddedTransport(response.dataToInt());
        String updatedJson = ts.getTransport(Transport.getLookupObject(response.dataToInt()).toJson());
        Response updatedResponse = Response.fromJson(updatedJson);
        Transport updatedTransport = updatedResponse.data(Transport.class);
        assertEquals(newTransport.id(), updatedTransport.id());
        assertEquals(newTransport.source(), updatedTransport.source());
        assertEquals(newTransport.route(), updatedTransport.route());
        assertEquals(newTransport.itemLists(), updatedTransport.itemLists());
        assertEquals(newTransport.truckId(), updatedTransport.truckId());
        assertEquals(newTransport.driverId(), updatedTransport.driverId());
        assertEquals(newTransport.departureTime(), updatedTransport.departureTime());
        assertEquals(newTransport.weight(), updatedTransport.weight());
    }

    @Test
    void addTransportPredefinedId(){
        try {
            ts.addTransport(Transport.getLookupObject(1001).toJson());
        } catch (UnsupportedOperationException e) {
            return;
        }
        fail();
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

        ItemList itemList = new ItemList(load1, unload1);

        String json = ils.addItemList(itemList.toJson());
        int id = Response.fromJson(json).dataToInt();
        itemList = itemList.newId(id);

        HashMap<String,Integer> hm = new HashMap<>();

        Site source = new Site("TODO: INSERT NAME HERE", "456 oak ave UPDATED", "zone b", "(555) 234-5678", "jane doe", Site.SiteType.LOGISTICAL_CENTER);
        Site destination = new Site("TODO: INSERT NAME HERE", "123 main st UPDATED", "zone a", "(555) 123-4567", "john smith", Site.SiteType.BRANCH);

        rms.addSite(source.toJson());
        rms.addSite(destination.toJson());
        initBranchSiteDestination();

        hm.put(destination.address(), itemList.id());
        LinkedList<String> destinations = new LinkedList<>(List.of(destination.address()));
        Transport newTransport = new Transport(
                1,
                source.address(),
                destinations,
                hm,
                "123",
                "abc123",
                LocalDateTime.of(2020, 1, 1, 0, 0),
                2000
        );

        String json2 = ts.updateTransport(newTransport.toJson());
        Response response = Response.fromJson(json2);
        assertTrue(response.success(),response.message());
        String updatedJson = ts.getTransport(Transport.getLookupObject(newTransport.id()).toJson());
        Response updatedResponse = Response.fromJson(updatedJson);
        Transport updatedTransport = updatedResponse.data(Transport.class);
        assertEquals(newTransport.id(), updatedTransport.id());
        assertEquals(newTransport.source(), updatedTransport.source());
        assertEquals(newTransport.route(), updatedTransport.route());
        assertEquals(newTransport.itemLists(), updatedTransport.itemLists());
        assertEquals(newTransport.truckId(), updatedTransport.truckId());
        assertEquals(newTransport.driverId(), updatedTransport.driverId());
        assertEquals(newTransport.departureTime(), updatedTransport.departureTime());
        assertEquals(newTransport.weight(), updatedTransport.weight());
    }

    @Test
    void updateTransportDoesNotExist(){
        Transport updatedTransport = Transport.getLookupObject(5);
        String json = ts.updateTransport(updatedTransport.toJson());
        Response response = Response.fromJson(json);
        assertFalse(response.success());
    }

    @Test
    void removeTransport() {
        String json = ts.removeTransport(Transport.getLookupObject(transport.id()).toJson());
        Response response = Response.fromJson(json);
        assertTrue(response.success(),response.message());
        String updatedJson = ts.getTransport(Transport.getLookupObject(transport.id()).toJson());
        Response updatedResponse = Response.fromJson(updatedJson);
        assertFalse(updatedResponse.success());
    }

    @Test
    void removeTransportDoesNotExist(){
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
        assertEquals(transport.route(), TransportReceived.route());
        assertEquals(transport.itemLists(), TransportReceived.itemLists());
        assertEquals(transport.truckId(), TransportReceived.truckId());
        assertEquals(transport.driverId(), TransportReceived.driverId());
        assertEquals(transport.departureTime(), TransportReceived.departureTime());
        assertEquals(transport.weight(), TransportReceived.weight());
    }

    @Test
    void getTransportDoesNotExist(){
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
                "123 main st",
                new LinkedList<>(),
                new HashMap<>(),
                "123",
                "abcd1234",
                LocalDateTime.of(2020, 1, 1, 0, 0),
                30000
        );
        String json = ts.addTransport(newTransport.toJson());
        Response response = Response.fromJson(json);
        assertFalse(response.success());
        assertEquals("weight",response.data());
    }

    @Test
    void createTransportWithBadLicense(){
        Driver driver = new Driver("12345","name", Driver.LicenseType.A1);
        Truck truck1 = new Truck("abcd1234", "ford", 1500, 15000, Truck.CoolingCapacity.FROZEN);
        rms.addDriver(driver.toJson());
        rms.addTruck(truck1.toJson());

        Transport newTransport = new Transport(
                "123 main st",
                new LinkedList<>(),
                new HashMap<>(),
                "12345",
                "abcd1234",
                LocalDateTime.of(2020, 1, 1, 0, 0),
                10000
        );
        String json = ts.addTransport(newTransport.toJson());
        Response response = Response.fromJson(json);
        assertFalse(response.success());
        assertEquals("license",response.data());
    }

    @Test
    void createTransportWithBadLicenseAndTooMuchWeight(){
        Driver driver = new Driver("12345","name", Driver.LicenseType.A1);
        Truck truck1 = new Truck("abcd1234", "ford", 1500, 15000, Truck.CoolingCapacity.FROZEN);
        rms.addDriver(driver.toJson());
        rms.addTruck(truck1.toJson());

        Transport newTransport = new Transport(
                "123 main st",
                new LinkedList<>(),
                new HashMap<>(),
                "12345",
                "abcd1234",
                LocalDateTime.of(2020, 1, 1, 0, 0),
                30000
        );
        String json = ts.addTransport(newTransport.toJson());
        Response response = Response.fromJson(json);
        assertFalse(response.success());
        assertEquals("license,weight",response.data());
    }
    @Test
    void createTransportEverythingDoesNotExist(){
        Transport newTransport = new Transport(
                "some name",
                new LinkedList<>(){{
                    add("some other name");
                    add("some other address2");
                }},
                new HashMap<>(){{
                    put("some other name", 10);
                    put("some other address2", 20);
                }},
                "some driver",
                "9999999",
                LocalDateTime.of(2020, 1, 1, 0, 0),
                10000
        );
        String json = ts.addTransport(newTransport.toJson());
        Response response = Response.fromJson(json);
        assertFalse(response.success());
        assertEquals("driver,truck,source,destination:0,itemList:0,destination:1,itemList:1",response.data());
    }

    String driverId1 = "123", driverId2 = "12345";

    String storekeeperId1 = "126", storekeeperId2 = "127", storekeeperId3 = "128", storekeeperId4 = "129";
    String HQAddress = "1", updatedBranchDestination = "123 main st UPDATED", site1Address = "123 main st";
    LocalDate shiftDate = LocalDate.of(2020, 1, 1);
    private void initEmployeesModuleTestData() {
        // Used for testing Employees module integration:
        // Creating the test branch and shifts, recruiting and certifying the employees
//        es.createData();
        es.updateBranchWorkingHours("admin123",HQAddress, LocalTime.of(0,0),LocalTime.of(14,0),LocalTime.of(14,0),LocalTime.of(23,0));
        es.createWeekShifts("admin123",HQAddress,shiftDate);
        es.setShiftNeededAmount("admin123",HQAddress,shiftDate,SShiftType.Morning,"Driver",1);
        es.recruitEmployee("admin123", HQAddress, "megan smith", driverId1,"Bank01",15, LocalDate.now(),"","");
        es.recruitEmployee("admin123", HQAddress, "name", driverId2,"Bank02",15, LocalDate.now(),"","");
        es.certifyEmployee("admin123",driverId1,"Driver"); // This call should probably be responsible for calling the rms.addDriver method that adds the driver, when executed normally through the HR Manager Menu (Employees Module CLI).
        es.certifyEmployee("admin123",driverId2,"Driver"); // This call should probably be responsible for calling the rms.addDriver method that adds the driver, when executed normally through the HR Manager Menu (Employees Module CLI).
        es.requestShift(driverId1,HQAddress,shiftDate,SShiftType.Morning,"Driver");
        es.requestShift(driverId2,HQAddress,shiftDate,SShiftType.Morning,"Driver");
        // Assigning the employees to the relevant shifts.
        es.setShiftEmployees("admin123",HQAddress,shiftDate, SShiftType.Morning,"Driver",new ArrayList<>(){{add(driverId1);add(driverId2);}});
        es.approveShift("admin123",HQAddress,shiftDate, SShiftType.Morning);
    }

    private void initBranchSite1() {
        es.updateBranchWorkingHours("admin123",site1Address, LocalTime.of(0,0),LocalTime.of(14,0),LocalTime.of(14,0),LocalTime.of(23,0));
        es.createWeekShifts("admin123",site1Address,shiftDate);
        es.recruitEmployee("admin123", site1Address, "Test Storekeeper5", storekeeperId3,"Bank5",25, LocalDate.now(),"","");
        es.recruitEmployee("admin123", site1Address, "Test Storekeeper6", storekeeperId4,"Bank6",25, LocalDate.now(),"","");
        es.certifyEmployee("admin123", storekeeperId3,"Storekeeper");
        es.certifyEmployee("admin123", storekeeperId4,"Storekeeper");
        es.requestShift(storekeeperId3,site1Address,shiftDate,SShiftType.Morning,"Storekeeper");
        es.requestShift(storekeeperId4,site1Address,shiftDate,SShiftType.Evening,"Storekeeper");
        es.setShiftEmployees("admin123",site1Address,shiftDate, SShiftType.Morning,"Storekeeper",new ArrayList<>(){{add(storekeeperId3);}});
        es.setShiftEmployees("admin123",site1Address,shiftDate, SShiftType.Evening,"Storekeeper",new ArrayList<>(){{add(storekeeperId4);}});
        es.approveShift("admin123",site1Address,shiftDate, SShiftType.Morning);
        es.approveShift("admin123",site1Address,shiftDate, SShiftType.Evening);
    }

    private void initBranchSiteDestination() {
        es.updateBranchWorkingHours("admin123", updatedBranchDestination, LocalTime.of(0,0),LocalTime.of(14,0),LocalTime.of(14,0),LocalTime.of(23,0));
        es.createWeekShifts("admin123", updatedBranchDestination,shiftDate);
        es.recruitEmployee("admin123", updatedBranchDestination, "Test Storekeeper3", storekeeperId1,"Bank3",25, LocalDate.now(),"","");
        es.recruitEmployee("admin123", updatedBranchDestination, "Test Storekeeper4", storekeeperId2,"Bank4",25, LocalDate.now(),"","");
        es.certifyEmployee("admin123", storekeeperId1,"Storekeeper");
        es.certifyEmployee("admin123", storekeeperId2,"Storekeeper");
        es.requestShift(storekeeperId1, updatedBranchDestination,shiftDate,SShiftType.Morning,"Storekeeper");
        es.requestShift(storekeeperId2, updatedBranchDestination,shiftDate,SShiftType.Evening,"Storekeeper");
        es.setShiftEmployees("admin123", updatedBranchDestination,shiftDate, SShiftType.Morning,"Storekeeper",new ArrayList<>(){{add(storekeeperId1);}});
        es.setShiftEmployees("admin123", updatedBranchDestination,shiftDate, SShiftType.Evening,"Storekeeper",new ArrayList<>(){{add(storekeeperId2);}});
        es.approveShift("admin123", updatedBranchDestination,shiftDate, SShiftType.Morning);
        es.approveShift("admin123", updatedBranchDestination,shiftDate, SShiftType.Evening);
    }

    private Transport fetchAddedTransport(int _id) {
        String _json = Transport.getLookupObject(_id).toJson();
        String _responseJson = ts.getTransport(_json);
        Response _response = JsonUtils.deserialize(_responseJson, Response.class);
        return Transport.fromJson(_response.data());
    }
}
