package transportModule.backend.serviceLayer;

import ServiceLayer.transportModule.ItemListsService;
import ServiceLayer.transportModule.ModuleFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.transportUtils.records.ItemList;
import utils.Response;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ItemListsServiceTest {

    private ItemList itemList;
    private ItemListsService ils;

    @BeforeEach
    void setUp() {
        ils = new ModuleFactory().getItemListsService();

        HashMap<String, Integer> load1 = new HashMap<>();
        load1.put("Shirts", 20);
        load1.put("Pants", 15);
        load1.put("Socks", 30);

        HashMap<String, Integer> unload1 = new HashMap<>();
        unload1.put("Jackets", 10);
        unload1.put("Hats", 5);
        unload1.put("Gloves", 20);

        itemList = new ItemList(load1, unload1);
        String json = ils.addItemList(itemList.toJson());
        int id = Response.fromJson(json).dataToInt();
        itemList = itemList.newId(id);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void addItemList() {
        HashMap<String, Integer> load2 = new HashMap<>();
        load2.put("Cups", 50);
        load2.put("Plates", 20);
        load2.put("Forks", 30);

        HashMap<String, Integer> unload2 = new HashMap<>();
        unload2.put("Knives", 40);
        unload2.put("Spoons", 15);
        unload2.put("Napkins", 25);

        ItemList itemList2 = new ItemList(load2, unload2);
        String json1 = ils.addItemList(itemList2.toJson());
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.success());

        String json2 = ils.getItemList(ItemList.getLookupObject(response1.dataToInt()).toJson());
        Response response2 = Response.fromJson(json2);
        ItemList addedItemList = response2.data(ItemList.class);
        assertEquals(itemList2.load(), addedItemList.load());
        assertEquals(itemList2.unload(), addedItemList.unload());
    }

    @Test
    void addItemListPredefinedId() {

        try {
            ils.addItemList(ItemList.getLookupObject(1001).toJson());
        } catch (UnsupportedOperationException e) {
            return;
        }
        fail();
    }

    @Test
    void removeItemList() {
        String json1 = ils.removeItemList(itemList.toJson());
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.success());

        String json2 = ils.getItemList(ItemList.getLookupObject(itemList.id()).toJson());
        Response response2 = Response.fromJson(json2);
        assertFalse(response2.success());
    }

    @Test
    void removeItemListDoesNotExist() {
        ItemList list = ItemList.getLookupObject(1002);
        String json1 = ils.removeItemList(list.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void updateItemList() {
        itemList.load().put("Shirts", 25);
        itemList.unload().put("Jackets", 15);
        String json1 = ils.updateItemList(itemList.toJson());
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.success());

        String json2 = ils.getItemList(ItemList.getLookupObject(itemList.id()).toJson());
        Response response2 = Response.fromJson(json2);
        assertTrue(response2.success());
        ItemList updatedItemList = response2.data(ItemList.class);
        assertEquals(itemList.load(), updatedItemList.load());
        assertEquals(itemList.unload(), updatedItemList.unload());
    }

    @Test
    void updateItemListDoesNotExist() {
        ItemList list = ItemList.getLookupObject(1002);
        String json1 = ils.updateItemList(list.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void getItemList() {
        String json1 = ils.getItemList(ItemList.getLookupObject(itemList.id()).toJson());
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.success());

        ItemList retrievedItemList = response1.data(ItemList.class);
        assertEquals(itemList.load(), retrievedItemList.load());
        assertEquals(itemList.unload(), retrievedItemList.unload());
    }

    @Test
    void getItemListDoesNotExist() {
        ItemList list = ItemList.getLookupObject(1002);
        String json1 = ils.getItemList(list.toJson());
        Response response1 = Response.fromJson(json1);
        assertFalse(response1.success());
    }

    @Test
    void getAllItemLists() {
        //generate more item lists
        for(int i = 0; i < 20; i++){
            HashMap<String, Integer> load = new HashMap<>();
            load.put("Shirts", 20);
            load.put("Pants", 15);
            load.put("Socks", 30);

            HashMap<String, Integer> unload = new HashMap<>();
            unload.put("Jackets", 10);
            unload.put("Hats", 5);
            unload.put("Gloves", 20);

            ItemList itemList = new ItemList(load, unload);
            ils.addItemList(itemList.toJson());
        }

        String json1 = ils.getAllItemLists();
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.success());
        assertEquals(21, ItemList.listFromJson(response1.data()).size());

    }
}