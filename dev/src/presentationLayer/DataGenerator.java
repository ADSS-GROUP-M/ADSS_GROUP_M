package presentationLayer;

import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.Branch;
import businessLayer.employeeModule.Controllers.UserController;
import businessLayer.employeeModule.Role;
import businessLayer.transportModule.SitesController;
import dataAccessLayer.DalFactory;
import exceptions.DalException;
import exceptions.EmployeeException;
import exceptions.TransportException;
import objects.transportObjects.*;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Objects.SShiftType;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;
import serviceLayer.transportModule.ItemListsService;
import serviceLayer.transportModule.ResourceManagementService;
import serviceLayer.transportModule.TransportsService;
import utils.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static serviceLayer.employeeModule.Services.UserService.HR_MANAGER_USERNAME;

public class DataGenerator {

    private static final LocalDate EMPLOYMENT_DATE = LocalDate.of(2020,2,2);
    public static final LocalDate SHIFT_DATE = LocalDate.of(2023,2,2);
    public static UserService us;
    public static EmployeesService es;
    private static Driver driver1;
    private static Driver driver2;
    private static Driver driver3;
    private static Driver driver4;
    private static Driver driver5;
    private static Truck truck1;
    private static Truck truck2;
    private static Truck truck3;
    private static Truck truck4;
    private static Truck truck5;
    private static ItemList itemList1;
    private static ItemList itemList2;
    private static ItemList itemList3;
    private static ItemList itemList4;
    private static ItemList itemList5;
    private static Site branch1;
    private static Site branch2;
    private static Site branch3;
    private static Site branch4;
    private static Site branch5;
    private static Site branch6;
    private static Site branch7;
    private static Site branch8;
    private static Site branch9;
    private static Site supplier1;
    private static Site supplier2;
    private static Site supplier3;
    private static Site supplier4;
    private static Site supplier5;
    private static Site logistical1;

    public static void generateData(){
        deleteData();
        ServiceFactory factory = new ServiceFactory();
        UserController uc = factory.businessFactory().userController();
        us = factory.userService();
        es = factory.employeesService();
        ItemListsService ils = factory.itemListsService();
        initializeUserData(us,uc);
        generateSites(factory.businessFactory().sitesController());
        generateItemLists(ils);
        generateTrucks(factory.resourceManagementService());

        // Driver Data
        driver1 = new Driver("1234", "megan smith", Driver.LicenseType.A1);
        driver2 = new Driver("5678", "john doe", Driver.LicenseType.C3);
        driver3 = new Driver("9012", "emily chen", Driver.LicenseType.C2);
        driver4 = new Driver("3456", "david lee", Driver.LicenseType.B2);
        driver5 = new Driver("7890", "sarah kim", Driver.LicenseType.C3);
        List<Driver> morningDrivers = List.of(driver1, driver2);
        List<Driver> eveningDrivers = List.of(driver3, driver4, driver5);
        List<Driver> drivers = new ArrayList<>();
        drivers.addAll(morningDrivers);
        drivers.addAll(eveningDrivers);

        initializeBranches(drivers);
        initializeShiftDay(morningDrivers, eveningDrivers, SHIFT_DATE);
        assignStorekeepers(SHIFT_DATE);
        generateTransports(factory.transportsService());
    }

    public static void deleteData(){
        DalFactory.clearDB("SuperLiDB.db");
    }

    public static void generateTrucks(ResourceManagementService rms) {
        truck1 = new Truck("abc123", "ford", 1500, 10000, Truck.CoolingCapacity.NONE);
        truck2 = new Truck("def456", "chevy", 2000, 15000, Truck.CoolingCapacity.COLD);
        truck3 = new Truck("ghi789", "toyota", 2500, 20000, Truck.CoolingCapacity.COLD);
        truck4 = new Truck("jkl012", "honda", 3000, 25000, Truck.CoolingCapacity.FROZEN);
        truck5 = new Truck("mno345", "nissan", 3500, 30000, Truck.CoolingCapacity.FROZEN);

        validateOperation(rms.addTruck(truck1.toJson()));
        validateOperation(rms.addTruck(truck2.toJson()));
        validateOperation(rms.addTruck(truck3.toJson()));
        validateOperation(rms.addTruck(truck4.toJson()));
        validateOperation(rms.addTruck(truck5.toJson()));
    }

