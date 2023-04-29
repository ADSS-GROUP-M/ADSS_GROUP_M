package dataAccessLayer.transportModule;

import dataAccessLayer.DalFactory;
import dataAccessLayer.dalUtils.DalException;
import objects.transportObjects.ItemList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class ForeignKeyTest {

    ItemListsItemsDAO dao;

    @BeforeEach
    void setUp() {
        try {
            dao = new ItemListsItemsDAO(TESTING_DB_NAME);
        } catch (DalException e) {
            fail(e);
        }
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    void item_lists_items() {
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
        assertThrows(DalException.class, ()-> dao.insert(itemList));
    }
}