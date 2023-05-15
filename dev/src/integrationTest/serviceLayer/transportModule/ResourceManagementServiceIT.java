package serviceLayer.transportModule;

import businessLayer.employeeModule.Employee;
import dataAccessLayer.DalFactory;
import dataAccessLayer.employeeModule.EmployeeDAO;
import exceptions.DalException;
import objects.transportObjects.Driver;
import objects.transportObjects.ItemList;
import objects.transportObjects.Site;
import objects.transportObjects.Truck;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serviceLayer.ServiceFactory;
import utils.ErrorCollection;
import utils.Response;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class ResourceManagementServiceIT {

    private ResourceManagementService rms;
    private Driver driver;
    private Site site;
    private Truck truck;
    private EmployeeDAO empDao;
    private DalFactory dalFactory;
    private Employee employee;

    @BeforeEach
    void setUp() {

        ServiceFactory sFactory = new ServiceFactory(TESTING_DB_NAME);
        rms = sFactory.resourceManagementService();
        dalFactory = sFactory.businessFactory().dalFactory();
        empDao = dalFactory.employeeDAO();

        employee = new Employee("name", "123456789", "Poalim", 50, LocalDate.of(1999, 10, 10), "conditions", "details");
        driver = new Driver(employee.getId(),employee.getName(), Driver.LicenseType.C3);
        site = new Site("site1", "14441 s inglewood ave, hawthorne, ca 90250, united states", "zone a", "(555) 123-4567", "john smith", Site.SiteType.LOGISTICAL_CENTER);
        truck = new Truck("abc123", "ford", 1500, 10000, Truck.CoolingCapacity.FROZEN);
    }

    private void setUpDriver() {
        try {
            empDao.insert(employee);
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    void addDriver() {
        //set up
        setUpDriver();

        //test
        assertSuccessValue(rms.addDriver(driver.toJson()),true);
        Response response = assertSuccessValue(rms.getDriver(Driver.getLookupObject(driver.id()).toJson()),true);
        Driver addedDriver = Driver.fromJson(response.data());
        assertDriverDeepEquals(driver, addedDriver);
    }

    @Test
    void addDriverAlreadyExists(){
        //set up
        setUpDriver();
        assertSuccessValue(rms.addDriver(driver.toJson()),true);

        //test
        assertSuccessValue(rms.addDriver(driver.toJson()),false);
    }

    @Test
    void removeDriver() {
        //set up
        setUpDriver();
        assertSuccessValue(rms.addDriver(driver.toJson()),true);

        //test
        assertSuccessValue(rms.removeDriver(driver.toJson()),true);
        assertSuccessValue(rms.getDriver(Driver.getLookupObject(driver.id()).toJson()),false);
    }

    @Test
    void removeDriverDoesNotExist(){
        String lookupObject = Driver.getLookupObject("Some made up id").toJson();
        assertSuccessValue(rms.removeDriver(lookupObject),false);
    }

    @Test
    void updateDriver() {
        //set up
        setUpDriver();
        assertSuccessValue(rms.addDriver(driver.toJson()),true);

        //test
        Driver driverUpdate = new Driver(driver.id(), driver.name(), Driver.LicenseType.C1);
        assertSuccessValue(rms.updateDriver(driverUpdate.toJson()),true);
        Response response = assertSuccessValue(rms.getDriver(Driver.getLookupObject(driver.id()).toJson()),true);
        Driver updatedDriver = Driver.fromJson(response.data());
        assertDriverDeepEquals(driverUpdate, updatedDriver);
    }

    @Test
    void updateDriverDoesNotExist(){
        String lookupObject = Driver.getLookupObject("Some made up id").toJson();
        assertSuccessValue(rms.updateDriver(lookupObject),false);
    }

    @Test
    void getDriver() {
        //set up
        setUpDriver();
        assertSuccessValue(rms.addDriver(driver.toJson()),true);

        //test
        Response response = assertSuccessValue(rms.getDriver(Driver.getLookupObject(driver.id()).toJson()),true);
        Driver retrievedDriver = Driver.fromJson(response.data());
        assertDriverDeepEquals(driver, retrievedDriver);
    }

    @Test
    void getDriverDoesNotExist(){
        String lookupObject = Driver.getLookupObject("Some made up id").toJson();
        assertSuccessValue(rms.getDriver(lookupObject),false);
    }

    @Test
    void getAllDrivers() {
        //generate more drivers
        List<Driver> drivers = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Driver driver = new Driver(String.valueOf(i), "driver" + i, Driver.LicenseType.C3);
            try {
                empDao.insert(new Employee("driver" + i,String.valueOf(i),"Poalim",50, LocalDate.of(1999,10,10),"conditions","details"));
            } catch (DalException e) {
                throw new RuntimeException(e);
            }
            drivers.add(driver);
            assertSuccessValue(rms.addDriver(driver.toJson()),true);
        }

        Response response = assertSuccessValue(rms.getAllDrivers(),true);
        List<Driver> retrievedDrivers = Driver.listFromJson(response.data());
        assertEquals(drivers.size(), retrievedDrivers.size());
        for(int i = 0; i < drivers.size(); i++){
            assertDriverDeepEquals(drivers.get(i), retrievedDrivers.get(i));
        }
    }

    @Test
    void addTruck() {
        assertSuccessValue(rms.addTruck(truck.toJson()),true);
        Response response = assertSuccessValue(rms.getTruck(Truck.getLookupObject(truck.id()).toJson()),true);
        Truck addedTruck = Truck.fromJson(response.data());
        assertTruckDeepEquals(truck, addedTruck);
    }

    @Test
    void addTruckAlreadyExists(){
        //set up
        assertSuccessValue(rms.addTruck(truck.toJson()),true);

        //test
        assertSuccessValue(rms.addTruck(truck.toJson()),false);
    }

    @Test
    void addTruckNegativeBaseWeight(){
        Truck badTruck = new Truck("ghi789", "chevy", -2000, 15000, Truck.CoolingCapacity.FROZEN);
        Response response = assertSuccessValue(rms.addTruck(badTruck.toJson()),false);
        ErrorCollection errors = new ErrorCollection(response.message(), response.data());
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("negativeBaseWeight"));
    }

    @Test
    void addTruckNegativeMaxWeight(){
        Truck badTruck = new Truck("ghi789", "chevy", 2000, -15000, Truck.CoolingCapacity.FROZEN);
        Response response = assertSuccessValue(rms.addTruck(badTruck.toJson()),false);
        ErrorCollection errors = new ErrorCollection(response.message(), response.data());
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("negativeMaxWeight"));
    }

    @Test
    void addTruckBaseWeightGreaterThanMaxWeight(){
        Truck badTruck = new Truck("ghi789", "chevy", 2000, 1500, Truck.CoolingCapacity.FROZEN);
        Response response = assertSuccessValue(rms.addTruck(badTruck.toJson()),false);
        ErrorCollection errors = new ErrorCollection(response.message(), response.data());
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("baseWeightGreaterThanMaxWeight"));
    }

    @Test
    void removeTruck() {
        //set up
        assertSuccessValue(rms.addTruck(truck.toJson()),true);

        //test
        assertSuccessValue(rms.removeTruck(truck.toJson()),true);
        assertSuccessValue(rms.getTruck(Truck.getLookupObject(truck.id()).toJson()),false);
    }

    @Test
    void removeTruckDoesNotExist(){
        String lookupObject = Truck.getLookupObject("Some made up id").toJson();
        assertSuccessValue(rms.removeTruck(lookupObject),false);
    }

    @Test
    void updateTruck() {

        //set up
        assertSuccessValue(rms.addTruck(truck.toJson()),true);

        //test
        Truck truckUpdate = new Truck(truck.id(), truck.model(), 2000, 15000, Truck.CoolingCapacity.NONE);
        assertSuccessValue(rms.updateTruck(truckUpdate.toJson()),true);
        Response response = assertSuccessValue(rms.getTruck(Truck.getLookupObject(truck.id()).toJson()),true);
        Truck fetched = Truck.fromJson(response.data());
        assertTruckDeepEquals(truckUpdate, fetched);
    }

    @Test
    void updateTruckDoesNotExist(){
        String lookupObject = Truck.getLookupObject("Some made up id").toJson();
        assertSuccessValue(rms.updateTruck(lookupObject),false);
    }

    @Test
    void updateTruckNegativeBaseWeight(){
        //set up
        assertSuccessValue(rms.addTruck(truck.toJson()),true);

        //test
        Truck truckUpdate = new Truck(truck.id(), truck.model(), -2000, 15000, Truck.CoolingCapacity.NONE);
        Response response = assertSuccessValue(rms.updateTruck(truckUpdate.toJson()),false);
        ErrorCollection errors = new ErrorCollection(response.message(), response.data());
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("negativeBaseWeight"));
    }

    @Test
    void updateTruckNegativeMaxWeight(){
        //set up
        assertSuccessValue(rms.addTruck(truck.toJson()),true);

        //test
        Truck truckUpdate = new Truck(truck.id(), truck.model(), 2000, -15000, Truck.CoolingCapacity.NONE);
        Response response = assertSuccessValue(rms.updateTruck(truckUpdate.toJson()),false);
        ErrorCollection errors = new ErrorCollection(response.message(), response.data());
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("negativeMaxWeight"));
    }

    @Test
    void updateTruckBaseWeightGreaterThanMaxWeight(){
        //set up
        assertSuccessValue(rms.addTruck(truck.toJson()),true);

        //test
        Truck truckUpdate = new Truck(truck.id(), truck.model(), 2000, 1500, Truck.CoolingCapacity.NONE);
        Response response = assertSuccessValue(rms.updateTruck(truckUpdate.toJson()),false);
        ErrorCollection errors = new ErrorCollection(response.message(), response.data());
        assertTrue(Arrays.asList(errors.causesAsArray()).contains("baseWeightGreaterThanMaxWeight"));
    }

    @Test
    void getTruck() {
        //set up
        assertSuccessValue(rms.addTruck(truck.toJson()),true);

        //test
        Response response = assertSuccessValue(rms.getTruck(Truck.getLookupObject(truck.id()).toJson()),true);
        Truck fetched = Truck.fromJson(response.data());
        assertTruckDeepEquals(truck, fetched);
    }

    @Test
    void getTruckDoesNotExist(){
        String lookupObject = Truck.getLookupObject("Some made up id").toJson();
        assertSuccessValue(rms.getTruck(lookupObject),false);
    }

    @Test
    void getAllTrucks() {
        //generate more trucks
        List<Truck> trucks = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Truck truck = new Truck("abc" + i, "truck" + i, 2000, 15000, Truck.CoolingCapacity.FROZEN);
            trucks.add(truck);
            assertSuccessValue(rms.addTruck(truck.toJson()),true);
        }

        Response response = assertSuccessValue(rms.getAllTrucks(),true);
        List<Truck> fetched = Truck.listFromJson(response.data());
        assertEquals(trucks.size(), fetched.size());
        for(int i = 0; i < trucks.size(); i++){
            assertTruckDeepEquals(trucks.get(i), fetched.get(i));
        }
    }