    public static void generateItemLists(ItemListsService ils) {
        HashMap<String, Integer> load1 = new HashMap<>();
        load1.put("shirts", 20);
        load1.put("pants", 15);
        load1.put("socks", 30);

        HashMap<String, Integer> unload1 = new HashMap<>();
        unload1.put("jackets", 10);
        unload1.put("hats", 5);
        unload1.put("gloves", 20);

        itemList1 = new ItemList(load1, unload1);

        HashMap<String, Integer> load2 = new HashMap<>();
        load2.put("pencils", 50);
        load2.put("notebooks", 20);
        load2.put("erasers", 30);

        HashMap<String, Integer> unload2 = new HashMap<>();
        unload2.put("pens", 40);
        unload2.put("markers", 15);
        unload2.put("highlighters", 25);

        itemList2 = new ItemList(load2, unload2);

        HashMap<String, Integer> load3 = new HashMap<>();
        load3.put("laptops", 5);
        load3.put("tablets", 10);
        load3.put("smartphones", 20);

        HashMap<String, Integer> unload3 = new HashMap<>();
        unload3.put("desktops", 8);
        unload3.put("monitors", 12);
        unload3.put("printers", 6);

        itemList3 = new ItemList(load3, unload3);

        HashMap<String, Integer> load4 = new HashMap<>();
        load4.put("carrots", 15);
        load4.put("broccoli", 10);
        load4.put("celery", 20);

        HashMap<String, Integer> unload4 = new HashMap<>();
        unload4.put("tomatoes", 12);
        unload4.put("peppers", 8);
        unload4.put("cucumbers", 18);

        itemList4 = new ItemList(load4, unload4);

        HashMap<String, Integer> load5 = new HashMap<>();
        load5.put("screws", 500);
        load5.put("nails", 1000);
        load5.put("bolts", 750);

        HashMap<String, Integer> unload5 = new HashMap<>();
        unload5.put("washers", 800);
        unload5.put("anchors", 600);
        unload5.put("clamps", 900);

        itemList5 = new ItemList(load5, unload5);

        validateOperation(ils.addItemList(itemList1.toJson()));
        validateOperation(ils.addItemList(itemList2.toJson()));
        validateOperation(ils.addItemList(itemList3.toJson()));
        validateOperation(ils.addItemList(itemList4.toJson()));
        validateOperation(ils.addItemList(itemList5.toJson()));
    }

    public static void generateSites(SitesController controller){
        branch1 = new Site("branch1", "14441 s inglewood ave, hawthorne, ca 90250, united states", "zone1", "111-111-1111", "John Smith", Site.SiteType.BRANCH, 0, 0);
        branch2 = new Site("branch2", "19503 s normandie ave, torrance, ca 90501, united states", "zone1", "222-222-2222", "Jane Doe", Site.SiteType.BRANCH, 0, 0);
        branch3 = new Site("branch3", "22015 hawthorne blvd, torrance, ca 90503, united states", "zone1", "333-333-3333", "Bob Johnson", Site.SiteType.BRANCH, 0, 0);
        branch4 = new Site("branch4", "2100 n long beach blvd, compton, ca 90221, united states", "zone2", "444-444-4444", "Samantha Lee", Site.SiteType.BRANCH, 0, 0);
        branch5 = new Site("branch5", "19340 hawthorne blvd, torrance, ca 90503, united states", "zone2", "555-555-5555", "Mike Brown", Site.SiteType.BRANCH, 0, 0);
        branch6 = new Site("branch6", "4651 firestone blvd, south gate, ca 90280, united states", "zone2", "666-666-6666", "Emily Wilson", Site.SiteType.BRANCH, 0, 0);
        branch7 = new Site("branch7", "1301 n victory pl, burbank, ca 91502, united states", "zone3", "777-777-7777", "Tom Kim", Site.SiteType.BRANCH, 0, 0);
        branch8 = new Site("branch8", "6433 fallbrook ave, west hills, ca 91307, united states","zone3", "888-888-8888", "Amanda Garcia", Site.SiteType.BRANCH, 0, 0);
        branch9 = new Site("branch9", "8333 van nuys blvd, panorama city, ca 91402, united states","zone4", "123-456-7890" ,"David Kim", Site.SiteType.BRANCH, 0, 0);
        supplier1 = new Site("supplier1", "8500 washington blvd, pico rivera, ca 90660, united states", "zone4", "456-789-0123", "William Davis", Site.SiteType.SUPPLIER, 0, 0);
        supplier2 = new Site("supplier2", "20226 avalon blvd, carson, ca 90746, united states", "zone3", "999-999-9999", "Steve Chen", Site.SiteType.SUPPLIER, 0, 0);
        supplier3 = new Site("supplier3", "9001 apollo way, downey, ca 90242, united states", "zone4", "345-678-9012", "Andrew Chen", Site.SiteType.SUPPLIER, 0, 0);
        supplier4 = new Site("supplier4", "2770 e carson st, lakewood, ca 90712, united states", "zone5", "123-456-7890", "Andrew Chen", Site.SiteType.SUPPLIER, 0,0);
        supplier5 = new Site("supplier5", "14501 lakewood blvd, paramount, ca 90723, united states", "zone4", "234-567-8901", "Jessica Park", Site.SiteType.SUPPLIER, 0, 0);
        logistical1 = new Site("logistical1", "3705 e south st, long beach, ca 90805, united states", "zone5", "123-456-7890", "Jessica Park", Site.SiteType.LOGISTICAL_CENTER, 0,0);

        List<Site> sites = new LinkedList<>(){{
            add(branch1);
            add(branch2);
            add(branch3);
            add(branch4);
            add(branch5);
            add(branch6);
            add(branch7);
            add(branch8);
            add(branch9);
            add(supplier1);
            add(supplier2);
            add(supplier3);
            add(supplier4);
            add(supplier5);
            add(logistical1);
        }};

        try {
            controller.addAllSitesFirstTimeSystemLoad(sites);
        } catch (TransportException e) {
            throw new RuntimeException(e);
        }

    }

