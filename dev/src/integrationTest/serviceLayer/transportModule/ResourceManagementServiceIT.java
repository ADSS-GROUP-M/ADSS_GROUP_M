package serviceLayer.transportModule;

import businessLayer.employeeModule.Employee;
import dataAccessLayer.DalFactory;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.employeeModule.EmployeeDAO;
import objects.transportObjects.Driver;
import objects.transportObjects.Site;
import objects.transportObjects.Truck;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serviceLayer.ServiceFactory;
import utils.Response;

import java.time.LocalDate;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class ResourceManagementServiceIT {

    private ResourceManagementService rms;
    private Driver driver;
    private Site site;
    private Truck truck;
    private EmployeeDAO empDao;

    @BeforeEach
    void setUp() {
        rms = new ServiceFactory(TESTING_DB_NAME).resourceManagementService();
        //add drivers
        DalFactory dalFactory;
        try {
            dalFactory = new DalFactory(TESTING_DB_NAME);
            empDao = dalFactory.employeeDAO();
            empDao.insert(new Employee("name","123456789","Poalim",50, LocalDate.of(1999,10,10),"conditions","details"));
        } catch (DalException e) {
            throw new RuntimeException(e);
        }

        driver = new Driver("123456789","John", Driver.LicenseType.C3);
        rms.addDriver(driver.toJson());

        //add sites

        site = new Site("TODO: INSERT NAME HERE", "123 main st", "zone a", "(555) 123-4567", "john smith", Site.SiteType.BRANCH);
        rms.addSite(site.toJson());

        //add trucks
        truck = new Truck("abc123", "ford", 1500, 10000, Truck.CoolingCapacity.FROZEN);
        rms.addTruck(truck.toJson());

    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    void addDriver() {
        try {
            empDao.insert(new Employee("Jim","111111111","Poalim",50, LocalDate.of(1999,10,10),"conditions","details"));
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
        Driver driver3 = new Driver("111111111","Jim", Driver.LicenseType.C3);
        rms.addDriver(driver3.toJson());
        String responseJson = rms.getDriver(Driver.getLookupObject(driver3.id()).toJson());
        Response response = Response.fromJson(responseJson);
        assertTrue(response.success(),response.message());
        Driver driverToCheck = response.data(Driver.class);
        assertEquals(driver3.id(), driverToCheck.id());
        assertEquals(driver3.name(), driverToCheck.name());
        assertEquals(driver3.licenseType(), driverToCheck.licenseType());
    }

    @Test
    void addDriverAlreadyExists(){
        String json1 = rms.addDriver(driver.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void removeDriver() {
        String json1 = rms.removeDriver(driver.toJson());
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.success(),response1.message());
        String responseJson = rms.getDriver(Driver.getLookupObject(driver.id()).toJson());
        Response response = Response.fromJson(responseJson);
        assertFalse(response.success());
    }

    @Test
    void removeDriverDoesNotExist(){
        Driver driver3 = Driver.getLookupObject("111111111");
        String json1 = rms.removeDriver(driver3.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void updateDriver() {
        Driver driverUpdate = new Driver(driver.id(), driver.name(), Driver.LicenseType.C1);
        String json1 = rms.updateDriver(driverUpdate.toJson());
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.success(),response1.message());

        String json2 = rms.getDriver(Driver.getLookupObject(driver.id()).toJson());
        Response response2 = Response.fromJson(json2);
        assertTrue(response2.success(),response2.message());
        Driver driverToCheck = response2.data(Driver.class);
        assertEquals(driverUpdate.id(), driverToCheck.id());
        assertEquals(driverUpdate.name(), driverToCheck.name());
        assertEquals(driverUpdate.licenseType(), driverToCheck.licenseType());
    }

    @Test
    void updateDriverDoesNotExist(){
        Driver driver3 = new Driver ("111111111", "Jim", Driver.LicenseType.C3);
        String json1 = rms.updateDriver(driver3.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void getDriver() {
        String responseJson = rms.getDriver(Driver.getLookupObject(driver.id()).toJson());
        Response response = Response.fromJson(responseJson);
        assertTrue(response.success(),response.message());
        Driver driverToCheck = response.data(Driver.class);
        assertEquals(driver.id(), driverToCheck.id());
        assertEquals(driver.name(), driverToCheck.name());
        assertEquals(driver.licenseType(), driverToCheck.licenseType());
    }

    @Test
    void getDriverDoesNotExist(){
        Driver driver3 = Driver.getLookupObject("111111111");
        String responseJson = rms.getDriver(driver3.toJson());
        Response response = Response.fromJson(responseJson);
        assertFalse(response.success());
    }

    @Test
    void getAllDrivers() {
        //generate more drivers
        for (int i = 0; i < 20; i++) {
            Driver driver = new Driver(String.valueOf(i), "driver" + i, Driver.LicenseType.C3);
            try {
                empDao.insert(new Employee("driver" + i,String.valueOf(i),"Poalim",50, LocalDate.of(1999,10,10),"conditions","details"));
            } catch (DalException e) {
                throw new RuntimeException(e);
            }
            rms.addDriver(driver.toJson());
        }

        String responseJson = rms.getAllDrivers();
        Response response = Response.fromJson(responseJson);
        assertTrue(response.success(),response.message());

        assertEquals(21, Driver.listFromJson(response.data()).size());
    }

    @Test
    void addTruck() {
        Truck truck3 = new Truck("ghi789", "chevy", 2000, 15000, Truck.CoolingCapacity.FROZEN);

        String json1 = rms.addTruck(truck3.toJson());
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.success(),response1.message());

        String json2 = rms.getTruck(Truck.getLookupObject(truck3.id()).toJson());
        Response response2 = Response.fromJson(json2);
        assertTrue(response2.success(),response2.message());
        Truck truckToCheck = response2.data(Truck.class);
        assertEquals(truck3.id(), truckToCheck.id());
        assertEquals(truck3.model(), truckToCheck.model());
        assertEquals(truck3.coolingCapacity(), truckToCheck.coolingCapacity());
    }

    @Test
    void addTruckAlreadyExists(){
        String json1 = rms.addTruck(truck.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void addTruckNegativeBaseWeight(){
        Truck truck3 = new Truck("ghi789", "chevy", -2000, 15000, Truck.CoolingCapacity.FROZEN);

        String json1 = rms.addTruck(truck3.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void addTruckNegativeMaxWeight(){
        Truck truck3 = new Truck("ghi789", "chevy", 2000, -15000, Truck.CoolingCapacity.FROZEN);

        String json1 = rms.addTruck(truck3.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void addTruckBaseWeightGreaterThanMaxWeight(){
        Truck truck3 = new Truck("ghi789", "chevy", 2000, 1500, Truck.CoolingCapacity.FROZEN);

        String json1 = rms.addTruck(truck3.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void removeTruck() {
        String json1 = rms.removeTruck(truck.toJson());
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.success(),response1.message());

        String json2 = rms.getTruck(Truck.getLookupObject(truck.id()).toJson());
        Response response2 = Response.fromJson(json2);
        assertFalse(response2.success());
    }

    @Test
    void removeTruckDoesNotExist(){
        Truck truck3 = Truck.getLookupObject("ghi789");
        String json1 = rms.removeTruck(truck3.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void updateTruck() {
        Truck truckUpdate = new Truck(truck.id(), truck.model(), 2000, 15000, Truck.CoolingCapacity.NONE);

        String json1 = rms.updateTruck(truckUpdate.toJson());
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.success(),response1.message());

        String json2 = rms.getTruck(Truck.getLookupObject(truck.id()).toJson());
        Response response2 = Response.fromJson(json2);
        assertTrue(response2.success(),response2.message());
        Truck truckToCheck = response2.data(Truck.class);
        assertEquals(truckUpdate.id(), truckToCheck.id());
        assertEquals(truckUpdate.model(), truckToCheck.model());
        assertEquals(truckUpdate.coolingCapacity(), truckToCheck.coolingCapacity());
    }

    @Test
    void updateTruckDoesNotExist(){
        Truck truck3 = new Truck("ghi789", "chevy", 2000, 15000, Truck.CoolingCapacity.FROZEN);
        String json1 = rms.updateTruck(truck3.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void updateTruckNegativeBaseWeight(){
        Truck truckUpdate = new Truck("ggghhh222", truck.model(), -2000, 15000, Truck.CoolingCapacity.NONE);

        String json1 = rms.updateTruck(truckUpdate.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void updateTruckNegativeMaxWeight(){
        Truck truckUpdate = new Truck("ggghhh222", truck.model(), 2000, -15000, Truck.CoolingCapacity.NONE);

        String json1 = rms.updateTruck(truckUpdate.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void updateTruckBaseWeightGreaterThanMaxWeight(){
        Truck truckUpdate = new Truck("ggghhh222", truck.model(), 2000, 1500, Truck.CoolingCapacity.NONE);

        String json1 = rms.updateTruck(truckUpdate.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void getTruck() {
        String responseJson = rms.getTruck(Truck.getLookupObject(truck.id()).toJson());
        Response response = Response.fromJson(responseJson);
        assertTrue(response.success(),response.message());
        Truck truckToCheck = response.data(Truck.class);
        assertEquals(truck.id(), truckToCheck.id());
        assertEquals(truck.model(), truckToCheck.model());
        assertEquals(truck.coolingCapacity(), truckToCheck.coolingCapacity());
    }

    @Test
    void getTruckDoesNotExist(){
        Truck truck3 = Truck.getLookupObject("ghi789");
        String responseJson = rms.getTruck(truck3.toJson());
        Response response = Response.fromJson(responseJson);
        assertFalse(response.success());
    }

    @Test
    void getAllTrucks() {
        //generate more trucks
        for (int i = 0; i < 20; i++) {
            Truck truck = new Truck("abc" + i, "truck" + i, 2000, 15000, Truck.CoolingCapacity.FROZEN);
            rms.addTruck(truck.toJson());
        }

        String responseJson = rms.getAllTrucks();
        Response response = Response.fromJson(responseJson);
        assertTrue(response.success(),response.message());

        assertEquals(21, Truck.listFromJson(response.data()).size());
    }

    @Test
    void addSite() {
        Site site3 = new Site("TODO: INSERT NAME HERE", "address a", "zone a", "123456","bob", Site.SiteType.BRANCH);

        String json1 = rms.addSite(site3.toJson());
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.success(),response1.message());

        String json2 = rms.getSite(Site.getLookupObject(site3.address()).toJson());
        Response response2 = Response.fromJson(json2);
        assertTrue(response2.success(),response2.message());
        Site siteToCheck = response2.data(Site.class);
        assertEquals(site3.transportZone(), siteToCheck.transportZone());
        assertEquals(site3.address(), siteToCheck.address());
        assertEquals(site3.phoneNumber(), siteToCheck.phoneNumber());
        assertEquals(site3.contactName(), siteToCheck.contactName());
    }

    @Test
    void addSiteAlreadyExists(){
        String json1 = rms.addSite(site.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void removeSite() {
        String json1 = rms.removeSite(site.toJson());
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.success(),response1.message());

        String json2 = rms.getSite(Site.getLookupObject(site.address()).toJson());
        Response response2 = Response.fromJson(json2);
        assertFalse(response2.success());
    }

    @Test
    void removeSiteDoesNotExist(){
        Site site3 = new Site("TODO: INSERT NAME HERE", "address a", "zone a", "123456","bob", Site.SiteType.BRANCH);
        String json1 = rms.removeSite(site3.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void updateSite() {
        Site siteUpdate = new Site("TODO: INSERT NAME HERE", site.address(), site.transportZone(), "123456981251","new bob", Site.SiteType.BRANCH);

        String json1 = rms.updateSite(siteUpdate.toJson());
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.success(),response1.message());

        String json2 = rms.getSite(Site.getLookupObject(site.address()).toJson());
        Response response2 = Response.fromJson(json2);
        assertTrue(response2.success(),response2.message());
        Site siteToCheck = response2.data(Site.class);
        assertEquals(siteUpdate.transportZone(), siteToCheck.transportZone());
        assertEquals(siteUpdate.address(), siteToCheck.address());
        assertEquals(siteUpdate.phoneNumber(), siteToCheck.phoneNumber());
        assertEquals(siteUpdate.contactName(), siteToCheck.contactName());
        assertEquals(siteUpdate.siteType(), siteToCheck.siteType());
    }

    @Test
    void updateSiteDoesNotExist(){
        Site site3 = new Site("TODO: INSERT NAME HERE", "address a", "zone a", "123456","bob", Site.SiteType.BRANCH);
        String json1 = rms.updateSite(site3.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void getSite() {
        String responseJson = rms.getSite(Site.getLookupObject(site.address()).toJson());
        Response response = Response.fromJson(responseJson);
        assertTrue(response.success(),response.message());
        Site siteToCheck = response.data(Site.class);
        assertEquals(site.transportZone(), siteToCheck.transportZone());
        assertEquals(site.address(), siteToCheck.address());
        assertEquals(site.phoneNumber(), siteToCheck.phoneNumber());
        assertEquals(site.contactName(), siteToCheck.contactName());
        assertEquals(site.siteType(), siteToCheck.siteType());
    }

    @Test
    void getSiteDoesNotExist(){
        Site site3 = Site.getLookupObject("address a");
        String responseJson = rms.getSite(site3.toJson());
        Response response = Response.fromJson(responseJson);
        assertFalse(response.success());
    }

    @Test
    void getAllSites() {
        //generate more sites
        for (int i = 0; i < 20; i++) {
            Site site = new Site("TODO: INSERT NAME HERE", "address" + i, "abc" + i, "123456","bob", Site.SiteType.BRANCH);
            rms.addSite(site.toJson());
        }

        String responseJson = rms.getAllSites();
        Response response = Response.fromJson(responseJson);
        assertTrue(response.success(),response.message());

        assertEquals(21, Site.listFromJson(response.data()).size());
    }
}