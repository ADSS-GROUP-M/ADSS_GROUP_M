package dataAccessLayer.transportModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import dataAccessLayer.DalFactory;
import dataAccessLayer.dalAssociationClasses.transportModule.TransportMetaData;
import dataAccessLayer.employeeModule.EmployeeDAO;
import domainObjects.transportModule.Driver;
import domainObjects.transportModule.Truck;
import exceptions.DalException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class TransportsMetaDataDAOTest {

    private TransportsMetaDataDAO transportsMetaDataDAO;
    private TransportMetaData transport;
    private Driver driver;
    private Truck truck;
    private DalFactory factory;

    @BeforeEach
    void setUp() {

        //truck set up
        truck = new Truck("1", "model1", 1000, 20000, Truck.CoolingCapacity.FROZEN);

        //driver set up
        Employee employee = new Employee("name1","12345","Poalim",50, LocalDate.of(1999,10,10),"conditions","details");
        employee.addRole(Role.Driver);
        employee.addRole(Role.GeneralWorker);
        driver = new Driver(employee.getId(),employee.getName(), Driver.LicenseType.C3);

        transport = new TransportMetaData(1,
                driver.id(),
                truck.id(),
                LocalDateTime.of(2023, 2, 2, 12, 0),
                15000
        );

        try {
            factory = new DalFactory(TESTING_DB_NAME);
            TrucksDAO trucksDAO = factory.trucksDAO();
            EmployeeDAO employeeDAO = factory.employeeDAO();
            DriversDAO driversDAO = factory.driversDAO();
            transportsMetaDataDAO = factory.transportsMetaDataDAO();

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
            transportsMetaDataDAO.insert(transport);
            transportsMetaDataDAO.clearCache();
            TransportMetaData selected = transportsMetaDataDAO.select(transport);
            assertDeepEquals(transport, selected);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void selectAll() {

        //set up
        LinkedList<TransportMetaData> transports = new LinkedList<>();
        List.of(1,2,3,4,5).forEach(
            i -> {
                try {
                    TransportMetaData toAdd = new TransportMetaData(i,
                            driver.id(),
                            truck.id(),
                            LocalDateTime.of(2020, 1, 1, 1, 1+i),
                            15000+i
                    );
                    transports.add(toAdd);
                    transportsMetaDataDAO.insert(toAdd);
                } catch (DalException e) {
                    fail(e.getMessage(),e.getCause());
                }
            }
        );
        transportsMetaDataDAO.clearCache();

        //test
        try {
            List<TransportMetaData> selected = transportsMetaDataDAO.selectAll();
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
            transportsMetaDataDAO.insert(transport);
            TransportMetaData selected = transportsMetaDataDAO.select(transport);
            assertDeepEquals(transport, selected);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void select_does_not_exist(){
        assertThrows(DalException.class, () -> transportsMetaDataDAO.select(transport));
    }

    @Test
    void select_all_does_not_exist(){
        try {
            assertEquals(0, transportsMetaDataDAO.selectAll().size());
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void update() {
        try {
            //set up
            TransportMetaData updatedTransport = new TransportMetaData(transport.transportId(),
                    driver.id(),
                    truck.id(),
                    LocalDateTime.of(1997, 2, 2, 2, 2),
                    10000
            );
            transportsMetaDataDAO.insert(transport);
            transportsMetaDataDAO.clearCache();

            //test
            transportsMetaDataDAO.update(updatedTransport);
            transportsMetaDataDAO.clearCache();
            TransportMetaData selected = transportsMetaDataDAO.select(TransportMetaData.getLookupObject(transport.transportId()));
            assertDeepEquals(updatedTransport, selected);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void update_does_not_exist() {
        assertThrows(DalException.class, () -> transportsMetaDataDAO.update(transport));
    }

    @Test
    void delete() {
        try {
            //set up
            transportsMetaDataDAO.insert(transport);
            transportsMetaDataDAO.clearCache();
            assertDoesNotThrow(() -> transportsMetaDataDAO.select(TransportMetaData.getLookupObject(transport.transportId())));
            transportsMetaDataDAO.clearCache();

            //test
            transportsMetaDataDAO.delete(transport);
            assertThrows(DalException.class, () -> transportsMetaDataDAO.select(TransportMetaData.getLookupObject(transport.transportId())));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void delete_does_not_exist() {
        assertThrows(DalException.class, () -> transportsMetaDataDAO.delete(transport));
    }

    @Test
    void exists_not_in_cache(){
        try {
            //set up
            transportsMetaDataDAO.insert(transport);
            transportsMetaDataDAO.clearCache();

            //test
            assertTrue(transportsMetaDataDAO.exists(transport));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void exists_in_cache(){
        try {
            //set up
            transportsMetaDataDAO.insert(transport);

            //test
            assertTrue(transportsMetaDataDAO.exists(transport));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }


    @Test
    void exists_does_not_exist(){
        try {
            assertFalse(transportsMetaDataDAO.exists(transport));
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getCounter(){
        try {
            assertEquals(1, transportsMetaDataDAO.selectCounter());
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void incrementCounter(){
        try {
            transportsMetaDataDAO.incrementCounter();
            assertEquals(2, transportsMetaDataDAO.selectCounter());
            transportsMetaDataDAO.incrementCounter();
            assertEquals(3, transportsMetaDataDAO.selectCounter());
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void resetCounter(){
        try {
            transportsMetaDataDAO.incrementCounter();
            transportsMetaDataDAO.incrementCounter();
            transportsMetaDataDAO.resetCounter();
            assertEquals(1, transportsMetaDataDAO.selectCounter());
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insert_counter(){
        try{
            transportsMetaDataDAO.insertCounter(5);
            assertEquals(5, transportsMetaDataDAO.selectCounter());
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }


    private void assertDeepEquals(TransportMetaData transport1, TransportMetaData transport2) {
        assertEquals(transport1.transportId(), transport2.transportId());
        assertEquals(transport1.driverId(), transport2.driverId());
        assertEquals(transport1.truckId(), transport2.truckId());
        assertEquals(transport1.departureTime(), transport2.departureTime());
        assertEquals(transport1.weight(), transport2.weight());
    }
}