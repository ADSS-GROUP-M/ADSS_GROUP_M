package dataAccessLayer.transportModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.transportModule.TransportsController;
import dataAccessLayer.DalFactory;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalAssociationClasses.transportModule.DeliveryRoute;
import dataAccessLayer.dalAssociationClasses.transportModule.SiteRoute;
import dataAccessLayer.dalAssociationClasses.transportModule.TransportMetaData;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.employeeModule.EmployeeDAO;
import domainObjects.transportModule.Driver;
import domainObjects.transportModule.ItemList;
import domainObjects.transportModule.Site;
import domainObjects.transportModule.Truck;
import exceptions.DalException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryRoutesDAOTest {

    private DeliveryRoutesDAO dao;
    private Site source;
    private Site dest1;
    private Site dest2;
    private Truck truck;
    private Driver driver;
    private ItemList itemList;
    private SiteRoute route1;
    private SiteRoute route2;
    private TransportMetaData transport;
    private TransportsMetaDataDAO transportsMetaDataDAO;
    private Employee employee;
    private HashMap<String, LocalTime> arrivalTimes;
    private SitesDAO sitesDAO;
    private TrucksDAO trucksDAO;
    private EmployeeDAO employeeDAO;
    private DriversDAO driversDAO;
    private ItemListsDAO itemListsDAO;
    private SitesRoutesDAO routesDAO;
    private DalFactory factory;
    private DeliveryRoute deliveryRoute;

    @BeforeEach
    void setUp() {
        try {
            DalFactory.clearTestDB();

            //sites set up
            source = new Site("source1", "source1 name", "zone1", "12345","kobi", Site.SiteType.SUPPLIER);
            dest1 = new Site("dest1", "dest1 name", "zone1", "12345","kobi", Site.SiteType.SUPPLIER);
            dest2 = new Site("dest2", "dest2 name", "zone1", "12345","kobi", Site.SiteType.SUPPLIER);

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
            route1 = new SiteRoute(source.address(), dest1.address(), 10,10);
            route2 = new SiteRoute(dest1.address(), dest2.address(), 10,10);

            transport = new TransportMetaData(1,
                    driver.id(),
                    truck.id(),
                    LocalDateTime.of(2023, 2, 2, 12, 0),
                    15000
            );

            //initialize transport arrival times
            arrivalTimes = new HashMap<>();
            arrivalTimes.put(source.name(), LocalTime.of(12, 0));
            arrivalTimes.put(dest1.name(), LocalTime.of(12, (int) route1.duration()));
            arrivalTimes.put(dest2.name(), LocalTime.of(12, (int)(route1.duration() + route2.duration() + TransportsController.AVERAGE_TIME_PER_VISIT)));

            List<String> route = List.of(source.name(), dest1.name(), dest2.name());
            HashMap<String,Integer> itemLists = new HashMap<>(){{
                put(source.name(),-1);
                put(dest1.name(),1);
                put(dest2.name(),1);
            }};

            deliveryRoute = new DeliveryRoute(
                    transport.transportId(),
                    route,
                    itemLists,
                    arrivalTimes
            );

            factory = new DalFactory(TESTING_DB_NAME);
            sitesDAO = factory.sitesDAO();
            trucksDAO = factory.trucksDAO();
            employeeDAO = factory.employeeDAO();
            driversDAO = factory.driversDAO();
            itemListsDAO = factory.itemListsDAO();
            transportsMetaDataDAO = factory.transportsMetaDataDAO();
            routesDAO = factory.sitesRoutesDAO();
            dao = factory.deliveryRoutesDAO();

            sitesDAO.insert(source);
            sitesDAO.insert(dest1);
            sitesDAO.insert(dest2);
            routesDAO.insert(route1);
            routesDAO.insert(route2);
            trucksDAO.insert(truck);
            employeeDAO.insert(employee);
            driversDAO.insert(driver);
            itemListsDAO.insert(itemList);
            transportsMetaDataDAO.insert(transport);
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

            dao.insert(deliveryRoute);
            DeliveryRoute selected = dao.select(DeliveryRoute.getLookupObject(transport.transportId()));
            assertDeepEquals(deliveryRoute, selected);

        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void selectAll() {
        try {
            dao.insert(deliveryRoute);
            List<DeliveryRoute> selected = dao.selectAll();
            assertEquals(1, selected.size());
            assertDeepEquals(deliveryRoute, selected.get(0));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void update() {
        try{
            //set up
            DeliveryRoute updated = new DeliveryRoute(
                    deliveryRoute.transportId(),
                    deliveryRoute.route(),
                    deliveryRoute.itemLists(),
                    new HashMap<>(){{
                        put(source.name(),LocalTime.of(13,0));
                        put(dest1.name(),LocalTime.of(14,0));
                        put(dest2.name(),LocalTime.of(15,0));
                    }}
            );

            dao.insert(updated);

            //test
            dao.update(deliveryRoute);
            dao.clearCache();
            DeliveryRoute selected = dao.select(DeliveryRoute.getLookupObject(transport.transportId()));
            assertDeepEquals(deliveryRoute, selected);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void delete() {
        try {
            //set up
            dao.insert(deliveryRoute);
            dao.clearCache();

            assertTrue(dao.exists(DeliveryRoute.getLookupObject(1)));
            dao.delete(deliveryRoute);
            assertFalse(dao.exists(DeliveryRoute.getLookupObject(1)));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void exists_no_cache() {
        try {
            assertFalse(dao.exists(DeliveryRoute.getLookupObject(1)));
            dao.insert(deliveryRoute);
            dao.clearCache();
            assertTrue(dao.exists(DeliveryRoute.getLookupObject(1)));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void exists_with_cache() {
        try {
            assertFalse(dao.exists(DeliveryRoute.getLookupObject(1)));
            dao.insert(deliveryRoute);
            assertTrue(dao.exists(DeliveryRoute.getLookupObject(1)));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void getObjectFromResultSet() {
        SQLExecutor cursor = factory.cursor();
        try{
            //set up
            dao.insert(deliveryRoute);

            //test
            String query = String.format("SELECT * FROM delivery_routes WHERE transport_id = %d; ",transport.transportId());
            OfflineResultSet resultSet = cursor.executeRead(query);
            DeliveryRoute selected = dao.getObjectFromResultSet(resultSet);
            assertDeepEquals(deliveryRoute, selected);
        } catch (SQLException | DalException e){
            fail(e.getMessage(),e.getCause());
        }
    }

    private void assertDeepEquals(DeliveryRoute route1, DeliveryRoute route2) {
        assertEquals(route1.transportId(), route2.transportId());
        assertEquals(route1.route(), route2.route());
        assertEquals(route1.itemLists(), route2.itemLists());
        assertEquals(route1.estimatedArrivalTimes(), route2.estimatedArrivalTimes());
    }
}