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
        Site source = new Site("source", "source-address", "contact", "loading");
        Site destination = new Site("destination", "destination-address", "contact", "unloading");
        LinkedList<Site> sites = new LinkedList<>();
        sites.add(source);
        LocalDateTime dateTime = LocalDateTime.of(1977,1,1,12,24);
        HashMap<String,Integer> items = new HashMap<>();
        items.put("item", 5);
        ItemList itemList = new ItemList(1,items);
        HashMap<Site,ItemList> siteItems = new HashMap<>();
        siteItems.put(destination, itemList);
        Transport t = new Transport(1,source,sites,siteItems,1,1,dateTime);
        String output = JSON.serialize(t);
        String expected = """
                {
                  "id": 1,
                  "source": {
                    "TransportZone": "source",
                    "Address": "source-address",
                    "phoneNumber": "contact",
                    "contactName": "loading"
                  },
                  "destinations": [
                    {
                      "TransportZone": "source",
                      "Address": "source-address",
                      "phoneNumber": "contact",
                      "contactName": "loading"
                    }
                  ],
                  "itemLists": [
                    [
                      {
                        "TransportZone": "destination",
                        "Address": "destination-address",
                        "phoneNumber": "contact",
                        "contactName": "unloading"
                      },
                      {
                        "id": 1,
                        "items": {
                          "item": 5
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
                    "TransportZone": "source",
                    "Address": "source-address",
                    "phoneNumber": "contact",
                    "contactName": "loading"
                  },
                  "destinations": [
                    {
                      "TransportZone": "source",
                      "Address": "source-address",
                      "phoneNumber": "contact",
                      "contactName": "loading"
                    }
                  ],
                  "itemLists": [
                    [
                      {
                        "TransportZone": "destination",
                        "Address": "destination-address",
                        "phoneNumber": "contact",
                        "contactName": "unloading"
                      },
                      {
                        "id": 1,
                        "items": {
                          "item": 5
                        }
                      }
                    ]
                  ],
                  "truckId": 1,
                  "driverId": 1,
                  "scheduledTime": "1977-01-01T12:24",
                  "weight": 0
                }
                """;
        Transport t = JSON.deserialize(json, Transport.class);

        Site source = new Site("source", "source-address", "contact", "loading");
        Site destination = new Site("destination", "destination-address", "contact", "unloading");
        LinkedList<Site> sites = new LinkedList<>();
        sites.add(source);
        LocalDateTime dateTime = LocalDateTime.of(1977,1,1,12,24);
        HashMap<String,Integer> items = new HashMap<>();
        items.put("item", 5);
        ItemList itemList = new ItemList(1,items);
        HashMap<Site,ItemList> siteItems = new HashMap<>();
        siteItems.put(destination, itemList);
        Transport t2 = new Transport(1,source,sites,siteItems,1,1,dateTime);

        assertEquals(t2.getId(), t.getId());
        assertEquals(t2.getSource(), t.getSource());
        assertEquals(t2.getDestinations(), t.getDestinations());
        HashMap<Site, ItemList> siteItems2 = t.getItemLists();
        assertEquals(siteItems2.get(destination).getId(), siteItems.get(destination).getId());
        assertEquals(siteItems2.get(destination).getItems().get("item"), siteItems.get(destination).getItems().get("item"));
        assertEquals(t2.getTruckId(), t.getTruckId());
        assertEquals(t2.getDriverId(), t.getDriverId());
        assertEquals(t2.getScheduledTime(), t.getScheduledTime());
        assertEquals(t2.getWeight(), t.getWeight());
    }
}