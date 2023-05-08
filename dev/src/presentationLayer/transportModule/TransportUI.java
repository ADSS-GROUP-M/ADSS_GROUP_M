package presentationLayer.transportModule;


import presentationLayer.employeeModule.View.LoginMenu;
import presentationLayer.employeeModule.View.Menu;
import presentationLayer.employeeModule.View.MenuManager;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Services.EmployeesService;

public class TransportUI implements Menu {

    private final UiData UIData;
    private final TransportsManagement transportsManagement;
    private final ItemListsManagement itemListsManagement;
    private final SitesManagement sitesManagement;
    private final TrucksManagement trucksManagement;
    private final DriversManagement driversManagement;
    private final ServiceFactory factory;

    public TransportUI(ServiceFactory factory){
        this.factory = factory;
        UIData = new UiData(
                factory.resourceManagementService(),
                factory.itemListsService(),
                factory.transportsService()
        );
        EmployeesService employeesService = factory.employeesService();
        itemListsManagement = new ItemListsManagement(UIData,factory.itemListsService());
        sitesManagement = new SitesManagement(UIData,factory.resourceManagementService());
        trucksManagement = new TrucksManagement(UIData,factory.resourceManagementService());
        driversManagement = new DriversManagement(UIData,factory.resourceManagementService());
        transportsManagement = new TransportsManagement(UIData,factory.transportsService(), employeesService);
    }

    @Override
    public Menu run() {
        Thread loader = new Thread(UIData::loadData);
        loader.start();
        printCommands();
        try {
            loader.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int option = UIData.readInt();
        switch (option) {
            case 1 -> transportsManagement.manageTransports();
            case 2 -> itemListsManagement.manageItemLists();
            case 3 -> manageResources();
            case 4 -> {
                return new LoginMenu(factory);
            }
            case 5 -> {
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

