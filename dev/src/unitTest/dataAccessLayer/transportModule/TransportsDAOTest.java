package dataAccessLayer.transportModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import dataAccessLayer.DalFactory;
import dataAccessLayer.employeeModule.EmployeeDAO;
import exceptions.DalException;
import objects.transportObjects.Driver;
import objects.transportObjects.Transport;
import objects.transportObjects.Truck;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class TransportsDAOTest {

    private TransportsDAO transportsDAO;
    private Transport transport;
    private Driver driver;
    private Truck truck;

    @BeforeEach
    void setUp() {

        DalFactory.clearTestDB();

        //truck set up
        truck = new Truck("1", "model1", 1000, 20000, Truck.CoolingCapacity.FROZEN);

        //driver set up
        Employee employee = new Employee("name1","12345","Poalim",50, LocalDate.of(1999,10,10),"conditions","details");
        employee.addRole(Role.Driver);
        employee.addRole(Role.GeneralWorker);
        driver = new Driver(employee.getId(),employee.getName(), Driver.LicenseType.C3);

        transport = new Transport(1,
                new LinkedList<>(),
                new HashMap<>(),
                driver.id(),
                truck.id(),
                LocalDateTime.of(2023, 2, 2, 12, 0),
                15000
        );

        try {
            DalFactory factory = new DalFactory(TESTING_DB_NAME);
            TrucksDAO trucksDAO = factory.trucksDAO();
            EmployeeDAO employeeDAO = factory.employeeDAO();
            DriversDAO driversDAO = factory.driversDAO();
            transportsDAO = factory.transportsDAO();

            trucksDAO.insert(truck);
            employeeDAO.insert(employee);
            driversDAO.insert(driver);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    void insert_and_select_success() {
        try {
            transportsDAO.insert(transport);
            transportsDAO.clearCache();
            Transport selected = transportsDAO.select(transport);
            assertDeepEquals(transport, selected);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void selectAll() {

        //set up
        LinkedList<Transport> transports = new LinkedList<>();
        List.of(1,2,3,4,5).forEach(
            i -> {
                try {
                    Transport toAdd = new Transport(i,
                            new LinkedList<>(),
                            new HashMap<>(),
                            driver.id(),
                            truck.id(),
                            LocalDateTime.of(2020, 1, 1, 1, 1+i),
                            15000+i
                    );
                    transports.add(toAdd);
                    transportsDAO.insert(toAdd);
                } catch (DalException e) {
                    fail(e.getMessage(),e.getCause());
                }
            }
        );
        transportsDAO.clearCache();

        //test
        try {
            List<Transport> selected = transportsDAO.selectAll();
            assertEquals(transports.size(), selected.size());
            transports.forEach(
                    transport ->{
                        assertTrue(selected.contains(transport));
                        assertDeepEquals(transport, selected.get(selected.indexOf(transport)));
                    });
        } catch (DalException | IndexOutOfBoundsException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void select_from_cache() {
        try {
            //set up
            transportsDAO.insert(transport);
            Transport selected = transportsDAO.select(transport);
            assertDeepEquals(transport, selected);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void select_does_not_exist(){
        assertThrows(DalException.class, () -> transportsDAO.select(transport));
    }

    @Test
    void select_all_does_not_exist(){
        try {
            assertEquals(0, transportsDAO.selectAll().size());
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void update() {
        try {
            //set up
            Transport updatedTransport = new Transport(transport.id(),
                    new LinkedList<>(),
                    new HashMap<>(),
                    driver.id(),
                    truck.id(),
                    LocalDateTime.of(1997, 2, 2, 2, 2),
                    10000
            );
            transportsDAO.insert(transport);
            transportsDAO.clearCache();

            //test
            transportsDAO.update(updatedTransport);
            transportsDAO.clearCache();
            Transport selected = transportsDAO.select(Transport.getLookupObject(transport.id()));
            assertDeepEquals(updatedTransport, selected);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void update_does_not_exist() {
        assertThrows(DalException.class, () -> transportsDAO.update(transport));
    }

    @Test
    void delete() {
        try {
            //set up
            transportsDAO.insert(transport);
            transportsDAO.clearCache();
            assertDoesNotThrow(() -> transportsDAO.select(Transport.getLookupObject(transport.id())));
            transportsDAO.clearCache();

            //test
            transportsDAO.delete(transport);
            assertThrows(DalException.class, () -> transportsDAO.select(Transport.getLookupObject(transport.id())));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void delete_does_not_exist() {
        assertThrows(DalException.class, () -> transportsDAO.delete(transport));
    }

    @Test
    void exists_not_in_cache(){
        try {
            //set up
            transportsDAO.insert(transport);
            transportsDAO.clearCache();

            //test
            assertTrue(transportsDAO.exists(transport));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void exists_in_cache(){
        try {
            //set up
            transportsDAO.insert(transport);

            //test
            assertTrue(transportsDAO.exists(transport));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }


    @Test
    void exists_does_not_exist(){
        try {
            assertFalse(transportsDAO.exists(transport));
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getCounter(){
        try {
            assertEquals(1, transportsDAO.selectCounter());
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void incrementCounter(){
        try {
            transportsDAO.incrementCounter();
            assertEquals(2, transportsDAO.selectCounter());
            transportsDAO.incrementCounter();
            assertEquals(3, transportsDAO.selectCounter());
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void resetCounter(){
        try {
            transportsDAO.incrementCounter();
            transportsDAO.incrementCounter();
            transportsDAO.resetCounter();
            assertEquals(1, transportsDAO.selectCounter());
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insert_counter(){
        try{
            transportsDAO.insertCounter(5);
            assertEquals(5, transportsDAO.selectCounter());
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }


    private void assertDeepEquals(Transport transport1, Transport transport2) {
        assertEquals(transport1.id(), transport2.id());
        assertEquals(transport1.driverId(), transport2.driverId());
        assertEquals(transport1.truckId(), transport2.truckId());
        assertEquals(transport1.departureTime(), transport2.departureTime());
        assertEquals(transport1.weight(), transport2.weight());

        // currently not checking for equality of the following fields:
//        assertEquals(transport1.route(), transport2.route());
//        assertEquals(transport1.itemLists(), transport2.itemLists());
//        assertEquals(transport1.deliveryRoute().estimatedArrivalTimes(), transport2.deliveryRoute().estimatedArrivalTimes());
    }
}