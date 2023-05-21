package serviceLayer.transportModule;

import businessLayer.employeeModule.Branch;
import businessLayer.employeeModule.Role;
import businessLayer.transportModule.SitesController;
import dataAccessLayer.DalFactory;
import domainObjects.transportModule.*;
import exceptions.TransportException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Objects.SShiftType;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;
import utils.ErrorCollection;
import utils.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static serviceLayer.employeeModule.Services.UserService.HR_MANAGER_USERNAME;

class TransportsServiceIT {

    public static final LocalDateTime DEPARTURE_TIME = LocalDateTime.of(2023, 2, 2, 9, 0);
    private Transport transport;
    private TransportsService ts;
    private EmployeesService es;
    private UserService us;
    private ItemListsService ils;
    private ResourceManagementService rms;
    private Site source;
    private Site dest1;
    private Site dest2;
    private Driver driver;
    private Truck truck;
    private ItemList itemList;
    private static int ITEM_LIST_ID = 1;
    private static int TRANSPORT_ID = 1;
    private SitesController sc;

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
        sc = factory.businessFactory().sitesController();

        us.initializeManagers();

        // sites
        source = new Site("logistical1", "14441 s inglewood ave, hawthorne, ca 90250, united states", "zone1", "111-111-1111", "John Smith", Site.SiteType.LOGISTICAL_CENTER, 0, 0);
        dest1 = new Site("branch1", "19503 s normandie ave, torrance, ca 90501, united states", "zone1", "222-222-2222", "Jane Doe", Site.SiteType.BRANCH, 0, 0);
        dest2 = new Site("branch2", "22015 hawthorne blvd, torrance, ca 90503, united states", "zone1", "333-333-3333", "Bob Johnson", Site.SiteType.BRANCH, 0, 0);
        List<Site> sites = new LinkedList<>(){{
            add(source);
            add(dest1);
            add(dest2);
        }};
        try {
            sc.addAllSitesFirstTimeSystemLoad(sites);
        } catch (TransportException e) {
            fail(e.getMessage(),e);
        }

        // item lists
        HashMap<String, Integer> load1 = new HashMap<>();
        load1.put("shirts", 20);
        load1.put("pants", 15);
        load1.put("socks", 30);
        HashMap<String, Integer> unload1 = new HashMap<>();
        unload1.put("jackets", 10);
        unload1.put("hats", 5);
        unload1.put("gloves", 20);
        itemList = new ItemList(load1, unload1);
        assertSuccessValue(ils.addItemList(itemList.toJson()), true);

        // truck
        truck = new Truck("abc123", "ford", 1500, 30000, Truck.CoolingCapacity.FROZEN);
        assertSuccessValue(rms.addTruck(truck.toJson()),true);

        initializeEmployeesData();

