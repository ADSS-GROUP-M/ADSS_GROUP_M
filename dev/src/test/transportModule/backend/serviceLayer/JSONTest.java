package transportModule.backend.serviceLayer;

import transportModule.backend.businessLayer.records.ItemList;
import transportModule.backend.businessLayer.records.Site;
import transportModule.backend.businessLayer.records.Transport;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        Transport t = new Transport(1,source,sites,siteItems,"1",1,dateTime,0);
        String output = JSON.serialize(t);
        String expected = """
                 {
                   "id": 1,
                   "source": {
                     "transportZone": "Zone1",
                     "address": "source-address",
                     "phoneNumber": "054-6666666",
                     "contactName": "moshe",
                     "siteType": "LOGISTICAL_CENTER"
                   },
                   "destinations": [
                     {
                       "transportZone": "Zone1",
                       "address": "source-address",
                       "phoneNumber": "054-6666666",
                       "contactName": "moshe",
                       "siteType": "LOGISTICAL_CENTER"
                     }
                   ],
                   "itemLists": [
                     [
                       {
                         "transportZone": "Zone1",
                         "address": "destination-address",
                         "phoneNumber": "054-7777777",
                         "contactName": "yossi",
                         "siteType": "BRANCH"
                       },
                       {
                         "id": 1,
                         "load": {
                           "itemToLoad": 5
                         },
                         "unload": {
                           "itemToUnload": 7
                         }
                       }
                     ]
                   ],
                   "truckId": "1",
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
                     "transportZone": "Zone1",
                     "address": "source-address",
                     "phoneNumber": "054-6666666",
                     "contactName": "moshe",
                     "siteType": "LOGISTICAL_CENTER"
                   },
                   "destinations": [
                     {
                       "transportZone": "Zone1",
                       "address": "source-address",
                       "phoneNumber": "054-6666666",
                       "contactName": "moshe",
                       "siteType": "LOGISTICAL_CENTER"
                     }
                   ],
                   "itemLists": [
                     [
                       {
                         "transportZone": "Zone1",
                         "address": "destination-address",
                         "phoneNumber": "054-7777777",
                         "contactName": "yossi",
                         "siteType": "BRANCH"
                       },
                       {
                         "id": "1",
                         "load": {
                           "itemToLoad": 5
                         },
                         "unload": {
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
        Transport t2 = new Transport(1,source,sites,siteItems,"1",1,dateTime,0);

        assertEquals(t2.id(), t.id());
        assertEquals(t2.source(), t.source());
        assertEquals(t2.destinations().get(0), t.destinations().get(0));
        assertEquals(t2.destinations().get(0).siteType(), t.destinations().get(0).siteType());
        HashMap<Site, ItemList> siteItems2 = t.itemLists();
        assertEquals(siteItems2.get(destination).id(), siteItems.get(destination).id());
        assertEquals(siteItems2.get(destination).load().get("itemToLoad"), siteItems.get(destination).load().get("itemToLoad"));
        assertEquals(siteItems2.get(destination).unload().get("itemToUnload"), siteItems.get(destination).unload().get("itemToUnload"));
        assertEquals(t2.id(), t.id());
        assertEquals(t2.id(), t.id());
        assertEquals(t2.scheduledTime(), t.scheduledTime());
        assertEquals(t2.weight(), t.weight());
    }
}