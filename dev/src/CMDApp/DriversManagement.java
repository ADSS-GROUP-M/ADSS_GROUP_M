package CMDApp;

import static CMDApp.Main.getInt;
import static CMDApp.Main.getString;

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

    }

    private static void removeDriver() {

    }

    private static void getDriver() {

    }

    private static void getAllDrivers() {

    }
}
