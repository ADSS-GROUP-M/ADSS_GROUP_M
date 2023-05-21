package dataAccessLayer.transportModule;

import dataAccessLayer.DalFactory;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.OfflineResultSet;
import domainObjects.transportModule.Truck;
import exceptions.DalException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class TrucksDAOTest {

    private TrucksDAO dao;
    private Truck truck;
    private DalFactory factory;

    @BeforeEach
    void setUp() {
        try {
            DalFactory.clearTestDB();
            factory = new DalFactory(TESTING_DB_NAME);
            dao = factory.trucksDAO();
            truck = new Truck("1", "model1", 1000, 20000, Truck.CoolingCapacity.FROZEN);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    void insert_and_select() {
        try {
            // setup
            dao.insert(truck);
            dao.clearCache();

            // test
            assertDeepEquals(truck,dao.select(Truck.getLookupObject(truck.id())));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void select_does_not_exist() {
        assertThrows(DalException.class, () -> dao.select(Truck.getLookupObject(truck.id())));
    }

    @Test
    void selectAll() {

        // setup
        LinkedList<Truck> trucks = new LinkedList<>();
        List.of(1,2,3,4,5).forEach(i -> {
            try {
                trucks.add(new Truck(String.valueOf(i), "model" + i, 1000, 20000, Truck.CoolingCapacity.FROZEN));
                dao.insert(trucks.getLast());
            } catch (DalException e) {
                fail(e.getMessage(),e.getCause());
            }
        });

        // test
        try {
            List<Truck> selectedTrucks = dao.selectAll();
            assertEquals(trucks.size(), selectedTrucks.size());
            for (int i = 0; i < trucks.size(); i++) {
                assertDeepEquals(trucks.get(i), selectedTrucks.get(i));
            }
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void selectAll_empty() {
        try {
            assertEquals(0, dao.selectAll().size());
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void update() {
        try {
            // setup
            dao.insert(truck);

            // test
            Truck updatedTruck = new Truck("1", "model2", 2000, 30000, Truck.CoolingCapacity.NONE);
            dao.update(updatedTruck);
            assertDeepEquals(updatedTruck, dao.select(Truck.getLookupObject(truck.id())));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void update_does_not_exist() {
        assertThrows(DalException.class, () -> dao.update(truck));
    }

    @Test
    void delete() {

        // setup
        try {
            dao.insert(truck);
            dao.clearCache();
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }

        // test
        assertDoesNotThrow(() -> dao.delete(truck));
        try {
            assertFalse(dao.exists(Truck.getLookupObject(truck.id())));
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
        assertThrows(DalException.class, () -> dao.select(Truck.getLookupObject(truck.id())));
    }

    @Test
    void delete_does_not_exist() {
        assertThrows(DalException.class, () -> dao.delete(truck));
    }

    @Test
    void getObjectFromResultSet() {
        SQLExecutor cursor = factory.cursor();
        try {
            dao.insert(truck);

            OfflineResultSet resultSet = cursor.executeRead("SELECT * FROM Trucks WHERE id = "+truck.id());
            resultSet.next();
            assertDeepEquals(truck, dao.getObjectFromResultSet(resultSet));
        } catch (SQLException | DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void exists_without_cache() {
        try {
            dao.insert(truck);
            dao.clearCache();
            assertTrue(dao.exists(Truck.getLookupObject(truck.id())));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void exists_with_cache() {
        try {
            dao.insert(truck);
            assertTrue(dao.exists(Truck.getLookupObject(truck.id())));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void select_with_cache() {
        try {
            dao.insert(truck);
            assertDeepEquals(truck, dao.select(Truck.getLookupObject(truck.id())));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    private void assertDeepEquals(Truck truck1, Truck truck2) {
        assertEquals(truck1.id(), truck2.id());
        assertEquals(truck1.model(), truck2.model());
        assertEquals(truck1.baseWeight(), truck2.baseWeight());
        assertEquals(truck1.maxWeight(), truck2.maxWeight());
        assertEquals(truck1.coolingCapacity(), truck2.coolingCapacity());
    }
}