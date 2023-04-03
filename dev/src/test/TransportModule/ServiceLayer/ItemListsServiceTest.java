package TransportModule.ServiceLayer;

import TransportModule.BusinessLayer.Records.ItemList;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ItemListsServiceTest {

    ItemList itemList1;
    ItemList itemList2;

    @BeforeEach
    void setUp() {
        HashMap<String, Integer> load1 = new HashMap<>();
        load1.put("Shirts", 20);
        load1.put("Pants", 15);
        load1.put("Socks", 30);

        HashMap<String, Integer> unload1 = new HashMap<>();
        unload1.put("Jackets", 10);
        unload1.put("Hats", 5);
        unload1.put("Gloves", 20);

        itemList1 = new ItemList(1001, load1, unload1);

        HashMap<String, Integer> load2 = new HashMap<>();
        load2.put("Pencils", 50);
        load2.put("Notebooks", 20);
        load2.put("Erasers", 30);

        HashMap<String, Integer> unload2 = new HashMap<>();
        unload2.put("Pens", 40);
        unload2.put("Markers", 15);
        unload2.put("Highlighters", 25);

        itemList2 = new ItemList(1002, load2, unload2);

        ItemListsService ils = ModuleFactory.getInstance().getItemListsService();
        ils.addItemList(JSON.serialize(itemList1));
        ils.addItemList(JSON.serialize(itemList2));
    }

    @Test
    void addItemList() {
    }

    @Test
    void removeItemList() {
    }

    @Test
    void updateItemList() {
        ItemListsService ils = ModuleFactory.getInstance().getItemListsService();
        itemList1.load().put("Shirts", 25);
        itemList1.unload().put("Jackets", 15);
        String json = JSON.serialize(itemList1);
        String responseJson = ils.updateItemList(json);
        Response<String> response = JSON.deserialize(responseJson, Response.class);
        if(response.isSuccess() == false) fail("Failed to update item list");
        String updatedResponseJson = ils.getItemList(json);
        Type type = new TypeToken<Response<ItemList>>(){}.getType();
        Response<ItemList> updatedResponse = JSON.deserialize(updatedResponseJson, type);
        ItemList updatedItemList = updatedResponse.getData();
        assertEquals(itemList1.load(), updatedItemList.load());
        assertEquals(itemList1.unload(), updatedItemList.unload());
    }

    @Test
    void getItemList() {
    }

    @Test
    void getAllItemLists() {
    }
}