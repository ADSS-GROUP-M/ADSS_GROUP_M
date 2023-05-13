package dataAccessLayer.transportModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.transportModule.*;
import dataAccessLayer.DalFactory;
import dataAccessLayer.dalAssociationClasses.transportModule.SiteRoute;
import dataAccessLayer.employeeModule.EmployeeDAO;
import exceptions.DalException;
import exceptions.TransportException;
import objects.transportObjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private Site dest1;
    private Site dest2;
    private Site source;
    private Employee employee;
    private SitesRoutesDAO routesDAO;
    private SiteRoute siteRoute1;
    private SiteRoute siteRoute2;
    private Map<String, LocalTime> arrivalTimes;

    @BeforeEach
    void setUp() {

        DalFactory.clearTestDB();

        //sites set up
        source = new Site("source1", "source1 name", "zone1", "12345","kobi", Site.SiteType.SUPPLIER);
        dest1 = new Site("dest1", "dest1 name", "zone1", "12345","kobi", Site.SiteType.SUPPLIER);
        dest2 = new Site("dest2", "dest2 name", "zone1", "12345","kobi", Site.SiteType.SUPPLIER);
        source = new Site("source1", "source1 name", "zone1", "12345","kobi", Site.SiteType.BRANCH);

        //truck set up
        truck = new Truck("1", "model1", 1000, 20000, Truck.CoolingCapacity.FROZEN);

        //driver set up
        employee = new Employee("name1","12345","Poalim",50, LocalDate.of(1999,10,10),"conditions","details");
        employee.addRole(Role.Driver);
        employee.addRole(Role.GeneralWorker);
        driver = new Driver(employee.getId(),employee.getName(), Driver.LicenseType.C3);

        //itemList set up
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

        //transport set up
        siteRoute1 = new SiteRoute(
                source.address(),
                dest1.address(),
                10,10
        );
        siteRoute2 = new SiteRoute(
                dest1.address(),
                dest2.address(),
                10,10
        );
        transport = new Transport(1,
                source.name(),
                new LinkedList<>(){{
                    add(dest1.name());
                    add(dest2.name());
                }} ,
                new HashMap<>(){{
                    put(dest1.name(), itemList.id());
                    put(dest2.name(), itemList.id());
                }},
                driver.id(),
                truck.id(),
                LocalDateTime.of(2023, 2, 2, 12, 0),
                15000
        );

        //initialize transport arrival times
        arrivalTimes = new HashMap<>();
        arrivalTimes.put(dest1.name(), LocalTime.of(12, (int) siteRoute1.duration()));
        arrivalTimes.put(dest2.name(), LocalTime.of(12, (int)(siteRoute1.duration() + siteRoute2.duration() + TransportsController.AVERAGE_TIME_PER_VISIT)));
        transport.deliveryRoute().initializeArrivalTimes(arrivalTimes);


        try {
            DalFactory factory = new DalFactory(TESTING_DB_NAME);
            sitesDAO = factory.sitesDAO();
            trucksDAO = factory.trucksDAO();
            employeeDAO = factory.employeeDAO();
            driversDAO = factory.driversDAO();
            itemListsDAO = factory.itemListsDAO();
            transportsDAO = factory.transportsDAO();
            routesDAO = factory.sitesRoutesDAO();

            sitesDAO.insert(source);
            sitesDAO.insert(dest1);
            sitesDAO.insert(dest2);
            routesDAO.insert(siteRoute1);
            routesDAO.insert(siteRoute2);
            trucksDAO.insert(truck);
            employeeDAO.insert(employee);
            driversDAO.insert(driver);
            itemListsDAO.insert(itemList);
        } catch (DalException e) {
            fail(e);
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
            fail(e);
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
                            source.name(),
                            new LinkedList<>(){{
                                add(dest1.name());
                                add(dest2.name());
                            }} ,
                            new HashMap<>(){{
                                put(dest1.name(), itemList.id());
                                put(dest2.name(), itemList.id());
                            }},
                            driver.id(),
                            truck.id(),
                            LocalDateTime.of(2020, 1, 1, 1, 1),
                            15000
                    );
                    transports.add(toAdd);
                    toAdd.deliveryRoute().initializeArrivalTimes(arrivalTimes);
                    transportsDAO.insert(toAdd);
                } catch (DalException e) {
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
    void update() {
        try {
            //set up
            Transport updatedTransport = new Transport(transport.id(),
                    source.name(),
                    new LinkedList<>() {{
                        add(dest1.name());
                    }},
                    new HashMap<>() {{
                        put(dest1.name(), itemList.id());
                    }},
                    driver.id(),
                    truck.id(),
                    LocalDateTime.of(1997, 2, 2, 2, 2),
                    10000
            );
            arrivalTimes.remove(dest2.name());
            updatedTransport.deliveryRoute().initializeArrivalTimes(arrivalTimes);
            transportsDAO.insert(transport);
            transportsDAO.clearCache();

            //test
            transportsDAO.update(updatedTransport);
            transportsDAO.clearCache();
            Transport selected = transportsDAO.select(Transport.getLookupObject(transport.id()));
            assertDeepEquals(updatedTransport, selected);
        } catch (DalException e) {
            fail(e);
        }
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