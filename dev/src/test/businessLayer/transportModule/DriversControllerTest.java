package businessLayer.transportModule;

import dataAccessLayer.transportModule.DriversDAO;
import domainObjects.transportModule.Driver;
import exceptions.DalException;
import exceptions.TransportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DriversControllerTest {

    public static final String DRIVER_ID = "1";
    private DriversController controller;
    private DriversDAO dao;
    private Driver driver;


    @BeforeEach
    void setUp() {
        dao = mock(DriversDAO.class);
        controller = new DriversController(dao);
        driver = new Driver(DRIVER_ID,"moshe", Driver.LicenseType.C3);
    }

    @Test
    void addDriver() {
        try{
            when(dao.exists(Driver.getLookupObject(DRIVER_ID))).thenReturn(false);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertDoesNotThrow(() -> controller.addDriver(driver));
    }

    @Test
    void addDriverAlreadyExists(){
        try{
            when(dao.exists(Driver.getLookupObject(DRIVER_ID))).thenReturn(true);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertThrows(TransportException.class, () -> controller.addDriver(driver));
    }

    @Test
    void removeDriver() {
        try{
            when(dao.exists(Driver.getLookupObject(DRIVER_ID))).thenReturn(true);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertDoesNotThrow(() -> controller.removeDriver(DRIVER_ID));
    }

    @Test
    void removeDriverDoesNotExist(){
        try{
            when(dao.exists(Driver.getLookupObject(DRIVER_ID))).thenReturn(false);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertThrows(TransportException.class, () -> controller.removeDriver(DRIVER_ID));
    }

    @Test
    void updateDriver() {
        try{
            when(dao.exists(Driver.getLookupObject(DRIVER_ID))).thenReturn(true);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertDoesNotThrow(() -> controller.updateDriver(driver.id(), driver));
    }

    @Test
    void updateDriverDoesNotExist(){
        try{
            when(dao.exists(Driver.getLookupObject(DRIVER_ID))).thenReturn(false);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertThrows(TransportException.class, () -> controller.updateDriver(driver.id(), driver));
    }

    @Test
    void getDriver() {
        try{
            when(dao.exists(Driver.getLookupObject(DRIVER_ID))).thenReturn(true);
            when(dao.select(Driver.getLookupObject(DRIVER_ID))).thenReturn(driver);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        Driver fetched = assertDoesNotThrow(() -> controller.getDriver(DRIVER_ID));
        assertDeepEquals(driver, fetched);
    }

    @Test
    void getDriverDoesNotExist(){
        try{
            when(dao.exists(Driver.getLookupObject(DRIVER_ID))).thenReturn(false);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertThrows(TransportException.class, () -> controller.getDriver(DRIVER_ID));
    }

    @Test
    void getAllDrivers() {
        Driver driver2 = new Driver("2","david", Driver.LicenseType.C1);
        try{
            when(dao.selectAll()).thenReturn(List.of(driver, driver2));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        List<Driver> fetched = assertDoesNotThrow(() -> controller.getAllDrivers());
        assertEquals(2, fetched.size());
        assertDeepEquals(driver, fetched.get(0));
        assertDeepEquals(driver2, fetched.get(1));
    }

    private void assertDeepEquals(Driver driver1, Driver driver2) {
        assertEquals(driver1.id(), driver2.id());
        assertEquals(driver1.name(), driver2.name());
        assertEquals(driver1.licenseType(), driver2.licenseType());
    }
}