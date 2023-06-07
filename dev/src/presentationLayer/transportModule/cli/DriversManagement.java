package presentationLayer.transportModule.cli;

import domainObjects.transportModule.Driver;
import serviceLayer.transportModule.ResourceManagementService;
import utils.JsonUtils;
import utils.Response;

public class DriversManagement {

    private final CLIData cliData;

    private final ResourceManagementService rms;

    public DriversManagement(CLIData cliData, ResourceManagementService rms) {
        this.cliData = cliData;
        this.rms = rms;
    }

    void manageDrivers() {
        while (true) {
            System.out.println("=========================================");
            System.out.println("Drivers management");
            System.out.println("Please select an option:");
            System.out.println("1. Update driver's license");
            System.out.println("2. View full driver information");
            System.out.println("3. View all drivers");
            System.out.println("4. Return to previous menu");
            int option = cliData.readInt();
            switch (option) {
                case 1 -> updateLicense();
                case 2 -> viewDriver();
                case 3 -> viewAllDrivers();
                case 4 -> {
                    return;
                }
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private void updateLicense() {
        System.out.println("=========================================");
        System.out.println("Select driver to update:");
        Driver driver = cliData.pickDriver(true);
        if (driver == null) {
            return;
        }
        System.out.println("Select new license type:");
        Driver.LicenseType licenseType = pickLicenseType();
        if (licenseType == null) {
            return;
        }
        updateDriverHelperMethod(driver.id(), driver.name(), licenseType);
    }

    /**
     * @deprecated no longer used
     */
    @Deprecated
    private void createDriver() {
        System.out.println("=========================================");
        System.out.println("Enter driver details:");
        String id = cliData.readLine("Employee ID: ");
        String fullName = cliData.readLine("Name: ");
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
            cliData.drivers().put(id, newDriver);
        }
        System.out.println("\n"+response.message());
    }

    /**
     * @deprecated no longer used
     */
    @Deprecated
    private void updateDriver() {
        while (true) {
            System.out.println("=========================================");
            System.out.println("Select driver to update:");
            Driver driver = cliData.pickDriver(true);
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
                int option = cliData.readInt();
                switch (option) {
                    case 1 -> {
                        String name = cliData.readLine("Name: ");
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
            cliData.drivers().put(id, updatedDriver);
        }
        System.out.println("\n"+response.message());
    }

    /**
     * @deprecated no longer used
     */
    @Deprecated
    private void removeDriver() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select driver to remove:");
            Driver driver = cliData.pickDriver(true);
            if(driver == null) {
                return;
            }
            System.out.println("=========================================");
            System.out.println("Driver details:");
            printDriverDetails(driver);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to remove this driver? (y/n)");
            String option = cliData.readLine();
            switch(option) {
                case "y" ->{
                    String json = driver.toJson();
                    String responseJson = rms.removeDriver(json);
                    Response response = JsonUtils.deserialize(responseJson, Response.class);
                    if(response.success()) {
                        cliData.drivers().remove(driver.id());
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
            String driverId = cliData.readLine("Enter employee ID of driver to view (enter 'done!' to return to previous menu): ");
            if(driverId.equalsIgnoreCase("done!")) {
                return;
            }
            Driver driver = cliData.drivers().get(driverId);
            System.out.println("=========================================");
            System.out.println("Driver details:");
            printDriverDetails(driver);
            System.out.println("=========================================");
            System.out.println("\nEnter 'done!' to return to previous menu");
            cliData.readLine();
        }
    }

    private void viewAllDrivers() {
        System.out.println("=========================================");
        System.out.println("All drivers:");
        for(Driver driver : cliData.drivers().values()){
            System.out.println("-----------------------------------------");
            printDriverDetails(driver);
        }
        System.out.println("\nEnter 'done!' to return to previous menu");
        cliData.readLine();
    }

    private void printDriverDetails(Driver driver) {
        System.out.println("Employee ID:  " + driver.id());
        System.out.println("Name:         " + driver.name());
        System.out.println("License type: " + driver.licenseType());
    }

    private Driver.LicenseType pickLicenseType() {
        while(true){
            int i = 0;
            for (; i < Driver.LicenseType.values().length; i++) {
                System.out.println((i+1) + ". " + Driver.LicenseType.values()[i]);
            }
            System.out.println((i+1) + ". Cancel");
            int option = cliData.readInt()-1;
            if(option == Driver.LicenseType.values().length) {
                return null;
            }
            if (option < 0 || option > Driver.LicenseType.values().length) {
                System.out.println("Invalid option!");
                continue;
            }
            return Driver.LicenseType.values()[option];
        }
    }
}
