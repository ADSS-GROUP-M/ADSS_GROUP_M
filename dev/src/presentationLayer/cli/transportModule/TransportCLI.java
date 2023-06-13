package presentationLayer.cli.transportModule;


import exceptions.ErrorOccurredException;
import presentationLayer.cli.employeeModule.View.LoginMenu;
import presentationLayer.cli.employeeModule.View.Menu;
import presentationLayer.cli.employeeModule.View.MenuManager;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Services.EmployeesService;

public class TransportCLI implements Menu {

    private static final String USERNAME = "transport";
    private final CLIData cliData;
    private final TransportsManagement transportsManagement;
    private final ItemListsManagement itemListsManagement;
    private final SitesManagement sitesManagement;
    private final TrucksManagement trucksManagement;
    private final DriversManagement driversManagement;
    private final ServiceFactory factory;

    public TransportCLI(ServiceFactory factory){
        this.factory = factory;
        cliData = new CLIData(
                factory.resourceManagementService(),
                factory.itemListsService(),
                factory.transportsService()
        );
        EmployeesService employeesService = factory.employeesService();
        itemListsManagement = new ItemListsManagement(cliData,factory.itemListsService());
        sitesManagement = new SitesManagement(cliData,factory.resourceManagementService());
        trucksManagement = new TrucksManagement(cliData,factory.resourceManagementService());
        driversManagement = new DriversManagement(cliData,factory.resourceManagementService());
        transportsManagement = new TransportsManagement(cliData,factory.transportsService(), employeesService);
    }

    @Override
    public Menu run() {
        try {
            cliData.loadData();
        } catch (ErrorOccurredException e) {
            System.out.printf("""
                    
                    Failed to load transport module data with the following error(s):
                    %s
                                
                    Returning to main menu...%n
                    """, e.getMessage());
            return returnToMainMenu();
        }
        printCommands();
        int option = cliData.readInt();
        switch (option) {
            case 1 -> transportsManagement.manageTransports();
            case 2 -> itemListsManagement.manageItemLists();
            case 3 -> manageResources();
            case 4 -> {
                return returnToMainMenu();
            }
            case 5 -> {
                MenuManager.terminate();
                return null;
            }
            default -> System.out.println("\nInvalid option!");
        }
        return this;
    }

    private LoginMenu returnToMainMenu() {
        factory.userService().logout(USERNAME);
        return new LoginMenu(factory);
    }

    @Override
    public void printCommands() {
        System.out.println("=========================================");
        System.out.println("Welcome to the Transport Module!");
        System.out.println("Please select an option:");
        System.out.println("1. Manage transports");
        System.out.println("2. Manage item lists");
        System.out.println("3. Manage transport module resources");
        System.out.println("4. Return to login menu");
        System.out.println("5. Exit");
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
            int option = cliData.readInt();
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

