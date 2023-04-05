package transportModule.serviceLayer;

import transportModule.businessLayer.records.ItemList;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ItemListsServiceTest {

    private static final Type responseItemListType = new TypeToken<Response<ItemList>>(){}.getType();
    private ItemList itemList;
    private ItemListsService ils;

    @BeforeEach
    void setUp() {
        ils = ModuleFactory.getInstance().getItemListsService();

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
        ModuleFactory.tearDownForTests();
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
        Response<String> response1 = JSON.deserialize(json1, Response.class);
        assertTrue(response1.isSuccess());

        String json2 = ils.getItemList(JSON.serialize(ItemList.getLookupObject(itemList2.id())));
        Response<ItemList> response2 = JSON.deserialize(json2, responseItemListType);
        ItemList addedItemList = response2.getData();
        assertEquals(itemList2.load(), addedItemList.load());
        assertEquals(itemList2.unload(), addedItemList.unload());
    }

    @Test
    void removeItemList() {
        String json1 = ils.removeItemList(JSON.serialize(itemList));
        Response<String> response1 = JSON.deserialize(json1, Response.class);
        assertTrue(response1.isSuccess());

        String json2 = ils.getItemList(JSON.serialize(ItemList.getLookupObject(itemList.id())));
        Response<ItemList> response2 = JSON.deserialize(json2, responseItemListType);
        assertFalse(response2.isSuccess());
    }

    @Test
    void updateItemList() {
        itemList.load().put("Shirts", 25);
        itemList.unload().put("Jackets", 15);
        String json1 = ils.updateItemList(JSON.serialize(itemList));
        Response<String> response1 = JSON.deserialize(json1, Response.class);
        assertTrue(response1.isSuccess());

        String json2 = ils.getItemList(JSON.serialize(ItemList.getLookupObject(itemList.id())));
        Response<ItemList> response2 = JSON.deserialize(json2, responseItemListType);
        assertTrue(response2.isSuccess());
        ItemList updatedItemList = response2.getData();
        assertEquals(itemList.load(), updatedItemList.load());
        assertEquals(itemList.unload(), updatedItemList.unload());
    }

    @Test
    void getItemList() {
        String json1 = ils.getItemList(JSON.serialize(ItemList.getLookupObject(itemList.id())));
        Response<ItemList> response1 = JSON.deserialize(json1, responseItemListType);
        assertTrue(response1.isSuccess());

        ItemList retrievedItemList = response1.getData();
        assertEquals(itemList.load(), retrievedItemList.load());
        assertEquals(itemList.unload(), retrievedItemList.unload());
    }

    @Test
    void getAllItemLists() {
        //generate more item lists
        LinkedList<ItemList> itemLists = new LinkedList<>();
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
            itemLists.add(itemList);
        }

        String json1 = ils.getAllItemLists();
        Type responseItemType = new TypeToken<Response<LinkedList<ItemList>>>(){}.getType();
        Response<LinkedList<ItemList>> response1 = JSON.deserialize(json1, responseItemType);
        assertTrue(response1.isSuccess());
        assertEquals(21, response1.getData().size());

    }
}