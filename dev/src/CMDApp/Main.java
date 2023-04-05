package CMDApp;

import CMDApp.Records.*;
import TransportModule.ServiceLayer.ModuleFactory;

import java.util.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    private AppData appData;
    private TransportsManagement transportsManagement;
    private ItemListsManagement itemListsManagement;
    private SitesManagement sitesManagement;
    private TrucksManagement trucksManagement;
    private DriversManagement driversManagement;

    public static void main(String[] args) {
        Main program = new Main();
        program.mainMenu();
    }

    private void mainMenu() {

        ModuleFactory factory = ModuleFactory.getInstance();

        appData = new AppData();
        itemListsManagement = new ItemListsManagement(this,appData,factory.getItemListsService());
        sitesManagement = new SitesManagement(this,appData,factory.getResourceManagementService());
        trucksManagement = new TrucksManagement(this,appData,factory.getResourceManagementService());
        driversManagement = new DriversManagement(this,appData,factory.getResourceManagementService());
        transportsManagement = new TransportsManagement(this,appData,factory.getTransportsService()
        );

        while(true){
            System.out.println("=========================================");
            System.out.println("Welcome to the Transport Module!");
            System.out.println("Please select an option:");
            System.out.println("1. Manage transports");
            System.out.println("2. Manage item lists");
            System.out.println("3. Manage transport module resources");
            System.out.println("4. Generate data");
            System.out.println("5. Exit");
            int option = getInt();
            switch (option) {
                case 1 -> transportsManagement.manageTransports();
                case 2 -> itemListsManagement.manageItemLists();
                case 3 -> manageResources();
                case 4 -> appData.generateData();
                case 5 -> System.exit(0);
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private void manageResources() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Transport module resources management");
            System.out.println("Please select an option:");
            System.out.println("1. Manage sites");
            System.out.println("2. Manage drivers");
            System.out.println("3. Manage trucks");
            System.out.println("4. Return to main menu");
            int option = getInt();
            switch (option) {
                case 1 -> sitesManagement.manageSites();
                case 2 -> driversManagement.manageDrivers();
                case 3 -> trucksManagement.manageTrucks();
                case 4 -> {
                    return;
                }
                default -> System.out.println("\nInvalid option!");
            }
        }
    }


    //==========================================================================|
    //============================== HELPER METHODS ============================|
    //==========================================================================|

    int getInt(){
        return getInt(">> ");
    }
    int getInt(String prefix){
        if(prefix != "") System.out.print(prefix);
        int option = 0;
        try{
            option = scanner.nextInt();
        }catch(InputMismatchException e){
            scanner = new Scanner(System.in);
            return 0;
        }
        scanner.nextLine();
        return option;
    }

    String getLine(){
        return getLine(">> ");
    }

    String getLine(String prefix) {
        if(prefix != "") System.out.print(prefix);
        return scanner.nextLine().toLowerCase();
    }

    String getWord(){
        return getWord(">> ");
    }

    String getWord(String prefix) {
        if(prefix != "") System.out.print(prefix);
        String str = scanner.next().toLowerCase();
        scanner.nextLine();
        return str;
    }

    Site pickSite(boolean allowDone) {
        int i = 1;
        Site[] siteArray = new Site[appData.sites().size()];
        for(Site site : appData.sites().values()){
            System.out.print(i+".");
            System.out.println(" Transport zone: "+site.transportZone());
            System.out.println("   address:        "+site.address());
            siteArray[i-1] = site;
            i++;
        }
        if(allowDone) System.out.println(i+". Done");
        int option = getInt()-1;
        if( (allowDone && (option < 0 || option > appData.sites().size()))
                || (!allowDone && (option < 0 || option > appData.sites().size()-1))){
            System.out.println("\nInvalid option!");
            return pickSite(allowDone);
        }
        if(allowDone && option == appData.sites().size()) return null;
        return siteArray[option];
    }

    Driver pickDriver(boolean allowDone) {
    int i = 1;
    Driver[] driverArray = new Driver[appData.drivers().size()];
    for(Driver driver : appData.drivers().values()){
        System.out.print(i+".");
        System.out.println(" name:         "+driver.name());
        System.out.println("   license type: "+driver.licenseType());
        driverArray[i-1] = driver;
            i++;
        }
        if(allowDone) System.out.println(i+". Done");
        int option = getInt()-1;
        if( (allowDone && (option < 0 || option > appData.drivers().size()))
                || (!allowDone && (option < 0 || option > appData.drivers().size()-1))){
            System.out.println("\nInvalid option!");
            return pickDriver(allowDone);
        }
        if(allowDone && option == appData.drivers().size()) return null;
        return driverArray[option];
    }

    Truck pickTruck(boolean allowDone) {
        int i = 1;
        Truck[] truckArray = new Truck[appData.trucks().size()];
        for(Truck truck : appData.trucks().values()){
            System.out.print(i+".");
            System.out.println(" license plate:    "+truck.id());
            System.out.println("   model:            "+truck.model());
            System.out.println("   max weight:       "+truck.maxWeight());
            System.out.println("   cooling capacity: "+truck.coolingCapacity());
            truckArray[i-1] = truck;
            i++;
        }
        if(allowDone) System.out.println(i+". Done");
        int option = getInt()-1;
        if( (allowDone && (option < 0 || option > appData.trucks().size()))
                || (!allowDone && (option < 0 || option > appData.trucks().size()-1))){
            System.out.println("\nInvalid option!");
            return pickTruck(allowDone);
        }
        if(allowDone && option == appData.trucks().size()) return null;
        return truckArray[option];
    }

}

