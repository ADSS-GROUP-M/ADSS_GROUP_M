package serviceLayer.transportModule;


import businessLayer.transportModule.*;
import dataAccessLayer.dalUtils.DalException;
import serviceLayer.employeeModule.Services.EmployeesService;

public class ServiceFactory {

    private final TransportsService transportsService;
    private final ResourceManagementService resourceManagementService;
    private final ItemListsService itemListsService;

    public ServiceFactory(){

        BusinessFactory factory;
        try {
            factory = new BusinessFactory();
        } catch (DalException e) {
            throw new RuntimeException(e);
        }

        transportsService = new TransportsService(factory.transportsController());
        resourceManagementService = new ResourceManagementService(factory.sitesController(),
                factory.driversController(),
                factory.trucksController());
        itemListsService = new ItemListsService(factory.itemListsController());
    }

    public ServiceFactory(String dbName){

        BusinessFactory factory;
        try {
            factory = new BusinessFactory(dbName);
        } catch (DalException e) {
            throw new RuntimeException(e);
        }

        transportsService = new TransportsService(factory.transportsController());
        resourceManagementService = new ResourceManagementService(factory.sitesController(),
                factory.driversController(),
                factory.trucksController());
        itemListsService = new ItemListsService(factory.itemListsController());
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
