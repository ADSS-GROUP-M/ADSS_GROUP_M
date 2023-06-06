package dataAccessLayer.transportModule;

import dataAccessLayer.DalFactory;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.OfflineResultSet;
import domainObjects.transportModule.ItemList;
import exceptions.DalException;
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
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    void insert_and_select_no_cache() {
        try {
            dao.insert(itemList);
            dao.clearCache();
            assertDeepEquals(itemList, dao.select(itemList));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }
    @Test
    void insert_and_select_with_cache() {
        try {
            dao.insert(itemList);
            assertDeepEquals(itemList, dao.select(itemList));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void selectAll() {
        // setup
        LinkedList<ItemList> itemLists = new LinkedList<>();
        List.of(1,2,3,4,5).forEach(i -> {
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
                fail(e.getMessage(),e.getCause());
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
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void update() {

        try {
            //set up
            dao.insert(itemList);

            //test
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
            dao.update(itemList2);
            assertDeepEquals(itemList2, dao.select(itemList2));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }
    @Test
    void update_does_not_exist(){
        assertThrows(DalException.class, () -> dao.update(ItemList.getLookupObject(5)));
    }

    @Test
    void delete() {
        try {
            //set up
            dao.insert(itemList);
            dao.clearCache();
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }

        //test
        assertDoesNotThrow(() -> dao.delete(itemList));
        assertThrows(DalException.class, () -> dao.select(itemList));
    }

    @Test
    void delete_does_not_exist(){
        assertThrows(DalException.class, () -> dao.delete(ItemList.getLookupObject(5)));
    }

    @Test
    void exists_with_cache() {
        try {
            dao.insert(itemList);
            assertTrue(dao.exists(itemList));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void exists_no_cache() {
        try {
            dao.insert(itemList);
            dao.clearCache();
            assertTrue(dao.exists(itemList));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void getObjectFromResultSet() {
        SQLExecutor cursor = factory.cursor();
        try {
            dao.insert(itemList);

            OfflineResultSet resultSet = cursor.executeRead("SELECT * FROM item_lists_items WHERE id = "+itemList.id());
            assertDeepEquals(itemList, dao.getObjectFromResultSet(resultSet));
        } catch (SQLException | DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    private void assertDeepEquals(ItemList itemList1, ItemList itemList2) {
        assertEquals(itemList1.id(), itemList2.id());
        assertEquals(itemList1.load(), itemList2.load());
        assertEquals(itemList1.unload(), itemList2.unload());
    }
}