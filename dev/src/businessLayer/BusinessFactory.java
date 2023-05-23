package businessLayer;

import businessLayer.employeeModule.Controllers.EmployeesController;
import businessLayer.employeeModule.Controllers.ShiftsController;
import businessLayer.employeeModule.Controllers.UserController;
import businessLayer.inventoryModule.CategoryController;
import businessLayer.inventoryModule.DiscountController;
import businessLayer.inventoryModule.ProductController;
import businessLayer.suppliersModule.*;
import businessLayer.transportModule.*;
import businessLayer.transportModule.bingApi.BingAPI;
import dataAccessLayer.DalFactory;
import exceptions.DalException;
import exceptions.TransportException;
import serviceLayer.employeeModule.Services.EmployeesService;

public class BusinessFactory {

    private TransportsController transportsController;
    private DriversController driversController;
    private SitesController sitesController;
    private ItemListsController itemListsController;
    private TrucksController trucksController;
    private EmployeesController employeesController;
    private ShiftsController shiftsController;
    private UserController userController;
    private final DalFactory dalFactory;
    private SitesRoutesController sitesRoutesController;
    private OrderController orderController;
    private SupplierController supplierController;
    private AgreementController agreementController;
    private BillOfQuantitiesController billOfQuantitiesController;
    private PeriodicOrderController periodicOrderController;
    private ProductController productController;
    private DiscountController discountController;
    private CategoryController categoryController;

    public BusinessFactory() throws TransportException{
        try {
            dalFactory = new DalFactory();
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
        buildInstances();
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public BusinessFactory(String dbName) throws TransportException{
        try {
            dalFactory = new DalFactory(dbName);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
        buildInstances();
    }

    private void buildInstances() throws TransportException {
        userController = new UserController(dalFactory.userDAO()); // independent

        //==================== Dependencies ======================= |
        /*(1)*/ agreementController = new AgreementController(dalFactory.agreementDataMapper());
        /*(1)*/ supplierController = new SupplierController(dalFactory.supplierDataMapper());
        /*(1)*/ billOfQuantitiesController = new BillOfQuantitiesController(dalFactory.billOfQuantitiesDataMapper());
        /*(2)*/ periodicOrderController = new PeriodicOrderController(
                    dalFactory.periodicOrderDataMapper(),
                    agreementController);
        /*(2)*/ discountController = new DiscountController(dalFactory.discountManagerMapper());
        /*(2)*/ orderController = new OrderController(
                    supplierController,
                    agreementController,
                    billOfQuantitiesController,
                    dalFactory.orderHistoryDataMapper());
        /*(3)*/ productController = new ProductController(
                    discountController,
                    dalFactory.productManagerMapper(),
                    orderController,
                    periodicOrderController);
        /*(4)*/ categoryController = new CategoryController(
                    productController,
                    discountController,
                    dalFactory.categoryManagerMapper(),
                    dalFactory.productManagerMapper());
        /*(5)*/ discountController.injectDependencies(categoryController);
        //========================================================= |

        //==================== Dependencies ======================= |
        /*(1)*/ shiftsController = new ShiftsController(dalFactory.shiftDAO());
        /*(2)*/ employeesController = new EmployeesController(
                    shiftsController,
                    dalFactory.branchesDAO(),
                    dalFactory.employeeDAO());
        //========================================================= |

        // ======================== Dependencies ===================== |
        /*(1)*/ sitesRoutesController = new SitesRoutesController(new BingAPI(),dalFactory.sitesRoutesDAO());
        /*(1)*/ trucksController = new TrucksController(dalFactory.trucksDAO());
        /*(1)*/ driversController = new DriversController(dalFactory.driversDAO());
        /*(1)*/ itemListsController = new ItemListsController(dalFactory.itemListsDAO());
        /*(2)*/ sitesController = new SitesController(dalFactory.sitesDAO(), sitesRoutesController);
        /*(3)*/ transportsController = new TransportsController(
                    trucksController,
                    driversController,
                    sitesController,
                    itemListsController,
                    sitesRoutesController,
                    dalFactory.transportsDAO());
        //========================================================= |
    }

    public TransportsController transportsController() {
        return transportsController;
    }

    public void injectDependencies(EmployeesService employeesService) {
        transportsController.injectDependencies(employeesService);
        sitesController.injectDependencies(employeesService);
    }

    public DriversController driversController() {
        return driversController;
    }

    public SitesController sitesController() {
        return sitesController;
    }

    public ItemListsController itemListsController() {
        return itemListsController;
    }

    public TrucksController trucksController() {
        return trucksController;
    }

    public EmployeesController employeesController() {
        return employeesController;
    }

    public ShiftsController shiftsController() {
        return shiftsController;
    }

    public UserController userController() {
        return userController;
    }

    public SitesRoutesController sitesRoutesController() {
        return sitesRoutesController;
    }

    public DalFactory dalFactory() {
        return dalFactory;
    }

    public OrderController orderController() {
        return orderController;
    }

    public SupplierController supplierController() {
        return supplierController;
    }

    public AgreementController agreementController() {
        return agreementController;
    }

    public BillOfQuantitiesController billOfQuantitiesController() {
        return billOfQuantitiesController;
    }

    public PeriodicOrderController periodicOrderController() {
        return periodicOrderController;
    }

    public ProductController productController() {
        return productController;
    }

    public DiscountController discountController() {
        return discountController;
    }

    public CategoryController categoryController() {
        return categoryController;
    }
}
