package transportModule.frontend.cmdApp;

import employeeModule.PresentationLayer.View.LoginMenu;
import employeeModule.PresentationLayer.View.Menu;
import employeeModule.PresentationLayer.View.MenuManager;
import transportModule.backend.serviceLayer.ModuleFactory;

public class TransportUI implements Menu {

    private final TransportAppData transportAppData;
    private final TransportsManagement transportsManagement;
    private final ItemListsManagement itemListsManagement;
    private final SitesManagement sitesManagement;
    private final TrucksManagement trucksManagement;
    private final DriversManagement driversManagement;

    public TransportUI(){
        ModuleFactory factory = TransportUIFactory.getFactory();
        transportAppData = TransportAppData.getInstance();
        itemListsManagement = new ItemListsManagement(transportAppData,factory.getItemListsService());
        sitesManagement = new SitesManagement(transportAppData,factory.getResourceManagementService());
        trucksManagement = new TrucksManagement(transportAppData,factory.getResourceManagementService());
        driversManagement = new DriversManagement(transportAppData,factory.getResourceManagementService());
        transportsManagement = new TransportsManagement(transportAppData,factory.getTransportsService());
    }

    @Override
    public Menu run() {
        printCommands();
        int option = transportAppData.readInt();
        switch (option) {
            case 1 -> transportsManagement.manageTransports();
            case 2 -> itemListsManagement.manageItemLists();
            case 3 -> manageResources();
            case 4 -> transportAppData.generateData();
            case 5 -> {
                return new LoginMenu();
            }
            case 6 -> {
                MenuManager.terminate();
                return null;
            }
            default -> System.out.println("\nInvalid option!");
        }
        return this;
    }

    @Override
    public void printCommands() {
        System.out.println("=========================================");
        System.out.println("Welcome to the Transport Module!");
        System.out.println("Please select an option:");
        System.out.println("1. Manage transports");
        System.out.println("2. Manage item lists");
        System.out.println("3. Manage transport module resources");
        System.out.println("4. Generate data");
        System.out.println("5. Return to login menu");
        System.out.println("6. Exit");
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
            int option = transportAppData.readInt();
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

