package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import objects.transportObjects.ItemList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ItemListsItemsDAOTest {

    ItemListsItemsDAO dao;

    @BeforeEach
    void setUp() {
        try {
            dao = new ItemListsItemsDAO("TestingDB.db");
        } catch (DalException e) {
            fail(e);
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void foreign_key_test() {
        try {
            HashMap<String, Integer> load = new HashMap<>() {{
                put("item1", 1);
                put("item2", 2);
                put("item3", 3);
            }};
            HashMap<String, Integer> unload = new HashMap<>() {{
                put("item4", 4);
                put("item5", 5);
                put("item6", 6);
            }};
            ItemList itemList = new ItemList(1, load, unload);
            dao.insert(itemList);
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }
}