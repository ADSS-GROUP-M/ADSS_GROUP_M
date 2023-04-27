package presentationLayer.transportModule;


import presentationLayer.employeeModule.View.LoginMenu;
import presentationLayer.employeeModule.View.Menu;
import presentationLayer.employeeModule.View.MenuManager;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.transportModule.ServiceFactory;

public class TransportUI implements Menu {

    private final UiData UIData;
    private final TransportsManagement transportsManagement;
    private final ItemListsManagement itemListsManagement;
    private final SitesManagement sitesManagement;
    private final TrucksManagement trucksManagement;
    private final DriversManagement driversManagement;

    public TransportUI(ServiceFactory factory){
        UIData = new UiData(
                factory.getResourceManagementService(),
                factory.getItemListsService(),
                factory.getTransportsService()
        );
        EmployeesService employeesService = factory.employeesService();
        itemListsManagement = new ItemListsManagement(UIData,factory.getItemListsService());
        sitesManagement = new SitesManagement(UIData,factory.getResourceManagementService());
        trucksManagement = new TrucksManagement(UIData,factory.getResourceManagementService());
        driversManagement = new DriversManagement(UIData,factory.getResourceManagementService());
        transportsManagement = new TransportsManagement(UIData,factory.getTransportsService(), employeesService);
    }

    @Override
    public Menu run() {
        printCommands();
        int option = UIData.readInt();
        switch (option) {
            case 1 -> transportsManagement.manageTransports();
            case 2 -> itemListsManagement.manageItemLists();
            case 3 -> manageResources();
            case 4 -> UIData.loadData();
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
            int option = UIData.readInt();
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

