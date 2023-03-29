package TransportModuleTest.ServiceLayerTest;

import TransportModule.BusinessLayer.ItemList;
import TransportModule.BusinessLayer.Site;
import TransportModule.BusinessLayer.Transport;
import TransportModule.ServiceLayer.JSON;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class JSONTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void serialize() {
        Site source = new Site("Zone1", "source-address", "054-6666666", "moshe", Site.SiteType.LOGISTICAL_CENTER);
        Site destination = new Site("Zone1", "destination-address", "054-7777777", "yossi", Site.SiteType.BRANCH);
        LinkedList<Site> sites = new LinkedList<>();
        sites.add(source);
        LocalDateTime dateTime = LocalDateTime.of(1977,1,1,12,24);
        HashMap<String,Integer> items1 = new HashMap<>();
        items1.put("itemToLoad", 5);
        HashMap<String,Integer> items2 = new HashMap<>();
        items2.put("itemToUnload", 7);
        ItemList itemList = new ItemList(1,items1,items2);
        HashMap<Site,ItemList> siteItems = new HashMap<>();
        siteItems.put(destination, itemList);
        Transport t = new Transport(1,source,sites,siteItems,1,1,dateTime);
        String json = JSON.serialize(t);
        String output = JSON.serialize(t);
        String expected = """
                 {
                   "id": 1,
                   "source": {
                     "TransportZone": "Zone1",
                     "Address": "source-address",
                     "phoneNumber": "054-6666666",
                     "contactName": "moshe",
                     "siteType": "LOGISTICAL_CENTER"
                   },
                   "destinations": [
                     {
                       "TransportZone": "Zone1",
                       "Address": "source-address",
                       "phoneNumber": "054-6666666",
                       "contactName": "moshe",
                       "siteType": "LOGISTICAL_CENTER"
                     }
                   ],
                   "itemLists": [
                     [
                       {
                         "TransportZone": "Zone1",
                         "Address": "destination-address",
                         "phoneNumber": "054-7777777",
                         "contactName": "yossi",
                         "siteType": "BRANCH"
                       },
                       {
                         "id": 1,
                         "loadingItems": {
                           "itemToLoad": 5
                         },
                         "unloadingItems": {
                           "itemToUnload": 7
                         }
                       }
                     ]
                   ],
                   "truckId": 1,
                   "driverId": 1,
                   "scheduledTime": "1977-01-01T12:24",
                   "weight": 0
                 }""";

        assertEquals(expected,output);
    }

    @org.junit.jupiter.api.Test
    void deserialize() {
        String json = """
                {
                   "id": 1,
                   "source": {
                     "TransportZone": "Zone1",
                     "Address": "source-address",
                     "phoneNumber": "054-6666666",
                     "contactName": "moshe",
                     "siteType": "LOGISTICAL_CENTER"
                   },
                   "destinations": [
                     {
                       "TransportZone": "Zone1",
                       "Address": "source-address",
                       "phoneNumber": "054-6666666",
                       "contactName": "moshe",
                       "siteType": "LOGISTICAL_CENTER"
                     }
                   ],
                   "itemLists": [
                     [
                       {
                         "TransportZone": "Zone1",
                         "Address": "destination-address",
                         "phoneNumber": "054-7777777",
                         "contactName": "yossi",
                         "siteType": "BRANCH"
                       },
                       {
                         "id": 1,
                         "loadingItems": {
                           "itemToLoad": 5
                         },
                         "unloadingItems": {
                           "itemToUnload": 7
                         }
                       }
                     ]
                   ],
                   "truckId": 1,
                   "driverId": 1,
                   "scheduledTime": "1977-01-01T12:24",
                   "weight": 0
                 }""";
        Transport t = JSON.deserialize(json, Transport.class);

        Site source = new Site("Zone1", "source-address", "054-6666666", "moshe", Site.SiteType.LOGISTICAL_CENTER);
        Site destination = new Site("Zone1", "destination-address", "054-7777777", "yossi", Site.SiteType.BRANCH);
        LinkedList<Site> sites = new LinkedList<>();
        sites.add(source);
        LocalDateTime dateTime = LocalDateTime.of(1977,1,1,12,24);
        HashMap<String,Integer> items1 = new HashMap<>();
        items1.put("itemToLoad", 5);
        HashMap<String,Integer> items2 = new HashMap<>();
        items2.put("itemToUnload", 7);
        ItemList itemList = new ItemList(1,items1,items2);
        HashMap<Site,ItemList> siteItems = new HashMap<>();
        siteItems.put(destination, itemList);
        Transport t2 = new Transport(1,source,sites,siteItems,1,1,dateTime);

        assertEquals(t2.getId(), t.getId());
        assertEquals(t2.getSource(), t.getSource());
        assertEquals(t2.getDestinations().get(0), t.getDestinations().get(0));
        assertEquals(t2.getDestinations().get(0).getSiteType(), t.getDestinations().get(0).getSiteType());
        HashMap<Site, ItemList> siteItems2 = t.getItemLists();
        assertEquals(siteItems2.get(destination).getId(), siteItems.get(destination).getId());
        assertEquals(siteItems2.get(destination).getLoadingItems().get("itemToLoad"), siteItems.get(destination).getLoadingItems().get("itemToLoad"));
        assertEquals(siteItems2.get(destination).getUnloadingItems().get("itemToUnload"), siteItems.get(destination).getUnloadingItems().get("itemToUnload"));
        assertEquals(t2.getTruckId(), t.getTruckId());
        assertEquals(t2.getDriverId(), t.getDriverId());
        assertEquals(t2.getScheduledTime(), t.getScheduledTime());
        assertEquals(t2.getWeight(), t.getWeight());
    }
}