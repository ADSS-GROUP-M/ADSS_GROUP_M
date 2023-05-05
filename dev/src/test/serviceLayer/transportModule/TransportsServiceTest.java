package serviceLayer.transportModule;

import businessLayer.transportModule.*;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.transportModule.DistanceBetweenSites;
import dataAccessLayer.transportModule.SitesDistancesDAO;
import dataAccessLayer.transportModule.TransportsDAO;
import objects.transportObjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import serviceLayer.employeeModule.Objects.SShiftType;
import serviceLayer.employeeModule.Services.EmployeesService;
import utils.JsonUtils;
import utils.Response;
import utils.transportUtils.TransportException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransportsServiceTest {

    public static final String SOURCE_ADDRESS = "sourceAddress";
    public static final String DESTINATION_ADDRESS_1 = "destinationAddress1";
    public static final String DESTINATION_ADDRESS_2 = "destinationAddress2";
    public static final String DRIVER_ID = "123";
    public static final String TRUCK_ID = "123ABC";
    public static final int ITEM_LIST_ID_1 = 1;
    public static final int ITEM_LIST_ID_2 = 2;
    TransportsService transportsService;
    TransportsController transportController;
    ItemListsController itemListsController;
    TrucksController trucksController;
    SitesController sitesController;
    EmployeesService employeesService;
    DriversController driversController;
    TransportsDAO transportsDAO;
    SitesDistancesDAO sitesDistancesDAO;
    Transport transport;
    Site source;
    Driver driver;
    Truck truck;
    Site destination1;
    Site destination2;
    ItemList itemList1;
    ItemList itemList2;

    @BeforeEach
    void setUp() {

        itemListsController = mock(ItemListsController.class);
        trucksController = mock(TrucksController.class);
        sitesController = mock(SitesController.class);
        driversController = mock(DriversController.class);
        transportsDAO = mock(TransportsDAO.class);
        sitesDistancesDAO = mock(SitesDistancesDAO.class);

        try {
            transportController = new TransportsController(
                    trucksController,
                    driversController,
                    sitesController,
                    itemListsController,
                    transportsDAO,
                    sitesDistancesDAO
            );
        } catch (TransportException e) {
            fail(e);
        }

        employeesService = mock(EmployeesService.class);
        transportController.injectDependencies(employeesService);
        transportsService = new TransportsService(transportController);

        source = new Site("zone1", SOURCE_ADDRESS, "0545555550", "contactNameSource", Site.SiteType.LOGISTICAL_CENTER);
        destination1 = new Site("zone1", DESTINATION_ADDRESS_1, "0545555551", "contactNameDest1", Site.SiteType.BRANCH);
        destination2 = new Site("zone1", DESTINATION_ADDRESS_2, "0545555552", "contactNameDest2", Site.SiteType.BRANCH);
        driver = new Driver(DRIVER_ID, "driverName", Driver.LicenseType.C3);
        truck = new Truck(TRUCK_ID, "model", 2000, 25000, Truck.CoolingCapacity.FROZEN);
        itemList1 = new ItemList(ITEM_LIST_ID_1,
                new HashMap<>() {{
                    put("item1", 1);
                    put("item2", 2);
                }},
                new HashMap<>() {{
                    put("item3", 3);
                    put("item4", 4);
                }}
        );
        itemList2 = new ItemList(ITEM_LIST_ID_2,
                new HashMap<>() {{
                    put("item5", 5);
                    put("item6", 6);
                }},
                new HashMap<>() {{
                    put("item7", 7);
                    put("item8", 8);
                }}
        );

        transport = new Transport(
                source.address(),
                new LinkedList<>() {{
                    add(destination1.address());
                    add(destination2.address());
                }},
                new HashMap<>() {{
                    put(destination1.address(), itemList1.id());
                    put(destination2.address(), itemList2.id());
                }},
                driver.toJson(),
                truck.toJson(),
                LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(12, 0)),
                15000
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addTransport() {

        try {
            when(driversController.driverExists(driver.id())).thenReturn(true);
            when(driversController.getDriver(driver.id())).thenReturn(driver);
            when(transportsDAO.selectCounter()).thenReturn(1);
            when(sitesController.siteExists(source.address())).thenReturn(true);
            when(sitesController.siteExists(destination1.address())).thenReturn(true);
            when(sitesController.siteExists(destination2.address())).thenReturn(true);
            when(sitesController.getSite(source.address())).thenReturn(source);
            when(sitesController.getSite(destination1.address())).thenReturn(destination1);
            when(sitesController.getSite(destination2.address())).thenReturn(destination2);
            when(trucksController.getTruck(TRUCK_ID)).thenReturn(truck);
            when(itemListsController.getItemList(ITEM_LIST_ID_1)).thenReturn(itemList1);
            when(itemListsController.getItemList(ITEM_LIST_ID_2)).thenReturn(itemList2);
            when(trucksController.truckExists(truck.id())).thenReturn(true);
            when(itemListsController.listExists(itemList1.id())).thenReturn(true);
            when(itemListsController.listExists(itemList2.id())).thenReturn(true);
            DistanceBetweenSites distance1 = DistanceBetweenSites.getLookupObject(source.address(), destination1.address());
            DistanceBetweenSites distance2 = DistanceBetweenSites.getLookupObject(destination1.address(), destination2.address());
            when(sitesDistancesDAO.select(distance1)).thenReturn(new DistanceBetweenSites(distance1, 100));
            when(sitesDistancesDAO.select(distance2)).thenReturn(new DistanceBetweenSites(distance2, 100));
        } catch (DalException | TransportException e) {
            fail(e);
        }

        String json = transportsService.addTransport(transport.toJson());
        Response response = JsonUtils.deserialize(json, Response.class);
        assertTrue(response.success());
        assertEquals(1, response.dataToInt());

    }

    @Test
    void addTransportPredefinedId(){

    }

    @Test
    void updateTransport() {

    }

    @Test
    void updateTransportDoesNotExist(){

    }

    @Test
    void removeTransport() {

    }

    @Test
    void removeTransportDoesNotExist(){

    }

    @Test
    void getTransport() {

    }

    @Test
    void getTransportDoesNotExist(){

    }

    @Test
    void getAllTransports() {

    }

    @Test
    void createTransportWithTooMuchWeight(){

    }

    @Test
    void createTransportWithBadLicense(){

    }

    @Test
    void createTransportWithBadLicenseAndTooMuchWeight(){

    }
    @Test
    void createTransportEverythingDoesNotExist(){

    }

//    private Transport fetchAddedTransport(int _id) {
//        String _json = Transport.getLookupObject(_id).toJson();
//        String _responseJson = ts.getTransport(_json);
//        Response _response = JsonUtils.deserialize(_responseJson, Response.class);
//        return Transport.fromJson(_response.data());
//    }
}