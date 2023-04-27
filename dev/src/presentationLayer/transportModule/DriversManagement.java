package presentationLayer.transportModule;

import serviceLayer.transportModule.ResourceManagementService;
import objects.transportObjects.Driver;
import utils.JsonUtils;
import utils.Response;

public class DriversManagement {

    private final UiData uiData;

    private final ResourceManagementService rms;

    public DriversManagement(UiData uiData, ResourceManagementService rms) {
        this.uiData = uiData;
        this.rms = rms;
    }

    void manageDrivers() {
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
            int option = uiData.readInt();
            switch (option) {
                case 1 -> createDriver();
                case 2 -> updateDriver();
                case 3 -> removeDriver();
                case 4 -> viewDriver();
                case 5 -> viewAllDrivers();
                case 6 -> {
                    return;
                }
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private void createDriver() {
        System.out.println("=========================================");
        System.out.println("Enter driver details:");
        String id = uiData.readLine("Employee ID: ");
        String fullName = uiData.readLine("Name: ");
        System.out.println("License type: ");
        Driver.LicenseType licenseType = pickLicenseType();
        if (licenseType == null) {
            return;
        }
        Driver newDriver = new Driver(id, fullName, licenseType);
        String json = newDriver.toJson();
        String responseJson = rms.addDriver(json);
        Response response = JsonUtils.deserialize(responseJson, Response.class);
        if(response.success()) {
            uiData.drivers().put(id, newDriver);
        }
        System.out.println("\n"+response.message());
    }

    private void updateDriver() {
        while (true) {
            System.out.println("=========================================");
            System.out.println("Select driver to update:");
            Driver driver = uiData.pickDriver(true);
            if (driver == null) {
                return;
            }
            while (true) {
                System.out.println("=========================================");
                printDriverDetails(driver);
                System.out.println("=========================================");
                System.out.println("Please select an option:");
                System.out.println("1. Update name");
                System.out.println("2. Update license type");
                System.out.println("3. Return to previous menu");
                int option = uiData.readInt();
                switch (option) {
                    case 1 -> {
                        String name = uiData.readLine("Name: ");
                        updateDriverHelperMethod(driver.id(), name, driver.licenseType());
                    }
                    case 2 -> {
                        System.out.println("License type: ");
                        Driver.LicenseType licenseType = pickLicenseType();
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

    private void updateDriverHelperMethod(String id, String name, Driver.LicenseType licenseType) {
        Driver updatedDriver = new Driver(id, name, licenseType);
        String json = updatedDriver.toJson();
        String responseJson = rms.updateDriver(json);
        Response response = JsonUtils.deserialize(responseJson, Response.class);
        if(response.success()) {
            uiData.drivers().put(id, updatedDriver);
        }
        System.out.println("\n"+response.message());
    }

    private void removeDriver() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select driver to remove:");
            Driver driver = uiData.pickDriver(true);
            if(driver == null) {
                return;
            }
            System.out.println("=========================================");
            System.out.println("Driver details:");
            printDriverDetails(driver);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to remove this driver? (y/n)");
            String option = uiData.readLine();
            switch(option) {
                case "y" ->{
                    String json = driver.toJson();
                    String responseJson = rms.removeDriver(json);
                    Response response = JsonUtils.deserialize(responseJson, Response.class);
                    if(response.success()) {
                        uiData.drivers().remove(driver.id());
                    }
                    System.out.println("\n"+response.message());
                }
                case "n" ->{}
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private void viewDriver() {
        while(true){
            System.out.println("=========================================");
            int driverId = uiData.readInt("Enter employee ID of driver to view (enter '-1' to return to previous menu): ");
            if(driverId == -1) {
                return;
            }
            Driver driver = uiData.drivers().get(driverId);
            System.out.println("=========================================");
            System.out.println("Driver details:");
            printDriverDetails(driver);
            System.out.println("=========================================");
            System.out.println("\nEnter 'done!' to return to previous menu");
            uiData.readLine();
        }
    }

    private void viewAllDrivers() {
        System.out.println("=========================================");
        System.out.println("All drivers:");
        for(Driver driver : uiData.drivers().values()){
            System.out.println("-----------------------------------------");
            printDriverDetails(driver);
        }
        System.out.println("\nEnter 'done!' to return to previous menu");
        uiData.readLine();
    }

    private void printDriverDetails(Driver driver) {
        System.out.println("Employee ID:  " + driver.id());
        System.out.println("Name:         " + driver.name());
        System.out.println("License type: " + driver.licenseType());
    }

    private Driver.LicenseType pickLicenseType() {
        for (int i = 0; i < Driver.LicenseType.values().length; i++) {
            System.out.println((i+1) + ". " + Driver.LicenseType.values()[i]);
        }
        int option = uiData.readInt()-1;
        if (option < 0 || option >= Driver.LicenseType.values().length) {
            System.out.println("Invalid license type!");
            return null;
        }
        return Driver.LicenseType.values()[option];
    }
}
