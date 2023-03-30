package TransportModule.ServiceLayer;

import TransportModule.BusinessLayer.*;

public class ModuleFactory {
    private static ModuleFactory instance = null;

    private final TransportsService transportsService;
    private final ResourceManagementService moduleManagementService;
    private final ItemListsService itemListsService;

    private ModuleFactory(){

        TrucksController trucksController = new TrucksController();
        ItemListsController itemListsController = new ItemListsController();
        SitesController sitesController = new SitesController();
        DriversController driversController = new DriversController();
        TransportsController transportsController = new TransportsController(trucksController);

        transportsService = new TransportsService(transportsController,itemListsController);
        moduleManagementService = new ResourceManagementService(sitesController, driversController, trucksController);
        itemListsService = new ItemListsService(itemListsController);
    }

    public TransportsService getTransportsService() {
        return transportsService;
    }

    public ResourceManagementService getModuleManagementService() {
        return moduleManagementService;
    }

    public ItemListsService getItemListsService() {
        return itemListsService;
    }

    public static ModuleFactory getInstance(){
        if(instance == null){
            instance = new ModuleFactory();
        }
        return instance;
    }
}
