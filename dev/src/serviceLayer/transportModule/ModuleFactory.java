package serviceLayer.transportModule;


import businessLayer.transportModule.*;
import serviceLayer.employeeModule.Services.EmployeesService;

public class ModuleFactory {

    private final TransportsService transportsService;
    private final ResourceManagementService resourceManagementService;
    private final ItemListsService itemListsService;
    private EmployeesService employeesService;

    public ModuleFactory(){

        TrucksController trucksController = new TrucksController();
        ItemListsController itemListsController = new ItemListsController();
        SitesController sitesController = new SitesController();
        DriversController driversController = new DriversController();
        employeesService = EmployeesService.getInstance();
        TransportsController transportsController = new TransportsController(trucksController,driversController, sitesController, itemListsController,employeesService);

        transportsService = new TransportsService(transportsController);
        resourceManagementService = new ResourceManagementService(sitesController, driversController, trucksController);
        itemListsService = new ItemListsService(itemListsController);
    }

    public TransportsService getTransportsService() {
        return transportsService;
    }

    public ResourceManagementService getResourceManagementService() {
        return resourceManagementService;
    }

    public ItemListsService getItemListsService() {
        return itemListsService;
    }
}
