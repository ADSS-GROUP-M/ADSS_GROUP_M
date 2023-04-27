package dataAccessLayer.transportModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import dataAccessLayer.DalFactory;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import dataAccessLayer.employeeModule.EmployeeDAO;
import objects.transportObjects.Driver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class DriversDAOTest {

    private DriversDAO dao;
    private EmployeeDAO empDao;

    private Employee employee;
    private Driver driver;

    @BeforeEach
    void setUp() {
        employee = new Employee("name1","12345","Poalim",50, LocalDate.of(1999,10,10),"conditions","details");
        employee.addRole(Role.Driver);
        employee.addRole(Role.GeneralWorker);
        driver = new Driver(employee.getId(),employee.getName(), Driver.LicenseType.C3);
        try {
            DalFactory factory = new DalFactory(TESTING_DB_NAME);

            empDao = factory.employeeDAO();
            dao = factory.driversDAO();
            dao.clearTable();
            empDao.clearTable();


            empDao.insert(employee);
            dao.insert(driver);
        } catch (DalException e) {
            fail(e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            dao.selectAll().forEach(driver ->{
                try {
                    dao.delete(driver);
                } catch (DalException e) {
                    fail(e);
                }
            });
            empDao.clearTable();
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void select() {
        try{
            Driver selectedDriver = dao.select(Driver.getLookupObject(driver.id()));
            assertDeepEquals(driver,selectedDriver);
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void selectAll() {

        //set up
        LinkedList<Driver> drivers = new LinkedList<>();
        drivers.add(driver);
        List.of(2,3,4,5,6).forEach(i ->{
            try {
                empDao.insert(new Employee("name"+i,i+"2345","Poalim",50, LocalDate.of(1999,10,10),"conditions","details"));
                Driver newDriver = new Driver(i + "2345", "name" + i, Driver.LicenseType.C3);
                dao.insert(newDriver);
                drivers.add(newDriver);
            } catch (DalException e) {
                fail(e);
            }
        });

        //test
        try {
            List<Driver> selectedDrivers = dao.selectAll();
            assertEquals(drivers.size(),selectedDrivers.size());
            for (int i = 0; i < drivers.size(); i++) {
                assertDeepEquals(drivers.get(i),selectedDrivers.get(i));
            }
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void insert() {
        Employee employee2 = new Employee("name2","22345","Poalim",50, LocalDate.of(1999,10,10),"conditions","details");
        Driver driver2 = new Driver(employee2.getId(),employee2.getName(), Driver.LicenseType.C3);
        try {
            empDao.insert(employee2);
            dao.insert(driver2);
            Driver selectedDriver = dao.select(Driver.getLookupObject(driver2.id()));
            assertDeepEquals(driver2,selectedDriver);
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void update() {
        Driver updatedDriver = new Driver(driver.id(),driver.name(), Driver.LicenseType.A1);
        try {
            dao.update(updatedDriver);
            Driver selectedDriver = dao.select(Driver.getLookupObject(driver.id()));
            assertDeepEquals(updatedDriver,selectedDriver);
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void delete() {
        try {
            dao.delete(driver);
            assertThrows(DalException.class,() -> dao.select(Driver.getLookupObject(driver.id())));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void getObjectFromResultSet() {
        String TABLE_NAME = "truck_drivers";
        String[] PARENT_TABLE_NAME = {"employees"};
        String query = String.format("""
                    SELECT %s.id, %s.name,license_type FROM %s
                    INNER JOIN %s ON %s.id = %s.id
                    WHERE %s.id = '%s';
                        """, TABLE_NAME, PARENT_TABLE_NAME[0] ,TABLE_NAME, PARENT_TABLE_NAME[0], TABLE_NAME, PARENT_TABLE_NAME[0], TABLE_NAME, driver.id());

        SQLExecutor executor = new SQLExecutor(TESTING_DB_NAME);
        try {
            OfflineResultSet resultSet = executor.executeRead(query);
            resultSet.next();
            Driver selectedDriver = dao.getObjectFromResultSet(resultSet);
            assertDeepEquals(driver,selectedDriver);
        } catch (SQLException e) {
            fail(e);
        }
    }

    private void assertDeepEquals(Driver driver1, Driver driver2) {
        assertEquals(driver1.id(),driver2.id());
        assertEquals(driver1.name(),driver2.name());
        assertEquals(driver1.licenseType(),driver2.licenseType());
    }
}