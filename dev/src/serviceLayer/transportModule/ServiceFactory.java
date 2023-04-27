package serviceLayer.transportModule;


import businessLayer.BusinessFactory;
import dataAccessLayer.dalUtils.DalException;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;

public class ServiceFactory {

    private final TransportsService transportsService;
    private final ResourceManagementService resourceManagementService;
    private final ItemListsService itemListsService;
    private final EmployeesService employeesService;
    private final UserService userService;

    public ServiceFactory(){

        BusinessFactory factory;
        try {
            factory = new BusinessFactory(this);
        } catch (DalException e) {
            throw new RuntimeException(e);
        }

        transportsService = new TransportsService(factory.transportsController());
        resourceManagementService = new ResourceManagementService(factory.sitesController(),
                factory.driversController(),
                factory.trucksController());
        itemListsService = new ItemListsService(factory.itemListsController());
        userService = new UserService(this);
        employeesService = new EmployeesService(this);
    }

    public ServiceFactory(String dbName){

        BusinessFactory factory;
        try {
            factory = new BusinessFactory(this,dbName);
        } catch (DalException e) {
            throw new RuntimeException(e);
        }

        transportsService = new TransportsService(factory.transportsController());
        resourceManagementService = new ResourceManagementService(factory.sitesController(),
                factory.driversController(),
                factory.trucksController());
        itemListsService = new ItemListsService(factory.itemListsController());
        userService = new UserService(this);
        employeesService = new EmployeesService(this);
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

    public EmployeesService employeesService() {
        return employeesService;
    }

    public UserService userService() {
        return userService;
    }
}
