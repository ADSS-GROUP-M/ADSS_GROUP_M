package serviceLayer.transportModule;

import dataAccessLayer.DalFactory;
import domainObjects.transportModule.ItemList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serviceLayer.ServiceFactory;
import utils.Response;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class ItemListsServiceIT {

    private ItemList itemList;
    private ItemListsService ils;
    private ServiceFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ServiceFactory(TESTING_DB_NAME);
        ils = factory.itemListsService();

        HashMap<String, Integer> load1 = new HashMap<>();
        load1.put("Shirts", 20);
        load1.put("Pants", 15);
        load1.put("Socks", 30);

        HashMap<String, Integer> unload1 = new HashMap<>();
        unload1.put("Jackets", 10);
        unload1.put("Hats", 5);
        unload1.put("Gloves", 20);

        itemList = new ItemList(load1, unload1);
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    void addItemList() {
        Response response = assertSuccessValue(ils.addItemList(itemList.toJson()),true);
        ItemList addedItemList = ItemList.fromJson(response.data());
        assertEquals(itemList.load(), addedItemList.load());
    }

    @Test
    void addItemListPredefinedId() {
        assertThrows(UnsupportedOperationException.class, () -> ils.addItemList(ItemList.getLookupObject(1001).toJson()));
    }

    @Test
    void removeItemList() {
        //set up
        Response _r = assertSuccessValue(ils.addItemList(itemList.toJson()),true);
        itemList = ItemList.fromJson(_r.data());

        //test
        assertSuccessValue(ils.removeItemList(itemList.toJson()),true);
        assertSuccessValue(ils.getItemList(itemList.toJson()),false);
    }

    @Test
    void removeItemListDoesNotExist() {
        ItemList list = ItemList.getLookupObject(1002);
        assertSuccessValue(ils.removeItemList(list.toJson()),false);
    }

    @Test
    void updateItemList() {

        //set up
        Response _r = assertSuccessValue(ils.addItemList(itemList.toJson()),true);
        itemList = ItemList.fromJson(_r.data());

        //test
        itemList.load().put("Shirts", 25);
        itemList.unload().put("Jackets", 15);
        assertSuccessValue(ils.updateItemList(itemList.toJson()),true);

        Response response = assertSuccessValue(ils.getItemList(ItemList.getLookupObject(itemList.id()).toJson()),true);
        ItemList updatedItemList = ItemList.fromJson(response.data());
        assertEquals(itemList.load(), updatedItemList.load());
    }

    @Test
    void updateItemListDoesNotExist() {
        ItemList list = ItemList.getLookupObject(1002);
        assertSuccessValue(ils.updateItemList(list.toJson()),false);
    }

    @Test
    void getItemList() {
        //set up
        Response _r = assertSuccessValue(ils.addItemList(itemList.toJson()),true);
        itemList = ItemList.fromJson(_r.data());

        //test
        String lookupObject = ItemList.getLookupObject(1).toJson();
        Response response = assertSuccessValue(ils.getItemList(lookupObject),true);
        ItemList addedItemList = ItemList.fromJson(response.data());
        assertDeepEquals(itemList, addedItemList);
    }

    @Test
    void getItemListDoesNotExist() {
        ItemList list = ItemList.getLookupObject(1002);
        assertSuccessValue(ils.getItemList(list.toJson()),false);
    }

    @Test
    void getAllItemLists() {
        //generate more item lists
        List<ItemList> itemLists = new LinkedList<>();
        for(int i = 0; i < 5; i++){
            ItemList newList = new ItemList(itemList.load(), itemList.unload());
            Response _r = assertSuccessValue(ils.addItemList(newList.toJson()),true);
            itemLists.add(ItemList.fromJson(_r.data()));
        }

        Response response = assertSuccessValue(ils.getAllItemLists(),true);
        List<ItemList> addedItemLists = ItemList.listFromJson(response.data());
        assertEquals(itemLists.size(), addedItemLists.size());
        for(int i = 0; i < itemLists.size(); i++){
            assertDeepEquals(itemLists.get(i), addedItemLists.get(i));
        }
    }

    private void assertDeepEquals(ItemList itemList1, ItemList itemList2) {
        assertEquals(itemList1.id(), itemList2.id());
        assertEquals(itemList1.load(), itemList2.load());
        assertEquals(itemList1.unload(), itemList2.unload());
    }

    private Response assertSuccessValue(String json, boolean expectedSuccess) {
        Response response = Response.fromJson(json);
        if(response.success() != expectedSuccess){
            fail("Operation failed:\n" +
                    "Expected success value: "+expectedSuccess + "\n" +
                    "Actual success value: "+response.success() + "\n" +
                    "Response message:" + response.message() + "\n" +
                    "Response data:" + response.data());
        }
        return response;
    }
}