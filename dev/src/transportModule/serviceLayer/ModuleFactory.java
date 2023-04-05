package transportModule.serviceLayer;

import transportModule.businessLayer.*;

public class ModuleFactory {
    private static ModuleFactory instance = null;

    private final TransportsService transportsService;
    private final ResourceManagementService resourceManagementService;
    private final ItemListsService itemListsService;

    private ModuleFactory(){

        TrucksController trucksController = new TrucksController();
        ItemListsController itemListsController = new ItemListsController();
        SitesController sitesController = new SitesController();
        DriversController driversController = new DriversController();
        TransportsController transportsController = new TransportsController(trucksController,driversController);

        transportsService = new TransportsService(transportsController,itemListsController);
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

    public static ModuleFactory getInstance(){
        if(instance == null){
            instance = new ModuleFactory();
        }
        return instance;
    }

    public static void tearDownForTests(){
        instance = null;
    }
}
