package dataAccessLayer.transportModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.transportModule.*;
import dataAccessLayer.DalFactory;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.employeeModule.EmployeeDAO;
import objects.transportObjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.transportUtils.TransportException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TransportsDAOTest {

    private TransportsDAO transportsDAO;
    private ItemListsDAO itemListsDAO;
    private DriversDAO driversDAO;
    private TrucksDAO trucksDAO;
    private SitesDAO sitesDAO;
    private EmployeeDAO employeeDAO;

    private Transport transport;
    private ItemList itemList;
    private Driver driver;
    private Truck truck;
    private Site site;
    private Site source;
    private Employee employee;
    private SitesDistancesDAO distancesDAO;
    private TransportsController transportsController;

    @BeforeEach
    void setUp() {
        site = new Site("TODO: INSERT NAME HERE", "site address", "zone1", "12345","kobi", Site.SiteType.SUPPLIER);
        source = new Site("TODO: INSERT NAME HERE", "source address", "zone1", "12345","kobi", Site.SiteType.BRANCH);
        truck = new Truck("1", "model1", 1000, 20000, Truck.CoolingCapacity.FROZEN);
        employee = new Employee("name1","12345","Poalim",50, LocalDate.of(1999,10,10),"conditions","details");
        employee.addRole(Role.Driver);
        employee.addRole(Role.GeneralWorker);
        driver = new Driver(employee.getId(),employee.getName(), Driver.LicenseType.C3);

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

        transport = new Transport(1,
                source.address(),
                new LinkedList<>(){{
                    add(site.address());
                }} ,
                new HashMap<>(){{
                    put(site.address(), 1);
                }},
                driver.id(),
                truck.id(),
                LocalDateTime.of(2020, 1, 1, 1, 1),
                15000
        );

        DistanceBetweenSites distance = new DistanceBetweenSites(
                source.address(),
                site.address(),
                100,100
        );

        try {
            DalFactory factory = new DalFactory(TESTING_DB_NAME);
            sitesDAO = factory.sitesDAO();
            trucksDAO = factory.trucksDAO();
            employeeDAO = factory.employeeDAO();
            driversDAO = factory.driversDAO();
            itemListsDAO = factory.itemListsDAO();
            transportsDAO = factory.transportsDAO();
            distancesDAO = factory.sitesDistancesDAO();
            TrucksController trucksController = mock(TrucksController.class);
            ItemListsController itemListsController = mock(ItemListsController.class);
            DriversController driversController = mock(DriversController.class);
            SitesDistancesController distancesController = mock(SitesDistancesController.class);
            SitesController sitesController =  new SitesController(sitesDAO, distancesDAO, distancesController);
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

            sitesDAO.insert(site);
            sitesDAO.insert(source);
            distancesDAO.insert(distance);
            trucksDAO.insert(truck);
            employeeDAO.insert(employee);
            driversDAO.insert(driver);
            itemListsDAO.insert(itemList);
            transportsController.initializeEstimatedArrivalTimes(transport);
            transportsDAO.insert(transport);
        } catch (DalException | TransportException e) {
            fail(e);
        }
        transportsDAO.clearCache();
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    void select() {
        try {
            Transport selected = transportsDAO.select(transport);
            assertDeepEquals(transport, selected);
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void selectAll() {
        
        //set up
        DistanceBetweenSites distance = new DistanceBetweenSites(
                site.address(),
                site.address(),
                100,100
        );
        try {
            distancesDAO.insert(distance);
        } catch (DalException e) {
            fail(e);
        }

        LinkedList<Transport> transports = new LinkedList<>();
        transports.add(transport);
        List.of(2,3,4,5,6).forEach(
            i -> {
                try {
                    Transport toAdd = new Transport(i,
                            site.address(),
                            new LinkedList<>(){{
                                add(site.address());
                            }} ,
                            new HashMap<>(){{
                                put(site.address(), 1);
                            }},
                            driver.id(),
                            truck.id(),
                            LocalDateTime.of(2020, 1, 1, 1, 1),
                            15000
                    );
                    transports.add(toAdd);
                    transportsController.initializeEstimatedArrivalTimes(toAdd);
                    transportsDAO.insert(toAdd);
                } catch (DalException | TransportException e) {
                    fail(e);
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
            fail(e);
        }
    }

    @Test
    void insert() {
        try {
            Transport transport2 = new Transport(2,
                    source.address(),
                    new LinkedList<>() {{
                        add(site.address());
                    }},
                    new HashMap<>() {{
                        put(site.address(), 1);
                    }},
                    driver.id(),
                    truck.id(),
                    LocalDateTime.of(2020, 1, 1, 1, 1),
                    15000
            );
            try {
                transportsController.initializeEstimatedArrivalTimes(transport2);
            } catch (TransportException e) {
                fail(e);
            }
            transportsDAO.insert(transport2);
            Transport selected = transportsDAO.select(Transport.getLookupObject(transport2.id()));
            assertDeepEquals(transport2, selected);
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void update() {
        try {
            Transport updatedTransport = new Transport(transport.id(),
                    source.address(),
                    new LinkedList<>() {{
                        add(site.address());
                    }},
                    new HashMap<>() {{
                        put(site.address(), 1);
                    }},
                    driver.id(),
                    truck.id(),
                    LocalDateTime.of(1997, 2, 2, 2, 2),
                    10000
            );
            try {
                transportsController.initializeEstimatedArrivalTimes(updatedTransport);
            } catch (TransportException e) {
                fail(e);
            }
            transportsDAO.update(updatedTransport);

            Transport selected = transportsDAO.select(Transport.getLookupObject(transport.id()));
            assertDeepEquals(updatedTransport, selected);
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void delete() {
        try {
            transportsDAO.delete(transport);
            assertThrows(DalException.class, () -> transportsDAO.select(Transport.getLookupObject(transport.id())));
        } catch (DalException e) {
            fail(e);
        }
    }

    private void assertDeepEquals(Transport transport1, Transport transport2) {
        assertEquals(transport1.id(), transport2.id());
        assertEquals(transport1.source(), transport2.source());
        assertEquals(transport1.destinations(), transport2.destinations());
        assertEquals(transport1.itemLists(), transport2.itemLists());
        assertEquals(transport1.driverId(), transport2.driverId());
        assertEquals(transport1.truckId(), transport2.truckId());
        assertEquals(transport1.departureTime(), transport2.departureTime());
        assertEquals(transport1.weight(), transport2.weight());
        assertEquals(transport1.deliveryRoute().estimatedArrivalTimes(), transport2.deliveryRoute().estimatedArrivalTimes());
    }
}