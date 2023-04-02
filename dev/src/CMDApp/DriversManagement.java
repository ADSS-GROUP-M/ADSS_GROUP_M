package CMDApp;

import CMDApp.Records.Driver;
import TransportModule.ServiceLayer.ResourceManagementService;

import static CMDApp.Main.*;

public class DriversManagement {

    static ResourceManagementService rms = factory.getResourceManagementService();

    static void manageDrivers() {
        while (true) {
            System.out.println("=========================================");
            System.out.println("Drivers management");
            System.out.println("Please select an option:");
            System.out.println("1. Create new driver");
            System.out.println("2. Update driver");
            System.out.println("3. Remove driver");
            System.out.println("4. View full driver information");
            System.out.println("5. View all drivers");
            System.out.println("6. Return to previous menu");
            int option = getInt();
            switch (option) {
                case 1 -> createDriver();
                case 2 -> updateDriver();
                case 3 -> removeDriver();
                case 4 -> getDriver();
                case 5 -> getAllDrivers();
                case 6 -> {
                    return;
                }
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private static void createDriver() {
        System.out.println("=========================================");
        System.out.println("Enter driver details:");
        int id = getInt("Employee ID: ");
        String fullName = getLine("Name: ");
        String licenseType = getLine("License type: ");
        Driver newDriver = new Driver(id, fullName, licenseType);
        String json = JSON.serialize(newDriver);
        String responseJson = rms.addDriver(json);
        Response<String> response = JSON.deserialize(responseJson, Response.class);
        if(response.isSuccess()) drivers.put(id, newDriver);
        System.out.println("\n"+response.getMessage());
    }

    private static void updateDriver() {
        while (true) {
            System.out.println("=========================================");
            System.out.println("Select driver to update:");
            Driver driver = pickDriver(true);
            if (driver == null) return;
            while (true) {
                System.out.println("=========================================");
                printDriverDetails(driver);
                System.out.println("=========================================");
                System.out.println("Please select an option:");
                System.out.println("1. Update name");
                System.out.println("2. Update license type");
                System.out.println("3. Return to previous menu");
                int option = getInt();
                switch (option) {
                    case 1 -> {
                        String name = getLine("Name: ");
                        updateDriverHelperMethod(driver.id(), name, driver.licenseType());
                    }
                    case 2 -> {
                        String licenseType = getLine("License type: ");
                        updateDriverHelperMethod(driver.id(), driver.name(), licenseType);
                    }
                    case 3 -> {
                        return;
                    }
                    default -> {
                        System.out.println("\nInvalid option!");
                        continue;
                    }
                }
                break;
            }
        }
    }

    private static void updateDriverHelperMethod(int id, String name, String licenseType) {
        Driver updatedDriver = new Driver(id, name, licenseType);
        String json = JSON.serialize(updatedDriver);
        String responseJson = rms.updateDriver(json);
        Response<String> response = JSON.deserialize(responseJson, Response.class);
        if(response.isSuccess()) drivers.put(id, updatedDriver);
        System.out.println("\n"+response.getMessage());
    }

    private static void removeDriver() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select driver to remove:");
            Driver driver = pickDriver(true);
            if(driver == null) return;
            System.out.println("=========================================");
            System.out.println("Driver details:");
            printDriverDetails(driver);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to remove this driver? (y/n)");
            String option = getLine();
            switch(option) {
                case "y" ->{
                    String json = JSON.serialize(driver);
                    String responseJson = rms.removeDriver(json);
                    Response<String> response = JSON.deserialize(responseJson, Response.class);
                    if(response.isSuccess()) drivers.remove(driver.id());
                    System.out.println("\n"+response.getMessage());
                }
                case "n" ->{}
                default -> System.out.println("\nInvalid option!");
            }
        }

    }

    private static void getDriver() {
        while(true){
            System.out.println("=========================================");
            int driverId = getInt("Enter employee ID of driver to view (enter '-1' to return to previous menu): ");
            if(driverId == -1) return;
            Driver driver = drivers.get(driverId);
            System.out.println("=========================================");
            System.out.println("Driver details:");
            printDriverDetails(driver);
            System.out.println("=========================================");
            System.out.println("\nEnter 'done!' to return to previous menu");
            getLine();
        }
    }

    private static void getAllDrivers() {
        System.out.println("=========================================");
        System.out.println("All drivers:");
        for(Driver driver : drivers.values()){
            System.out.println("-----------------------------------------");
            printDriverDetails(driver);
        }
        System.out.println("\nEnter 'done!' to return to previous menu");
        getLine();
    }

    private static void printDriverDetails(Driver driver) {
        System.out.println("Employee ID: " + driver.id());
        System.out.println("Name: " + driver.name());
        System.out.println("License type: " + driver.licenseType());
    }
}
