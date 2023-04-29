package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import objects.transportObjects.Truck;
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

    //TODO: Tests that are supposed to fail

    @BeforeEach
    void setUp() {
        try {
            dao = new TrucksDAO(TESTING_DB_NAME);
            truck = new Truck("1", "model1", 1000, 20000, Truck.CoolingCapacity.FROZEN);
            dao.clearTable();

            dao.insert(truck);
        } catch (DalException e) {
            fail(e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            dao.selectAll().forEach(truck -> {
                try {
                    dao.delete(truck);
                } catch (DalException e) {
                    fail(e);
                }
            });
        } catch (DalException e) {
            fail(e);
        }
        dao = null;
        truck = null;
    }

    @Test
    void select() {
        try {
            assertDeepEquals(truck,dao.select(Truck.getLookupObject(truck.id())));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void selectAll() {

        // setup
        LinkedList<Truck> trucks = new LinkedList<>();
        trucks.add(truck);
        List.of(2,3,4,5,6).forEach(i -> {
            try {
                trucks.add(new Truck(String.valueOf(i), "model" + i, 1000, 20000, Truck.CoolingCapacity.FROZEN));
                dao.insert(trucks.getLast());
            } catch (DalException e) {
                fail(e);
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
            fail(e);
        }
    }

    @Test
    void insert() {
        Truck truck2 = new Truck("2", "model2", 1000, 20000, Truck.CoolingCapacity.FROZEN);
        try {
            dao.insert(truck2);
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void update() {
        Truck updatedTruck = new Truck("1", "model2", 2000, 30000, Truck.CoolingCapacity.NONE);
        try {
            dao.update(updatedTruck);
            assertDeepEquals(updatedTruck, dao.select(Truck.getLookupObject(truck.id())));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void delete() {
        try {
            dao.delete(truck);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(DalException.class, () -> dao.select(Truck.getLookupObject(truck.id())));
    }

    @Test
    void getObjectFromResultSet() {
        SQLExecutor cursor = new SQLExecutor(TESTING_DB_NAME);
        try {
            OfflineResultSet resultSet = cursor.executeRead("SELECT * FROM Trucks WHERE id = "+truck.id());
            resultSet.next();
            assertDeepEquals(truck, dao.getObjectFromResultSet(resultSet));
        } catch (SQLException e) {
            fail(e);
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