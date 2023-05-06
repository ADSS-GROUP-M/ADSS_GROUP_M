package serviceLayer.transportModule;

import dataAccessLayer.DalFactory;
import objects.transportObjects.ItemList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serviceLayer.ServiceFactory;
import utils.Response;

import java.util.HashMap;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class ItemListsServiceTest {

    private ItemList itemList;
    private ItemListsService ils;

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

    }

    @Test
    void addItemList() {

    }

    @Test
    void addItemListPredefinedId() {
    }

    @Test
    void removeItemList() {

    }

    @Test
    void removeItemListDoesNotExist() {

    }

    @Test
    void updateItemList() {

    }

    @Test
    void updateItemListDoesNotExist() {

    }

    @Test
    void getItemList() {

    }

    @Test
    void getItemListDoesNotExist() {

    }

    @Test
    void getAllItemLists() {

    }
}