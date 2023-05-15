//package serviceLayer.transportModule;
//
//import businessLayer.employeeModule.Branch;
//import dataAccessLayer.DalFactory;
//import objects.transportObjects.*;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import serviceLayer.ServiceFactory;
//import serviceLayer.employeeModule.Objects.SShiftType;
//import serviceLayer.employeeModule.Services.EmployeesService;
//import serviceLayer.employeeModule.Services.UserService;
//import utils.JsonUtils;
//import utils.Response;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//
//import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
//import static org.junit.jupiter.api.Assertions.*;
//import static serviceLayer.employeeModule.Services.UserService.HR_MANAGER_USERNAME;
//
//class TransportsServiceIT {
//
//    private Transport transport;
//    private TransportsService ts;
//    private EmployeesService es;
//    private UserService us;
//    private ItemListsService ils;
//    private ResourceManagementService rms;
//    private Site source;
//    private Site dest1;
//    private Site dest2;
//
//    @AfterEach
//    void tearDown() {
//        DalFactory.clearTestDB();
//    }
//
//    @BeforeEach
//    void setUp() {
//        DalFactory.clearTestDB();
//        ServiceFactory factory = new ServiceFactory(TESTING_DB_NAME);
//        ts = factory.transportsService();
//        ils = factory.itemListsService();
//        rms = factory.resourceManagementService();
//        es = factory.employeesService();
//        us = factory.userService();
//
//        branch1 = new Site("branch1", "14441 s inglewood ave, hawthorne, ca 90250, united states", "zone1", "111-111-1111", "John Smith", Site.SiteType.LOGISTICAL_CENTER, 0, 0);
//        branch2 = new Site("branch2", "19503 s normandie ave, torrance, ca 90501, united states", "zone1", "222-222-2222", "Jane Doe", Site.SiteType.SUPPLIER, 0, 0);
//        branch3 = new Site("branch3", "22015 hawthorne blvd, torrance, ca 90503, united states", "zone1", "333-333-3333", "Bob Johnson", Site.SiteType.SUPPLIER, 0, 0);
//        Driver driver1 = new Driver("123", "megan smith", Driver.LicenseType.C3);
//        Truck truck1 = new Truck("abc123", "ford", 1500, 10000, Truck.CoolingCapacity.FROZEN);
//        HashMap<String, Integer> load1 = new HashMap<>();
//        load1.put("shirts", 20);
//        load1.put("pants", 15);
//        load1.put("socks", 30);
//        HashMap<String, Integer> unload1 = new HashMap<>();
//        unload1.put("jackets", 10);
//        unload1.put("hats", 5);
//        unload1.put("gloves", 20);
//        ItemList itemList1 = new ItemList(load1, unload1);
//
//        String json = ils.addItemList(itemList1.toJson());
//        int id = Response.fromJson(json).dataToInt();
//        itemList1 = itemList1.newId(id);
//
//        initEmployeesModuleTestData();
//        rms.addDriver(driver1.toJson()); // Should probably be removed if the services are fully integrated, it should create the driver automatically when certifying a driver employee.
//        rms.addTruck(truck1.toJson());
//        rms.addSite(site1.toJson());
//        initBranchSite1();
//        rms.addSite(site2.toJson());
//
//        HashMap<String,Integer> hm = new HashMap<>();
//        hm.put(site2.address(), itemList1.id());
//
//        Transport transportToAdd = new Transport(
//                site1.address(),
//                new LinkedList<>(List.of(site2.address())),
//                hm,
//                driver1.id(),
//                truck1.id(),
//                LocalDateTime.of(2020, 1, 1, 0, 0),
//                100
//        );
//        String json2 = ts.addTransport(transportToAdd.toJson());
//        Response response = Response.fromJson(json2);
//        if(response.success() == false){
//            fail("Setup failed:\n" + response.message() + "\ncause: " + response.data());
//        } else {
//            int _id = response.dataToInt();
//            transport = fetchAddedTransport(_id);
//        }
//    }
//
//    @Test
//    void addTransport() {
//
//    }
//
//    @Test
//    void addTransportPredefinedId(){
//
//    }
//
//    @Test
//    void updateTransport() {
//
//    }
//
//    @Test
//    void updateTransportDoesNotExist(){
//
//    }
//
//    @Test
//    void removeTransport() {
//
//    }
//
//    @Test
//    void removeTransportDoesNotExist(){
//
//    }
//
//    @Test
//    void getTransport() {
//
//    }
//
//    @Test
//    void getTransportDoesNotExist(){
//
//    }
//
//    @Test
//    void getAllTransports() {
//
//    }
//
//    @Test
//    void createTransportWithTooMuchWeight(){
//
//    }
//
//    @Test
//    void createTransportWithBadLicense(){
//
//    }
//
//    @Test
//    void createTransportWithBadLicenseAndTooMuchWeight(){
//
//    }
//    @Test
//    void createTransportEverythingDoesNotExist(){
//
//    }
//
//    String driverId1 = "123", driverId2 = "12345";
//
//    String storekeeperId1 = "126", storekeeperId2 = "127", storekeeperId3 = "128", storekeeperId4 = "129";
//    String HQAddress = "1", updatedBranchDestination = "123 main st UPDATED", site1Address = "123 main st";
//    LocalDate shiftDate = LocalDate.of(2020, 1, 1);
//    private void initEmployeesModuleTestData() {
//        // Used for testing Employees module integration:
//        // Creating the test branch and shifts, recruiting and certifying the employees
////        es.createData();
//        es.updateBranchWorkingHours("admin123",HQAddress, LocalTime.of(0,0),LocalTime.of(14,0),LocalTime.of(14,0),LocalTime.of(23,0));
//        es.createWeekShifts("admin123",HQAddress,shiftDate);
//        es.setShiftNeededAmount("admin123",HQAddress,shiftDate,SShiftType.Morning,"Driver",1);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,HQAddress,shiftDate,SShiftType.Morning,"Cashier",0);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,HQAddress,shiftDate,SShiftType.Morning,"GeneralWorker",0);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,HQAddress,shiftDate,SShiftType.Morning,"Storekeeper",0);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,HQAddress,shiftDate,SShiftType.Morning,"ShiftManager",0);
//
//        es.recruitEmployee("admin123", HQAddress, "megan smith", driverId1,"Bank01",15, LocalDate.now(),"","");
//        es.recruitEmployee("admin123", HQAddress, "name", driverId2,"Bank02",15, LocalDate.now(),"","");
//        es.certifyEmployee("admin123",driverId1,"Driver"); // This call should probably be responsible for calling the rms.addDriver method that adds the driver, when executed normally through the HR Manager Menu (Employees Module CLI).
//        es.certifyEmployee("admin123",driverId2,"Driver"); // This call should probably be responsible for calling the rms.addDriver method that adds the driver, when executed normally through the HR Manager Menu (Employees Module CLI).
//        es.requestShift(driverId1,HQAddress,shiftDate,SShiftType.Morning,"Driver");
//        es.requestShift(driverId2,HQAddress,shiftDate,SShiftType.Morning,"Driver");
//        // Assigning the employees to the relevant shifts.
//        es.setShiftEmployees("admin123",HQAddress,shiftDate, SShiftType.Morning,"Driver",new ArrayList<>(){{add(driverId1);add(driverId2);}});
//        es.approveShift("admin123",HQAddress,shiftDate, SShiftType.Morning);
//    }
//
//    private void initBranchSite1() {
//        es.updateBranchWorkingHours("admin123",site1Address, LocalTime.of(0,0),LocalTime.of(14,0),LocalTime.of(14,0),LocalTime.of(23,0));
//        es.createWeekShifts("admin123",site1Address,shiftDate);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME, site1Address,shiftDate,SShiftType.Morning,"Cashier",0);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,site1Address,shiftDate,SShiftType.Morning,"GeneralWorker",0);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,site1Address,shiftDate,SShiftType.Morning,"Storekeeper",1);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,site1Address,shiftDate,SShiftType.Morning,"ShiftManager", 0);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME, site1Address,shiftDate,SShiftType.Evening,"Cashier",0);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,site1Address,shiftDate,SShiftType.Evening,"GeneralWorker",0);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,site1Address,shiftDate,SShiftType.Evening,"Storekeeper",1);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,site1Address,shiftDate,SShiftType.Evening,"ShiftManager", 0);
//        es.recruitEmployee("admin123", site1Address, "Test Storekeeper5", storekeeperId3,"Bank5",25, LocalDate.now(),"","");
//        es.recruitEmployee("admin123", site1Address, "Test Storekeeper6", storekeeperId4,"Bank6",25, LocalDate.now(),"","");
//        es.certifyEmployee("admin123", storekeeperId3,"Storekeeper");
//        es.certifyEmployee("admin123", storekeeperId4,"Storekeeper");
//        es.requestShift(storekeeperId3,site1Address,shiftDate,SShiftType.Morning,"Storekeeper");
//        es.requestShift(storekeeperId4,site1Address,shiftDate,SShiftType.Evening,"Storekeeper");
//        es.setShiftEmployees("admin123",site1Address,shiftDate, SShiftType.Morning,"Storekeeper",new ArrayList<>(){{add(storekeeperId3);}});
//        es.setShiftEmployees("admin123",site1Address,shiftDate, SShiftType.Evening,"Storekeeper",new ArrayList<>(){{add(storekeeperId4);}});
//        es.approveShift("admin123",site1Address,shiftDate, SShiftType.Morning);
//        es.approveShift("admin123",site1Address,shiftDate, SShiftType.Evening);
//    }
//
//    private void initBranchSiteDestination() {
//        es.updateBranchWorkingHours("admin123", updatedBranchDestination, LocalTime.of(0,0),LocalTime.of(14,0),LocalTime.of(14,0),LocalTime.of(23,0));
//        es.createWeekShifts("admin123", updatedBranchDestination,shiftDate);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,updatedBranchDestination,shiftDate,SShiftType.Morning,"Cashier",0);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,updatedBranchDestination,shiftDate,SShiftType.Morning,"GeneralWorker",0);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,updatedBranchDestination,shiftDate,SShiftType.Morning,"Storekeeper",1);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,updatedBranchDestination,shiftDate,SShiftType.Morning,"ShiftManager", 0);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,updatedBranchDestination,shiftDate,SShiftType.Evening,"Cashier",0);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,updatedBranchDestination,shiftDate,SShiftType.Evening,"GeneralWorker",0);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,updatedBranchDestination,shiftDate,SShiftType.Evening,"Storekeeper",1);
//        es.setShiftNeededAmount(HR_MANAGER_USERNAME,updatedBranchDestination,shiftDate,SShiftType.Evening,"ShiftManager", 0);
//        es.recruitEmployee("admin123", updatedBranchDestination, "Test Storekeeper3", storekeeperId1,"Bank3",25, LocalDate.now(),"","");
//        es.recruitEmployee("admin123", updatedBranchDestination, "Test Storekeeper4", storekeeperId2,"Bank4",25, LocalDate.now(),"","");
//        es.certifyEmployee("admin123", storekeeperId1,"Storekeeper");
//        es.certifyEmployee("admin123", storekeeperId2,"Storekeeper");
//        es.requestShift(storekeeperId1, updatedBranchDestination,shiftDate,SShiftType.Morning,"Storekeeper");
//        es.requestShift(storekeeperId2, updatedBranchDestination,shiftDate,SShiftType.Evening,"Storekeeper");
//        es.setShiftEmployees("admin123", updatedBranchDestination,shiftDate, SShiftType.Morning,"Storekeeper",new ArrayList<>(){{add(storekeeperId1);}});
//        es.setShiftEmployees("admin123", updatedBranchDestination,shiftDate, SShiftType.Evening,"Storekeeper",new ArrayList<>(){{add(storekeeperId2);}});
//        es.approveShift("admin123", updatedBranchDestination,shiftDate, SShiftType.Morning);
//        es.approveShift("admin123", updatedBranchDestination,shiftDate, SShiftType.Evening);
//    }
//
//    private void assertDeepEquals(Transport transport1, Transport transport2) {
//        assertEquals(transport1.id(),transport2.id());
//        assertEquals(transport1.route(),transport2.route());
//        assertEquals(transport1.itemLists(),transport2.itemLists());
//        assertEquals(transport1.deliveryRoute().estimatedArrivalTimes(),transport2.deliveryRoute().estimatedArrivalTimes());
//        assertEquals(transport1.departureTime(),transport2.departureTime());
//        assertEquals(transport1.driverId(),transport2.driverId());
//        assertEquals(transport1.weight(),transport2.weight());
//    }
//
//    private Response assertSuccessValue(String json, boolean expectedSuccess) {
//        Response response = Response.fromJson(json);
//        if(response.success() != expectedSuccess){
//            fail("Operation failed:\n" +
//                    "Expected success value: "+expectedSuccess + "\n" +
//                    "Actual success value: "+response.success() + "\n" +
//                    "Response message:" + response.message() + "\n" +
//                    "Response data:" + response.data());
//        }
//        return response;
//    }
//
//}
