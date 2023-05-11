package dataAccessLayer.transportModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.transportModule.*;
import dataAccessLayer.DalFactory;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import dataAccessLayer.employeeModule.EmployeeDAO;
import objects.transportObjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.transportUtils.TransportException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TransportDestinationsDAOTest {

    public static final int TRANSPORT_ID = 1;
    public static final int LIST_ID = 1;
    public static final LocalTime ARRIVAL_TIME = LocalTime.of(20, 20);
    public static final String SITE_NAME1 = "name1";
    public static final String SITE_NAME2 = "name2";
    private TransportDestinationsDAO dao;
    private TransportDestination transportDestination1;
    private TransportDestination transportDestination2;
    private TransportDestination transportDestination3;
    private TransportDestination transportDestination4;
    private DalFactory factory;
    private TransportsController transportsController;

    @BeforeEach
    void setUp() {
        try {
            Site site1 = new Site(SITE_NAME1, "address1", "zone1", "12345", "kobi", Site.SiteType.SUPPLIER);
            Site site2 = new Site(SITE_NAME2, "address2", "zone1", "12345", "kobi", Site.SiteType.SUPPLIER);
            Truck truck = new Truck("1", "model1", 1000, 20000, Truck.CoolingCapacity.FROZEN);
            Employee employee = new Employee(SITE_NAME1, "12345", "Poalim", 50, LocalDate.of(1999, 10, 10), "conditions", "details");
            employee.addRole(Role.Driver);
            employee.addRole(Role.GeneralWorker);
            Driver driver = new Driver(employee.getId(), employee.getName(), Driver.LicenseType.C3);

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
            ItemList itemList = new ItemList(LIST_ID, load, unload);

            transportDestination1 = new TransportDestination(TRANSPORT_ID, 1, SITE_NAME2, LIST_ID, ARRIVAL_TIME);

            Transport transport = new Transport(TRANSPORT_ID,
                    site1.name(),
                    new LinkedList<>(){{
                        add(site2.name());
                    }},
                    new HashMap<>(){{
                        put(site2.name(), LIST_ID);
                    }},
                    driver.id(),
                    truck.id(),
                    LocalDateTime.of(2020, 1, 1, 19, 50),
                    15000
            );

            SiteRoute distance = new SiteRoute(site1.address(), site2.address(), 40,30);

            factory = new DalFactory(TESTING_DB_NAME);
            dao = factory.transportDestinationsDAO();
            SitesDAO sitesDAO = factory.sitesDAO();
            TrucksDAO trucksDAO = factory.trucksDAO();
            EmployeeDAO employeeDAO = factory.employeeDAO();
            DriversDAO driversDAO = factory.driversDAO();
            ItemListsDAO itemListsDAO = factory.itemListsDAO();
            TransportsDAO transportsDAO = factory.transportsDAO();
            SitesRoutesDAO sitesRoutesDAO = factory.sitesDistancesDAO();
            TrucksController trucksController = mock(TrucksController.class);
            ItemListsController itemListsController = mock(ItemListsController.class);
            DriversController driversController = mock(DriversController.class);
            SitesRoutesController distancesController = mock(SitesRoutesController.class);
            SitesController sitesController =  new SitesController(sitesDAO, sitesRoutesDAO, distancesController);
            transportsController = new TransportsController(
                    trucksController,
                    driversController,
                    sitesController,
                    itemListsController,
                    transportsDAO
            );

            transportsDAO.clearTable();
            itemListsDAO.clearTable();
            driversDAO.clearTable();
            trucksDAO.clearTable();
            sitesDAO.clearTable();
            employeeDAO.clearTable();

            sitesDAO.insert(site1);
            sitesDAO.insert(site2);
            trucksDAO.insert(truck);
            employeeDAO.insert(employee);
            driversDAO.insert(driver);
            itemListsDAO.insert(itemList);
            sitesRoutesDAO.insert(distance);
            transportsController.initializeEstimatedArrivalTimes(transport);
            transportsDAO.insert(transport);


            transportDestination2 = new TransportDestination(TRANSPORT_ID, 2, SITE_NAME1, LIST_ID, ARRIVAL_TIME);
            transportDestination3 = new TransportDestination(TRANSPORT_ID, 3, SITE_NAME1, LIST_ID, ARRIVAL_TIME);
            transportDestination4 = new TransportDestination(TRANSPORT_ID, 4, SITE_NAME1, LIST_ID, ARRIVAL_TIME);

            dao.insert(transportDestination2);
            dao.insert(transportDestination3);
            dao.insert(transportDestination4);

        } catch (DalException | TransportException e) {
            fail(e);
        }
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    void select() {
        try {
            TransportDestination selected = dao.select(transportDestination2);
            assertDeepEquals(transportDestination2, selected);
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void selectAll() {
        //set up
        LinkedList<TransportDestination> expected = new LinkedList<>();
        expected.add(transportDestination1);
        expected.add(transportDestination2);
        expected.add(transportDestination3);
        expected.add(transportDestination4);
        List.of(5,6,7,8).forEach(i -> {
            try {
                TransportDestination newDestination = new TransportDestination(TRANSPORT_ID, i, SITE_NAME1, LIST_ID, ARRIVAL_TIME);
                expected.add(newDestination);
                dao.insert(newDestination);
            } catch (DalException e) {
                fail(e);
            }
        });

        //test
        try {
            List<TransportDestination> selected = dao.selectAll();
            assertEquals(expected.size(), selected.size());
            for (int i = 0; i < expected.size(); i++) {
                assertDeepEquals(expected.get(i), selected.get(i));
            }
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void selectAllRelated() {
        //set up
        LinkedList<TransportDestination> expected = new LinkedList<>();
        expected.add(transportDestination1);
        expected.add(transportDestination2);
        expected.add(transportDestination3);
        expected.add(transportDestination4);
        List.of(5,6,7,8).forEach(i -> {
            try {
                TransportDestination newDestination = new TransportDestination(TRANSPORT_ID, i, SITE_NAME1, LIST_ID, ARRIVAL_TIME);
                dao.insert(newDestination);
                expected.add(newDestination);
            } catch (DalException e) {
                fail(e);
            }
        });

        //test
        try {
            List<TransportDestination> selected = dao.selectAllRelated(Transport.getLookupObject(TRANSPORT_ID));
            assertEquals(expected.size(), selected.size());
            for (int i = 0; i < expected.size(); i++) {
                assertDeepEquals(expected.get(i), selected.get(i));
            }
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void insert() {
        try {
            TransportDestination newDestination = new TransportDestination(TRANSPORT_ID, 5, SITE_NAME1, LIST_ID, ARRIVAL_TIME);
            dao.insert(newDestination);
            TransportDestination selected = dao.select(TransportDestination.getLookupObject(
                    newDestination.transportId(),
                    newDestination.destination_index()));
            assertDeepEquals(newDestination, selected);
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void insertAll() {
        //set up
        LinkedList<TransportDestination> expected = new LinkedList<>();
        List.of(5,6,7,8).forEach(i -> {
            TransportDestination newDestination = new TransportDestination(TRANSPORT_ID, i, SITE_NAME1, LIST_ID, ARRIVAL_TIME);
            expected.add(newDestination);
        });
        try {
            dao.insertAll(expected);
        } catch (DalException e) {
            fail(e);
        }
        expected.addFirst(transportDestination4);
        expected.addFirst(transportDestination3);
        expected.addFirst(transportDestination2);
        expected.addFirst(transportDestination1);

        //test
        try {
            List<TransportDestination> selected = dao.selectAllRelated(Transport.getLookupObject(TRANSPORT_ID));
            assertEquals(expected.size(), selected.size());
            for (int i = 0; i < expected.size(); i++) {
                assertDeepEquals(expected.get(i), selected.get(i));
            }
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void update() {
        try {

            TransportDestination newDestination = new TransportDestination(
                    TRANSPORT_ID,
                    transportDestination2.destination_index(),
                    SITE_NAME2,
                    LIST_ID, ARRIVAL_TIME);
            dao.update(newDestination);
            TransportDestination selected = dao.select(TransportDestination.getLookupObject(
                    newDestination.transportId(),
                    newDestination.destination_index()));
            assertDeepEquals(newDestination, selected);
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void delete() {
        try {
            dao.delete(transportDestination4);
            List<TransportDestination> selected = dao.selectAllRelated(Transport.getLookupObject(TRANSPORT_ID));
            assertEquals(3, selected.size());
            assertDeepEquals(transportDestination1, selected.get(0));
            assertDeepEquals(transportDestination2, selected.get(1));
            assertDeepEquals(transportDestination3, selected.get(2));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void exists() {
        try {
            assertTrue(dao.exists(transportDestination2));
            assertFalse(dao.exists(TransportDestination.getLookupObject(TRANSPORT_ID, 10)));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void deleteAllRelated() {
        try {
            dao.deleteAllRelated(Transport.getLookupObject(TRANSPORT_ID));
            List<TransportDestination> selected = dao.selectAllRelated(Transport.getLookupObject(TRANSPORT_ID));
            assertEquals(0, selected.size());
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void getObjectFromResultSet() {
        SQLExecutor cursor = factory.cursor();
        try{
            String query = String.format("SELECT * FROM transport_destinations WHERE transport_id = %d AND destination_index = %d",
                    transportDestination2.transportId(),
                    transportDestination2.destination_index());
            OfflineResultSet resultSet = cursor.executeRead(query);
            resultSet.next();
            TransportDestination selected = dao.getObjectFromResultSet(resultSet);
            assertDeepEquals(transportDestination2, selected);
        } catch (SQLException e){
            fail(e);
        }
    }

    private void assertDeepEquals(TransportDestination transportDestination1, TransportDestination transportDestination2) {
        assertEquals(transportDestination1.transportId(), transportDestination2.transportId());
        assertEquals(transportDestination1.destination_index(), transportDestination2.destination_index());
        assertEquals(transportDestination1.name(), transportDestination2.name());
        assertEquals(transportDestination1.itemListId(), transportDestination2.itemListId());
        assertEquals(transportDestination1.expectedArrivalTime(), transportDestination2.expectedArrivalTime());
    }

}