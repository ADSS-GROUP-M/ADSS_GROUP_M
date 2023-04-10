package transportModule.backend.serviceLayer;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transportModule.records.ItemList;
import utils.JSON;
import utils.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;

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

        itemList = new ItemList(1001, load1, unload1);

        ils.addItemList(JSON.serialize(itemList));
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

        ItemList itemList2 = new ItemList(1002, load2, unload2);
        String json1 = ils.addItemList(JSON.serialize(itemList2));
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.isSuccess());

        String json2 = ils.getItemList(JSON.serialize(ItemList.getLookupObject(itemList2.id())));
        Response response2 = Response.fromJson(json2);
        ItemList addedItemList = response2.getData(ItemList.class);
        assertEquals(itemList2.load(), addedItemList.load());
        assertEquals(itemList2.unload(), addedItemList.unload());
    }

    @Test
    void removeItemList() {
        String json1 = ils.removeItemList(JSON.serialize(itemList));
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.isSuccess());

        String json2 = ils.getItemList(JSON.serialize(ItemList.getLookupObject(itemList.id())));
        Response response2 = Response.fromJson(json2);
        assertFalse(response2.isSuccess());
    }

    @Test
    void updateItemList() {
        itemList.load().put("Shirts", 25);
        itemList.unload().put("Jackets", 15);
        String json1 = ils.updateItemList(JSON.serialize(itemList));
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.isSuccess());

        String json2 = ils.getItemList(JSON.serialize(ItemList.getLookupObject(itemList.id())));
        Response response2 = Response.fromJson(json2);
        assertTrue(response2.isSuccess());
        ItemList updatedItemList = response2.getData(ItemList.class);
        assertEquals(itemList.load(), updatedItemList.load());
        assertEquals(itemList.unload(), updatedItemList.unload());
    }

    @Test
    void getItemList() {
        String json1 = ils.getItemList(JSON.serialize(ItemList.getLookupObject(itemList.id())));
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.isSuccess());

        ItemList retrievedItemList = response1.getData(ItemList.class);
        assertEquals(itemList.load(), retrievedItemList.load());
        assertEquals(itemList.unload(), retrievedItemList.unload());
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

            ItemList itemList = new ItemList(1002 + i, load, unload);
            ils.addItemList(JSON.serialize(itemList));
        }

        String json1 = ils.getAllItemLists();
        Response response1 = Response.fromJson(json1);
        assertTrue(response1.isSuccess());
        Type type = new TypeToken<LinkedList<ItemList>>(){}.getType();
        assertEquals(21, response1.<LinkedList<ItemList>>getData(type).size());

    }
}