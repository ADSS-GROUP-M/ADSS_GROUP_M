package transportModule.backend.serviceLayer;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transportModule.records.Driver;
import transportModule.records.Site;
import transportModule.records.Truck;
import utils.JSON;
import utils.Response;

import java.lang.reflect.Type;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceManagementServiceTest {

        ResourceManagementService rms;
//        private static final Type responseDriverType = new TypeToken<Response<Driver>>(){}.getType();
//        private static final Type responseTruckType = new TypeToken<Response<Truck>>(){}.getType();
//        private static final Type responseSiteType = new TypeToken<Response<Site>>(){}.getType();
        private Driver driver;
        private Site site;
        private Truck truck;

    @BeforeEach
    void setUp() {
        rms = new ModuleFactory().getResourceManagementService();

        //add drivers
        driver = new Driver(123456789,"John", Driver.LicenseType.C3);
        rms.addDriver(JSON.serialize(driver));

        //add sites

        site = new Site("zone a", "123 main st", "(555) 123-4567", "john smith", Site.SiteType.BRANCH);
        rms.addSite(JSON.serialize(site));

        //add trucks
        truck = new Truck("abc123", "ford", 1500, 10000, Truck.CoolingCapacity.FROZEN);
        rms.addTruck(JSON.serialize(truck));

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void addDriver() {
        Driver driver3 = new Driver(111111111,"Jim", Driver.LicenseType.C3);
        rms.addDriver(JSON.serialize(driver3));
        String responseJson = rms.getDriver(JSON.serialize(Driver.getLookupObject(driver3.id())));
        Response response = Response.fromJson(responseJson);
        assertTrue(response.isSuccess());
        Driver driverToCheck = response.getData(Driver.class);
        assertEquals(driver3.id(), driverToCheck.id());
        assertEquals(driver3.name(), driverToCheck.name());
        assertEquals(driver3.licenseType(), driverToCheck.licenseType());
    }

    @Test
    void removeDriver() {
        String json1 = rms.removeDriver(JSON.serialize(driver));
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.isSuccess());
        String responseJson = rms.getDriver(JSON.serialize(Driver.getLookupObject(driver.id())));
        Response response = Response.fromJson(responseJson);
        assertFalse(response.isSuccess());
    }

    @Test
    void updateDriver() {
        Driver driverUpdate = new Driver(driver.id(), driver.name(), Driver.LicenseType.C1);
        String json1 = rms.updateDriver(JSON.serialize(driverUpdate));
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.isSuccess());

        String json2 = rms.getDriver(JSON.serialize(Driver.getLookupObject(driver.id())));
        Response response2 = Response.fromJson(json2);
        assertTrue(response2.isSuccess());
        Driver driverToCheck = response2.getData(Driver.class);
        assertEquals(driverUpdate.id(), driverToCheck.id());
        assertEquals(driverUpdate.name(), driverToCheck.name());
        assertEquals(driverUpdate.licenseType(), driverToCheck.licenseType());
    }

    @Test
    void getDriver() {
        String responseJson = rms.getDriver(JSON.serialize(Driver.getLookupObject(driver.id())));
        Response response = Response.fromJson(responseJson);
        assertTrue(response.isSuccess());
        Driver driverToCheck = response.getData(Driver.class);
        assertEquals(driver.id(), driverToCheck.id());
        assertEquals(driver.name(), driverToCheck.name());
        assertEquals(driver.licenseType(), driverToCheck.licenseType());
    }

    @Test
    void getAllDrivers() {
        //generate more drivers
        for (int i = 0; i < 20; i++) {
            Driver driver = new Driver(i, "driver" + i, Driver.LicenseType.C3);
            rms.addDriver(JSON.serialize(driver));
        }

        String responseJson = rms.getAllDrivers();
        Response response = Response.fromJson(responseJson);
        assertTrue(response.isSuccess());
        Type type = new TypeToken<LinkedList<Driver>>(){}.getType();
        assertEquals(21, response.<LinkedList<Driver>>getData(type).size());
    }

    @Test
    void addTruck() {
        Truck truck3 = new Truck("ghi789", "chevy", 2000, 15000, Truck.CoolingCapacity.FROZEN);

        String json1 = rms.addTruck(JSON.serialize(truck3));
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.isSuccess());

        String json2 = rms.getTruck(JSON.serialize(Truck.getLookupObject(truck3.id())));
        Response response2 = Response.fromJson(json2);
        assertTrue(response2.isSuccess());
        Truck truckToCheck = response2.getData(Truck.class);
        assertEquals(truck3.id(), truckToCheck.id());
        assertEquals(truck3.model(), truckToCheck.model());
        assertEquals(truck3.coolingCapacity(), truckToCheck.coolingCapacity());
    }

    @Test
    void removeTruck() {
        String json1 = rms.removeTruck(JSON.serialize(truck));
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.isSuccess());

        String json2 = rms.getTruck(JSON.serialize(Truck.getLookupObject(truck.id())));
        Response response2 = Response.fromJson(json2);
        assertFalse(response2.isSuccess());
    }

    @Test
    void updateTruck() {
        Truck truckUpdate = new Truck(truck.id(), truck.model(), 2000, 15000, Truck.CoolingCapacity.NONE);

        String json1 = rms.updateTruck(JSON.serialize(truckUpdate));
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.isSuccess());

        String json2 = rms.getTruck(JSON.serialize(Truck.getLookupObject(truck.id())));
        Response response2 = Response.fromJson(json2);
        assertTrue(response2.isSuccess());
        Truck truckToCheck = response2.getData(Truck.class);
        assertEquals(truckUpdate.id(), truckToCheck.id());
        assertEquals(truckUpdate.model(), truckToCheck.model());
        assertEquals(truckUpdate.coolingCapacity(), truckToCheck.coolingCapacity());
    }

    @Test
    void getTruck() {
        String responseJson = rms.getTruck(JSON.serialize(Truck.getLookupObject(truck.id())));
        Response response = Response.fromJson(responseJson);
        assertTrue(response.isSuccess());
        Truck truckToCheck = response.getData(Truck.class);
        assertEquals(truck.id(), truckToCheck.id());
        assertEquals(truck.model(), truckToCheck.model());
        assertEquals(truck.coolingCapacity(), truckToCheck.coolingCapacity());
    }

    @Test
    void getAllTrucks() {
        //generate more trucks
        for (int i = 0; i < 20; i++) {
            Truck truck = new Truck("abc" + i, "truck" + i, 2000, 15000, Truck.CoolingCapacity.FROZEN);
            rms.addTruck(JSON.serialize(truck));
        }

        String responseJson = rms.getAllTrucks();
        Response response = Response.fromJson(responseJson);
        assertTrue(response.isSuccess());
        Type type = new TypeToken<LinkedList<Truck>>(){}.getType();
        assertEquals(21, response.<LinkedList<Truck>>getData(type).size());
    }

    @Test
    void addSite() {
        Site site3 = new Site("zone a", "address a", "123456","bob", Site.SiteType.BRANCH);

        String json1 = rms.addSite(JSON.serialize(site3));
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.isSuccess());

        String json2 = rms.getSite(JSON.serialize(Site.getLookupObject(site3.address())));
        Response response2 = Response.fromJson(json2);
        assertTrue(response2.isSuccess());
        Site siteToCheck = response2.getData(Site.class);
        assertEquals(site3.transportZone(), siteToCheck.transportZone());
        assertEquals(site3.address(), siteToCheck.address());
        assertEquals(site3.phoneNumber(), siteToCheck.phoneNumber());
        assertEquals(site3.contactName(), siteToCheck.contactName());
    }

    @Test
    void removeSite() {
        String json1 = rms.removeSite(JSON.serialize(site));
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.isSuccess());

        String json2 = rms.getSite(JSON.serialize(Site.getLookupObject(site.address())));
        Response response2 = Response.fromJson(json2);
        assertFalse(response2.isSuccess());
    }

    @Test
    void updateSite() {
        Site siteUpdate = new Site(site.transportZone(), site.address(), "123456981251","new bob", Site.SiteType.BRANCH);

        String json1 = rms.updateSite(JSON.serialize(siteUpdate));
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.isSuccess());

        String json2 = rms.getSite(JSON.serialize(Site.getLookupObject(site.address())));
        Response response2 = Response.fromJson(json2);
        assertTrue(response2.isSuccess());
        Site siteToCheck = response2.getData(Site.class);
        assertEquals(siteUpdate.transportZone(), siteToCheck.transportZone());
        assertEquals(siteUpdate.address(), siteToCheck.address());
        assertEquals(siteUpdate.phoneNumber(), siteToCheck.phoneNumber());
        assertEquals(siteUpdate.contactName(), siteToCheck.contactName());
        assertEquals(siteUpdate.siteType(), siteToCheck.siteType());
    }

    @Test
    void getSite() {
        String responseJson = rms.getSite(JSON.serialize(Site.getLookupObject(site.address())));
        Response response = Response.fromJson(responseJson);
        assertTrue(response.isSuccess());
        Site siteToCheck = response.getData(Site.class);
        assertEquals(site.transportZone(), siteToCheck.transportZone());
        assertEquals(site.address(), siteToCheck.address());
        assertEquals(site.phoneNumber(), siteToCheck.phoneNumber());
        assertEquals(site.contactName(), siteToCheck.contactName());
        assertEquals(site.siteType(), siteToCheck.siteType());
    }

    @Test
    void getAllSites() {
        //generate more sites
        for (int i = 0; i < 20; i++) {
            Site site = new Site("abc" + i, "address" + i, "123456","bob", Site.SiteType.BRANCH);
            rms.addSite(JSON.serialize(site));
        }

        String responseJson = rms.getAllSites();
        Response response = Response.fromJson(responseJson);
        assertTrue(response.isSuccess());
        Type type = new TypeToken<LinkedList<Site>>(){}.getType();
        assertEquals(21, response.<LinkedList<Site>>getData(type).size());
    }
}