    public static void initializeUserData(UserService us, UserController uc) {
        try {
            uc.createManagerUser(HR_MANAGER_USERNAME, "123");
        } catch (EmployeeException e) {
            throw new RuntimeException(e);
        }
        us.createUser(HR_MANAGER_USERNAME, UserService.TRANSPORT_MANAGER_USERNAME, "123");
        us.authorizeUser(HR_MANAGER_USERNAME, UserService.TRANSPORT_MANAGER_USERNAME, Authorization.TransportManager.name());
    }

    public static void initializeBranches(List<Driver> drivers) {
        initializeHeadquarters(drivers);
        for(int i=2; i<=9; i++) {
            String branchId = "branch"+i;
            List<String> morningStorekeeperIds = List.of(branchId + "Morning");
            List<String> eveningStorekeeperIds = List.of(branchId + "Evening");
            List<String> storekeeperIds = new ArrayList<>();
            storekeeperIds.addAll(morningStorekeeperIds);
            storekeeperIds.addAll(eveningStorekeeperIds);
            initializeStorekeepers(branchId, storekeeperIds);
        }
    }

    public static void initializeHeadquarters(List<Driver> drivers) {
        es.recruitEmployee(HR_MANAGER_USERNAME, Branch.HEADQUARTERS_ID, "Moshe Biton", "111","Hapoalim 12 230", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "More details about Moshe");
        es.certifyEmployee(HR_MANAGER_USERNAME,"111", Role.ShiftManager.name());
        es.certifyEmployee(HR_MANAGER_USERNAME,"111",Role.Storekeeper.name());
        us.createUser(HR_MANAGER_USERNAME,"111","1234");

        initializeDrivers(drivers);
    }

    public static void initializeDrivers(List<Driver> drivers) {
        for (Driver driver : drivers) {
            es.recruitEmployee(HR_MANAGER_USERNAME, Branch.HEADQUARTERS_ID, driver.name(), driver.id(), "Hapoalim 12 230", 40, EMPLOYMENT_DATE, "Employment Conditions Test", "More details about Driver");
            es.certifyDriver(HR_MANAGER_USERNAME, driver.id(), driver.licenseType().toString());
            us.createUser(HR_MANAGER_USERNAME, driver.id(), "123");
        }
    }

    public static void initializeStorekeepers(String branchId, List<String> storekeeperIds){
        for (String storekeeperId : storekeeperIds) {
            es.recruitEmployee(HR_MANAGER_USERNAME, branchId, "Name " + storekeeperId, storekeeperId, "Hapoalim 12 250", 30, LocalDate.of(2020, 2, 2), "Employment Conditions Test", "More details about Storekeeper");
            es.certifyEmployee(HR_MANAGER_USERNAME, storekeeperId, Role.Storekeeper.name());
            us.createUser(HR_MANAGER_USERNAME, storekeeperId, "123");
        }
    }

    public static void initializeShiftDay(List<Driver> morningDrivers, List<Driver> eveningDrivers, LocalDate date) {
        // Shift Creation
        es.createShiftDay(HR_MANAGER_USERNAME,Branch.HEADQUARTERS_ID,date);
        es.setShiftNeededAmount(HR_MANAGER_USERNAME,Branch.HEADQUARTERS_ID,date,SShiftType.Morning,"Cashier",0);
        es.setShiftNeededAmount(HR_MANAGER_USERNAME,Branch.HEADQUARTERS_ID,date,SShiftType.Morning,"GeneralWorker",0);
        es.setShiftNeededAmount(HR_MANAGER_USERNAME,Branch.HEADQUARTERS_ID,date,SShiftType.Morning,"Storekeeper",0);
        // Only one ShiftManager is still necessary to fully approve the shift at the end of the employees assignments

        for (int i = 2; i <= 9; i++) {
            String branchId = "branch"+i;
            es.createShiftDay(HR_MANAGER_USERNAME,branchId,date);
            es.setShiftNeededAmount(HR_MANAGER_USERNAME,Branch.HEADQUARTERS_ID,date,SShiftType.Morning,"Cashier",0);
            es.setShiftNeededAmount(HR_MANAGER_USERNAME,Branch.HEADQUARTERS_ID,date,SShiftType.Morning,"GeneralWorker",0);
            es.setShiftNeededAmount(HR_MANAGER_USERNAME,Branch.HEADQUARTERS_ID,date,SShiftType.Morning,"Storekeeper",0);
            // Only one ShiftManager is still necessary to fully approve the shift at the end of the employees assignments
        }

        // Drivers Assignment
        assignDrivers(morningDrivers,date, SShiftType.Morning);
        assignDrivers(eveningDrivers,date,SShiftType.Evening);
    }

