package TransportModule.ServiceLayer;

import TransportModule.BusinessLayer.Records.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class TransportsServiceTest {

    @BeforeEach
    void setUp() {
        Site site1 = new Site("zone a", "123 main st", "(555) 123-4567", "john smith", Site.SiteType.BRANCH);
        Site site2 = new Site("zone b", "456 oak ave", "(555) 234-5678", "jane doe", Site.SiteType.LOGISTICAL_CENTER);
        Driver driver1 = new Driver(1234, "megan smith", "class b");
        Truck truck1 = new Truck("abc123", "ford", 1500, 10000);
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

        TransportsService ts = ModuleFactory.getInstance().getTransportsService();
        Transport transport1 = new Transport(
                1,
                site1,
                new LinkedList<Site>(Arrays.asList(site2)),
                hm,
                truck1.id(),
                driver1.id(),
                LocalDateTime.of(2020, 1, 1, 0, 0),
                100
        );
        String json = ts.createTransport(JSON.serialize(transport1));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createTransport() {
    }

    @Test
    void updateTransport() {
        TransportsService ts = ModuleFactory.getInstance().getTransportsService();
        String json = ts.updateTransport(JSON.serialize(new Transport(
                1,
                new Site("zone a", "123 main st", "(555) 123-4567", "john smith", Site.SiteType.BRANCH),
                new LinkedList<Site>(Arrays.asList(new Site("zone b", "456 oak ave", "(555) 234-5678", "jane doe", Site.SiteType.LOGISTICAL_CENTER))),
                new HashMap<Site, ItemList>(),
                "abc123",
                1234,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                2000
        )));
        Response<String> response = JSON.deserialize(json, Response.class);
        assertTrue(response.isSuccess());
    }

    @Test
    void removeTransport() {
    }

    @Test
    void getTransport() {
    }

    @Test
    void getAllTransports() {
    }
}