package transportModule.serviceLayer;

import transportModule.businessLayer.records.Driver;
import transportModule.businessLayer.records.Site;
import transportModule.businessLayer.records.Truck;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

    class ResourceManagementServiceTest {

        ResourceManagementService rms;
        private static final Type responseDriverType = new TypeToken<Response<Driver>>(){}.getType();
        private static final Type responseTruckType = new TypeToken<Response<Truck>>(){}.getType();
        private static final Type responseSiteType = new TypeToken<Response<Site>>(){}.getType();
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
        Response<Driver> response = JSON.deserialize(responseJson, responseDriverType);
        assertTrue(response.isSuccess());
        assertEquals(driver3.id(), response.getData().id());
        assertEquals(driver3.name(), response.getData().name());
        assertEquals(driver3.licenseType(), response.getData().licenseType());
    }

    @Test
    void removeDriver() {
        String json1 = rms.removeDriver(JSON.serialize(driver));
        Response<String> response1 = JSON.deserialize(json1, responseDriverType);
        assertTrue(response1.isSuccess());
        String responseJson = rms.getDriver(JSON.serialize(Driver.getLookupObject(driver.id())));
        Response<Driver> response = JSON.deserialize(responseJson, responseDriverType);
        assertFalse(response.isSuccess());
    }

    @Test
    void updateDriver() {
        Driver driverUpdate = new Driver(driver.id(), driver.name(), Driver.LicenseType.C1);
        String json1 = rms.updateDriver(JSON.serialize(driverUpdate));
        Response<String> response1 = JSON.deserialize(json1, responseDriverType);
        assertTrue(response1.isSuccess());

        String json2 = rms.getDriver(JSON.serialize(Driver.getLookupObject(driver.id())));
        Response<Driver> response2 = JSON.deserialize(json2, responseDriverType);
        assertTrue(response2.isSuccess());
        assertEquals(driverUpdate.id(), response2.getData().id());
        assertEquals(driverUpdate.name(), response2.getData().name());
        assertEquals(driverUpdate.licenseType(), response2.getData().licenseType());
    }

    @Test
    void getDriver() {
        String responseJson = rms.getDriver(JSON.serialize(Driver.getLookupObject(driver.id())));
        Response<Driver> response = JSON.deserialize(responseJson, responseDriverType);
        assertTrue(response.isSuccess());
        assertEquals(driver.id(), response.getData().id());
        assertEquals(driver.name(), response.getData().name());
        assertEquals(driver.licenseType(), response.getData().licenseType());
    }

    @Test
    void getAllDrivers() {
        //generate more drivers
        for (int i = 0; i < 20; i++) {
            Driver driver = new Driver(i, "driver" + i, Driver.LicenseType.C3);
            rms.addDriver(JSON.serialize(driver));
        }

        String responseJson = rms.getAllDrivers();
        Type type = new TypeToken<Response<LinkedList<Driver>>>(){}.getType();
        Response<LinkedList<Driver>> response = JSON.deserialize(responseJson,type);
        assertTrue(response.isSuccess());
        assertEquals(21, response.getData().size());
    }

    @Test
    void addTruck() {
        Truck truck3 = new Truck("ghi789", "chevy", 2000, 15000, Truck.CoolingCapacity.FROZEN);

        String json1 = rms.addTruck(JSON.serialize(truck3));
        Response<String> response1 = JSON.deserialize(json1, responseTruckType);
        assertTrue(response1.isSuccess());

        String json2 = rms.getTruck(JSON.serialize(Truck.getLookupObject(truck3.id())));
        Response<Truck> response2 = JSON.deserialize(json2, responseTruckType);
        assertTrue(response2.isSuccess());
        assertEquals(truck3.id(), response2.getData().id());
        assertEquals(truck3.model(), response2.getData().model());
        assertEquals(truck3.coolingCapacity(), response2.getData().coolingCapacity());
    }

    @Test
    void removeTruck() {
        String json1 = rms.removeTruck(JSON.serialize(truck));
        Response<String> response1 = JSON.deserialize(json1, responseTruckType);
        assertTrue(response1.isSuccess());

        String json2 = rms.getTruck(JSON.serialize(Truck.getLookupObject(truck.id())));
        Response<Truck> response2 = JSON.deserialize(json2, responseTruckType);
        assertFalse(response2.isSuccess());
    }

    @Test
    void updateTruck() {
        Truck truckUpdate = new Truck(truck.id(), truck.model(), 2000, 15000, Truck.CoolingCapacity.NONE);

        String json1 = rms.updateTruck(JSON.serialize(truckUpdate));
        Response<String> response1 = JSON.deserialize(json1, responseTruckType);
        assertTrue(response1.isSuccess());

        String json2 = rms.getTruck(JSON.serialize(Truck.getLookupObject(truck.id())));
        Response<Truck> response2 = JSON.deserialize(json2, responseTruckType);
        assertTrue(response2.isSuccess());
        assertEquals(truckUpdate.id(), response2.getData().id());
        assertEquals(truckUpdate.model(), response2.getData().model());
        assertEquals(truckUpdate.coolingCapacity(), response2.getData().coolingCapacity());
    }

    @Test
    void getTruck() {
        String responseJson = rms.getTruck(JSON.serialize(Truck.getLookupObject(truck.id())));
        Response<Truck> response = JSON.deserialize(responseJson, responseTruckType);
        assertTrue(response.isSuccess());
        assertEquals(truck.id(), response.getData().id());
        assertEquals(truck.model(), response.getData().model());
        assertEquals(truck.coolingCapacity(), response.getData().coolingCapacity());
    }

    @Test
    void getAllTrucks() {
        //generate more trucks
        for (int i = 0; i < 20; i++) {
            Truck truck = new Truck("abc" + i, "truck" + i, 2000, 15000, Truck.CoolingCapacity.FROZEN);
            rms.addTruck(JSON.serialize(truck));
        }

        String responseJson = rms.getAllTrucks();
        Type type = new TypeToken<Response<LinkedList<Truck>>>(){}.getType();
        Response<LinkedList<Truck>> response = JSON.deserialize(responseJson,type);
        assertTrue(response.isSuccess());
        assertEquals(21, response.getData().size());
    }

    @Test
    void addSite() {
        Site site3 = new Site("zone a", "address a", "123456","bob", Site.SiteType.BRANCH);

        String json1 = rms.addSite(JSON.serialize(site3));
        Response<String> response1 = JSON.deserialize(json1, responseSiteType);
        assertTrue(response1.isSuccess());

        String json2 = rms.getSite(JSON.serialize(Site.getLookupObject(site3.address())));
        Response<Site> response2 = JSON.deserialize(json2, responseSiteType);
        assertTrue(response2.isSuccess());
        assertEquals(site3.transportZone(), response2.getData().transportZone());
        assertEquals(site3.address(), response2.getData().address());
        assertEquals(site3.phoneNumber(), response2.getData().phoneNumber());
        assertEquals(site3.contactName(), response2.getData().contactName());
    }

    @Test
    void removeSite() {
        String json1 = rms.removeSite(JSON.serialize(site));
        Response<String> response1 = JSON.deserialize(json1, responseSiteType);
        assertTrue(response1.isSuccess());

        String json2 = rms.getSite(JSON.serialize(Site.getLookupObject(site.address())));
        Response<Site> response2 = JSON.deserialize(json2, responseSiteType);
        assertFalse(response2.isSuccess());
    }

    @Test
    void updateSite() {
        Site siteUpdate = new Site(site.transportZone(), site.address(), "123456981251","new bob", Site.SiteType.BRANCH);

        String json1 = rms.updateSite(JSON.serialize(siteUpdate));
        Response<String> response1 = JSON.deserialize(json1, responseSiteType);
        assertTrue(response1.isSuccess());

        String json2 = rms.getSite(JSON.serialize(Site.getLookupObject(site.address())));
        Response<Site> response2 = JSON.deserialize(json2, responseSiteType);
        assertTrue(response2.isSuccess());
        assertEquals(siteUpdate.transportZone(), response2.getData().transportZone());
        assertEquals(siteUpdate.address(), response2.getData().address());
        assertEquals(siteUpdate.phoneNumber(), response2.getData().phoneNumber());
        assertEquals(siteUpdate.contactName(), response2.getData().contactName());
        assertEquals(siteUpdate.siteType(), response2.getData().siteType());
    }

    @Test
    void getSite() {
        String responseJson = rms.getSite(JSON.serialize(Site.getLookupObject(site.address())));
        Response<Site> response = JSON.deserialize(responseJson, responseSiteType);
        assertTrue(response.isSuccess());
        assertEquals(site.transportZone(), response.getData().transportZone());
        assertEquals(site.address(), response.getData().address());
        assertEquals(site.phoneNumber(), response.getData().phoneNumber());
        assertEquals(site.contactName(), response.getData().contactName());
        assertEquals(site.siteType(), response.getData().siteType());
    }

    @Test
    void getAllSites() {
        //generate more sites
        for (int i = 0; i < 20; i++) {
            Site site = new Site("abc" + i, "address" + i, "123456","bob", Site.SiteType.BRANCH);
            rms.addSite(JSON.serialize(site));
        }

        String responseJson = rms.getAllSites();
        Type type = new TypeToken<Response<LinkedList<Site>>>(){}.getType();
        Response<LinkedList<Site>> response = JSON.deserialize(responseJson,type);
        assertTrue(response.isSuccess());
        assertEquals(21, response.getData().size());
    }
}