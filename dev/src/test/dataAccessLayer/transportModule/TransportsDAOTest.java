package dataAccessLayer.transportModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.employeeModule.EmployeeDAO;
import objects.transportObjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
    private Employee employee;


    @BeforeEach
    void setUp() {
        site = new Site("zone1","address1","12345","kobi", Site.SiteType.SUPPLIER);
        truck = new Truck("1", "model1", 1000, 20000, Truck.CoolingCapacity.FROZEN);
        employee = new Employee("name1","12345","Poalim",50, LocalDate.of(1999,10,10),"conditions","details");
        employee.addRole(Role.Driver);
        employee.addRole(Role.GeneralWorker);
        driver = new Driver(employee.getId(),employee.getName(), Driver.LicenseType.C3);

        transport = new Transport(1,
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

        try {
            sitesDAO = new SitesDAO("TestingDB.db");
            trucksDAO = new TrucksDAO("TestingDB.db");
            employeeDAO = EmployeeDAO.getTestingInstance("TestingDB.db");
            driversDAO = new DriversDAO("TestingDB.db");
            itemListsDAO = new ItemListsDAO("TestingDB.db");
            transportsDAO = new TransportsDAO("TestingDB.db");

            transportsDAO.clearTable();
            itemListsDAO.clearTable();
            driversDAO.clearTable();
            trucksDAO.clearTable();
            sitesDAO.clearTable();
            employeeDAO.deleteAll();


            sitesDAO.insert(site);
            trucksDAO.insert(truck);
            employeeDAO.insert(employee);
            driversDAO.insert(driver);
            itemListsDAO.insert(itemList);
            transportsDAO.insert(transport);
        } catch (DalException e) {
            fail(e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            transportsDAO.selectAll().forEach(transport ->{
                try {
                    transportsDAO.delete(transport);
                } catch (DalException e) {
                    fail(e);
                }
            });
            itemListsDAO.selectAll().forEach(itemList ->{
                try {
                    itemListsDAO.delete(itemList);
                } catch (DalException e) {
                    fail(e);
                }
            });
            driversDAO.selectAll().forEach(driver ->{
                try {
                    driversDAO.delete(driver);
                } catch (DalException e) {
                    fail(e);
                }
            });
            trucksDAO.selectAll().forEach(truck ->{
                try {
                    trucksDAO.delete(truck);
                } catch (DalException e) {
                    fail(e);
                }
            });
            sitesDAO.selectAll().forEach(site ->{
                try {
                    sitesDAO.delete(site);
                } catch (DalException e) {
                    fail(e);
                }
            });
            employeeDAO.deleteAll();
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void select() {
    }

    @Test
    void selectAll() {
    }

    @Test
    void insert() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getObjectFromResultSet() {
    }
}