        transport = new Transport(
                new LinkedList<>() {{
                    add(source.name());
                    add(dest1.name());
                    add(dest2.name());
                }},
                new HashMap<>(){{
                    put(source.name(), ITEM_LIST_ID);
                    put(dest1.name(), ITEM_LIST_ID);
                    put(dest2.name(), ITEM_LIST_ID);
                }},
                driver.id(),
                truck.id(),
                DEPARTURE_TIME,
                29800
        );

    }

    @Test
    void addTransport() {
        Response response = assertSuccessValue(ts.addTransport(transport.toJson()), true);
        Transport returned = Transport.fromJson(response.data());
        assertEquals(TRANSPORT_ID, returned.id());
        assertDeepEquals(transport, returned);
        assertDoesNotThrow(() -> returned.getEstimatedTimeOfArrival(source.name()));
        assertDoesNotThrow(() -> returned.getEstimatedTimeOfArrival(dest1.name()));
        assertDoesNotThrow(() -> returned.getEstimatedTimeOfArrival(dest2.name()));
    }

    @Test
    void addTransportPredefinedId(){
        assertThrows(UnsupportedOperationException.class, () -> ts.addTransport(Transport.getLookupObject(5).toJson()));
    }

    @Test
    void updateTransport() {
        // set up
        Response _r = assertSuccessValue(ts.addTransport(transport.toJson()), true);
        transport = Transport.fromJson(_r.data());

        // update
        Transport updated = new Transport(
                transport.id(),
                new LinkedList<>() {{
                    add(source.name());
                    add(dest1.name());
                }},
                new HashMap<>(){{
                    put(source.name(), ITEM_LIST_ID);
                    put(dest1.name(), ITEM_LIST_ID);
                }},
                driver.id(),
                truck.id(),
                DEPARTURE_TIME,
                15000
        );

        Response response = assertSuccessValue(ts.updateTransport(updated.toJson()), true);
        Transport returned = Transport.fromJson(response.data());
        assertDeepEquals(updated, returned);
        assertDoesNotThrow(() -> returned.getEstimatedTimeOfArrival(source.name()));
        assertDoesNotThrow(() -> returned.getEstimatedTimeOfArrival(dest1.name()));
    }

    @Test
    void updateTransportDoesNotExist(){
        assertSuccessValue(ts.updateTransport(transport.toJson()), false);
    }

    @Test
    void removeTransport() {
        // set up
        Response _r = assertSuccessValue(ts.addTransport(transport.toJson()), true);
        transport = Transport.fromJson(_r.data());

        // test
        assertSuccessValue(ts.removeTransport(transport.toJson()), true);
        assertSuccessValue(ts.getTransport(Transport.getLookupObject(transport.id()).toJson()),false);
    }

    @Test
    void removeTransportDoesNotExist(){
        assertSuccessValue(ts.removeTransport(transport.toJson()), false);
    }

    @Test
    void getTransport() {
        // set up
        Response _r = assertSuccessValue(ts.addTransport(transport.toJson()), true);
        transport = Transport.fromJson(_r.data());

        // test
        Response response = assertSuccessValue(ts.getTransport(Transport.getLookupObject(transport.id()).toJson()), true);
        Transport returned = Transport.fromJson(response.data());
        assertDeepEquals(transport, returned);
        assertDoesNotThrow(() -> returned.getEstimatedTimeOfArrival(source.name()));
        assertDoesNotThrow(() -> returned.getEstimatedTimeOfArrival(dest1.name()));
        assertDoesNotThrow(() -> returned.getEstimatedTimeOfArrival(dest2.name()));
    }

    @Test
    void getTransportDoesNotExist(){
        assertSuccessValue(ts.getTransport(Transport.getLookupObject(transport.id()).toJson()), false);
    }

    @Test
    void getAllTransports() {
        // set up
        Response _r = assertSuccessValue(ts.addTransport(transport.toJson()), true);
        transport = Transport.fromJson(_r.data());

        // test
        Response response = assertSuccessValue(ts.getAllTransports(), true);
        List<Transport> returned = Transport.listFromJson(response.data());
        assertEquals(1, returned.size());
        assertDeepEquals(transport, returned.get(0));
        assertDoesNotThrow(() -> returned.get(0).getEstimatedTimeOfArrival(source.name()));
        assertDoesNotThrow(() -> returned.get(0).getEstimatedTimeOfArrival(dest1.name()));
        assertDoesNotThrow(() -> returned.get(0).getEstimatedTimeOfArrival(dest2.name()));
    }

    @Test
    void createTransportWithTooMuchWeight(){
        transport = new Transport(
                new LinkedList<>() {{
                    add(source.name());
                    add(dest1.name());
                    add(dest2.name());
                }},
                new HashMap<>(){{
                    put(source.name(), ITEM_LIST_ID);
                    put(dest1.name(), ITEM_LIST_ID);
                    put(dest2.name(), ITEM_LIST_ID);
                }},
                driver.id(),
                truck.id(),
                DEPARTURE_TIME,
                35000
        );
        Response response = assertSuccessValue(ts.addTransport(transport.toJson()), false);
        ErrorCollection errors = new ErrorCollection(response.message(),response.data());
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("weight"));
    }

    @Test
    void createTransportWithBadLicense(){

        driver = new Driver(
                driver.id(),
                driver.name(),
                Driver.LicenseType.A1
        );
        assertSuccessValue(rms.updateDriver(driver.toJson()),true);

        transport = new Transport(
                new LinkedList<>() {{
                    add(source.name());
                    add(dest1.name());
                    add(dest2.name());
                }},
                new HashMap<>(){{
                    put(source.name(), ITEM_LIST_ID);
                    put(dest1.name(), ITEM_LIST_ID);
                    put(dest2.name(), ITEM_LIST_ID);
                }},
                driver.id(),
                truck.id(),
                DEPARTURE_TIME,
                35000
        );
        Response response = assertSuccessValue(ts.addTransport(transport.toJson()), false);
        ErrorCollection errors = new ErrorCollection(response.message(),response.data());
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("license"));
    }

    @Test
    void createTransportWithBadLicenseAndTooMuchWeight(){

        driver = new Driver(
                driver.id(),
                driver.name(),
                Driver.LicenseType.A1
        );
        assertSuccessValue(rms.updateDriver(driver.toJson()),true);

        transport = new Transport(
                new LinkedList<>() {{
                    add(source.name());
                    add(dest1.name());
                    add(dest2.name());
                }},
                new HashMap<>(){{
                    put(source.name(), ITEM_LIST_ID);
                    put(dest1.name(), ITEM_LIST_ID);
                    put(dest2.name(), ITEM_LIST_ID);
                }},
                driver.id(),
                truck.id(),
                DEPARTURE_TIME,
                35000
        );
        Response response = assertSuccessValue(ts.addTransport(transport.toJson()), false);
        ErrorCollection errors = new ErrorCollection(response.message(),response.data());
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("weight"));
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("license"));
    }

    @Test
    void createTransportEverythingDoesNotExist(){

        // generate some made up sites
        Site sourceBad = new Site("site1","address1","tz1","25125","awgawgaw", Site.SiteType.SUPPLIER);
        Site dest1Bad = new Site("site2","address2","tz1","25125","awgawgaw", Site.SiteType.BRANCH);
        Site dest2Bad = new Site("site3","address3","tz1","25125","awgawgaw", Site.SiteType.BRANCH);

        transport = new Transport(
                new LinkedList<>() {{
                    add(sourceBad.name());
                    add(dest1Bad.name());
                    add(dest2Bad.name());
                }},
                new HashMap<>(){{
                    put(sourceBad.name(), 5);
                    put(dest1Bad.name(), 5);
                    put(dest2Bad.name(), 5);
                }},
                "fake driver",
                "fake truck",
                DEPARTURE_TIME,
                35000
        );
        Response response = assertSuccessValue(ts.addTransport(transport.toJson()), false);
        ErrorCollection errors = new ErrorCollection(response.message(),response.data());
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("truck"));
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("driver"));
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("source"));
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("destination:0"));
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("destination:1"));
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("itemList:0"));
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("itemList:1"));
    }

    private void assertDeepEquals(Transport transport1, Transport transport2) {
//        assertEquals(transport1.id(),transport2.id());
        assertEquals(transport1.route(),transport2.route());
        assertEquals(transport1.itemLists(),transport2.itemLists());
//        assertEquals(transport1.deliveryRoute().estimatedArrivalTimes(),transport2.deliveryRoute().estimatedArrivalTimes());
        assertEquals(transport1.departureTime(),transport2.departureTime());
        assertEquals(transport1.driverId(),transport2.driverId());
        assertEquals(transport1.weight(),transport2.weight());
    }

    private Response assertSuccessValue(String json, boolean expectedSuccess) {
        Response response = Response.fromJson(json);
        if(response.success() != expectedSuccess){
            fail("Operation failed:\n" +
                    "Expected success value: "+expectedSuccess + "\n" +
                    "Actual success value: "+response.success() + "\n" +
                    "Response message:" + response.message() + "\n" +
                    "Response data:" + response.data());
        }
        return response;
    }

    private void initializeEmployeesData() {

        // headquarters initialization
        assertSuccessValue(es.recruitEmployee(HR_MANAGER_USERNAME, Branch.HEADQUARTERS_ID, "Moshe Biton", "111","Hapoalim 12 230", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "More details about Moshe"),true);
        assertSuccessValue(es.certifyEmployee(HR_MANAGER_USERNAME,"111", Role.ShiftManager.name()),true);
        assertSuccessValue(es.certifyEmployee(HR_MANAGER_USERNAME,"111",Role.Storekeeper.name()),true);
        assertSuccessValue(us.createUser(HR_MANAGER_USERNAME,"111","1234"),true);

        // driver initialization
        driver = new Driver("123", "megan smith", Driver.LicenseType.C3);
        assertSuccessValue(es.recruitEmployee(HR_MANAGER_USERNAME, Branch.HEADQUARTERS_ID, driver.name(), driver.id(), "Hapoalim 12 230", 40, LocalDate.of(2023,2,2), "Employment Conditions Test", "More details about Driver"),true);
        assertSuccessValue(es.certifyDriver(HR_MANAGER_USERNAME, driver.id(), driver.licenseType().toString()),true);
        assertSuccessValue(us.createUser(HR_MANAGER_USERNAME, driver.id(), "123"),true);

        // storekeeper initialization
        List<String> storekeeperIds_dest1 = new LinkedList<>(){{
            add(dest1.name()+"morning");
            add(dest1.name()+"evening");
        }};
        for (String storekeeperId : storekeeperIds_dest1) {
            assertSuccessValue(es.recruitEmployee(HR_MANAGER_USERNAME, dest1.name(), "Name " + storekeeperId, storekeeperId, "Hapoalim 12 250", 30, LocalDate.of(2020, 2, 2), "Employment Conditions Test", "More details about Storekeeper"),true);
            assertSuccessValue(es.certifyEmployee(HR_MANAGER_USERNAME, storekeeperId, Role.Storekeeper.name()),true);
            assertSuccessValue(us.createUser(HR_MANAGER_USERNAME, storekeeperId, "123"),true);
        }
        List<String> storekeeperIds_dest2 = new LinkedList<>(){{
            add(dest2.name()+"morning");
            add(dest2.name()+"evening");
        }};
        for (String storekeeperId : storekeeperIds_dest2) {
            assertSuccessValue(es.recruitEmployee(HR_MANAGER_USERNAME, dest2.name(), "Name " + storekeeperId, storekeeperId, "Hapoalim 12 250", 30, LocalDate.of(2020, 2, 2), "Employment Conditions Test", "More details about Storekeeper"),true);
            assertSuccessValue(es.certifyEmployee(HR_MANAGER_USERNAME, storekeeperId, Role.Storekeeper.name()),true);
            assertSuccessValue(us.createUser(HR_MANAGER_USERNAME, storekeeperId, "123"),true);
        }

        //==================================== initialize shifts ==================================== |

        //                                  Shift Creation for dest1
        assertSuccessValue(es.createShiftDay(HR_MANAGER_USERNAME,dest1.name(),DEPARTURE_TIME.toLocalDate()),true);
        assertSuccessValue(es.setShiftNeededAmount(HR_MANAGER_USERNAME,dest1.name(),DEPARTURE_TIME.toLocalDate(),SShiftType.Morning,"Cashier",0),true);
        assertSuccessValue(es.setShiftNeededAmount(HR_MANAGER_USERNAME,dest1.name(),DEPARTURE_TIME.toLocalDate(),SShiftType.Morning,"GeneralWorker",0),true);
        assertSuccessValue(es.setShiftNeededAmount(HR_MANAGER_USERNAME,dest1.name(),DEPARTURE_TIME.toLocalDate(),SShiftType.Morning,"Storekeeper",0),true);
        //                                  Shift Creation for dest2
        assertSuccessValue(es.createShiftDay(HR_MANAGER_USERNAME,dest2.name(),DEPARTURE_TIME.toLocalDate()),true);
        assertSuccessValue(es.setShiftNeededAmount(HR_MANAGER_USERNAME,dest2.name(),DEPARTURE_TIME.toLocalDate(),SShiftType.Morning,"Cashier",0),true);
        assertSuccessValue(es.setShiftNeededAmount(HR_MANAGER_USERNAME,dest2.name(),DEPARTURE_TIME.toLocalDate(),SShiftType.Morning,"GeneralWorker",0),true);
        assertSuccessValue(es.setShiftNeededAmount(HR_MANAGER_USERNAME,dest2.name(),DEPARTURE_TIME.toLocalDate(),SShiftType.Morning,"Storekeeper",0),true);
        //                                  Assign drivers to shifts
        assertSuccessValue(es.setShiftNeededAmount(HR_MANAGER_USERNAME,Branch.HEADQUARTERS_ID,DEPARTURE_TIME.toLocalDate(), SShiftType.Morning,"Driver",1),true);
        assertSuccessValue(es.requestShift(driver.id(), Branch.HEADQUARTERS_ID, DEPARTURE_TIME.toLocalDate(), SShiftType.Morning, "Driver"),true);
        assertSuccessValue(es.setShiftEmployees(HR_MANAGER_USERNAME,Branch.HEADQUARTERS_ID,DEPARTURE_TIME.toLocalDate(), SShiftType.Morning,"Driver", List.of(driver.id())),true);
        //                                 Assign storekeepers to shifts
        // dest1 - morning
        assertSuccessValue(es.setShiftNeededAmount(HR_MANAGER_USERNAME, dest1.name(), DEPARTURE_TIME.toLocalDate(), SShiftType.Morning, "Storekeeper", 1),true);
        assertSuccessValue(es.requestShift(storekeeperIds_dest1.get(0), dest1.name(), DEPARTURE_TIME.toLocalDate(), SShiftType.Morning, "Storekeeper"),true);
        assertSuccessValue(es.setShiftEmployees(HR_MANAGER_USERNAME, dest1.name(), DEPARTURE_TIME.toLocalDate(), SShiftType.Morning, "Storekeeper", List.of(storekeeperIds_dest1.get(0))),true);
        // dest1 - evening
        assertSuccessValue(es.setShiftNeededAmount(HR_MANAGER_USERNAME, dest1.name(), DEPARTURE_TIME.toLocalDate(), SShiftType.Evening, "Storekeeper", 1),true);
        assertSuccessValue(es.requestShift(storekeeperIds_dest1.get(1), dest1.name(), DEPARTURE_TIME.toLocalDate(), SShiftType.Evening, "Storekeeper"),true);
        assertSuccessValue(es.setShiftEmployees(HR_MANAGER_USERNAME, dest1.name(), DEPARTURE_TIME.toLocalDate(), SShiftType.Evening, "Storekeeper", List.of(storekeeperIds_dest1.get(1))),true);
        // dest2 - morning
        assertSuccessValue(es.setShiftNeededAmount(HR_MANAGER_USERNAME, dest2.name(), DEPARTURE_TIME.toLocalDate(), SShiftType.Morning, "Storekeeper", 1),true);
        assertSuccessValue(es.requestShift(storekeeperIds_dest2.get(0), dest2.name(), DEPARTURE_TIME.toLocalDate(), SShiftType.Morning, "Storekeeper"),true);
        assertSuccessValue(es.setShiftEmployees(HR_MANAGER_USERNAME, dest2.name(), DEPARTURE_TIME.toLocalDate(), SShiftType.Morning, "Storekeeper", List.of(storekeeperIds_dest2.get(0))),true);
        // dest2 - evening
        assertSuccessValue(es.setShiftNeededAmount(HR_MANAGER_USERNAME, dest2.name(), DEPARTURE_TIME.toLocalDate(), SShiftType.Evening, "Storekeeper", 1),true);
        assertSuccessValue(es.requestShift(storekeeperIds_dest2.get(1), dest2.name(), DEPARTURE_TIME.toLocalDate(), SShiftType.Evening, "Storekeeper"),true);
        assertSuccessValue(es.setShiftEmployees(HR_MANAGER_USERNAME, dest2.name(), DEPARTURE_TIME.toLocalDate(), SShiftType.Evening, "Storekeeper", List.of(storekeeperIds_dest2.get(1))),true);
    }
}
