package BusinessLayer;

import java.util.TreeSet;

public class DriversController {
    TreeSet <Driver> drivers;

    public DriversController(){
        drivers = new TreeSet<>();
    }

    public Driver createDriver(int id, String name, String licenseType){
        Driver driver = new Driver(id, name, licenseType);
        drivers.add(driver);
        return driver;
    }

    public  Driver getDriver(int id){
        for (Driver driver : drivers) {
            if (driver.getId() == id) {
                return driver;
            }
        }
        return null;
    }
    public Driver removeDriver(int id) throws Exception {
        Driver driver = getDriver(id);
        if (driver != null) {
            Driver removedDriver = driver;
            drivers.remove(driver);
            return removedDriver;
        }
        throw new Exception("Driver not found");
    }

    public boolean updateDriver(int id, Driver newDriver){
        Driver driver = getDriver(id);
        if (driver != null) {
            drivers.remove(driver);
            drivers.add(newDriver);
            return true;
        }
        return false;
    }








}

