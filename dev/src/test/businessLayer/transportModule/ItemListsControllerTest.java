package businessLayer.transportModule;

import exceptions.DalException;
import dataAccessLayer.transportModule.ItemListsDAO;
import objects.transportObjects.ItemList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import exceptions.TransportException;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemListsControllerTest {


    private ItemListsController controller;
    private ItemListsDAO dao;
    private ItemList itemList;
    private final int LIST_ID = 1;

    @BeforeEach
    void setUp() {

        try {
            dao = mock(ItemListsDAO.class);
            when(dao.selectCounter()).thenReturn(1);
            controller = new ItemListsController(dao);
        } catch (TransportException | DalException e) {
            fail(e);
        }

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

    @Test
    void addItemList() {
        try{
            when(dao.exists(ItemList.getLookupObject(LIST_ID))).thenReturn(false);
        } catch (DalException e) {
            fail(e);
        }
        ItemList itemList1 = assertDoesNotThrow(() -> controller.addItemList(itemList));
        assertEquals(LIST_ID, itemList1.id());
    }

    @Test
    void addItemListPredefinedId() {
        ItemList badItemList = new ItemList(500, itemList.load(), itemList.unload());
        try{
            when(dao.exists(badItemList)).thenReturn(false);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(RuntimeException.class, () -> controller.addItemList(badItemList));
    }

    @Test
    void removeItemList() {
        try{
            when(dao.exists(ItemList.getLookupObject(LIST_ID))).thenReturn(true);
        } catch (DalException e) {
            fail(e);
        }
        assertDoesNotThrow(() -> controller.removeItemList(LIST_ID));
    }

    @Test
    void removeItemListDoesNotExist() {
        try{
            when(dao.exists(ItemList.getLookupObject(LIST_ID))).thenReturn(false);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(TransportException.class, () -> controller.removeItemList(LIST_ID));
    }

    @Test
    void updateItemList() {
        ItemList updatedItemList = new ItemList(LIST_ID, itemList.load(), itemList.unload());
        try{
            when(dao.exists(ItemList.getLookupObject(LIST_ID))).thenReturn(true);
        } catch (DalException e) {
            fail(e);
        }
        assertDoesNotThrow(() -> controller.updateItemList(updatedItemList.id(), updatedItemList));
    }

    @Test
    void updateItemListDoesNotExist() {
        try{
            when(dao.exists(ItemList.getLookupObject(LIST_ID))).thenReturn(false);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(TransportException.class, () -> controller.updateItemList(LIST_ID, itemList));
    }

    @Test
    void getItemList() {
        try{
            ItemList lookupObject = ItemList.getLookupObject(LIST_ID);
            when(dao.exists(lookupObject)).thenReturn(true);
            when(dao.select(lookupObject)).thenReturn(itemList);
        } catch (DalException e) {
            fail(e);
        }
        ItemList fetched = assertDoesNotThrow(() -> controller.getItemList(LIST_ID));
        assertDeepEquals(itemList, fetched);
    }

    @Test
    void getItemListDoesNotExist() {
        try{
            when(dao.exists(ItemList.getLookupObject(LIST_ID))).thenReturn(false);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(TransportException.class, () -> controller.getItemList(LIST_ID));
    }

    @Test
    void getAllItemLists() {
        ItemList itemList2 = new ItemList(LIST_ID + 1, itemList.load(), itemList.unload());
        try{
            when(dao.selectAll()).thenReturn(List.of(itemList, itemList2));
        } catch (DalException e) {
            fail(e);
        }
        List<ItemList> fetched = assertDoesNotThrow(() -> controller.getAllItemLists());
        assertEquals(2, fetched.size());
        assertDeepEquals(itemList, fetched.get(0));
        assertDeepEquals(itemList2, fetched.get(1));
    }

    private void assertDeepEquals(ItemList itemList1, ItemList itemList2) {
        assertEquals(itemList1.id(), itemList2.id());
        assertEquals(itemList1.load(), itemList2.load());
        assertEquals(itemList1.unload(), itemList2.unload());
    }
}