//    @Test
//    void addSite() {
//        Site site3 = new Site("TODO: INSERT NAME HERE", "name a", "zone a", "123456","bob", Site.SiteType.BRANCH);
//
//        String json1 = rms.addSite(site3.toJson());
//        Response response1 = Response.fromJson(json1);
//        assertTrue(response1.success(),response1.message());
//
//        String json2 = rms.getSite(Site.getLookupObject(site3.address()).toJson());
//        Response response2 = Response.fromJson(json2);
//        assertTrue(response2.success(),response2.message());
//        Site siteToCheck = response2.data(Site.class);
//        assertEquals(site3.transportZone(), siteToCheck.transportZone());
//        assertEquals(site3.address(), siteToCheck.address());
//        assertEquals(site3.phoneNumber(), siteToCheck.phoneNumber());
//        assertEquals(site3.contactName(), siteToCheck.contactName());
//    }
//
//    @Test
//    void addSiteAlreadyExists(){
//        String json1 = rms.addSite(site.toJson());
//        Response response1 = Response.fromJson(json1);
//        assertFalse(response1.success());
//    }
//
//    @Test
//    void removeSite() {
//        String json1 = rms.removeSite(site.toJson());
//        Response response1 = Response.fromJson(json1);
//        assertTrue(response1.success(),response1.message());
//
//        String json2 = rms.getSite(Site.getLookupObject(site.name()).toJson());
//        Response response2 = Response.fromJson(json2);
//        assertFalse(response2.success());
//    }
//
//    @Test
//    void removeSiteDoesNotExist(){
//        Site site3 = new Site("TODO: INSERT NAME HERE", "name a", "zone a", "123456","bob", Site.SiteType.BRANCH);
//        String json1 = rms.removeSite(site3.toJson());
//        Response response1 = Response.fromJson(json1);
//        assertFalse(response1.success());
//    }
//
//    @Test
//    void updateSite() {
//        Site siteUpdate = new Site("TODO: INSERT NAME HERE", site.address(), site.transportZone(), "123456981251","new bob", Site.SiteType.BRANCH);
//
//        String json1 = rms.updateSite(siteUpdate.toJson());
//        Response response1 = Response.fromJson(json1);
//        assertTrue(response1.success(),response1.message());
//
//        String json2 = rms.getSite(Site.getLookupObject(site.name()).toJson());
//        Response response2 = Response.fromJson(json2);
//        assertTrue(response2.success(),response2.message());
//        Site siteToCheck = response2.data(Site.class);
//        assertEquals(siteUpdate.transportZone(), siteToCheck.transportZone());
//        assertEquals(siteUpdate.address(), siteToCheck.address());
//        assertEquals(siteUpdate.phoneNumber(), siteToCheck.phoneNumber());
//        assertEquals(siteUpdate.contactName(), siteToCheck.contactName());
//        assertEquals(siteUpdate.siteType(), siteToCheck.siteType());
//    }
//
//    @Test
//    void updateSiteDoesNotExist(){
//        Site site3 = new Site("TODO: INSERT NAME HERE", "name a", "zone a", "123456","bob", Site.SiteType.BRANCH);
//        String json1 = rms.updateSite(site3.toJson());
//        Response response1 = Response.fromJson(json1);
//        assertFalse(response1.success());
//    }
//
//    @Test
//    void getSite() {
//        String responseJson = rms.getSite(Site.getLookupObject(site.name()).toJson());
//        Response response = Response.fromJson(responseJson);
//        assertTrue(response.success(),response.message());
//        Site siteToCheck = response.data(Site.class);
//        assertEquals(site.transportZone(), siteToCheck.transportZone());
//        assertEquals(site.address(), siteToCheck.address());
//        assertEquals(site.phoneNumber(), siteToCheck.phoneNumber());
//        assertEquals(site.contactName(), siteToCheck.contactName());
//        assertEquals(site.siteType(), siteToCheck.siteType());
//    }
//
//    @Test
//    void getSiteDoesNotExist(){
//        Site site3 = Site.getLookupObject("name a");
//        String responseJson = rms.getSite(site3.toJson());
//        Response response = Response.fromJson(responseJson);
//        assertFalse(response.success());
//    }
//
//    @Test
//    void getAllSites() {
//        //generate more sites
//        for (int i = 0; i < 20; i++) {
//            Site site = new Site("TODO: INSERT NAME HERE", "name" + i, "abc" + i, "123456","bob", Site.SiteType.BRANCH);
//            rms.addSite(site.toJson());
//        }
//
//        String responseJson = rms.getAllSites();
//        Response response = Response.fromJson(responseJson);
//        assertTrue(response.success(),response.message());
//
//        assertEquals(21, Site.listFromJson(response.data()).size());
//    }

    private void assertItemListDeepEquals(ItemList itemList1, ItemList itemList2) {
        assertEquals(itemList1.id(), itemList2.id());
        assertEquals(itemList1.load(), itemList2.load());
        assertEquals(itemList1.unload(), itemList2.unload());
    }

    private void assertDriverDeepEquals(Driver driver1, Driver driver2) {
        assertEquals(driver1.id(), driver2.id());
        assertEquals(driver1.name(), driver2.name());
        assertEquals(driver1.licenseType(), driver2.licenseType());
    }

    private void assertSiteDeepEquals(Site site1, Site site2) {
        assertEquals(site1.name(), site2.name());
        assertEquals(site1.address(), site2.address());
        assertEquals(site1.transportZone(), site2.transportZone());
        assertEquals(site1.phoneNumber(), site2.phoneNumber());
        assertEquals(site1.contactName(), site2.contactName());
        assertEquals(site1.siteType(), site2.siteType());
    }

    private void assertTruckDeepEquals(Truck truck1, Truck truck2) {
        assertEquals(truck1.id(), truck2.id());
        assertEquals(truck1.model(), truck2.model());
        assertEquals(truck1.baseWeight(), truck2.baseWeight());
        assertEquals(truck1.maxWeight(), truck2.maxWeight());
        assertEquals(truck1.coolingCapacity(), truck2.coolingCapacity());
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



}