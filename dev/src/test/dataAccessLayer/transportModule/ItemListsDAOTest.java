package dataAccessLayer.transportModule;

import dataAccessLayer.DalFactory;
import exceptions.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import objects.transportObjects.ItemList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class ItemListsDAOTest {

    private ItemListsDAO dao;

    private ItemList itemList;
    private DalFactory factory;

    @BeforeEach
    void setUp() {
        HashMap<String, Integer> load = new HashMap<>(){{
            put("item1", 1);
            put("item2", 2);
            put("item3", 3);
        }};
        HashMap<String, Integer> unload = new HashMap<>(){{
            put("item4", 4);
            put("item5", 5);
            put("item6", 6);
        }};
        itemList = new ItemList(1, load, unload);

        try {
            factory = new DalFactory(TESTING_DB_NAME);
            dao = factory.itemListsDAO();
            dao.clearTable();
            dao.insert(itemList);
        } catch (DalException e) {
            fail(e);
        }
        dao.clearCache();
    }

    @AfterEach
    void tearDown() {
        try {
            dao.selectAll().forEach(itemList -> {
                try {
                    dao.delete(itemList);
                } catch (DalException e) {
                    fail(e);
                }
            });
        } catch (DalException e) {
            fail(e);
        }
        dao = null;
        itemList = null;
    }

    @Test
    void select() {
        try {
            assertDeepEquals(itemList, dao.select(itemList));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void selectAll() {
        // setup
        LinkedList<ItemList> itemLists = new LinkedList<>();
        itemLists.add(itemList);
        List.of(2,3,4,5,6).forEach(i -> {
            try {
                HashMap<String, Integer> load = new HashMap<>(){{
                    put("item1", 1);
                    put("item2", 2);
                    put("item3", 3);
                }};
                HashMap<String, Integer> unload = new HashMap<>(){{
                    put("item4", 4);
                    put("item5", 5);
                    put("item6", 6);
                }};
                itemLists.add(new ItemList(i, load,unload));
                dao.insert(itemLists.getLast());
            } catch (DalException e) {
                fail(e);
            }
        });

        // test
        try {
            List<ItemList> select = dao.selectAll();
            assertEquals(itemLists.size(), select.size());
            for (int i = 0; i < itemLists.size(); i++) {
                assertDeepEquals(itemLists.get(i), select.get(i));
            }
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void insert() {
        HashMap<String, Integer> load = new HashMap<>(){{
            put("item1", 1);
            put("item2", 2);
            put("item3", 3);
        }};
        HashMap<String, Integer> unload = new HashMap<>(){{
            put("item4", 4);
            put("item5", 5);
            put("item6", 6);
        }};
        ItemList itemList2 = new ItemList(2, load, unload);
        try {
            dao.insert(itemList2);
            assertDeepEquals(itemList2, dao.select(itemList2));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void update() {
        HashMap<String, Integer> load = new HashMap<>(){{
            put("item1", 2); // should be updated
            //put("item2", 2); // should be removed
            put("item3", 3); // should not change
            put("item7", 7); // should be added
        }};
        HashMap<String, Integer> unload = new HashMap<>(){{
            put("item4", 2); // should be updated
            //put("item5", 5); // should be removed
            put("item6", 6); // should not change
            put("item8", 8); // should be added
        }};
        ItemList itemList2 = new ItemList(itemList.id(), load, unload);

        try {
            dao.update(itemList2);
            assertDeepEquals(itemList2, dao.select(itemList2));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void delete() {
        try {
            dao.delete(itemList);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(DalException.class, () -> dao.select(itemList));
    }

    @Test
    void getObjectFromResultSet() {
        SQLExecutor cursor = factory.cursor();
        try {
            OfflineResultSet resultSet = cursor.executeRead("SELECT * FROM item_lists_items WHERE id = "+itemList.id());
            assertDeepEquals(itemList, dao.getObjectFromResultSet(resultSet));
        } catch (SQLException e) {
            fail(e);
        }
    }

    private void assertDeepEquals(ItemList itemList1, ItemList itemList2) {
        assertEquals(itemList1.id(), itemList2.id());
        assertEquals(itemList1.load(), itemList2.load());
        assertEquals(itemList1.unload(), itemList2.unload());
    }
}