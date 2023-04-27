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
    private final BusinessFactory businessFactory;

    public ServiceFactory(){
        try {
            businessFactory = new BusinessFactory(this);
        } catch (DalException e) {
            throw new RuntimeException(e);
        }

        transportsService = new TransportsService(businessFactory.transportsController());
        resourceManagementService = new ResourceManagementService(businessFactory.sitesController(),
                businessFactory.driversController(),
                businessFactory.trucksController());
        itemListsService = new ItemListsService(businessFactory.itemListsController());
        userService = new UserService(this);
        employeesService = new EmployeesService(this, businessFactory.employeesController(), businessFactory.shiftsController());
    }

    public ServiceFactory(String dbName){
        try {
            businessFactory = new BusinessFactory(this,dbName);
        } catch (DalException e) {
            throw new RuntimeException(e);
        }

        userService = new UserService(this);
        employeesService = new EmployeesService(this, businessFactory.employeesController(), businessFactory.shiftsController());
        businessFactory.updateTransportsController(employeesService);

        transportsService = new TransportsService(businessFactory.transportsController());
        resourceManagementService = new ResourceManagementService(businessFactory.sitesController(),
                businessFactory.driversController(),
                businessFactory.trucksController());
        itemListsService = new ItemListsService(businessFactory.itemListsController());
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
