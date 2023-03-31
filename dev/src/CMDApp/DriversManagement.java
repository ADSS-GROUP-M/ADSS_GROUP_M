package CMDApp;

import static CMDApp.Main.*;
import static CMDApp.Main.sites;

public class DriversManagement {
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
                case 1:
                    createDriver();
                    break;
                case 2:
                    updateDriver();
                    break;
                case 3:
                    removeDriver();
                    break;
                case 4:
                    getDriver();
                    break;
                case 5:
                    getAllDrivers();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void createDriver() {
        System.out.println("=========================================");
        System.out.println("Enter driver details:");
        int id = getInt("Employee ID: ");
        String fullName = getString("Name: ");
        String licenseType = getString("License type: ");
        //TODO: code for adding driver

        System.out.println("\nDriver added successfully!");
    }

    private static void updateDriver() {
        while (true) {
            System.out.println("=========================================");
            System.out.println("Select driver to update:");
            //TODO: code for fetching drivers

            int driverId = pickDriver(true);
            if (driverId == -1) return;
            while (true) {
                System.out.println("=========================================");
                System.out.println("Driver details:");
                System.out.println("Employee ID: " + drivers[driverId]);
                System.out.println("Name: " + drivers[driverId]);
                System.out.println("License type: " + drivers[driverId]);
                System.out.println("=========================================");
                System.out.println("Please select an option:");
                System.out.println("1. Update name");
                System.out.println("2. Update license type");
                System.out.println("3. Return to previous menu");
                int option = getInt();
                switch (option) {
                    case 1:
                        String fullName = getString("Name: ");
                        break;
                    case 2:
                        String licenseType = getString("License type: ");
                        break;
                    case 3:
                        return;
                }
                //TODO: code for updating driver
                System.out.println("\nDriver updated successfully!");
                break;
            }
        }
    }

    private static void removeDriver() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select driver to remove:");
            int driverId = pickDriver(true);
            if(driverId == -1) return;
            System.out.println("=========================================");
            System.out.println("Driver details:");
            System.out.println("Employee ID: " + drivers[driverId]);
            System.out.println("Name: " + drivers[driverId]);
            System.out.println("License type: " + drivers[driverId]);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to remove this driver? (y/n)");
            String option = getString();
            switch(option){
                case "y":
                    //TODO: code for removing driver
                    System.out.println("\nDriver removed successfully!");
                    break;
                case "n":
                    break;
                default:
                    System.out.println("Invalid option!");
                    continue;
        }
    }

    private static void getDriver() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Select driver to view:");
            //TODO: code for fetching drivers

            int driverId = pickDriver(true);
            if(driverId == -1) return;
            System.out.println("=========================================");
            System.out.println("Driver details:");
            System.out.println("Employee ID: " + drivers[driverId]);
            System.out.println("Name: " + drivers[driverId]);
            System.out.println("License type: " + drivers[driverId]);
            System.out.println("=========================================");
            System.out.println("Press enter to return to previous menu");
            getString();
        }

    }

    private static void getAllDrivers() {
        System.out.println("=========================================");
        System.out.println("All drivers:");

        //TODO: code for fetching drivers

        for(int i = 0; i < drivers.length; i++){
            System.out.println("-----------------------------------------");
            System.out.println("Employee ID: " + drivers[i]);
            System.out.println("Name: " + drivers[i]);
            System.out.println("License type: " + drivers[i]);
        }
    }
}
