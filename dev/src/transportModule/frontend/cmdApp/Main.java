package transportModule.frontend.cmdApp;

import transportModule.backend.serviceLayer.ModuleFactory;

public class Main {

    private final AppData appData;
    private final TransportsManagement transportsManagement;
    private final ItemListsManagement itemListsManagement;
    private final SitesManagement sitesManagement;
    private final TrucksManagement trucksManagement;
    private final DriversManagement driversManagement;

    public static void main(String[] args) {
        Main program = new Main();
        program.mainMenu();
    }

    private Main(){
        ModuleFactory factory = new ModuleFactory();
        appData = new AppData(
                factory.getResourceManagementService(),
                factory.getItemListsService(),
                factory.getTransportsService()
        );
        itemListsManagement = new ItemListsManagement(appData,factory.getItemListsService());
        sitesManagement = new SitesManagement(appData,factory.getResourceManagementService());
        trucksManagement = new TrucksManagement(appData,factory.getResourceManagementService());
        driversManagement = new DriversManagement(appData,factory.getResourceManagementService());
        transportsManagement = new TransportsManagement(appData,factory.getTransportsService());
    }

    private void mainMenu() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Welcome to the Transport Module!");
            System.out.println("Please select an option:");
            System.out.println("1. Manage transports");
            System.out.println("2. Manage item lists");
            System.out.println("3. Manage transport module resources");
            System.out.println("4. Generate data");
            System.out.println("5. Exit");
            int option = appData.readInt();
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
            int option = appData.readInt();
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
}