    public static void assignStorekeepers(LocalDate date) {
        List<String> morningStorekeeperIds = List.of(Branch.HEADQUARTERS_ID + "Morning");
        List<String> eveningStorekeeperIds = List.of(Branch.HEADQUARTERS_ID + "Evening");
        assignShiftStorekeepers(Branch.HEADQUARTERS_ID, morningStorekeeperIds, date, SShiftType.Morning);
        assignShiftStorekeepers(Branch.HEADQUARTERS_ID, eveningStorekeeperIds, date, SShiftType.Evening);

        for(int i=2; i<=9; i++) {
            String branchId = "branch" + i;
            morningStorekeeperIds = List.of(branchId + "Morning");
            eveningStorekeeperIds = List.of(branchId + "Evening");
            assignShiftStorekeepers(branchId, morningStorekeeperIds, date, SShiftType.Morning);
            assignShiftStorekeepers(branchId, eveningStorekeeperIds, date, SShiftType.Evening);
        }
    }

    public static void assignDrivers(List<Driver> drivers, LocalDate date, SShiftType shiftType) {
        // Assign Drivers
        es.setShiftNeededAmount(HR_MANAGER_USERNAME,Branch.HEADQUARTERS_ID,date, shiftType,"Driver",drivers.size());
        for(Driver driver : drivers) {
            es.requestShift(driver.id(), Branch.HEADQUARTERS_ID, date, shiftType, "Driver");
        }
        es.setShiftEmployees(HR_MANAGER_USERNAME,Branch.HEADQUARTERS_ID,date, shiftType,"Driver", drivers.stream().map(d->d.id()).toList());
    }

    public static void assignShiftStorekeepers(String branchId, List<String> storekeeperIds, LocalDate date, SShiftType shiftType) {
        // Assign Storekeepers
        es.setShiftNeededAmount(HR_MANAGER_USERNAME, branchId, date, shiftType, "Storekeeper", storekeeperIds.size());
        for (String storekeeperId : storekeeperIds) {
            es.requestShift(storekeeperId, branchId, date, shiftType, "Storekeeper");
        }
        es.setShiftEmployees(HR_MANAGER_USERNAME, branchId, date, shiftType, "Storekeeper", storekeeperIds);
    }

    private static void generateTransports(TransportsService ts) {

        Transport transport1 = new Transport(
                logistical1.name(),
                new LinkedList<>(){{
                    add(supplier1.name());
                    add(branch2.name());
                    add(supplier2.name());
                    add(branch3.name());
                    add(supplier3.name());
                    add(branch4.name());
                }},
                new HashMap<>(){{
                    put(supplier1.name(), 4);
                    put(branch2.name(), 1);
                    put(supplier2.name(), 5);
                    put(branch3.name(), 2);
                    put(supplier3.name(), 5);
                    put(branch4.name(), 3);
                }},
                driver2.id(),
                truck5.id(),
                LocalDateTime.of(2023,2,2,12,0),
                27800
        );

        Transport transport2 = new Transport(
                logistical1.name(),
                new LinkedList<>(){{
                    add(supplier4.name());
                    add(branch5.name());
                    add(supplier5.name());
                    add(branch6.name());
                    add(branch7.name());
                    add(branch8.name());
                    add(branch9.name());
                }},
                new HashMap<>(){{
                    put(supplier4.name(), 4);
                    put(branch5.name(), 1);
                    put(supplier5.name(), 5);
                    put(branch6.name(), 2);
                    put(branch7.name(), 3);
                    put(branch8.name(), 3);
                    put(branch9.name(), 3);
                }},
                driver1.id(),
                truck1.id(),
                LocalDateTime.of(2023,2,2,12,0),
                9800
        );

        validateOperation(ts.addTransport(transport1.toJson()));
        validateOperation(ts.addTransport(transport2.toJson()));
    }

    private static void validateOperation(String json) {
        Response response;
        response = Response.fromJson(json);
        if(response.success() == false){
            throw new RuntimeException("Failed to add transport: " + response.message());
        }
    }
}
