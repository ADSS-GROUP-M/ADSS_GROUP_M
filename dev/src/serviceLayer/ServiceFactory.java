package serviceLayer;


import businessLayer.BusinessFactory;
import exceptions.TransportException;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;
import serviceLayer.inventoryModule.CategoriesService;
import serviceLayer.inventoryModule.StockService;
import serviceLayer.suppliersModule.*;
import serviceLayer.transportModule.ItemListsService;
import serviceLayer.transportModule.ResourceManagementService;
import serviceLayer.transportModule.TransportsService;

public class ServiceFactory {

    private TransportsService transportsService;
    private ResourceManagementService resourceManagementService;
    private ItemListsService itemListsService;
    private EmployeesService employeesService;
    private UserService userService;
    private OrderService orderService;
    private SupplierService supplierService;
    private AgreementService agreementService;
    private BillOfQuantitiesService billOfQuantitiesService;
    private PeriodicOrderService periodicOrderService;
    private StockService stockService;
    private CategoriesService categoriesService;
    private final BusinessFactory businessFactory;

    public ServiceFactory(){
        try {
            businessFactory = new BusinessFactory();
        } catch (TransportException e) {
            throw new RuntimeException(e);
        }
        buildInstances();
    }

    public ServiceFactory(String dbName){
        try {
            businessFactory = new BusinessFactory(dbName);
        } catch (TransportException e) {
            throw new RuntimeException(e);
        }
        buildInstances();
    }

    private void buildInstances() {

        orderService = new OrderService(businessFactory.orderController());
        supplierService = new SupplierService(
                businessFactory.supplierController(),
                businessFactory.agreementController(),
                businessFactory.billOfQuantitiesController());

        agreementService = new AgreementService(
                businessFactory.agreementController(),
                businessFactory.billOfQuantitiesController());

        billOfQuantitiesService = new BillOfQuantitiesService(
                businessFactory.agreementController(),
                businessFactory.billOfQuantitiesController());

        periodicOrderService = new PeriodicOrderService(businessFactory.periodicOrderController());

        stockService = new StockService(
                businessFactory.productController(),
                businessFactory.discountController(),
                businessFactory.categoryController());

        categoriesService = new CategoriesService(
                businessFactory.productController(),
                businessFactory.discountController(),
                businessFactory.categoryController());

        userService = new UserService(businessFactory.userController());
        itemListsService = new ItemListsService(businessFactory.itemListsController());
        transportsService = new TransportsService(businessFactory.transportsController());

        //==================== Dependencies ======================= |
        /*(1)*/ resourceManagementService = new ResourceManagementService(businessFactory.sitesController(), businessFactory.driversController(), businessFactory.trucksController());
        /*(2)*/ employeesService = new EmployeesService(resourceManagementService,userService, businessFactory.employeesController(), businessFactory.shiftsController());
        /*(3)*/ businessFactory.injectDependencies(employeesService);
        //========================================================= |
    }

    public TransportsService transportsService() {
        return transportsService;
    }

    public ResourceManagementService resourceManagementService() {
        return resourceManagementService;
    }

    public ItemListsService itemListsService() {
        return itemListsService;
    }

    public EmployeesService employeesService() {
        return employeesService;
    }

    public UserService userService() {
        return userService;
    }

    public BusinessFactory businessFactory() {
        return businessFactory;
    }

    public OrderService orderService() {
        return orderService;
    }

    public SupplierService supplierService() {
        return supplierService;
    }

    public AgreementService agreementService() {
        return agreementService;
    }

    public BillOfQuantitiesService billOfQuantitiesService() {
        return billOfQuantitiesService;
    }

    public PeriodicOrderService periodicOrderService() {
        return periodicOrderService;
    }

    public StockService stockService() {
        return stockService;
    }

    public CategoriesService categoriesService() {
        return categoriesService;
    }
}
