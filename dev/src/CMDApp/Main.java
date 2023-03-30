package CMDApp;

import TransportModule.ServiceLayer.*;

import java.util.Scanner;


public class Main {

    private static ModuleFactory factory = ModuleFactory.getInstance();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while(true){
            System.out.println("=========================================");
            System.out.println("Welcome to the Transport Module!");
            System.out.println("Please select an option:");
            System.out.println("1. Manage transports");
            System.out.println("2. Manage transport module resources");
            System.out.println("3. Exit");
            int option = getOption();
            switch (option){
                case 1:
                    manageTransports();
                    break;
                case 2:
                    manageResources();
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }

        }
    }

    private static void manageResources() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Transport module resources management");
            System.out.println("Please select an option:");
            System.out.println("1. Manage sites");
            System.out.println("2. Manage drivers");
            System.out.println("3. Manage trucks");
            System.out.println("4. return to main menu");
            int option = getOption();
            switch (option){
                case 1:
                    manageDrivers();
                    break;
                case 2:
                    manageTrucks();
                    break;
                case 3:
                    manageSites();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void manageSites() {
    }

    private static void manageTrucks() {
    }

    private static void manageDrivers() {
    }

    private static void manageTransports() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Transports management");
            System.out.println("Please select an option:");
            System.out.println("1. Create new transport");
            System.out.println("2. Update existing transport");
            System.out.println("3. Delete transport");
            System.out.println("4. View transport");
            System.out.println("5. View all transports");
            System.out.println("6. return to main menu");
            int option = getOption();
            switch (option){
                case 1:
                    createTransport();
                    break;
                case 2:
                    updateTransport();
                    break;
                case 3:
                    deleteTransport();
                    break;
                case 4:
                    viewTransport();
                    break;
                case 5:
                    viewAllTransports();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void viewAllTransports() {
    }

    private static void viewTransport() {
    }

    private static void deleteTransport() {
    }

    private static void updateTransport() {
    }

    private static void createTransport() {
    }

    private static int getOption() {
        System.out.print(">> ");
        int option = scanner.nextInt();
        return option;
    }
    private static String getString(){
        return getString("");
    }
    private static String getString(String defaultStr) {
        System.out.print(">> ");
        String option = scanner.next();
        return option;
    }

}
